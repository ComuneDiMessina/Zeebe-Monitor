package it.almaviva.eai.zeebe.monitor.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "TAB_ZMO_WORKFLOW")
public class WorkflowEntity {
	
	  @Id
	  @Column(name = "KEY_")
	  private long key;

	  @Column(name = "BPMN_PROCESS_ID_")
	  private String bpmnProcessId;

	  @Column(name = "VERSION_")
	  private int version;

	  @Column(name = "RESOURCE_", columnDefinition = "text")
	  private String resource;

	  @Column(name = "TIMESTAMP_")
	  private long timestamp;


}
