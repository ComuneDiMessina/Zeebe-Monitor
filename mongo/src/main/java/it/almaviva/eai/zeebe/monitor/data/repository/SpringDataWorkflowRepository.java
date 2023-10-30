package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.WorkflowDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataWorkflowRepository extends MongoRepository<WorkflowDocument, Long> {
	
	Optional<WorkflowDocument> findByKey(long key);
		      

}
