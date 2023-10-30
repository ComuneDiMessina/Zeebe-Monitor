package it.almaviva.eai.zeebe.monitor.data.entity;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "HAZELCAST")
public class HazelcastDocument {
	 
	  @Id
	  private String id;
	  
	  private long sequence;

}
