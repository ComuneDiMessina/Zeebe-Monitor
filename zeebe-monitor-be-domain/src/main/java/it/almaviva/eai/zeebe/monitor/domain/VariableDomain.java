package it.almaviva.eai.zeebe.monitor.domain;

import lombok.Data;

@Data
public class VariableDomain {
	
	  private Long position;

	  private String name;

	  private String value;

	  private long workflowInstanceKey;

	  private long scopeKey;

	  private String state;

	  private long timestamp;

}
