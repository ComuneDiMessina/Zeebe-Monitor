package it.almaviva.eai.zeebe.monitor.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "JOB")
public class JobDocument {


	  @Id
	  private long key;

	  private long workflowInstanceKey;

	  private long elementInstanceKey;

	  private String jobType;

	  private String worker;

	  private String state;

	  private int retries;

	  private long timestamp;
}
