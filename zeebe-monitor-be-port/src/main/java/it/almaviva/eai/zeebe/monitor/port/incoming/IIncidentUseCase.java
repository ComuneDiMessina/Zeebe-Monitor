package it.almaviva.eai.zeebe.monitor.port.incoming;

import it.almaviva.eai.zeebe.monitor.domain.IncidentDomain;

public interface IIncidentUseCase {
	
	  IncidentDomain findById(long id);
	
	  Iterable<IncidentDomain> findByWorkflowInstanceKey(long workflowInstanceKey);

	  Iterable<IncidentDomain> findByResolvedIsNull();

	  long countByResolvedIsNull();
	  
	  void save(IncidentDomain incidentDomain);

	  void sendNotification(IncidentDomain incidentDomain);
}
