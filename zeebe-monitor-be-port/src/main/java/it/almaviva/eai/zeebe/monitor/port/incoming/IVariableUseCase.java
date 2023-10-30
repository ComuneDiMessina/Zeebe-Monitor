package it.almaviva.eai.zeebe.monitor.port.incoming;

import it.almaviva.eai.zeebe.monitor.domain.VariableDomain;

import java.util.List;

public interface IVariableUseCase {
	
	List<VariableDomain> findByWorkflowInstanceKey(long workflowInstanceKey);
	
	boolean existsById(long id);
	
	void save(VariableDomain variableDomain);

}
