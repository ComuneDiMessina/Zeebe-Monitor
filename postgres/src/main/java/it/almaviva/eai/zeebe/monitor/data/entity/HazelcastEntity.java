package it.almaviva.eai.zeebe.monitor.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "TAB_ZMO_HAZELCAST")
public final class HazelcastEntity {
	
	  @Id
	  private String id;
	  
	  private long sequence;
	

}
