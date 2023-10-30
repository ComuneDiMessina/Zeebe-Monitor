package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.WorkflowEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataWorkflowRepository extends CrudRepository<WorkflowEntity, Long> {
	
	Optional<WorkflowEntity> findByKey(long key);

}
