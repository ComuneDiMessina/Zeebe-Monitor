package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class WorkflowInstanceListDto {

	private long workflowInstanceKey;

	private String bpmnProcessId;
	private long workflowKey;

	private String state;

	private String startTime = "";
	private String endTime = "";

}
