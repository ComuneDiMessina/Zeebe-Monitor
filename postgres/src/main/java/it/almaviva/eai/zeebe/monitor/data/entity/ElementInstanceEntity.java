package it.almaviva.eai.zeebe.monitor.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "TAB_ZMO_ELEMENT_INSTANCE")
public class ElementInstanceEntity {
	
	  @Id
	  @Column(name = "POSITION_")
	  private Long position;

	  @Column(name = "PARTITION_ID_")
	  private int partitionId;

	  @Column(name = "KEY_")
	  private long key;

	  @Column(name = "INTENT_")
	  private String intent;

	  @Column(name = "WORKFLOW_INSTANCE_KEY_")
	  private long workflowInstanceKey;

	  @Column(name = "ELEMENT_ID_")
	  private String elementId;

	  @Column(name = "BPMN_ELEMENT_TYPE_")
	  private String bpmnElementType;

	  @Column(name = "FLOW_SCOPE_KEY_")
	  private long flowScopeKey;

	  @Column(name = "WORKFLOW_KEY_")
	  private long workflowKey;

	  @Column(name = "TIMESTAMP_")
	  private long timestamp;

}
