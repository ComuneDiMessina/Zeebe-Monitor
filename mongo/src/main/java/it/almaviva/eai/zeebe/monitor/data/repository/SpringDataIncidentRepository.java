package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.IncidentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataIncidentRepository extends MongoRepository<IncidentDocument, Long> {
	
	Iterable<IncidentDocument> findByWorkflowInstanceKey(long workflowInstanceKey);

	Iterable<IncidentDocument> findByResolvedIsNull();

	long countByResolvedIsNull();

}
