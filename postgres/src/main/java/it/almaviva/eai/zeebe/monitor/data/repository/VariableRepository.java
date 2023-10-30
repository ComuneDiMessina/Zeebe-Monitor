package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.VariableEntity;
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
		List<VariableEntity> byWorkflowInstanceKey = springDataVariableRepository.findByWorkflowInstanceKey(workflowInstanceKey);
		return iVariableMapper.map(byWorkflowInstanceKey);
	}

	@Override
	public boolean existsById(long id) {
		return springDataVariableRepository.existsById(id);
	}

	@Override
	public void save(VariableDomain variableDomain) {
		VariableEntity variableEntity = new VariableEntity();
		variableEntity.setPosition(variableDomain.getPosition());
		variableEntity.setName(variableDomain.getName());
		variableEntity.setScopeKey(variableDomain.getScopeKey());
		variableEntity.setState(variableDomain.getState());
		variableEntity.setTimestamp(variableDomain.getTimestamp());
		variableEntity.setValue(variableDomain.getValue());
		variableEntity.setWorkflowInstanceKey(variableDomain.getWorkflowInstanceKey());
		springDataVariableRepository.save(variableEntity);
		
	}

}
