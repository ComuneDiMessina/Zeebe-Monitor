package it.almaviva.eai.zeebe.monitor.domain;

import lombok.Data;

@Data
public class TimerDomain {

	private long key;

	private long workflowKey;

	private Long workflowInstanceKey;

	private Long elementInstanceKey;

	private String targetFlowNodeId;

	private long dueDate;

	private int repetitions;

	private String state;

	private long timestamp;

}
