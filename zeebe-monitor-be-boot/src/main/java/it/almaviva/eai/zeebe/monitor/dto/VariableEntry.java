package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VariableEntry {
	
	  private String name;
	  private String value;
	  private long scopeKey;
	  private String elementId;
	  private String timestamp;

	  private List<VariableUpdateEntry> updates = new ArrayList<>();

}
