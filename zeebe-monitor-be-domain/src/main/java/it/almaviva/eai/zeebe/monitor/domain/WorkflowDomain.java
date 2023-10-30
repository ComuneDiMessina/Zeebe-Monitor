package it.almaviva.eai.zeebe.monitor.domain;

import lombok.Data;

@Data
public class WorkflowDomain {
	
	private long key;
	
	private String bpmnProcessId;
	
	private int version;
	
	private String resource;
	
	private long timestamp;
	
}
