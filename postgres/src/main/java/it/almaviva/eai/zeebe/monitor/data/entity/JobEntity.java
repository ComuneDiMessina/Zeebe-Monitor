package it.almaviva.eai.zeebe.monitor.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "TAB_ZMO_JOB")
public class JobEntity {
	
	  @Id
	  @Column(name = "KEY_")
	  private long key;

	  @Column(name = "WORKFLOW_INSTANCE_KEY_")
	  private long workflowInstanceKey;

	  @Column(name = "ELEMENT_INSTANCE_KEY_")
	  private long elementInstanceKey;

	  @Column(name = "JOB_TYPE_")
	  private String jobType;

	  @Column(name = "WORKER_")
	  private String worker;

	  @Column(name = "STATE_")
	  private String state;

	  @Column(name = "RETRIES_")
	  private int retries;

	  @Column(name = "TIMESTAMP_")
	  private long timestamp;

}
