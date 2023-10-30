package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.IncidentDocument;
import it.almaviva.eai.zeebe.monitor.data.mapper.IIncidentMapper;
import it.almaviva.eai.zeebe.monitor.domain.IncidentDomain;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IIncidentPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class IncidentRepository implements IIncidentPort {
	
	@Autowired
	private SpringDataIncidentRepository springDataIncidentRepository;
	
	@Autowired
	private IIncidentMapper incidentMapper;

	@Override
	public IncidentDomain findById(long id) {
        Optional<IncidentDocument> byId = springDataIncidentRepository.findById(id);
        IncidentDocument IncidentDocument = byId.orElseGet(() -> {return null;});
		return incidentMapper.map(IncidentDocument);
	}

	@Override
	public Iterable<IncidentDomain> findByWorkflowInstanceKey(long workflowInstanceKey) {
		Iterable<IncidentDocument> byWorkflowInstanceKey = springDataIncidentRepository.findByWorkflowInstanceKey(workflowInstanceKey);
		return incidentMapper.map(byWorkflowInstanceKey);
	}

	@Override
	public Iterable<IncidentDomain> findByResolvedIsNull() {
		Iterable<IncidentDocument> byResolvedIsNull = springDataIncidentRepository.findByResolvedIsNull();
		return incidentMapper.map(byResolvedIsNull);
	}

	@Override
	public long countByResolvedIsNull() {
		return springDataIncidentRepository.countByResolvedIsNull();
	}

	@Override
	public void save(IncidentDomain incidentDomain) {
		
		IncidentDocument IncidentDocument = new IncidentDocument();
		IncidentDocument.setKey(incidentDomain.getKey());
		IncidentDocument.setBpmnProcessId(incidentDomain.getBpmnProcessId());
		IncidentDocument.setCreated(incidentDomain.getCreated());
		IncidentDocument.setElementInstanceKey(incidentDomain.getElementInstanceKey());
		IncidentDocument.setErrorMessage(incidentDomain.getErrorMessage());
		IncidentDocument.setErrorType(incidentDomain.getErrorType());
		IncidentDocument.setJobKey(incidentDomain.getJobKey());
		IncidentDocument.setResolved(incidentDomain.getResolved());
		IncidentDocument.setWorkflowInstanceKey(incidentDomain.getWorkflowInstanceKey());
		IncidentDocument.setWorkflowKey(incidentDomain.getWorkflowKey());
		springDataIncidentRepository.save(IncidentDocument);
		
	}

}
