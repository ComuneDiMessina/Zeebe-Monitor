package it.almaviva.eai.zeebe.monitor.service;

import it.almaviva.eai.zeebe.monitor.domain.VariableDomain;
import it.almaviva.eai.zeebe.monitor.port.incoming.IVariableUseCase;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IVariablePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VariableService implements IVariableUseCase{
	
	@Autowired
	private IVariablePort iVariablePort;

	@Override
	public List<VariableDomain> findByWorkflowInstanceKey(long workflowInstanceKey) {
		return iVariablePort.findByWorkflowInstanceKey(workflowInstanceKey);
	}

	@Override
	public boolean existsById(long id) {
		return iVariablePort.existsById(id);
	}

	@Override
	public void save(VariableDomain variableDomain) {
		iVariablePort.save(variableDomain);
	}

}
