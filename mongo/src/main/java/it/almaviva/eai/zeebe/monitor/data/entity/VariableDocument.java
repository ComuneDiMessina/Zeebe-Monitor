package it.almaviva.eai.zeebe.monitor.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "VARIABLE")
public class VariableDocument {
	
	@Id
	private Long position;

	private String name;

	private String value;

	private long workflowInstanceKey;

	private long scopeKey;

	private String state;

	private long timestamp;

}
