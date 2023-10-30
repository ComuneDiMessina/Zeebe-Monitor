package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.VariableEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpringDataVariableRepository extends CrudRepository<VariableEntity, Long> {

	List<VariableEntity> findByWorkflowInstanceKey(long workflowInstanceKey);
}
