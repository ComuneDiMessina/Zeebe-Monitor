package it.almaviva.eai.zeebe.monitor.port.incoming;

import it.almaviva.eai.zeebe.monitor.domain.HazelcastConfigDomain;

public interface IHazelcastUseCase {
	
	HazelcastConfigDomain findById(String id);
	
	void save(HazelcastConfigDomain hazelcastConfigDomain);

}
