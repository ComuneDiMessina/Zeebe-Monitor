package it.almaviva.eai.zeebe.monitor.port.incoming;

import it.almaviva.eai.zeebe.monitor.domain.MessageSubscriptionDomain;

import java.util.List;

public interface IMessageSubscriptionUseCase {
	
	  List<MessageSubscriptionDomain> findByWorkflowInstanceKey(long workflowInstanceKey);

	  MessageSubscriptionDomain findByElementInstanceKeyAndMessageName(
	          long elementInstanceKey, String messageName);

	  MessageSubscriptionDomain findByWorkflowKeyAndMessageName(
	          long workflowKey, String messageName);

	  List<MessageSubscriptionDomain> findByWorkflowKeyAndWorkflowInstanceKeyIsNull(long workflowKey);
	  
	  void save(MessageSubscriptionDomain messageSubscriptionDomain);

}
