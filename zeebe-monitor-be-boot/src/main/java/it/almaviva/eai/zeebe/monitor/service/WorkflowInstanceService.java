package it.almaviva.eai.zeebe.monitor.service;

import it.almaviva.eai.zeebe.monitor.client.IUserTaskClient;
import it.almaviva.eai.zeebe.monitor.domain.WorkflowInstanceDomain;
import it.almaviva.eai.zeebe.monitor.port.incoming.IWorkflowInstanceUseCase;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IWorkflowInstancePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowInstanceService implements IWorkflowInstanceUseCase {
	
	@Autowired
	private IWorkflowInstancePort iWorkflowInstancePort;
	
	@Autowired
	private IUserTaskClient iUserTaskClient;
	
    public void deleteTasks(String token,String key) {
    	iUserTaskClient.deleteTasks(token,key);
    }


	@Override
	public List<WorkflowInstanceDomain> findByWorkflowKey(long workflowKey) {
		return iWorkflowInstancePort.findByWorkflowKey(workflowKey);
	}

	@Override
	public WorkflowInstanceDomain findByKey(long key) {
		return iWorkflowInstancePort.findByKey(key);
	}

	@Override
	public long countByWorkflowKey(long workflowKey) {
		return iWorkflowInstancePort.countByWorkflowKey(workflowKey);
	}

	@Override
	public long countByWorkflowKeyAndEndIsNotNull(long workflowKey) {
		return iWorkflowInstancePort.countByWorkflowKeyAndEndIsNotNull(workflowKey);
	}

	@Override
	public long countByWorkflowKeyAndEndIsNull(long workflowKey) {
		return iWorkflowInstancePort.countByWorkflowKeyAndEndIsNull(workflowKey);
	}

	@Override
	public List<WorkflowInstanceDomain> findByParentWorkflowInstanceKey(long parentWorkflowInstanceKey) {
		return iWorkflowInstancePort.findByParentWorkflowInstanceKey(parentWorkflowInstanceKey);
	}

	@Override
	public void save(WorkflowInstanceDomain workflowInstanceDomain) {
		iWorkflowInstancePort.save(workflowInstanceDomain);
	}

	@Override
	public long count() {
		return iWorkflowInstancePort.count();
	}

	@Override
	public List<WorkflowInstanceDomain> findAll() {
		return iWorkflowInstancePort.findAll();
	}

}
