package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class MessageDTO {
	
	  private long key;
	  private String name;
	  private String correlationKey;
	  private String messageId;

	  private String payload;

	  private String state;

	  private String timestamp = "";

}
