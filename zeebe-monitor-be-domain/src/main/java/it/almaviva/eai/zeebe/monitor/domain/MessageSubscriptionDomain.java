package it.almaviva.eai.zeebe.monitor.domain;

import lombok.Data;

@Data
public class MessageSubscriptionDomain {

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
