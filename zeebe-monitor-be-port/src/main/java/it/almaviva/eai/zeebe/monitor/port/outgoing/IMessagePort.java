package it.almaviva.eai.zeebe.monitor.port.outgoing;

import it.almaviva.eai.zeebe.monitor.domain.MessageDomain;

import java.util.List;

public interface IMessagePort {
	
	MessageDomain findById(long id);
	
	List<MessageDomain> findAll();
	
	void save(MessageDomain messageDomain);
	
	long count();

}
