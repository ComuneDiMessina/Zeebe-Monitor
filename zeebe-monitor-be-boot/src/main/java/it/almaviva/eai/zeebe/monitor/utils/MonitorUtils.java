package it.almaviva.eai.zeebe.monitor.utils;

import io.zeebe.model.bpmn.BpmnModelInstance;
import io.zeebe.model.bpmn.instance.*;
import io.zeebe.model.bpmn.instance.zeebe.ZeebeTaskDefinition;
import it.almaviva.eai.zeebe.monitor.domain.IncidentDomain;
import it.almaviva.eai.zeebe.monitor.domain.JobDomain;
import it.almaviva.eai.zeebe.monitor.domain.MessageDomain;
import it.almaviva.eai.zeebe.monitor.domain.WorkflowInstanceDomain;
import it.almaviva.eai.zeebe.monitor.dto.*;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MonitorUtils {
	
	public static WorkflowInstanceListDto toDto(WorkflowInstanceDomain instance) {

		final WorkflowInstanceListDto dto = new WorkflowInstanceListDto();
		dto.setWorkflowInstanceKey(instance.getKey());

		dto.setBpmnProcessId(instance.getBpmnProcessId());
		dto.setWorkflowKey(instance.getWorkflowKey());

		final boolean isEnded = instance.getEnd() != null && instance.getEnd() > 0;
		dto.setState(instance.getState());

		dto.setStartTime(Instant.ofEpochMilli(instance.getStart()).toString());

		if (isEnded) {
			dto.setEndTime(Instant.ofEpochMilli(instance.getEnd()).toString());
		}

		return dto;
	}
	
	public static JobDTO toDto(JobDomain job) {
		final JobDTO dto = new JobDTO();

		dto.setKey(job.getKey());
		dto.setJobType(job.getJobType());
		dto.setWorkflowInstanceKey(job.getWorkflowInstanceKey());
		dto.setElementInstanceKey(job.getElementInstanceKey());
		dto.setState(job.getState());
		dto.setRetries(job.getRetries());
		Optional.ofNullable(job.getWorker()).ifPresent(dto::setWorker);
		dto.setTimestamp(Instant.ofEpochMilli(job.getTimestamp()).toString());

		return dto;
	}
	
	public static IncidentListDTO toDto(IncidentDomain incident) {
		final IncidentListDTO dto = new IncidentListDTO();
		dto.setKey(incident.getKey());

		dto.setBpmnProcessId(incident.getBpmnProcessId());
		dto.setWorkflowKey(incident.getWorkflowKey());
		;
		dto.setWorkflowInstanceKey(incident.getWorkflowInstanceKey());

		dto.setErrorType(incident.getErrorType());
		dto.setErrorMessage(incident.getErrorMessage());

		final boolean isResolved = incident.getResolved() != null && incident.getResolved() > 0;

		dto.setCreatedTime(Instant.ofEpochMilli(incident.getCreated()).toString());

		if (isResolved) {
			dto.setResolvedTime(Instant.ofEpochMilli(incident.getResolved()).toString());

			dto.setState("Resolved");
		} else {
			dto.setState("Created");
		}

		return dto;
	}
	
	public static MessageDTO toDto(MessageDomain message) {
		final MessageDTO dto = new MessageDTO();

		dto.setKey(message.getKey());
		dto.setName(message.getName());
		dto.setCorrelationKey(message.getCorrelationKey());
		dto.setMessageId(message.getMessageId());
		dto.setPayload(message.getPayload());
		dto.setState(message.getState());
		dto.setTimestamp(Instant.ofEpochMilli(message.getTimestamp()).toString());

		return dto;
	}
	
	public static List<BpmnElementInfo> getBpmnElementInfos(BpmnModelInstance bpmn) {
		final List<BpmnElementInfo> infos = new ArrayList<>();

		bpmn.getModelElementsByType(ServiceTask.class).forEach(t -> {
			final var info = new BpmnElementInfo();
			info.setElementId(t.getId());
			final var jobType = t.getSingleExtensionElement(ZeebeTaskDefinition.class).getType();
			info.setInfo("job-type: " + jobType);

			infos.add(info);
		});

		bpmn.getModelElementsByType(SequenceFlow.class).forEach(s -> {
			final var conditionExpression = s.getConditionExpression();

			if (conditionExpression != null && !conditionExpression.getTextContent().isEmpty()) {

				final var info = new BpmnElementInfo();
				info.setElementId(s.getId());
				final var condition = conditionExpression.getTextContent();
				info.setInfo("condition: " + condition);

				infos.add(info);
			}
		});

		bpmn.getModelElementsByType(CatchEvent.class).forEach(catchEvent -> {
			final var info = new BpmnElementInfo();
			info.setElementId(catchEvent.getId());

			catchEvent.getEventDefinitions().forEach(eventDefinition -> {
				if (eventDefinition instanceof ErrorEventDefinition) {
					final var errorEventDefinition = (ErrorEventDefinition) eventDefinition;
					final var errorCode = errorEventDefinition.getError().getErrorCode();

					info.setInfo("errorCode: " + errorCode);
					infos.add(info);
				}

				if (eventDefinition instanceof TimerEventDefinition) {
					final var timerEventDefinition = (TimerEventDefinition) eventDefinition;

					Optional.<ModelElementInstance>ofNullable(timerEventDefinition.getTimeCycle())
							.or(() -> Optional.ofNullable(timerEventDefinition.getTimeDate()))
							.or(() -> Optional.ofNullable(timerEventDefinition.getTimeDuration()))
							.map(ModelElementInstance::getTextContent).ifPresent(timer -> {
								info.setInfo("timer: " + timer);
								infos.add(info);
							});
				}
			});
		});

		return infos;
	}
	
	public static class VariableTuple {
		public final long scopeKey;
		public final String name;

		public VariableTuple(long scopeKey, String name) {
			this.scopeKey = scopeKey;
			this.name = name;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			VariableTuple that = (VariableTuple) o;
			return scopeKey == that.scopeKey && Objects.equals(name, that.name);
		}

		@Override
		public int hashCode() {
			return Objects.hash(scopeKey, name);
		}
	}

}
