package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.MessageSubscriptionEntity;
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
		Iterable<MessageSubscriptionEntity> byWorkflowInstanceKey = springDataMessageSubscriptionRepository.findByWorkflowInstanceKey(workflowInstanceKey);
		return iMessageSubscriptionMapper.map(byWorkflowInstanceKey);
	}

	@Override
	public MessageSubscriptionDomain findByElementInstanceKeyAndMessageName(long elementInstanceKey,
			String messageName) {
        Optional<MessageSubscriptionEntity> byElementInstanceKeyAndMessageName = springDataMessageSubscriptionRepository.findByElementInstanceKeyAndMessageName(elementInstanceKey, messageName);
        MessageSubscriptionEntity messageSubscriptionEntity = byElementInstanceKeyAndMessageName.orElseGet(() -> {return null;});
		return iMessageSubscriptionMapper.map(messageSubscriptionEntity);
	}

	@Override
	public MessageSubscriptionDomain findByWorkflowKeyAndMessageName(long workflowKey, String messageName) {
		Optional<MessageSubscriptionEntity> byWorkflowKeyAndMessageName = springDataMessageSubscriptionRepository.findByWorkflowKeyAndMessageName(workflowKey, messageName);
		MessageSubscriptionEntity messageSubscriptionEntity = byWorkflowKeyAndMessageName.orElseGet(() -> {return null;});
		return iMessageSubscriptionMapper.map(messageSubscriptionEntity);
	}

	@Override
	public List<MessageSubscriptionDomain> findByWorkflowKeyAndWorkflowInstanceKeyIsNull(long workflowKey) {
		Iterable<MessageSubscriptionEntity> byWorkflowKeyAndWorkflowInstanceKeyIsNull = springDataMessageSubscriptionRepository.findByWorkflowKeyAndWorkflowInstanceKeyIsNull(workflowKey);
		return iMessageSubscriptionMapper.map(byWorkflowKeyAndWorkflowInstanceKeyIsNull);
	}

	@Override
	public void save(MessageSubscriptionDomain messageSubscriptionDomain) {
		
		MessageSubscriptionEntity messageSubscriptionEntity = new MessageSubscriptionEntity();
		messageSubscriptionEntity.setId(messageSubscriptionDomain.getId());
		messageSubscriptionEntity.setCorrelationKey(messageSubscriptionDomain.getCorrelationKey());
		messageSubscriptionEntity.setElementInstanceKey(messageSubscriptionDomain.getElementInstanceKey());
		messageSubscriptionEntity.setMessageName(messageSubscriptionDomain.getMessageName());
		messageSubscriptionEntity.setState(messageSubscriptionDomain.getState());
		messageSubscriptionEntity.setTargetFlowNodeId(messageSubscriptionDomain.getTargetFlowNodeId());
		messageSubscriptionEntity.setTimestamp(messageSubscriptionDomain.getTimestamp());
		messageSubscriptionEntity.setWorkflowInstanceKey(messageSubscriptionDomain.getWorkflowInstanceKey());
		messageSubscriptionEntity.setWorkflowKey(messageSubscriptionDomain.getWorkflowKey());
		springDataMessageSubscriptionRepository.save(messageSubscriptionEntity);
		
	}

}
