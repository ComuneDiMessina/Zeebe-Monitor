package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.ElementInstanceDocument;
import it.almaviva.eai.zeebe.monitor.data.entity.ElementInstanceStatisticClass;
import it.almaviva.eai.zeebe.monitor.data.mapper.IElementInstanceMapper;
import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceDomain;
import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceStatistics;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IElementInstancePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Component
public class ElementInstanceRepository implements IElementInstancePort{
	
	@Autowired
	private SpringDataElementInstanceRepository springDataElementInstanceRepository;
	
	@Autowired
	private IElementInstanceMapper iElementInstanceMapper;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Iterable<ElementInstanceDomain> findByWorkflowInstanceKey(long workflowInstanceKey) {
		Iterable<ElementInstanceDocument> ByWorkflowInstanceKey = springDataElementInstanceRepository.findByWorkflowInstanceKey(workflowInstanceKey);
		return iElementInstanceMapper.map(ByWorkflowInstanceKey);
	}

	@Override
	public boolean existsById(long position) {
		return springDataElementInstanceRepository.existsById(position);
	}

	@Override
	public void save(ElementInstanceDomain elementInstanceDomain) {

		ElementInstanceDocument ElementInstanceDocument = new ElementInstanceDocument();
		ElementInstanceDocument.setPosition(elementInstanceDomain.getPosition());
		ElementInstanceDocument.setBpmnElementType(elementInstanceDomain.getBpmnElementType());
		ElementInstanceDocument.setElementId(elementInstanceDomain.getElementId());
		ElementInstanceDocument.setFlowScopeKey(elementInstanceDomain.getFlowScopeKey());
		ElementInstanceDocument.setIntent(elementInstanceDomain.getIntent());
		ElementInstanceDocument.setKey(elementInstanceDomain.getKey());
		ElementInstanceDocument.setPartitionId(elementInstanceDomain.getPartitionId());
		ElementInstanceDocument.setTimestamp(elementInstanceDomain.getTimestamp());
		ElementInstanceDocument.setWorkflowInstanceKey(elementInstanceDomain.getWorkflowInstanceKey());
		ElementInstanceDocument.setWorkflowKey(elementInstanceDomain.getWorkflowKey());
		springDataElementInstanceRepository.save(ElementInstanceDocument);
	}

	@Override
	public List<ElementInstanceStatistics> getElementInstanceStatisticsByKeyAndIntentIn(long key,
			Collection<String> intents, Collection<String> excludeElementTypes) {

		
		Aggregation agg = newAggregation(match(Criteria.where("workflowKey").is(key)),
	             match(Criteria.where("intent").in(intents)),
	             match(Criteria.where("bpmnElementType").nin(excludeElementTypes)),
		    group("elementId").count().as("count"),
		    project("count").and("elementId").previousOperation()
		    );
		
		List<ElementInstanceStatisticClass> result = mongoTemplate.aggregate(agg, ElementInstanceDocument.class, ElementInstanceStatisticClass.class).getMappedResults();
		
		List<ElementInstanceStatistics> list = new ArrayList<ElementInstanceStatistics>();
		for (ElementInstanceStatistics elementInstanceStatistics : result) {
			list.add(elementInstanceStatistics);
		}
		
		
		return list;
	}

}
