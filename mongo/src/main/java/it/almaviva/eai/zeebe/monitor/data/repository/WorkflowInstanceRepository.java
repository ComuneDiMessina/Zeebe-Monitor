package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.WorkflowInstanceDocument;
import it.almaviva.eai.zeebe.monitor.data.mapper.IWorkflowInstanceMapper;
import it.almaviva.eai.zeebe.monitor.domain.WorkflowInstanceDomain;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IWorkflowInstancePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WorkflowInstanceRepository implements IWorkflowInstancePort{
	
	@Autowired
	private SpringDataWorkflowInstanceRepository springDataWorkflowInstanceRepository;
	
	@Autowired
	private IWorkflowInstanceMapper iWorkflowInstanceMapper;

	@Override
	public List<WorkflowInstanceDomain> findByWorkflowKey(long workflowKey) {
		List<WorkflowInstanceDocument> byWorkflowKey = springDataWorkflowInstanceRepository.findByWorkflowKey(workflowKey);
		return iWorkflowInstanceMapper.map(byWorkflowKey);
	}

	@Override
	public WorkflowInstanceDomain findByKey(long key) {
		Optional<WorkflowInstanceDocument> byKey = springDataWorkflowInstanceRepository.findById(key);
		WorkflowInstanceDocument WorkflowInstanceDocument = byKey.orElseGet(() -> {return null;});
		return  iWorkflowInstanceMapper.map(WorkflowInstanceDocument);
	}

	@Override
	public long countByWorkflowKey(long workflowKey) {
		return springDataWorkflowInstanceRepository.countByWorkflowKey(workflowKey);
	}

	@Override
	public long countByWorkflowKeyAndEndIsNotNull(long workflowKey) {
		return springDataWorkflowInstanceRepository.countByWorkflowKeyAndEndIsNotNull(workflowKey);
	}

	@Override
	public long countByWorkflowKeyAndEndIsNull(long workflowKey) {
		return springDataWorkflowInstanceRepository.countByWorkflowKeyAndEndIsNull(workflowKey);
	}

	@Override
	public List<WorkflowInstanceDomain> findByParentWorkflowInstanceKey(long parentWorkflowInstanceKey) {
		List<WorkflowInstanceDocument> byParentWorkflowInstanceKey = springDataWorkflowInstanceRepository.findByParentWorkflowInstanceKey(parentWorkflowInstanceKey);
		return iWorkflowInstanceMapper.map(byParentWorkflowInstanceKey);
	}

	@Override
	public void save(WorkflowInstanceDomain workflowInstanceDomain) {
		
		WorkflowInstanceDocument WorkflowInstanceDocument = new WorkflowInstanceDocument();
		WorkflowInstanceDocument.setKey(workflowInstanceDomain.getKey());
		WorkflowInstanceDocument.setBpmnProcessId(workflowInstanceDomain.getBpmnProcessId());
		WorkflowInstanceDocument.setEnd(workflowInstanceDomain.getEnd());
		WorkflowInstanceDocument.setParentElementInstanceKey(workflowInstanceDomain.getParentElementInstanceKey());
		WorkflowInstanceDocument.setParentWorkflowInstanceKey(workflowInstanceDomain.getParentWorkflowInstanceKey());
		WorkflowInstanceDocument.setPartitionId(workflowInstanceDomain.getPartitionId());
		WorkflowInstanceDocument.setStart(workflowInstanceDomain.getStart());
		WorkflowInstanceDocument.setVersion(workflowInstanceDomain.getVersion());
		WorkflowInstanceDocument.setState(workflowInstanceDomain.getState());
		WorkflowInstanceDocument.setWorkflowKey(workflowInstanceDomain.getWorkflowKey());
		springDataWorkflowInstanceRepository.save(WorkflowInstanceDocument);
		
	}

	@Override
	public long count() {
		return springDataWorkflowInstanceRepository.count();
	}

	@Override
	public List<WorkflowInstanceDomain> findAll() {
		Iterable<WorkflowInstanceDocument> findAll = springDataWorkflowInstanceRepository.findAll();
		return iWorkflowInstanceMapper.map(findAll);
	}

}
