package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.TimerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataTimerRepository extends CrudRepository<TimerEntity, Long> {

	List<TimerEntity> findByWorkflowInstanceKey(Long workflowInstanceKey);

	List<TimerEntity> findByWorkflowKeyAndWorkflowInstanceKeyIsNull(Long workflowInstanceKey);
}
