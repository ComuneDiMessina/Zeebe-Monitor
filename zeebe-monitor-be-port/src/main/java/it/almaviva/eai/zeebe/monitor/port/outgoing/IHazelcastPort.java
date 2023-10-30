package it.almaviva.eai.zeebe.monitor.port.outgoing;

import it.almaviva.eai.zeebe.monitor.domain.HazelcastConfigDomain;

public interface IHazelcastPort {

	HazelcastConfigDomain findById(String id);
	
	void save(HazelcastConfigDomain hazelcastConfigDomain);
	
}
