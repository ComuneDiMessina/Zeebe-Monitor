package it.almaviva.eai.zeebe.monitor.service;

import it.almaviva.eai.zeebe.monitor.domain.WorkflowDomain;
import it.almaviva.eai.zeebe.monitor.port.incoming.IWorkflowUseCase;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IWorkflowPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowService implements IWorkflowUseCase {
	
	@Autowired
	private IWorkflowPort iWorkflowPort;

	@Override
	public WorkflowDomain findByKey(long key) {
		// TODO Auto-generated method stub
		return iWorkflowPort.findByKey(key);
	}

	@Override
	public void save(WorkflowDomain workflow) {
		iWorkflowPort.save(workflow);
		
	}

	@Override
	public long count() {
		return iWorkflowPort.count();
	}

	@Override
	public List<WorkflowDomain> findAll() {
		return iWorkflowPort.findAll();
	}
	
	

}
