package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

import java.util.Map;

@Data
public class UserTaskInfo {
	
	private String elementId;
	private String name;
	private Map<String, String> header;

}
