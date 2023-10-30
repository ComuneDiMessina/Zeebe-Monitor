package it.almaviva.eai.zeebe.monitor.service;

import it.almaviva.eai.zeebe.monitor.domain.MessageSubscriptionDomain;
import it.almaviva.eai.zeebe.monitor.port.incoming.IMessageSubscriptionUseCase;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IMessageSubscriptionPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageSubscriptionService implements IMessageSubscriptionUseCase{
	
	@Autowired
	private IMessageSubscriptionPort iMessageSubscriptionPort;

	@Override
	public List<MessageSubscriptionDomain> findByWorkflowInstanceKey(long workflowInstanceKey) {
		return iMessageSubscriptionPort.findByWorkflowInstanceKey(workflowInstanceKey);
	}

	@Override
	public MessageSubscriptionDomain findByElementInstanceKeyAndMessageName(long elementInstanceKey,
			String messageName) {
		return iMessageSubscriptionPort.findByElementInstanceKeyAndMessageName(elementInstanceKey, messageName);
	}

	@Override
	public MessageSubscriptionDomain findByWorkflowKeyAndMessageName(long workflowKey, String messageName) {
		return iMessageSubscriptionPort.findByWorkflowKeyAndMessageName(workflowKey, messageName);
	}

	@Override
	public List<MessageSubscriptionDomain> findByWorkflowKeyAndWorkflowInstanceKeyIsNull(long workflowKey) {
		return iMessageSubscriptionPort.findByWorkflowKeyAndWorkflowInstanceKeyIsNull(workflowKey);
	}

	@Override
	public void save(MessageSubscriptionDomain messageSubscriptionDomain) {
		iMessageSubscriptionPort.save(messageSubscriptionDomain);
	}

}
