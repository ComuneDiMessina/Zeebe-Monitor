package it.almaviva.eai.zeebe.monitor.port.incoming;

import it.almaviva.eai.zeebe.monitor.domain.WorkflowInstanceDomain;

import java.util.List;

public interface IWorkflowInstanceUseCase {

	List<WorkflowInstanceDomain> findByWorkflowKey(long workflowKey);
	
	List<WorkflowInstanceDomain> findAll();

	WorkflowInstanceDomain findByKey(long key);

	long countByWorkflowKey(long workflowKey);

	long countByWorkflowKeyAndEndIsNotNull(long workflowKey);

	long countByWorkflowKeyAndEndIsNull(long workflowKey);
	
	long count();

	List<WorkflowInstanceDomain> findByParentWorkflowInstanceKey(long parentWorkflowInstanceKey);

	void save(WorkflowInstanceDomain workflowInstanceDomain);

}
