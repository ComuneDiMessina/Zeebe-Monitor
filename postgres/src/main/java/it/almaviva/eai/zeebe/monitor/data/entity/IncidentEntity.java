package it.almaviva.eai.zeebe.monitor.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "TAB_ZMO_INCIDENT")
public class IncidentEntity {

	  @Id
	  @Column(name = "KEY_")
	  private long key;

	  @Column(name = "BPMN_PROCESS_ID_")
	  private String bpmnProcessId;

	  @Column(name = "WORKFLOW_KEY_")
	  private long workflowKey;

	  @Column(name = "WORKFLOW_INSTANCE_KEY_")
	  private long workflowInstanceKey;

	  @Column(name = "ELEMENT_INSTANCE_KEY_")
	  private long elementInstanceKey;

	  @Column(name = "JOB_KEY_")
	  private long jobKey;

	  @Column(name = "ERROR_TYPE_")
	  private String errorType;

	  @Column(name = "ERROR_MSG_", columnDefinition = "text")
	  private String errorMessage;

	  @Column(name = "CREATED_")
	  private long created;

	  @Column(name = "RESOLVED_")
	  private Long resolved;
	
}
