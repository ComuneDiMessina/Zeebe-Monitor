package it.almaviva.eai.zeebe.monitor.data.entity;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "MESSAGE_SUBSCRIPTION")
public class MessageSubscriptionDocument {
	
	@Id
	private String id;

	private String messageName;

	private String correlationKey;

	private Long workflowInstanceKey;

	private Long elementInstanceKey;

	private Long workflowKey;

	private String targetFlowNodeId;

	private String state;

	private long timestamp;

}
