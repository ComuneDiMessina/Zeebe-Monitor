package it.almaviva.eai.zeebe.monitor.service;

import it.almaviva.eai.zeebe.monitor.domain.MessageDomain;
import it.almaviva.eai.zeebe.monitor.port.incoming.IMessageUseCase;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IMessagePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService implements IMessageUseCase {
	
	@Autowired
	private IMessagePort iMessagePort;

	@Override
	public MessageDomain findById(long id) {
		return iMessagePort.findById(id);
	}

	@Override
	public void save(MessageDomain messageDomain) {
		iMessagePort.save(messageDomain);
	}

	@Override
	public long count() {
		return iMessagePort.count();
	}

	@Override
	public List<MessageDomain> findAll() {
		return iMessagePort.findAll();
	}

}
