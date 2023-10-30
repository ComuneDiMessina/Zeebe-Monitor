package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class ResolveIncidentDTO {
	

	  private long incidentKey;

	  private Long jobKey;
	  private int remainingRetries;

}
