package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.ElementInstanceDocument;
import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SpringDataElementInstanceRepository extends MongoRepository<ElementInstanceDocument, Long> {
	
	Iterable<ElementInstanceDocument> findByWorkflowInstanceKey(long workflowInstanceKey);
	
    List<ElementInstanceStatistics> getElementInstanceStatisticsByKeyAndIntentIn(long key,Collection<String> intents,Collection<String> excludeElementTypes);

}
