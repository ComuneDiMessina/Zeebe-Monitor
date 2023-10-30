package it.almaviva.eai.zeebe.monitor.port.incoming;

import it.almaviva.eai.zeebe.monitor.domain.MessageDomain;

import java.util.List;

public interface IMessageUseCase {
	
	MessageDomain findById(long id);
	
	List<MessageDomain> findAll();
	
	void save(MessageDomain messageDomain);
	
	long count();

}
