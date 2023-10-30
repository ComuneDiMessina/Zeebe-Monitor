package it.almaviva.eai.zeebe.monitor.port.outgoing;

import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceDomain;
import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceStatistics;

import java.util.Collection;
import java.util.List;

public interface IElementInstancePort {

	Iterable<ElementInstanceDomain> findByWorkflowInstanceKey(long workflowInstanceKey);
	
	List<ElementInstanceStatistics> getElementInstanceStatisticsByKeyAndIntentIn(long key,Collection<String> intents, Collection<String> excludeElementTypes);
	
	boolean existsById(long position);
	
	void save(ElementInstanceDomain elementInstanceDomain);
	
}
