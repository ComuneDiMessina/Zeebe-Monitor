package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.IncidentEntity;
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
        Optional<IncidentEntity> byId = springDataIncidentRepository.findById(id);
        IncidentEntity incidentEntity = byId.orElseGet(() -> {return null;});
		return incidentMapper.map(incidentEntity);
	}

	@Override
	public Iterable<IncidentDomain> findByWorkflowInstanceKey(long workflowInstanceKey) {
		Iterable<IncidentEntity> byWorkflowInstanceKey = springDataIncidentRepository.findByWorkflowInstanceKey(workflowInstanceKey);
		return incidentMapper.map(byWorkflowInstanceKey);
	}

	@Override
	public Iterable<IncidentDomain> findByResolvedIsNull() {
		Iterable<IncidentEntity> byResolvedIsNull = springDataIncidentRepository.findByResolvedIsNull();
		return incidentMapper.map(byResolvedIsNull);
	}

	@Override
	public long countByResolvedIsNull() {
		return springDataIncidentRepository.countByResolvedIsNull();
	}

	@Override
	public void save(IncidentDomain incidentDomain) {
		
		IncidentEntity incidentEntity = new IncidentEntity();
		incidentEntity.setKey(incidentDomain.getKey());
		incidentEntity.setBpmnProcessId(incidentDomain.getBpmnProcessId());
		incidentEntity.setCreated(incidentDomain.getCreated());
		incidentEntity.setElementInstanceKey(incidentDomain.getElementInstanceKey());
		incidentEntity.setErrorMessage(incidentDomain.getErrorMessage());
		incidentEntity.setErrorType(incidentDomain.getErrorType());
		incidentEntity.setJobKey(incidentDomain.getJobKey());
		incidentEntity.setResolved(incidentDomain.getResolved());
		incidentEntity.setWorkflowInstanceKey(incidentDomain.getWorkflowInstanceKey());
		incidentEntity.setWorkflowKey(incidentDomain.getWorkflowKey());
		springDataIncidentRepository.save(incidentEntity);
		
	}

}
