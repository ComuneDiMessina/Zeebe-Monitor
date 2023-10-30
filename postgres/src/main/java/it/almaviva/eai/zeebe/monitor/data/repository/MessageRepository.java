package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.MessageEntity;
import it.almaviva.eai.zeebe.monitor.data.mapper.IMessageMapper;
import it.almaviva.eai.zeebe.monitor.domain.MessageDomain;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IMessagePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MessageRepository implements IMessagePort{
	
	@Autowired
	private SpringDataMessageRepository springDataMessageRepository;
	
	@Autowired
	private IMessageMapper iMessageMapper;

	@Override
	public MessageDomain findById(long id) {
		Optional<MessageEntity> byId = springDataMessageRepository.findById(id);
		MessageEntity messageEntity = byId.orElseGet(() -> {return null;});
		return iMessageMapper.map(messageEntity);
	}

	@Override
	public void save(MessageDomain messageDomain) {
		
		MessageEntity messageEntity = new MessageEntity();
		messageEntity.setKey(messageDomain.getKey());
		messageEntity.setCorrelationKey(messageDomain.getCorrelationKey());
		messageEntity.setMessageId(messageDomain.getMessageId());
		messageEntity.setName(messageDomain.getName());
		messageEntity.setPayload(messageDomain.getPayload());
		messageEntity.setState(messageDomain.getPayload());
		messageEntity.setTimestamp(messageDomain.getTimestamp());
		springDataMessageRepository.save(messageEntity);
	}

	@Override
	public long count() {
		return springDataMessageRepository.count();
	}

	@Override
	public List<MessageDomain> findAll() {
		Iterable<MessageEntity> findAll = springDataMessageRepository.findAll();
		return iMessageMapper.map(findAll);
	}

}
