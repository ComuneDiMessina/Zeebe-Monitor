package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class ElementInstanceState {

	private String elementId;
	private long activeInstances;
	private long endedInstances;

}
