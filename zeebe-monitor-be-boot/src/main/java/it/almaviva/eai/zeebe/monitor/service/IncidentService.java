package it.almaviva.eai.zeebe.monitor.service;

import it.almaviva.eai.zeebe.monitor.domain.IncidentDomain;
import it.almaviva.eai.zeebe.monitor.port.incoming.IIncidentUseCase;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IIncidentPort;
import it.almaviva.eai.zeebe.monitor.port.outgoing.INotificationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IncidentService implements IIncidentUseCase{

	@Autowired
	private IIncidentPort iIncidentPort;

	@Autowired(required=false)
	private INotificationPort iNotificationPort;
	
	@Override
	public Iterable<IncidentDomain> findByWorkflowInstanceKey(long workflowInstanceKey) {
		return iIncidentPort.findByWorkflowInstanceKey(workflowInstanceKey);
	}

	@Override
	public Iterable<IncidentDomain> findByResolvedIsNull() {
		return iIncidentPort.findByResolvedIsNull();
	}

	@Override
	public long countByResolvedIsNull() {
		return iIncidentPort.countByResolvedIsNull();
	}

	@Override
	public IncidentDomain findById(long id) {
		return iIncidentPort.findById(id);
	}

	@Override
	public void save(IncidentDomain incidentDomain) {
		iIncidentPort.save(incidentDomain);
	}

	@Override
	public void sendNotification(IncidentDomain incidentDomain) {
		iNotificationPort.sendIncidentNotification(incidentDomain);
	}


}
