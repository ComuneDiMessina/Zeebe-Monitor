package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class JobDTO {
	
	  private long key;
	  private String jobType;
	  private String state = "";
	  private String worker = "";
	  private int retries;

	  private String elementId = "";
	  private long elementInstanceKey;
	  private long workflowInstanceKey;

	  private String timestamp = "";

	  private boolean isActivatable;

}
