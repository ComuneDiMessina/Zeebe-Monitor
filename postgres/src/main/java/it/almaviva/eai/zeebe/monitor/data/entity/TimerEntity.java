package it.almaviva.eai.zeebe.monitor.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "TAB_ZMO_TIMER")
public class TimerEntity {

	@Id
	@Column(name = "KEY_")
	private long key;

	@Column(name = "WORKFLOW_KEY_")
	private long workflowKey;

	@Column(name = "WORKFLOW_INSTANCE_KEY_")
	private Long workflowInstanceKey;

	@Column(name = "ELEMENT_INSTANCE_KEY_")
	private Long elementInstanceKey;

	@Column(name = "TARGET_FLOW_NODE_ID_")
	private String targetFlowNodeId;

	@Column(name = "DUE_DATE_")
	private long dueDate;

	@Column(name = "REPETITIONS")
	private int repetitions;

	@Column(name = "STATE_")
	private String state;

	@Column(name = "TIMESTAMP_")
	private long timestamp;

}
