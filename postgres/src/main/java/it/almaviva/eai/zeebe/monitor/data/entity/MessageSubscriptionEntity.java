package it.almaviva.eai.zeebe.monitor.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "TAB_ZMO_MESSAGE_SUBSCRIPTION")
public class MessageSubscriptionEntity {

	@Id
	@Column(name = "ID_")
	private String id;

	@Column(name = "MESSAGE_NAME_")
	private String messageName;

	@Column(name = "CORRELATION_KEY_")
	private String correlationKey;

	@Column(name = "WORKFLOW_INSTANCE_KEY_")
	private Long workflowInstanceKey;

	@Column(name = "ELEMENT_INSTANCE_KEY_")
	private Long elementInstanceKey;

	@Column(name = "WORKFLOW_KEY_")
	private Long workflowKey;

	@Column(name = "TARGET_FLOW_NODE_ID_")
	private String targetFlowNodeId;

	@Column(name = "STATE_")
	private String state;

	@Column(name = "TIMESTAMP_")
	private long timestamp;

}
