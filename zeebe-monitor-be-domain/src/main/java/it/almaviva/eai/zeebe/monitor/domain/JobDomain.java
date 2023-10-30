package it.almaviva.eai.zeebe.monitor.domain;

import lombok.Data;

@Data
public class JobDomain {

	  private long key;
	  
	  private long workflowInstanceKey;

	  private long elementInstanceKey;

	  private String jobType;

	  private String worker;

	  private String state;

	  private int retries;

	  private long timestamp;
	  
}
