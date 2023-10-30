package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.TimerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataTimerRepository extends MongoRepository<TimerDocument, Long> {

	List<TimerDocument> findByWorkflowInstanceKey(Long workflowInstanceKey);

	List<TimerDocument> findByWorkflowKeyAndWorkflowInstanceKeyIsNull(Long workflowInstanceKey);
}
