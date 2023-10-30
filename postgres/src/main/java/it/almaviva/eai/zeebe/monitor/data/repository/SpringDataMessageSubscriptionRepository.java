package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.MessageSubscriptionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataMessageSubscriptionRepository extends CrudRepository<MessageSubscriptionEntity, String> {
	
	  List<MessageSubscriptionEntity> findByWorkflowInstanceKey(long workflowInstanceKey);

	  Optional<MessageSubscriptionEntity> findByElementInstanceKeyAndMessageName(
	          long elementInstanceKey, String messageName);

	  Optional<MessageSubscriptionEntity> findByWorkflowKeyAndMessageName(
	          long workflowKey, String messageName);

	  List<MessageSubscriptionEntity> findByWorkflowKeyAndWorkflowInstanceKeyIsNull(long workflowKey);

}
