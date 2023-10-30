package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.WorkflowInstanceEntity;
import it.almaviva.eai.zeebe.monitor.data.mapper.IWorkflowInstanceMapper;
import it.almaviva.eai.zeebe.monitor.domain.WorkflowInstanceDomain;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IWorkflowInstancePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WorkflowInstanceRepository implements IWorkflowInstancePort {
	
	@Autowired
	private SpringDataWorkflowInstanceRepository springDataWorkflowInstanceRepository;
	
	@Autowired
	private IWorkflowInstanceMapper iWorkflowInstanceMapper;

	@Override
	public List<WorkflowInstanceDomain> findByWorkflowKey(long workflowKey) {
		List<WorkflowInstanceEntity> byWorkflowKey = springDataWorkflowInstanceRepository.findByWorkflowKey(workflowKey);
		return iWorkflowInstanceMapper.map(byWorkflowKey);
	}

	@Override
	public WorkflowInstanceDomain findByKey(long key) {
		Optional<WorkflowInstanceEntity> byKey = springDataWorkflowInstanceRepository.findByKey(key);
		WorkflowInstanceEntity workflowInstanceEntity = byKey.orElseGet(() -> {return null;});
		return  iWorkflowInstanceMapper.map(workflowInstanceEntity);
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
		List<WorkflowInstanceEntity> byParentWorkflowInstanceKey = springDataWorkflowInstanceRepository.findByParentWorkflowInstanceKey(parentWorkflowInstanceKey);
		return iWorkflowInstanceMapper.map(byParentWorkflowInstanceKey);
	}

	@Override
	public void save(WorkflowInstanceDomain workflowInstanceDomain) {
		
		WorkflowInstanceEntity workflowInstanceEntity = new WorkflowInstanceEntity();
		workflowInstanceEntity.setKey(workflowInstanceDomain.getKey());
		workflowInstanceEntity.setBpmnProcessId(workflowInstanceDomain.getBpmnProcessId());
		workflowInstanceEntity.setEnd(workflowInstanceDomain.getEnd());
		workflowInstanceEntity.setParentElementInstanceKey(workflowInstanceDomain.getParentElementInstanceKey());
		workflowInstanceEntity.setParentWorkflowInstanceKey(workflowInstanceDomain.getParentWorkflowInstanceKey());
		workflowInstanceEntity.setPartitionId(workflowInstanceDomain.getPartitionId());
		workflowInstanceEntity.setStart(workflowInstanceDomain.getStart());
		workflowInstanceEntity.setVersion(workflowInstanceDomain.getVersion());
		workflowInstanceEntity.setState(workflowInstanceDomain.getState());
		workflowInstanceEntity.setWorkflowKey(workflowInstanceDomain.getWorkflowKey());
		springDataWorkflowInstanceRepository.save(workflowInstanceEntity);
		
	}

	@Override
	public long count() {
		return springDataWorkflowInstanceRepository.count();
	}

	@Override
	public List<WorkflowInstanceDomain> findAll() {
		Iterable<WorkflowInstanceEntity> findAll = springDataWorkflowInstanceRepository.findAll();
		return iWorkflowInstanceMapper.map(findAll);
	}

}
