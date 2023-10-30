package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class MessageSubscriptionDTO {

	private String key = "";

	private String messageName = "";
	private String correlationKey = "";

	private String elementId = "";
	private Long elementInstanceKey;
	private Long workflowInstanceKey;

	private String state;
	private String timestamp = "";

	private boolean isOpen;

}
