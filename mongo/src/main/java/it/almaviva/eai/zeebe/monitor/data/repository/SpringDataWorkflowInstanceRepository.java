package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.WorkflowInstanceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataWorkflowInstanceRepository extends MongoRepository<WorkflowInstanceDocument, Long> {
	
	List<WorkflowInstanceDocument> findByWorkflowKey(long workflowKey);

	  Optional<WorkflowInstanceDocument> findByKey(long key);

	  long countByWorkflowKey(long workflowKey);

	  long countByWorkflowKeyAndEndIsNotNull(long workflowKey);

	  long countByWorkflowKeyAndEndIsNull(long workflowKey);

	  List<WorkflowInstanceDocument> findByParentWorkflowInstanceKey(long parentWorkflowInstanceKey);

}
