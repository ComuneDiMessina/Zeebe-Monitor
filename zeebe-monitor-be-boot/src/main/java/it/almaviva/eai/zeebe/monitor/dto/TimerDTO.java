package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

@Data
public class TimerDTO {

	private String elementId = "";
	private Long elementInstanceKey;

	private String dueDate = "";
	private String repetitions = "";

	private String state;
	private String timestamp = "";

}
