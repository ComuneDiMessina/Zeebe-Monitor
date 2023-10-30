package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.IncidentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataIncidentRepository extends CrudRepository<IncidentEntity, Long> {
	
	Iterable<IncidentEntity> findByWorkflowInstanceKey(long workflowInstanceKey);

	Iterable<IncidentEntity> findByResolvedIsNull();

	long countByResolvedIsNull();

}
