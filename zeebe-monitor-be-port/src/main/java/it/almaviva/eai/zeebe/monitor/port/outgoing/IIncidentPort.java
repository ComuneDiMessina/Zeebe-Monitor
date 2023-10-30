package it.almaviva.eai.zeebe.monitor.port.outgoing;

import it.almaviva.eai.zeebe.monitor.domain.IncidentDomain;

public interface IIncidentPort {
	
	  IncidentDomain findById(long id);
	
	  Iterable<IncidentDomain> findByWorkflowInstanceKey(long workflowInstanceKey);

	  Iterable<IncidentDomain> findByResolvedIsNull();

	  long countByResolvedIsNull();
	  
	  void save(IncidentDomain incidentDomain);

}
