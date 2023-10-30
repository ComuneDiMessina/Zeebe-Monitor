package it.almaviva.eai.zeebe.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.almaviva.eai.zeebe.monitor.domain.HazelcastConfigDomain;
import it.almaviva.eai.zeebe.monitor.port.incoming.IHazelcastUseCase;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IHazelcastPort;

@Service
public class HazelcastService implements IHazelcastUseCase {
	
	@Autowired
	private IHazelcastPort iHazelcastPort;

	@Override
	public HazelcastConfigDomain findById(String id) {
		return iHazelcastPort.findById(id);
	}

	@Override
	public void save(HazelcastConfigDomain hazelcastConfigDomain) {
		iHazelcastPort.save(hazelcastConfigDomain);
	}

}
