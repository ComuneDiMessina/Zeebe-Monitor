package it.almaviva.eai.zeebe.monitor.domain;

import lombok.Data;

@Data
public class ElementInstanceDomain {
	
	  private Long position;

	  private int partitionId;

	  private long key;

	  private String intent;

	  private long workflowInstanceKey;

	  private String elementId;

	  private String bpmnElementType;

	  private long flowScopeKey;

	  private long workflowKey;

	  private long timestamp;

}
