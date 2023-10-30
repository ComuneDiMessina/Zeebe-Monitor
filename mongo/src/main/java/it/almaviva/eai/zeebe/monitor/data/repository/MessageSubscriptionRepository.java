package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.MessageSubscriptionDocument;
import it.almaviva.eai.zeebe.monitor.data.mapper.IMessageSubscriptionMapper;
import it.almaviva.eai.zeebe.monitor.domain.MessageSubscriptionDomain;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IMessageSubscriptionPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MessageSubscriptionRepository implements IMessageSubscriptionPort{
	
	@Autowired
	private SpringDataMessageSubscriptionRepository springDataMessageSubscriptionRepository;
	
	@Autowired
	private IMessageSubscriptionMapper iMessageSubscriptionMapper;

	@Override
	public List<MessageSubscriptionDomain> findByWorkflowInstanceKey(long workflowInstanceKey) {
		Iterable<MessageSubscriptionDocument> byWorkflowInstanceKey = springDataMessageSubscriptionRepository.findByWorkflowInstanceKey(workflowInstanceKey);
		return iMessageSubscriptionMapper.map(byWorkflowInstanceKey);
	}

	@Override
	public MessageSubscriptionDomain findByElementInstanceKeyAndMessageName(long elementInstanceKey,
			String messageName) {
        Optional<MessageSubscriptionDocument> byElementInstanceKeyAndMessageName = springDataMessageSubscriptionRepository.findByElementInstanceKeyAndMessageName(elementInstanceKey, messageName);
        MessageSubscriptionDocument MessageSubscriptionDocument = byElementInstanceKeyAndMessageName.orElseGet(() -> {return null;});
		return iMessageSubscriptionMapper.map(MessageSubscriptionDocument);
	}

	@Override
	public MessageSubscriptionDomain findByWorkflowKeyAndMessageName(long workflowKey, String messageName) {
		Optional<MessageSubscriptionDocument> byWorkflowKeyAndMessageName = springDataMessageSubscriptionRepository.findByWorkflowKeyAndMessageName(workflowKey, messageName);
		MessageSubscriptionDocument MessageSubscriptionDocument = byWorkflowKeyAndMessageName.orElseGet(() -> {return null;});
		return iMessageSubscriptionMapper.map(MessageSubscriptionDocument);
	}

	@Override
	public List<MessageSubscriptionDomain> findByWorkflowKeyAndWorkflowInstanceKeyIsNull(long workflowKey) {
		Iterable<MessageSubscriptionDocument> byWorkflowKeyAndWorkflowInstanceKeyIsNull = springDataMessageSubscriptionRepository.findByWorkflowKeyAndWorkflowInstanceKeyIsNull(workflowKey);
		return iMessageSubscriptionMapper.map(byWorkflowKeyAndWorkflowInstanceKeyIsNull);
	}

	@Override
	public void save(MessageSubscriptionDomain messageSubscriptionDomain) {
		
		MessageSubscriptionDocument MessageSubscriptionDocument = new MessageSubscriptionDocument();
		MessageSubscriptionDocument.setId(messageSubscriptionDomain.getId());
		MessageSubscriptionDocument.setCorrelationKey(messageSubscriptionDomain.getCorrelationKey());
		MessageSubscriptionDocument.setElementInstanceKey(messageSubscriptionDomain.getElementInstanceKey());
		MessageSubscriptionDocument.setMessageName(messageSubscriptionDomain.getMessageName());
		MessageSubscriptionDocument.setState(messageSubscriptionDomain.getState());
		MessageSubscriptionDocument.setTargetFlowNodeId(messageSubscriptionDomain.getTargetFlowNodeId());
		MessageSubscriptionDocument.setTimestamp(messageSubscriptionDomain.getTimestamp());
		MessageSubscriptionDocument.setWorkflowInstanceKey(messageSubscriptionDomain.getWorkflowInstanceKey());
		MessageSubscriptionDocument.setWorkflowKey(messageSubscriptionDomain.getWorkflowKey());
		springDataMessageSubscriptionRepository.save(MessageSubscriptionDocument);
		
	}

}
