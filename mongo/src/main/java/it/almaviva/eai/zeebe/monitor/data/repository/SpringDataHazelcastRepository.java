package it.almaviva.eai.zeebe.monitor.data.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.almaviva.eai.zeebe.monitor.data.entity.HazelcastDocument;

public interface SpringDataHazelcastRepository extends MongoRepository<HazelcastDocument, String> {
	
	Optional<HazelcastDocument> findById(String id);

}
