package it.almaviva.eai.zeebe.monitor.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "TAB_ZMO_WORKFLOW_INSTANCE")
public class WorkflowInstanceEntity {
	
	
	  @Id
	  @Column(name = "KEY_")
	  private long key;

	  @Column(name = "PARTITION_ID_")
	  private int partitionId;

	  @Column(name = "WORKFLOW_KEY_")
	  private long workflowKey;

	  @Column(name = "BPMN_PROCESS_ID_")
	  private String bpmnProcessId;

	  @Column(name = "VERSION_")
	  private int version;

	  @Column(name = "STATE_")
	  private String state;

	  @Column(name = "START_")
	  private long start;

	  @Column(name = "END_")
	  private Long end;

	  @Column(name = "PARENT_WORKFLOW_INSTANCE_KEY_")
	  private Long parentWorkflowInstanceKey;

	  @Column(name = "PARENT_ELEMENT_INSTANCE_KEY_")
	  private Long parentElementInstanceKey;
	

}
