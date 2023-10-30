package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.VariableDocument;
import it.almaviva.eai.zeebe.monitor.data.mapper.IVariableMapper;
import it.almaviva.eai.zeebe.monitor.domain.VariableDomain;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IVariablePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VariableRepository implements IVariablePort {
	
	@Autowired
	private SpringDataVariableRepository springDataVariableRepository;
	
	@Autowired
	private IVariableMapper iVariableMapper;

	@Override
	public List<VariableDomain> findByWorkflowInstanceKey(long workflowInstanceKey) {
		List<VariableDocument> byWorkflowInstanceKey = springDataVariableRepository.findByWorkflowInstanceKey(workflowInstanceKey);
		return iVariableMapper.map(byWorkflowInstanceKey);
	}

	@Override
	public boolean existsById(long id) {
		return springDataVariableRepository.existsById(id);
	}

	@Override
	public void save(VariableDomain variableDomain) {
		VariableDocument VariableDocument = new VariableDocument();
		VariableDocument.setPosition(variableDomain.getPosition());
		VariableDocument.setName(variableDomain.getName());
		VariableDocument.setScopeKey(variableDomain.getScopeKey());
		VariableDocument.setState(variableDomain.getState());
		VariableDocument.setTimestamp(variableDomain.getTimestamp());
		VariableDocument.setValue(variableDomain.getValue());
		VariableDocument.setWorkflowInstanceKey(variableDomain.getWorkflowInstanceKey());
		springDataVariableRepository.save(VariableDocument);
		
	}

}
