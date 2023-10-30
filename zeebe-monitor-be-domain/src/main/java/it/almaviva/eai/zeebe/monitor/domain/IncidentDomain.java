package it.almaviva.eai.zeebe.monitor.domain;

import lombok.Data;

@Data
public class IncidentDomain {
	
	  private long key;

	  private String bpmnProcessId;

	  private long workflowKey;
	  
	  private long workflowInstanceKey;

	  private long elementInstanceKey;

	  private long jobKey;

	  private String errorType;

	  private String errorMessage;

	  private long created;

	  private Long resolved;

}
