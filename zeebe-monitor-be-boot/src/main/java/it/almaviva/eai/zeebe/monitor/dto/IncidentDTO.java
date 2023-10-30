package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class IncidentDTO {
	
	  private long key;

	  private String elementId;
	  private long elementInstanceKey;
	  private Long jobKey;

	  private String payload = "";

	  private String errorType;
	  private String errorMessage;

	  private String state = "";
	  private String createdTime = "";
	  private String resolvedTime = "";

	  private boolean isResolved;

}
