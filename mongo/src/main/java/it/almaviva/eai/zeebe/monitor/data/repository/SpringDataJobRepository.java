package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.JobDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJobRepository extends MongoRepository<JobDocument, Long> {

	List<JobDocument> findByWorkflowInstanceKey(long workflowInstanceKey);

	Optional<JobDocument> findByKey(long key);

	List<JobDocument> findByStateNotIn(Collection<String> state);

	long countByStateNotIn(Collection<String> state);

}
