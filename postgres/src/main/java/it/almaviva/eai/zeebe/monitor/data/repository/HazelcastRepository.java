package it.almaviva.eai.zeebe.monitor.data.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.almaviva.eai.zeebe.monitor.data.entity.HazelcastEntity;
import it.almaviva.eai.zeebe.monitor.data.mapper.IHazelcastMapper;
import it.almaviva.eai.zeebe.monitor.domain.HazelcastConfigDomain;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IHazelcastPort;

@Component
public class HazelcastRepository implements IHazelcastPort {
	
	@Autowired
	private SpringDataHazelcastRepository springDataHazelcastRepository;
	
	@Autowired
	private IHazelcastMapper iHazelcastMapper;

	@Override
	public HazelcastConfigDomain findById(String id) {
        Optional<HazelcastEntity> byId = springDataHazelcastRepository.findById(id);
        HazelcastEntity hazelcastEntity = byId.orElseGet(() -> {return null;});
		return iHazelcastMapper.map(hazelcastEntity);
	}

	@Override
	public void save(HazelcastConfigDomain hazelcastConfigDomain) {
		
		HazelcastEntity hazelcastEntity = new HazelcastEntity();
		hazelcastEntity.setId(hazelcastConfigDomain.getId());
		hazelcastEntity.setSequence(hazelcastConfigDomain.getSequence());
		springDataHazelcastRepository.save(hazelcastEntity);
		
	}
	
	

}
