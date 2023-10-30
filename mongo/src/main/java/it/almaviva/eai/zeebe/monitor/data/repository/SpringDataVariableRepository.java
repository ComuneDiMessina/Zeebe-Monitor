package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.VariableDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpringDataVariableRepository extends MongoRepository<VariableDocument, Long> {

	List<VariableDocument> findByWorkflowInstanceKey(long workflowInstanceKey);
}
