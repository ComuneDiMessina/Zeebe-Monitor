package it.almaviva.eai.zeebe.monitor.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "ELEMENT_INSTANCE")
public class ElementInstanceDocument {
	
	
	  @Id
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
