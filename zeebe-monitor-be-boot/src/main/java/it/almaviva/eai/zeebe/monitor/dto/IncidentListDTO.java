package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class IncidentListDTO {
	
	  private long key;

	  private String bpmnProcessId;
	  private long workflowInstanceKey;
	  private long workflowKey;

	  private String errorType;
	  private String errorMessage;

	  private String state = "";
	  private String createdTime = "";
	  private String resolvedTime = "";

}
