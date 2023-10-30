package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class CalledWorkflowInstanceDTO {
	
	  private String elementId;
	  private long elementInstanceKey;

	  private long childWorkflowInstanceKey;
	  private String childBpmnProcessId;

	  private String childState;

}
