package it.almaviva.eai.zeebe.monitor.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "TIMER")
public class TimerDocument {
	
	@Id
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
