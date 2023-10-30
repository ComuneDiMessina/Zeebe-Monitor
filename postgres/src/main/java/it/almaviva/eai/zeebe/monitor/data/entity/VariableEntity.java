package it.almaviva.eai.zeebe.monitor.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "TAB_ZMO_VARIABLE")
public class VariableEntity {

	@Id
	@Column(name = "POSITION_")
	private Long position;

	@Column(name = "NAME_")
	private String name;

	@Column(name = "VALUE_", columnDefinition = "text")
	private String value;

	@Column(name = "WORKFLOW_INSTANCE_KEY_")
	private long workflowInstanceKey;

	@Column(name = "SCOPE_KEY_")
	private long scopeKey;

	@Column(name = "STATE_")
	private String state;

	@Column(name = "TIMESTAMP_")
	private long timestamp;

}
