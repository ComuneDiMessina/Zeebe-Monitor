package it.almaviva.eai.zeebe.monitor.controller;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zeebe.model.bpmn.Bpmn;
import io.zeebe.model.bpmn.instance.FlowElement;
import io.zeebe.model.bpmn.instance.ServiceTask;
import io.zeebe.model.bpmn.instance.zeebe.ZeebeHeader;
import io.zeebe.model.bpmn.instance.zeebe.ZeebeTaskDefinition;
import io.zeebe.model.bpmn.instance.zeebe.ZeebeTaskHeaders;
import io.zeebe.protocol.record.value.BpmnElementType;
import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceDomain;
import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceStatistics;
import it.almaviva.eai.zeebe.monitor.domain.IncidentDomain;
import it.almaviva.eai.zeebe.monitor.domain.JobDomain;
import it.almaviva.eai.zeebe.monitor.domain.MessageDomain;
import it.almaviva.eai.zeebe.monitor.domain.MessageSubscriptionDomain;
import it.almaviva.eai.zeebe.monitor.domain.TimerDomain;
import it.almaviva.eai.zeebe.monitor.domain.VariableDomain;
import it.almaviva.eai.zeebe.monitor.domain.WorkflowDomain;
import it.almaviva.eai.zeebe.monitor.domain.WorkflowInstanceDomain;
import it.almaviva.eai.zeebe.monitor.dto.ActiveScope;
import it.almaviva.eai.zeebe.monitor.dto.AuditLogEntry;
import it.almaviva.eai.zeebe.monitor.dto.CalledWorkflowInstanceDTO;
import it.almaviva.eai.zeebe.monitor.dto.ElementInstanceState;
import it.almaviva.eai.zeebe.monitor.dto.IncidentDTO;
import it.almaviva.eai.zeebe.monitor.dto.IncidentListDTO;
import it.almaviva.eai.zeebe.monitor.dto.JobDTO;
import it.almaviva.eai.zeebe.monitor.dto.MessageDTO;
import it.almaviva.eai.zeebe.monitor.dto.MessageSubscriptionDTO;
import it.almaviva.eai.zeebe.monitor.dto.TimerDTO;
import it.almaviva.eai.zeebe.monitor.dto.UserTaskInfo;
import it.almaviva.eai.zeebe.monitor.dto.VariableEntry;
import it.almaviva.eai.zeebe.monitor.dto.VariableUpdateEntry;
import it.almaviva.eai.zeebe.monitor.dto.WorkflowDTO;
import it.almaviva.eai.zeebe.monitor.dto.WorkflowInstanceDTO;
import it.almaviva.eai.zeebe.monitor.dto.WorkflowInstanceListDto;
import it.almaviva.eai.zeebe.monitor.port.incoming.IElementInstanceUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IIncidentUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IJobUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IMessageSubscriptionUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IMessageUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.ITimerUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IVariableUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IWorkflowInstanceUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IWorkflowUseCase;
import it.almaviva.eai.zeebe.monitor.utils.MonitorUtils;

@RestController
@RequestMapping("/rest")
public class ViewController {

	private static final List<String> WORKFLOW_INSTANCE_ENTERED_INTENTS = Arrays.asList("ELEMENT_ACTIVATED");

	private static final List<String> WORKFLOW_INSTANCE_COMPLETED_INTENTS = Arrays.asList("ELEMENT_COMPLETED",
			"ELEMENT_TERMINATED");

	private static final List<String> EXCLUDE_ELEMENT_TYPES = Arrays.asList(BpmnElementType.MULTI_INSTANCE_BODY.name());

	private static final List<String> JOB_COMPLETED_INTENTS = Arrays.asList("completed", "canceled");

	@Autowired
	private IWorkflowUseCase iWorkflowUseCase;

	@Autowired
	private IWorkflowInstanceUseCase iWorkflowInstanceUseCase;
	
	@Autowired
	private ITimerUseCase iTimerUseCase;
	
	@Autowired
	private IMessageSubscriptionUseCase iMessageSubscriptionUseCase;
	
	@Autowired
	private IElementInstanceUseCase iElementInstanceUseCase;
	
	@Autowired
	private IIncidentUseCase iIncidentUseCase;
	
	@Autowired
	private IVariableUseCase iVariableUseCase;
	
	@Autowired
	private IJobUseCase iJobUseCase;
	
	@Autowired
	private IMessageUseCase iMessageUseCase;
	
	

	@GetMapping(value = "/views/workflows", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> workflowList(Map<String, Object> model) {

		ResponseEntity<Map<String, Object>> response;

		try {
			final long count = iWorkflowUseCase.count();

			final List<WorkflowDTO> workflows = new ArrayList<>();
			for (WorkflowDomain workflowDomain : iWorkflowUseCase.findAll()) {
				final WorkflowDTO dto = toDto(workflowDomain);
				workflows.add(dto);
			}
			model.put("workflows", workflows);
			model.put("count", count);

			response = new ResponseEntity<>(model, HttpStatus.OK);
		} catch (Exception e) {
			model.put("status", 500);
			model.put("message", e.getMessage());
			response = new ResponseEntity<>(model, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	private WorkflowDTO toDto(WorkflowDomain workflowDomain) {
		final long workflowKey = workflowDomain.getKey();

		final long running = iWorkflowInstanceUseCase.countByWorkflowKeyAndEndIsNull(workflowKey);
		final long ended = iWorkflowInstanceUseCase.countByWorkflowKeyAndEndIsNotNull(workflowKey);

		final WorkflowDTO dto = WorkflowDTO.from(workflowDomain, running, ended);
		return dto;
	}

	@GetMapping("/views/workflows/{key}")
	// @Transactional
	public ResponseEntity<Map<String, Object>> workflowDetail(@PathVariable long key, Map<String, Object> model) {

		ResponseEntity<Map<String, Object>> response;

		try {

			final WorkflowDomain workflow = Optional.of(iWorkflowUseCase.findByKey(key))
					.orElseThrow(() -> new RuntimeException("No workflow found with key: " + key));

			model.put("workflow", toDto(workflow));
			model.put("resource", workflow.getResource());

			final List<ElementInstanceState> elementInstanceStates = getElementInstanceStates(key);
			model.put("instance.elementInstances", elementInstanceStates);

			final long count = iWorkflowInstanceUseCase.countByWorkflowKey(key);

			final List<WorkflowInstanceListDto> instances = new ArrayList<>();
			for (WorkflowInstanceDomain instanceEntity : iWorkflowInstanceUseCase.findByWorkflowKey(key)) {
				instances.add(MonitorUtils.toDto(instanceEntity));
			}

			model.put("instances", instances);
			model.put("count", count);

			final List<TimerDTO> timers = iTimerUseCase.findByWorkflowKeyAndWorkflowInstanceKeyIsNull(key).stream()
					.map(this::toDto).collect(Collectors.toList());
			model.put("timers", timers);

			final List<MessageSubscriptionDTO> messageSubscriptions = iMessageSubscriptionUseCase
					.findByWorkflowKeyAndWorkflowInstanceKeyIsNull(key).stream().map(this::toDto)
					.collect(Collectors.toList());
			model.put("messageSubscriptions", messageSubscriptions);

			final var resourceAsStream = new ByteArrayInputStream(workflow.getResource().getBytes());
			final var bpmn = Bpmn.readModelFromStream(resourceAsStream);
			model.put("instance.bpmnElementInfos", MonitorUtils.getBpmnElementInfos(bpmn));
			
			response = new ResponseEntity<>(model, HttpStatus.OK);

		} catch (Exception e) {
			model.put("status", 500);
			model.put("message", e.getMessage());
			response = new ResponseEntity<>(model, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;

	}
	
	@GetMapping("/workflows/{key}/usertask")
	//@Transactional
	public ResponseEntity<Map<String, Object>> workflowUserTask(@PathVariable long key, Map<String, Object> model) {

		ResponseEntity<Map<String, Object>> response;

		try {

			final WorkflowDomain workflow = Optional.of(iWorkflowUseCase.findByKey(key))
					.orElseThrow(() -> new RuntimeException("No workflow found with key: " + key));

			final var resourceAsStream = new ByteArrayInputStream(workflow.getResource().getBytes());
			final var bpmn = Bpmn.readModelFromStream(resourceAsStream);
			ArrayList<UserTaskInfo> listUserTask = new 	ArrayList<UserTaskInfo>();
			bpmn.getModelElementsByType(ServiceTask.class).forEach(t -> {
				if(t.getSingleExtensionElement(ZeebeTaskDefinition.class).getType().equalsIgnoreCase("user"))
				{
				UserTaskInfo infoTask = new UserTaskInfo();
				Map<String, String> header = new HashMap<String, String>();
				infoTask.setElementId(t.getId());
				infoTask.setName(t.getName());
				ZeebeTaskHeaders taskHeaders = t.getSingleExtensionElement(ZeebeTaskHeaders.class);
				if (taskHeaders != null) {
					final List<ZeebeHeader> validHeaders = taskHeaders.getHeaders().stream()
							.collect(Collectors.toList());
					for (ZeebeHeader h : validHeaders) {
						header.put(h.getKey(), h.getValue());
					}
				}
				infoTask.setHeader(header);
                listUserTask.add(infoTask);
				}
			});

			
			model.put("usertask", listUserTask);

			response = new ResponseEntity<>(model, HttpStatus.OK);

		} catch (Exception e) {
			model.put("status", 500);
			model.put("message", e.getMessage());
			response = new ResponseEntity<>(model, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;

	}
	
	@GetMapping(value = "/views/instances", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Map<String, Object>> instanceList(Map<String, Object> model) {

		ResponseEntity<Map<String, Object>> response;

		try {

			final long count = iWorkflowInstanceUseCase.count();

			final List<WorkflowInstanceListDto> instances = new ArrayList<>();
			for (WorkflowInstanceDomain instanceEntity : iWorkflowInstanceUseCase.findAll()) {
				final WorkflowInstanceListDto dto = MonitorUtils.toDto(instanceEntity);
				instances.add(dto);
			}

			model.put("instances", instances);
			model.put("count", count);
			model.put("status", 200);

			response = new ResponseEntity<>(model, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception;
			model.put("status", 500);
			model.put("message", e.getMessage());
			response = new ResponseEntity<>(model, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	@GetMapping(value = "/views/instances/{key}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	// @Transactional
	public ResponseEntity<Map<String, Object>> instanceDetail(@PathVariable long key, Map<String, Object> model) {

		ResponseEntity<Map<String, Object>> response;

		try {

			final WorkflowInstanceDomain instance = Optional.of(iWorkflowInstanceUseCase.findByKey(key))
					.orElseThrow(() -> new RuntimeException("No workflow instance found with key: " + key));

			Optional.of(iWorkflowUseCase.findByKey(instance.getWorkflowKey()))
					.ifPresent(workflow -> model.put("resource", workflow.getResource()));

			model.put("instance", toInstanceDto(instance));
			model.put("status", 200);
			response = new ResponseEntity<>(model, HttpStatus.OK);

		} catch (Exception e) {
			model.put("status", 500);
			model.put("message", e.getMessage());
			response = new ResponseEntity<>(model, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	
	
	@GetMapping(value = "/views/incidents", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional
	public ResponseEntity<Map<String, Object>> incidentList(Map<String, Object> model) {

		ResponseEntity<Map<String, Object>> response;

		try {

			final long count = iIncidentUseCase.countByResolvedIsNull();

			final List<IncidentListDTO> incidents = new ArrayList<>();
			for (IncidentDomain incidentEntity : iIncidentUseCase.findByResolvedIsNull()) {
				final IncidentListDTO dto = MonitorUtils.toDto(incidentEntity);
				incidents.add(dto);
			}

			model.put("incidents", incidents);
			model.put("count", count);
			model.put("status", 200);

			response = new ResponseEntity<>(model, HttpStatus.OK);

		} catch (Exception e) {
			// TODO: handle exception;
			model.put("status", 500);
			model.put("message", e.getMessage());
			response = new ResponseEntity<>(model, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	
	
	@GetMapping(value = "/views/jobs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Map<String, Object>> jobList(Map<String, Object> model) {

		ResponseEntity<Map<String, Object>> response;

		try {

			final long count = iJobUseCase.countByStateNotIn(JOB_COMPLETED_INTENTS);

			final List<JobDTO> dtos = new ArrayList<>();
			for (JobDomain jobEntity : iJobUseCase.findByStateNotIn(JOB_COMPLETED_INTENTS)) {
				final JobDTO dto = MonitorUtils.toDto(jobEntity);
				dtos.add(dto);
			}

			model.put("jobs", dtos);
			model.put("count", count);
			model.put("status", 200);
			response = new ResponseEntity<>(model, HttpStatus.OK);

		} catch (Exception e) {
			model.put("status", 500);
			model.put("message", e.getMessage());
			response = new ResponseEntity<>(model, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	
	@GetMapping(value = "/views/messages", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Map<String, Object>> messageList(Map<String, Object> model, Pageable pageable) {

		ResponseEntity<Map<String, Object>> response;

		try {

			final long count = iMessageUseCase.count();

			final List<MessageDTO> dtos = new ArrayList<>();
			for (MessageDomain messageEntity : iMessageUseCase.findAll()) {
				final MessageDTO dto = MonitorUtils.toDto(messageEntity);
				dtos.add(dto);
			}

			model.put("messages", dtos);
			model.put("count", count);
			model.put("status", 200);
			response = new ResponseEntity<>(model, HttpStatus.OK);

		} catch (Exception e) {
			model.put("status", 500);
			model.put("message", e.getMessage());
			response = new ResponseEntity<>(model, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}


	
	/*function*/
	
	private WorkflowInstanceDTO toInstanceDto(WorkflowInstanceDomain instance) {
		final List<ElementInstanceDomain> events = StreamSupport
				.stream(iElementInstanceUseCase.findByWorkflowInstanceKey(instance.getKey()).spliterator(), false)
				.collect(Collectors.toList());

		final WorkflowInstanceDTO dto = new WorkflowInstanceDTO();
		dto.setWorkflowInstanceKey(instance.getKey());

		dto.setPartitionId(instance.getPartitionId());

		dto.setWorkflowKey(instance.getWorkflowKey());

		dto.setBpmnProcessId(instance.getBpmnProcessId());
		dto.setVersion(instance.getVersion());

		final boolean isEnded = instance.getEnd() != null && instance.getEnd() > 0;
		dto.setState(instance.getState());
		dto.setRunning(!isEnded);

		dto.setStartTime(Instant.ofEpochMilli(instance.getStart()).toString());

		if (isEnded) {
			dto.setEndTime(Instant.ofEpochMilli(instance.getEnd()).toString());
		}

		if (instance.getParentElementInstanceKey() > 0) {
			dto.setParentWorkflowInstanceKey(instance.getParentWorkflowInstanceKey());

			Optional.of(iWorkflowInstanceUseCase.findByKey(instance.getParentWorkflowInstanceKey())).ifPresent(parent -> {
				dto.setParentBpmnProcessId(parent.getBpmnProcessId());
			});
		}

		final List<String> completedActivities = events.stream()
				.filter(e -> WORKFLOW_INSTANCE_COMPLETED_INTENTS.contains(e.getIntent()))
				.filter(e -> e.getBpmnElementType() != BpmnElementType.PROCESS.name())
				.map(ElementInstanceDomain::getElementId).collect(Collectors.toList());

		final List<String> activeActivities = events.stream()
				.filter(e -> WORKFLOW_INSTANCE_ENTERED_INTENTS.contains(e.getIntent()))
				.filter(e-> e.getBpmnElementType() != BpmnElementType.PROCESS.name())
				.map(ElementInstanceDomain::getElementId).filter(id -> !completedActivities.contains(id))
				.collect(Collectors.toList());
		dto.setActiveActivities(activeActivities);

		final List<String> takenSequenceFlows = events.stream().filter(e -> e.getIntent().equals("SEQUENCE_FLOW_TAKEN"))
				.map(ElementInstanceDomain::getElementId).collect(Collectors.toList());
		dto.setTakenSequenceFlows(takenSequenceFlows);

		final Map<String, Long> completedElementsById = events.stream()
				.filter(e -> WORKFLOW_INSTANCE_COMPLETED_INTENTS.contains(e.getIntent()))
				.filter(e -> !EXCLUDE_ELEMENT_TYPES.contains(e.getBpmnElementType()))
				.collect(Collectors.groupingBy(ElementInstanceDomain::getElementId, Collectors.counting()));

		final Map<String, Long> enteredElementsById = events.stream()
				.filter(e -> WORKFLOW_INSTANCE_ENTERED_INTENTS.contains(e.getIntent()))
				.filter(e -> !EXCLUDE_ELEMENT_TYPES.contains(e.getBpmnElementType()))
				.collect(Collectors.groupingBy(ElementInstanceDomain::getElementId, Collectors.counting()));

		final List<ElementInstanceState> elementStates = enteredElementsById.entrySet().stream().map(e -> {
			final String elementId = e.getKey();

			final long enteredInstances = e.getValue();
			final long completedInstances = completedElementsById.getOrDefault(elementId, 0L);

			final ElementInstanceState state = new ElementInstanceState();
			state.setElementId(elementId);
			state.setActiveInstances(enteredInstances - completedInstances);
			state.setEndedInstances(completedInstances);

			return state;
		}).collect(Collectors.toList());

		dto.setElementInstances(elementStates);

		final var bpmnModelInstance = Optional.of(iWorkflowUseCase.findByKey(instance.getWorkflowKey()))
				.map(w -> new ByteArrayInputStream(w.getResource().getBytes()))
				.map(stream -> Bpmn.readModelFromStream(stream));

		final Map<String, String> flowElements = new HashMap<>();

		bpmnModelInstance.ifPresent(bpmn -> {
			bpmn.getModelElementsByType(FlowElement.class).forEach(e -> {
				flowElements.put(e.getId(), Optional.ofNullable(e.getName()).orElse(""));
			});

			dto.setBpmnElementInfos(MonitorUtils.getBpmnElementInfos(bpmn));
		});

		final List<AuditLogEntry> auditLogEntries = events.stream().map(e -> {
			final AuditLogEntry entry = new AuditLogEntry();

			entry.setKey(e.getKey());
			entry.setFlowScopeKey(e.getFlowScopeKey());
			entry.setElementId(e.getElementId());
			entry.setElementName(flowElements.getOrDefault(e.getElementId(), ""));
			entry.setBpmnElementType(e.getBpmnElementType());
			entry.setState(e.getIntent());
			entry.setTimestamp(Instant.ofEpochMilli(e.getTimestamp()).toString());

			return entry;
		}).collect(Collectors.toList());

		dto.setAuditLogEntries(auditLogEntries);

		final List<IncidentDomain> incidents = StreamSupport
				.stream(iIncidentUseCase.findByWorkflowInstanceKey(instance.getKey()).spliterator(), false)
				.collect(Collectors.toList());

		final Map<Long, String> elementIdsForKeys = new HashMap<>();
		elementIdsForKeys.put(instance.getKey(), instance.getBpmnProcessId());
		events.forEach(e -> elementIdsForKeys.put(e.getKey(), e.getElementId()));

		final List<IncidentDTO> incidentDtos = incidents.stream().map(i -> {
			final long incidentKey = i.getKey();

			final IncidentDTO incidentDto = new IncidentDTO();
			incidentDto.setKey(incidentKey);

			incidentDto.setElementId(elementIdsForKeys.get(i.getElementInstanceKey()));
			incidentDto.setElementInstanceKey(i.getElementInstanceKey());

			if (i.getJobKey() > 0) {
				incidentDto.setJobKey(i.getJobKey());
			}

			incidentDto.setErrorType(i.getErrorType());
			incidentDto.setErrorMessage(i.getErrorMessage());

			final boolean isResolved = i.getResolved() != null && i.getResolved() > 0;
			incidentDto.setResolved(isResolved);

			incidentDto.setCreatedTime(Instant.ofEpochMilli(i.getCreated()).toString());

			if (isResolved) {
				incidentDto.setResolvedTime(Instant.ofEpochMilli(i.getResolved()).toString());

				incidentDto.setState("Resolved");
			} else {
				incidentDto.setState("Created");
			}

			return incidentDto;
		}).collect(Collectors.toList());
		dto.setIncidents(incidentDtos);

		final List<String> activitiesWitIncidents = incidents.stream()
				.filter(i -> i.getResolved() == null || i.getResolved() <= 0)
				.map(i -> elementIdsForKeys.get(i.getElementInstanceKey())).distinct().collect(Collectors.toList());

		dto.setIncidentActivities(activitiesWitIncidents);

		activeActivities.removeAll(activitiesWitIncidents);
		dto.setActiveActivities(activeActivities);

		final Map<MonitorUtils.VariableTuple, List<VariableDomain>> variablesByScopeAndName = iVariableUseCase
				.findByWorkflowInstanceKey(instance.getKey()).stream()
				.collect(Collectors.groupingBy(v -> new MonitorUtils.VariableTuple(v.getScopeKey(), v.getName())));
		variablesByScopeAndName.forEach((scopeKeyName, variables) -> {
			final VariableEntry variableDto = new VariableEntry();
			final long scopeKey = scopeKeyName.scopeKey;

			variableDto.setScopeKey(scopeKey);
			variableDto.setElementId(elementIdsForKeys.get(scopeKey));

			variableDto.setName(scopeKeyName.name);

			final VariableDomain lastUpdate = variables.get(variables.size() - 1);
			variableDto.setValue(lastUpdate.getValue());
			variableDto.setTimestamp(Instant.ofEpochMilli(lastUpdate.getTimestamp()).toString());

			final List<VariableUpdateEntry> varUpdates = variables.stream().map(v -> {
				final VariableUpdateEntry varUpdate = new VariableUpdateEntry();
				varUpdate.setValue(v.getValue());
				varUpdate.setTimestamp(Instant.ofEpochMilli(v.getTimestamp()).toString());
				return varUpdate;
			}).collect(Collectors.toList());
			variableDto.setUpdates(varUpdates);

			dto.getVariables().add(variableDto);
		});

		final List<ActiveScope> activeScopes = new ArrayList<>();
		if (!isEnded) {
			activeScopes.add(new ActiveScope(instance.getKey(), instance.getBpmnProcessId()));

			final List<Long> completedElementInstances = events.stream()
					.filter(e -> WORKFLOW_INSTANCE_COMPLETED_INTENTS.contains(e.getIntent()))
					.map(ElementInstanceDomain::getKey).collect(Collectors.toList());

			final List<ActiveScope> activeElementInstances = events.stream()
					.filter(e -> WORKFLOW_INSTANCE_ENTERED_INTENTS.contains(e.getIntent()))
					.map(ElementInstanceDomain::getKey).filter(id -> !completedElementInstances.contains(id))
					.map(scopeKey -> new ActiveScope(scopeKey, elementIdsForKeys.get(scopeKey)))
					.collect(Collectors.toList());

			activeScopes.addAll(activeElementInstances);
		}
		dto.setActiveScopes(activeScopes);

		final List<JobDTO> jobDtos = iJobUseCase.findByWorkflowInstanceKey(instance.getKey()).stream().map(job -> {
			final JobDTO jobDto = MonitorUtils.toDto(job);
			jobDto.setElementId(elementIdsForKeys.getOrDefault(job.getElementInstanceKey(), ""));

			final boolean isActivatable = job.getRetries() > 0
					&& Arrays.asList("created", "failed", "timed_out", "retries_updated").contains(job.getState());
			jobDto.setActivatable(isActivatable);

			return jobDto;
		}).collect(Collectors.toList());
		dto.setJobs(jobDtos);

		final List<MessageSubscriptionDTO> messageSubscriptions = iMessageSubscriptionUseCase
				.findByWorkflowInstanceKey(instance.getKey()).stream().map(subscription -> {
					final MessageSubscriptionDTO subscriptionDto = toDto(subscription);
					subscriptionDto
							.setElementId(elementIdsForKeys.getOrDefault(subscriptionDto.getElementInstanceKey(), ""));

					return subscriptionDto;
				}).collect(Collectors.toList());
		dto.setMessageSubscriptions(messageSubscriptions);

		final List<TimerDTO> timers = iTimerUseCase.findByWorkflowInstanceKey(instance.getKey()).stream()
				.map(timer -> toDto(timer)).collect(Collectors.toList());
		dto.setTimers(timers);

		final var calledWorkflowInstances = iWorkflowInstanceUseCase
				.findByParentWorkflowInstanceKey(instance.getKey()).stream().map(childEntity -> {
					final var childDto = new CalledWorkflowInstanceDTO();

					childDto.setChildWorkflowInstanceKey(childEntity.getKey());
					childDto.setChildBpmnProcessId(childEntity.getBpmnProcessId());
					childDto.setChildState(childEntity.getState());

					childDto.setElementInstanceKey(childEntity.getParentElementInstanceKey());

					final var callElementId = elementIdsForKeys.getOrDefault(childEntity.getParentElementInstanceKey(),
							"");
					childDto.setElementId(callElementId);

					return childDto;
				}).collect(Collectors.toList());
		dto.setCalledWorkflowInstances(calledWorkflowInstances);

		return dto;
	}
	
	private List<ElementInstanceState> getElementInstanceStates(long key) {

		final List<ElementInstanceStatistics> elementEnteredStatistics = iElementInstanceUseCase
				.getElementInstanceStatisticsByKeyAndIntentIn(key, WORKFLOW_INSTANCE_ENTERED_INTENTS,
						EXCLUDE_ELEMENT_TYPES);

		final Map<String, Long> elementCompletedCount = iElementInstanceUseCase
				.getElementInstanceStatisticsByKeyAndIntentIn(key, WORKFLOW_INSTANCE_COMPLETED_INTENTS,
						EXCLUDE_ELEMENT_TYPES)
				.stream().collect(
						Collectors.toMap(ElementInstanceStatistics::getElementId, ElementInstanceStatistics::getCount));

		final List<ElementInstanceState> elementInstanceStates = elementEnteredStatistics.stream().map(s -> {
			final ElementInstanceState state = new ElementInstanceState();

			final String elementId = s.getElementId();
			state.setElementId(elementId);

			final long completedInstances = elementCompletedCount.getOrDefault(elementId, 0L);
			long enteredInstances = s.getCount();

			state.setActiveInstances(enteredInstances - completedInstances);
			state.setEndedInstances(completedInstances);

			return state;
		}).collect(Collectors.toList());
		return elementInstanceStates;
	}

	
	private TimerDTO toDto(TimerDomain timer) {
		final TimerDTO dto = new TimerDTO();

		dto.setElementId(timer.getTargetFlowNodeId());
		dto.setState(timer.getState());
		dto.setDueDate(Instant.ofEpochMilli(timer.getDueDate()).toString());
		dto.setTimestamp(Instant.ofEpochMilli(timer.getTimestamp()).toString());
		dto.setElementInstanceKey(timer.getElementInstanceKey());

		final int repetitions = timer.getRepetitions();
		dto.setRepetitions(repetitions >= 0 ? String.valueOf(repetitions) : "∞");

		return dto;
	}
	
	private MessageSubscriptionDTO toDto(MessageSubscriptionDomain subscription) {
		final MessageSubscriptionDTO dto = new MessageSubscriptionDTO();

		dto.setKey(subscription.getId());
		dto.setMessageName(subscription.getMessageName());
		dto.setCorrelationKey(Optional.ofNullable(subscription.getCorrelationKey()).orElse(""));

		dto.setWorkflowInstanceKey(subscription.getWorkflowInstanceKey());
		dto.setElementInstanceKey(subscription.getElementInstanceKey());

		dto.setElementId(subscription.getTargetFlowNodeId());

		dto.setState(subscription.getState());
		dto.setTimestamp(Instant.ofEpochMilli(subscription.getTimestamp()).toString());

		dto.setOpen(subscription.getState().equals("opened"));

		return dto;
	}

}
