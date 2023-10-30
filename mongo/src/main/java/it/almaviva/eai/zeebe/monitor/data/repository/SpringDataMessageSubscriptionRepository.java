package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.MessageSubscriptionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataMessageSubscriptionRepository extends MongoRepository<MessageSubscriptionDocument, String> {
	
	  List<MessageSubscriptionDocument> findByWorkflowInstanceKey(long workflowInstanceKey);

	  Optional<MessageSubscriptionDocument> findByElementInstanceKeyAndMessageName(
	          long elementInstanceKey, String messageName);

	  Optional<MessageSubscriptionDocument> findByWorkflowKeyAndMessageName(
	          long workflowKey, String messageName);

	  List<MessageSubscriptionDocument> findByWorkflowKeyAndWorkflowInstanceKeyIsNull(long workflowKey);

}
