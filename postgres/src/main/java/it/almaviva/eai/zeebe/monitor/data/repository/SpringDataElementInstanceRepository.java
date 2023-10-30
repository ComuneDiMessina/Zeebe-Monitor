package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.ElementInstanceEntity;
import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceStatistics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SpringDataElementInstanceRepository extends CrudRepository<ElementInstanceEntity, Long> {
	
	Iterable<ElementInstanceEntity> findByWorkflowInstanceKey(long workflowInstanceKey);
	
	  @Query(
		      nativeQuery = true,
		      value =
		          "SELECT ELEMENT_ID_ AS elementId, COUNT(*) AS count "
		              + "FROM TAB_ZMO_ELEMENT_INSTANCE "
		              + "WHERE WORKFLOW_KEY_ = :key and INTENT_ in (:intents) and BPMN_ELEMENT_TYPE_ not in (:excludeElementTypes)"
		              + "GROUP BY ELEMENT_ID_")
		  List<ElementInstanceStatistics> getElementInstanceStatisticsByKeyAndIntentIn(
		      @Param("key") long key,
		      @Param("intents") Collection<String> intents,
		      @Param("excludeElementTypes") Collection<String> excludeElementTypes);

}
