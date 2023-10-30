package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.MessageDocument;
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
		Optional<MessageDocument> byId = springDataMessageRepository.findById(id);
		MessageDocument MessageDocument = byId.orElseGet(() -> {return null;});
		return iMessageMapper.map(MessageDocument);
	}

	@Override
	public void save(MessageDomain messageDomain) {
		
		MessageDocument MessageDocument = new MessageDocument();
		MessageDocument.setKey(messageDomain.getKey());
		MessageDocument.setCorrelationKey(messageDomain.getCorrelationKey());
		MessageDocument.setMessageId(messageDomain.getMessageId());
		MessageDocument.setName(messageDomain.getName());
		MessageDocument.setPayload(messageDomain.getPayload());
		MessageDocument.setState(messageDomain.getPayload());
		MessageDocument.setTimestamp(messageDomain.getTimestamp());
		springDataMessageRepository.save(MessageDocument);
	}

	@Override
	public long count() {
		return springDataMessageRepository.count();
	}

	@Override
	public List<MessageDomain> findAll() {
		Iterable<MessageDocument> findAll = springDataMessageRepository.findAll();
		return iMessageMapper.map(findAll);
	}

}
