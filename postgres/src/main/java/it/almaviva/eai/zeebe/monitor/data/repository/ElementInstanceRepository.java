package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.ElementInstanceEntity;
import it.almaviva.eai.zeebe.monitor.data.mapper.IElementInstanceMapper;
import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceDomain;
import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceStatistics;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IElementInstancePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class ElementInstanceRepository implements IElementInstancePort{
	
	@Autowired
	private SpringDataElementInstanceRepository springDataElementInstanceRepository;
	
	@Autowired
	private IElementInstanceMapper iElementInstanceMapper;

	@Override
	public Iterable<ElementInstanceDomain> findByWorkflowInstanceKey(long workflowInstanceKey) {
		Iterable<ElementInstanceEntity> ByWorkflowInstanceKey = springDataElementInstanceRepository.findByWorkflowInstanceKey(workflowInstanceKey);
		return iElementInstanceMapper.map(ByWorkflowInstanceKey);
	}

	@Override
	public boolean existsById(long position) {
		return springDataElementInstanceRepository.existsById(position);
	}

	@Override
	public void save(ElementInstanceDomain elementInstanceDomain) {

		ElementInstanceEntity elementInstanceEntity = new ElementInstanceEntity();
		elementInstanceEntity.setPosition(elementInstanceDomain.getPosition());
		elementInstanceEntity.setBpmnElementType(elementInstanceDomain.getBpmnElementType());
		elementInstanceEntity.setElementId(elementInstanceDomain.getElementId());
		elementInstanceEntity.setFlowScopeKey(elementInstanceDomain.getFlowScopeKey());
		elementInstanceEntity.setIntent(elementInstanceDomain.getIntent());
		elementInstanceEntity.setKey(elementInstanceDomain.getKey());
		elementInstanceEntity.setPartitionId(elementInstanceDomain.getPartitionId());
		elementInstanceEntity.setTimestamp(elementInstanceDomain.getTimestamp());
		elementInstanceEntity.setWorkflowInstanceKey(elementInstanceDomain.getWorkflowInstanceKey());
		elementInstanceEntity.setWorkflowKey(elementInstanceDomain.getWorkflowKey());
		springDataElementInstanceRepository.save(elementInstanceEntity);
	}

	@Override
	public List<ElementInstanceStatistics> getElementInstanceStatisticsByKeyAndIntentIn(long key,
			Collection<String> intents, Collection<String> excludeElementTypes) {
		return springDataElementInstanceRepository.getElementInstanceStatisticsByKeyAndIntentIn(key, intents, excludeElementTypes);
	}

}
