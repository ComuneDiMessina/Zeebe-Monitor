package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class AuditLogEntry {
	
	  private long key;
	  private long flowScopeKey;

	  private String elementId;
	  private String elementName;

	  private String bpmnElementType;

	  private String paylaod;

	  private String state;
	  private String timestamp;

}
