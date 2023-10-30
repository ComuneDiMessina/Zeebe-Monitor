package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.JobEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJobRepository extends CrudRepository<JobEntity, Long> {

	List<JobEntity> findByWorkflowInstanceKey(long workflowInstanceKey);

	Optional<JobEntity> findByKey(long key);

	List<JobEntity> findByStateNotIn(Collection<String> state);

	long countByStateNotIn(Collection<String> state);

}
