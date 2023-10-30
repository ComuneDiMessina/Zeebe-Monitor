package it.almaviva.eai.zeebe.monitor.port.outgoing;

import it.almaviva.eai.zeebe.monitor.domain.MessageSubscriptionDomain;

import java.util.List;

public interface IMessageSubscriptionPort {
	
	  List<MessageSubscriptionDomain> findByWorkflowInstanceKey(long workflowInstanceKey);

	  MessageSubscriptionDomain findByElementInstanceKeyAndMessageName(
	          long elementInstanceKey, String messageName);

	  MessageSubscriptionDomain findByWorkflowKeyAndMessageName(
	          long workflowKey, String messageName);

	  List<MessageSubscriptionDomain> findByWorkflowKeyAndWorkflowInstanceKeyIsNull(long workflowKey);
	  
	  void save(MessageSubscriptionDomain messageSubscriptionDomain);

}
