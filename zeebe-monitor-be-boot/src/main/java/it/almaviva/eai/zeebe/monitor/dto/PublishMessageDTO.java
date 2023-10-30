package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class PublishMessageDTO {

	  private String name;
	  private String correlationKey;
	  private String payload;
	  private String timeToLive;
}
