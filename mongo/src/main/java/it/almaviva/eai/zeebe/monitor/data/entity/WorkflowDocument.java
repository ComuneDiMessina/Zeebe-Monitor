package it.almaviva.eai.zeebe.monitor.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "WORKFLOW")
public class WorkflowDocument {
	
	  @Id
	  private long key;

	  private String bpmnProcessId;

	  private int version;

	  private String resource;

	  private long timestamp;

}
