package it.almaviva.eai.zeebe.monitor.port.outgoing;

import it.almaviva.eai.zeebe.monitor.domain.WorkflowDomain;

import java.util.List;

public interface IWorkflowPort {
	
	WorkflowDomain findByKey(long key);

	//List<ElementInstanceStatistics> getElementInstanceStatisticsByKeyAndIntentIn(long key, Collection<String> intents,Collection<String> excludeElementTypes);
	  
	void save(WorkflowDomain workflow);
	
	long count();
	
	List<WorkflowDomain> findAll();

}
