package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class WorkflowInstanceNotification {

	private long workflowInstanceKey;
	private long workflowKey;

	private Type type;

	public enum Type {
		CREATED, UPDATED, REMOVED
	}

}
