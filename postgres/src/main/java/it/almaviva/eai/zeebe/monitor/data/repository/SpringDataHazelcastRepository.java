package it.almaviva.eai.zeebe.monitor.data.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.almaviva.eai.zeebe.monitor.data.entity.HazelcastEntity;

@Repository
public interface SpringDataHazelcastRepository extends CrudRepository<HazelcastEntity,String> {
	
	Optional<HazelcastEntity> findById(String id);

}
