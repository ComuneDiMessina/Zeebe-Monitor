package it.almaviva.eai.zeebe.monitor.port.incoming;

import it.almaviva.eai.zeebe.monitor.domain.JobDomain;

import java.util.Collection;
import java.util.List;

public interface IJobUseCase {
	
	  List<JobDomain> findByWorkflowInstanceKey(long workflowInstanceKey);

	  JobDomain findByKey(long key);

	  List<JobDomain> findByStateNotIn(Collection<String> state);

	  long countByStateNotIn(Collection<String> state);
	  
	  void save(JobDomain jobDomain);

}
