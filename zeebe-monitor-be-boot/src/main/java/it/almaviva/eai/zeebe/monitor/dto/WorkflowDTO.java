package it.almaviva.eai.zeebe.monitor.dto;

import it.almaviva.eai.zeebe.monitor.domain.WorkflowDomain;
import lombok.Data;

@Data
public class WorkflowDTO {
	
	  private long workflowKey;
	  private String bpmnProcessId;
	  private int version;
	  private String resource;

	  private long countRunning;
	  private long countEnded;
	  
	  public static WorkflowDTO from(WorkflowDomain workflowDomain, long countRunning, long countEnded) {
		    final WorkflowDTO dto = new WorkflowDTO();

		    dto.workflowKey = workflowDomain.getKey();
		    dto.bpmnProcessId = workflowDomain.getBpmnProcessId();
		    dto.version = workflowDomain.getVersion();

		    dto.countRunning = countRunning;
		    dto.countEnded = countEnded;

		    return dto;
		  }

}
