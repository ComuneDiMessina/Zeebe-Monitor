package it.almaviva.eai.zeebe.monitor.domain;

import lombok.Data;

@Data
public class MessageDomain {
	
	  private long key;

	  private String name;

	  private String correlationKey;

	  private String messageId;

	  private String payload;

	  private String state;

	  private long timestamp;

}
