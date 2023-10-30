package it.almaviva.eai.zeebe.monitor.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "INCIDENT")
public class IncidentDocument {
	
	  @Id
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
