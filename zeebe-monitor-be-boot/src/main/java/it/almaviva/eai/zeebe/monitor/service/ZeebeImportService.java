package it.almaviva.eai.zeebe.monitor.service;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;

import io.zeebe.exporter.proto.Schema;
import io.zeebe.hazelcast.connect.java.ZeebeHazelcast;
import io.zeebe.protocol.Protocol;
import io.zeebe.protocol.record.intent.DeploymentIntent;
import io.zeebe.protocol.record.intent.IncidentIntent;
import io.zeebe.protocol.record.intent.Intent;
import io.zeebe.protocol.record.intent.JobIntent;
import io.zeebe.protocol.record.intent.MessageIntent;
import io.zeebe.protocol.record.intent.MessageStartEventSubscriptionIntent;
import io.zeebe.protocol.record.intent.MessageSubscriptionIntent;
import io.zeebe.protocol.record.intent.TimerIntent;
import io.zeebe.protocol.record.intent.WorkflowInstanceIntent;
import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceDomain;
import it.almaviva.eai.zeebe.monitor.domain.HazelcastConfigDomain;
import it.almaviva.eai.zeebe.monitor.domain.IncidentDomain;
import it.almaviva.eai.zeebe.monitor.domain.JobDomain;
import it.almaviva.eai.zeebe.monitor.domain.MessageDomain;
import it.almaviva.eai.zeebe.monitor.domain.MessageSubscriptionDomain;
import it.almaviva.eai.zeebe.monitor.domain.TimerDomain;
import it.almaviva.eai.zeebe.monitor.domain.VariableDomain;
import it.almaviva.eai.zeebe.monitor.domain.WorkflowDomain;
import it.almaviva.eai.zeebe.monitor.domain.WorkflowInstanceDomain;
import it.almaviva.eai.zeebe.monitor.port.incoming.IElementInstanceUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IHazelcastUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IIncidentUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IJobUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IMessageSubscriptionUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IMessageUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.ITimerUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IVariableUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IWorkflowInstanceUseCase;
import it.almaviva.eai.zeebe.monitor.port.incoming.IWorkflowUseCase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ZeebeImportService {

	@Autowired
	private IWorkflowUseCase iWorkflowUseCase;
    @Autowired
    private IWorkflowInstanceUseCase iWorkflowInstanceUseCase;
    @Autowired
    private IElementInstanceUseCase elementInstanceUseCase;
    @Autowired
    private IVariableUseCase iVariableUseCase;
    @Autowired
    private IJobUseCase iJobUseCase;
    @Autowired
    private IIncidentUseCase incidentUseCase;
    @Autowired
    private IMessageUseCase iMessageUseCase;
    @Autowired
    private IMessageSubscriptionUseCase iMessageSubscriptionUseCase;
    @Autowired
    private ITimerUseCase iTimerUseCase;
    @Autowired
    private IHazelcastUseCase iHazelcastUseCase;

    @Autowired
    private ZeebeNotificationService notificationService;
    
    @Value("${mail.active}")
    String active;

    public ZeebeHazelcast importFrom(HazelcastInstance hazelcast) {
    	
    	final var hazelcastConfig =
    			Optional.ofNullable(iHazelcastUseCase
    	            .findById("cfg"))
    	            .orElseGet(
    	                () -> {
    	                  final var config = new HazelcastConfigDomain();
    	                  config.setId("cfg");
    	                  config.setSequence(-1);
    	                  return config;
    	                });
    	final var builder =  ZeebeHazelcast.newBuilder(hazelcast)
                .addDeploymentListener(
                        record ->
                            withKey(record, Schema.DeploymentRecord::getMetadata, this::importDeployment))
                    .addWorkflowInstanceListener(
                        record ->
                            withKey(
                                record,
                                Schema.WorkflowInstanceRecord::getMetadata,
                                this::importWorkflowInstance))
                    .addIncidentListener(
                        record -> withKey(record, Schema.IncidentRecord::getMetadata, this::importIncident))
                    .addJobListener(
                        record -> withKey(record, Schema.JobRecord::getMetadata, this::importJob))
                    .addVariableListener(
                        record -> withKey(record, Schema.VariableRecord::getMetadata, this::importVariable))
                    .addTimerListener(
                        record -> withKey(record, Schema.TimerRecord::getMetadata, this::importTimer))
                    .addMessageListener(
                        record -> withKey(record, Schema.MessageRecord::getMetadata, this::importMessage))
                    .addMessageSubscriptionListener(this::importMessageSubscription)
                    .addMessageStartEventSubscriptionListener(this::importMessageStartEventSubscription)
                    .postProcessListener(
                        sequence -> {
                          hazelcastConfig.setSequence(sequence);
                          iHazelcastUseCase.save(hazelcastConfig);
                        });
    	
    	if (hazelcastConfig.getSequence() >= 0) {
    	      builder.readFrom(hazelcastConfig.getSequence());
    	    } else {
    	      builder.readFromHead();
    	    }

    	    return builder.build();
    }
    
    private <T> void withKey(
    	      T record, Function<T, Schema.RecordMetadata> extractor, Consumer<T> consumer) {
    	    final var metadata = extractor.apply(record);
    	    if (!hasKey(metadata)) {
    	      consumer.accept(record);
    	    }
    	  }
    
    private boolean hasKey(Schema.RecordMetadata metadata) {
        return metadata.getKey() < 0;
      }

    private void importDeployment(final Schema.DeploymentRecord record) {

        final DeploymentIntent intent = DeploymentIntent.valueOf(record.getMetadata().getIntent());
        final int partitionId = record.getMetadata().getPartitionId();

        if (intent != DeploymentIntent.CREATED  || partitionId != Protocol.DEPLOYMENT_PARTITION) {
            // ignore deployment event on other partitions to avoid duplicates
            return;
        }

        record
                .getResourcesList()
                .forEach(
                        resource -> {
                            record.getDeployedWorkflowsList().stream()
                                    .filter(w -> w.getResourceName().equals(resource.getResourceName()))
                                    .forEach(
                                            deployedWorkflow -> {
                                                final WorkflowDomain workflowDTO = new WorkflowDomain();
                                                workflowDTO.setKey(deployedWorkflow.getWorkflowKey());
                                                workflowDTO.setBpmnProcessId(deployedWorkflow.getBpmnProcessId());
                                                workflowDTO.setVersion(deployedWorkflow.getVersion());
                                                workflowDTO.setResource(resource.getResource().toStringUtf8());
                                                workflowDTO.setTimestamp(record.getMetadata().getTimestamp());
                                                iWorkflowUseCase.save(workflowDTO);
                                            });
                        });
    }

    private void importWorkflowInstance(final Schema.WorkflowInstanceRecord record) {
        if (record.getWorkflowInstanceKey() == record.getMetadata().getKey()) {
            addOrUpdateWorkflowInstance(record);
        }
        addElementInstance(record);
    }

    private void addOrUpdateWorkflowInstance(final Schema.WorkflowInstanceRecord record) {

        final Intent intent = WorkflowInstanceIntent.valueOf(record.getMetadata().getIntent());
        final long timestamp = record.getMetadata().getTimestamp();
        final long workflowInstanceKey = record.getWorkflowInstanceKey();

        final WorkflowInstanceDomain workflowInstanceDTO =
        		Optional.ofNullable(iWorkflowInstanceUseCase
                        .findByKey(workflowInstanceKey))
                        .orElseGet(
                                () -> {
                                    final WorkflowInstanceDomain newDTO = new WorkflowInstanceDomain();
                                    newDTO.setPartitionId(record.getMetadata().getPartitionId());
                                    newDTO.setKey(workflowInstanceKey);
                                    newDTO.setBpmnProcessId(record.getBpmnProcessId());
                                    newDTO.setVersion(record.getVersion());
                                    newDTO.setWorkflowKey(record.getWorkflowKey());
                                    newDTO.setParentWorkflowInstanceKey(record.getParentWorkflowInstanceKey());
                                    newDTO.setParentElementInstanceKey(record.getParentElementInstanceKey());
                                    return newDTO;
                                });

        if (intent == WorkflowInstanceIntent.ELEMENT_ACTIVATED) {
        	workflowInstanceDTO.setState("Active");
        	workflowInstanceDTO.setStart(timestamp);
        	iWorkflowInstanceUseCase.save(workflowInstanceDTO);

            notificationService.sendCreatedWorkflowInstance(
                    record.getWorkflowInstanceKey(), record.getWorkflowKey());

        } else if (intent == WorkflowInstanceIntent.ELEMENT_COMPLETED) {
        	workflowInstanceDTO.setState("Completed");
        	workflowInstanceDTO.setEnd(timestamp);
        	iWorkflowInstanceUseCase.save(workflowInstanceDTO);

            notificationService.sendEndedWorkflowInstance(
                    record.getWorkflowInstanceKey(), record.getWorkflowKey());

        } else if (intent == WorkflowInstanceIntent.ELEMENT_TERMINATED) {
        	workflowInstanceDTO.setState("Terminated");
        	workflowInstanceDTO.setEnd(timestamp);
        	iWorkflowInstanceUseCase.save(workflowInstanceDTO);

            notificationService.sendEndedWorkflowInstance(
                    record.getWorkflowInstanceKey(), record.getWorkflowKey());
        }
    }

    private void addElementInstance(final Schema.WorkflowInstanceRecord record) {

        final long position = record.getMetadata().getPosition();
        if (!elementInstanceUseCase.existsById(position)) {

            final ElementInstanceDomain elementInstanceDTO = new ElementInstanceDomain();
            elementInstanceDTO.setPosition(position);
            elementInstanceDTO.setPartitionId(record.getMetadata().getPartitionId());
            elementInstanceDTO.setKey(record.getMetadata().getKey());
            elementInstanceDTO.setIntent(record.getMetadata().getIntent());
            elementInstanceDTO.setTimestamp(record.getMetadata().getTimestamp());
            elementInstanceDTO.setWorkflowInstanceKey(record.getWorkflowInstanceKey());
            elementInstanceDTO.setElementId(record.getElementId());
            elementInstanceDTO.setFlowScopeKey(record.getFlowScopeKey());
            elementInstanceDTO.setWorkflowKey(record.getWorkflowKey());
            elementInstanceDTO.setBpmnElementType(record.getBpmnElementType().name());

            elementInstanceUseCase.save(elementInstanceDTO);

            notificationService.sendWorkflowInstanceUpdated(
                    record.getWorkflowInstanceKey(), record.getWorkflowKey());
        }
    }

    private void importIncident(final Schema.IncidentRecord record) {

        final IncidentIntent intent = IncidentIntent.valueOf(record.getMetadata().getIntent());
        final long key = record.getMetadata().getKey();
        final long timestamp = record.getMetadata().getTimestamp();

        final IncidentDomain incidentDTO =
        		Optional.ofNullable(incidentUseCase
                        .findById(key))
                        .orElseGet(
                                () -> {
                                    final IncidentDomain newDTO = new IncidentDomain();
                                    newDTO.setKey(key);
                                    newDTO.setBpmnProcessId(record.getBpmnProcessId());
                                    newDTO.setWorkflowKey(record.getWorkflowKey());
                                    newDTO.setWorkflowInstanceKey(record.getWorkflowInstanceKey());
                                    newDTO.setElementInstanceKey(record.getElementInstanceKey());
                                    newDTO.setJobKey(record.getJobKey());
                                    newDTO.setErrorType(record.getErrorType());
                                    newDTO.setErrorMessage(record.getErrorMessage());
                                    return newDTO;
                                });

        if (intent == IncidentIntent.CREATED) {
        	incidentDTO.setCreated(timestamp);
        	incidentUseCase.save(incidentDTO);
        	try {
        		if(active.equalsIgnoreCase("true"))
              incidentUseCase.sendNotification(incidentDTO);
				
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}


        } else if (intent == IncidentIntent.RESOLVED) {
        	incidentDTO.setResolved(timestamp);
        	incidentUseCase.save(incidentDTO);
        }
    }

    private void importJob(final Schema.JobRecord record) {

        final JobIntent intent = JobIntent.valueOf(record.getMetadata().getIntent());
        final long key = record.getMetadata().getKey();
        final long timestamp = record.getMetadata().getTimestamp();

        final JobDomain jobDomain =
                Optional.ofNullable(iJobUseCase
                        .findByKey(key))
                        .orElseGet(
                                () -> {
                                    final JobDomain newDTO = new JobDomain();
                                    newDTO.setKey(key);
                                    newDTO.setWorkflowInstanceKey(record.getWorkflowInstanceKey());
                                    newDTO.setElementInstanceKey(record.getElementInstanceKey());
                                    newDTO.setJobType(record.getType());
                                    return newDTO;
                                });

        jobDomain.setState(intent.name().toLowerCase());
        jobDomain.setTimestamp(timestamp);
        jobDomain.setWorker(record.getWorker());
        jobDomain.setRetries(record.getRetries());
        iJobUseCase.save(jobDomain);
    }

    private void importMessage(final Schema.MessageRecord record) {

        final MessageIntent intent = MessageIntent.valueOf(record.getMetadata().getIntent());
        final long key = record.getMetadata().getKey();
        final long timestamp = record.getMetadata().getTimestamp();

        final MessageDomain messageDomain =
        		Optional.ofNullable(iMessageUseCase
                        .findById(key))
                        .orElseGet(
                                () -> {
                                    final MessageDomain newDTO = new MessageDomain();
                                    newDTO.setKey(key);
                                    newDTO.setName(record.getName());
                                    newDTO.setCorrelationKey(record.getCorrelationKey());
                                    newDTO.setMessageId(record.getMessageId());
                                    newDTO.setPayload(record.getVariables().toString());
                                    return newDTO;
                                });

        messageDomain.setState(intent.name().toLowerCase());
        messageDomain.setTimestamp(timestamp);
        iMessageUseCase.save(messageDomain);
    }

    private void importMessageSubscription(final Schema.MessageSubscriptionRecord record) {

        final MessageSubscriptionIntent intent =
                MessageSubscriptionIntent.valueOf(record.getMetadata().getIntent());
        final long timestamp = record.getMetadata().getTimestamp();

        final MessageSubscriptionDomain messageSubscriptionDomain =
        		Optional.ofNullable(iMessageSubscriptionUseCase
                        .findByElementInstanceKeyAndMessageName(
                                record.getElementInstanceKey(), record.getMessageName()))
                        .orElseGet(
                                () -> {
                                    final MessageSubscriptionDomain newDTO = new MessageSubscriptionDomain();
                                    newDTO.setId(
                                            generateId()); // message subscription doesn't have a key - it is always '-1'
                                    newDTO.setElementInstanceKey(record.getElementInstanceKey());
                                    newDTO.setMessageName(record.getMessageName());
                                    newDTO.setCorrelationKey(record.getCorrelationKey());
                                    newDTO.setWorkflowInstanceKey(record.getWorkflowInstanceKey());
                                    return newDTO;
                                });

        messageSubscriptionDomain.setState(intent.name().toLowerCase());
        messageSubscriptionDomain.setTimestamp(timestamp);
        iMessageSubscriptionUseCase.save(messageSubscriptionDomain);
    }

    private void importMessageStartEventSubscription(
            final Schema.MessageStartEventSubscriptionRecord record) {

        final MessageStartEventSubscriptionIntent intent =
                MessageStartEventSubscriptionIntent.valueOf(record.getMetadata().getIntent());
        final long timestamp = record.getMetadata().getTimestamp();

        final MessageSubscriptionDomain messageSubscriptionDomain =
        		Optional.ofNullable(iMessageSubscriptionUseCase
                        .findByWorkflowKeyAndMessageName(record.getWorkflowKey(), record.getMessageName()))
                        .orElseGet(
                                () -> {
                                    final MessageSubscriptionDomain newDTO = new MessageSubscriptionDomain();
                                    newDTO.setId(
                                            generateId()); // message subscription doesn't have a key - it is always '-1'
                                    newDTO.setMessageName(record.getMessageName());
                                    newDTO.setWorkflowKey(record.getWorkflowKey());
                                    newDTO.setTargetFlowNodeId(record.getStartEventId());
                                    return newDTO;
                                });

        messageSubscriptionDomain.setState(intent.name().toLowerCase());
        messageSubscriptionDomain.setTimestamp(timestamp);
        iMessageSubscriptionUseCase.save(messageSubscriptionDomain);
    }

    private void importTimer(final Schema.TimerRecord record) {

        final TimerIntent intent = TimerIntent.valueOf(record.getMetadata().getIntent());
        final long key = record.getMetadata().getKey();
        final long timestamp = record.getMetadata().getTimestamp();

        final TimerDomain timerDomain =
        		Optional.ofNullable(iTimerUseCase
                        .findById(key))
                        .orElseGet(
                                () -> {
                                    final TimerDomain newDTO = new TimerDomain();
                                    newDTO.setKey(key);
                                    newDTO.setWorkflowKey(record.getWorkflowKey());
                                    newDTO.setTargetFlowNodeId(record.getTargetFlowNodeId());
                                    newDTO.setDueDate(record.getDueDate());
                                    newDTO.setRepetitions(record.getRepetitions());

                                    if (record.getWorkflowInstanceKey() > 0) {
                                    	newDTO.setWorkflowInstanceKey(record.getWorkflowInstanceKey());
                                    	newDTO.setElementInstanceKey(record.getElementInstanceKey());
                                    }

                                    return newDTO;
                                });

        timerDomain.setState(intent.name().toLowerCase());
        timerDomain.setTimestamp(timestamp);
        iTimerUseCase.save(timerDomain);
    }

    private void importVariable(final Schema.VariableRecord record) {

        final long position = record.getMetadata().getPosition();
        if (!iVariableUseCase.existsById(position)) {

            final VariableDomain variableDomain = new VariableDomain();
            variableDomain.setPosition(position);
            variableDomain.setTimestamp(record.getMetadata().getTimestamp());
            variableDomain.setWorkflowInstanceKey(record.getWorkflowInstanceKey());
            variableDomain.setName(record.getName());
            variableDomain.setValue(record.getValue());
            variableDomain.setScopeKey(record.getScopeKey());
            variableDomain.setState(record.getMetadata().getIntent().toLowerCase());
            iVariableUseCase.save(variableDomain);
        }
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }
}
