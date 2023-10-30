package it.almaviva.eai.zeebe.monitor.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "MESSAGE")
public class MessageDocument {
	
	  @Id
	  private long key;

	  private String name;

	  private String correlationKey;

	  private String messageId;

	  private String payload;

	  private String state;

	  private long timestamp;

}
