package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.WorkflowInstanceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataWorkflowInstanceRepository extends CrudRepository<WorkflowInstanceEntity, Long> {
	
	  List<WorkflowInstanceEntity> findByWorkflowKey(long workflowKey);

	  Optional<WorkflowInstanceEntity> findByKey(long key);
	  
	  Optional<WorkflowInstanceEntity> findById(long id);

	  long countByWorkflowKey(long workflowKey);

	  long countByWorkflowKeyAndEndIsNotNull(long workflowKey);

	  long countByWorkflowKeyAndEndIsNull(long workflowKey);

	  List<WorkflowInstanceEntity> findByParentWorkflowInstanceKey(long parentWorkflowInstanceKey);

}
