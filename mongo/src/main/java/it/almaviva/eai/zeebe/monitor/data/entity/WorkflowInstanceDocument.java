package it.almaviva.eai.zeebe.monitor.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "WORKFLOW_INSTANCE")
public class WorkflowInstanceDocument {
	
	  @Id
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
