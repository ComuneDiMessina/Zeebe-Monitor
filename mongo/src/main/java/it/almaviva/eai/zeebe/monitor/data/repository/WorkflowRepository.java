package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.WorkflowDocument;
import it.almaviva.eai.zeebe.monitor.data.mapper.IWorkflowMapper;
import it.almaviva.eai.zeebe.monitor.domain.WorkflowDomain;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IWorkflowPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Component
public class WorkflowRepository implements IWorkflowPort {
	
	@Autowired
	private SpringDataWorkflowRepository springDataWorkflowRepository;
	
	@Autowired
	private IWorkflowMapper iWorkflowMapper;

	@Override
	public WorkflowDomain findByKey(long key) {
		Optional<WorkflowDocument> byKey = springDataWorkflowRepository.findById(key);
		WorkflowDocument workflowEntity = byKey.orElseThrow(() -> new EntityNotFoundException("[id]: "+ key));
		return iWorkflowMapper.map(workflowEntity);
	}

	@Override
	public void save(WorkflowDomain workflow) {
		WorkflowDocument workflowEntity = new WorkflowDocument();
		workflowEntity.setKey(workflow.getKey());
		workflowEntity.setBpmnProcessId(workflow.getBpmnProcessId());
		workflowEntity.setResource(workflow.getResource());
		workflowEntity.setTimestamp(workflow.getTimestamp());
		workflowEntity.setVersion(workflow.getVersion());
		springDataWorkflowRepository.save(workflowEntity);
		
	}

	@Override
	public long count() {
		return springDataWorkflowRepository.count();
	}

	@Override
	public List<WorkflowDomain> findAll() {
		Iterable<WorkflowDocument> findAll = springDataWorkflowRepository.findAll();
		return iWorkflowMapper.map(findAll);
	}

}
