package it.almaviva.eai.zeebe.monitor.domain;

import lombok.Data;

@Data
public class WorkflowInstanceDomain {

	  private long key;

	  private int partitionId;

	  private long workflowKey;

	  private String bpmnProcessId;

	  private int version;

	  private String state;

	  private long start;

	  private Long end;

	  private Long parentWorkflowInstanceKey;

	  private Long parentElementInstanceKey;

	
}
