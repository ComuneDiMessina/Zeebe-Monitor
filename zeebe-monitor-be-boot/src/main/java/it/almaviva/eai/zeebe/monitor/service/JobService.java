package it.almaviva.eai.zeebe.monitor.service;

import it.almaviva.eai.zeebe.monitor.domain.JobDomain;
import it.almaviva.eai.zeebe.monitor.port.incoming.IJobUseCase;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IJobPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class JobService implements IJobUseCase{
	
	@Autowired
	private IJobPort iJobPort;

	@Override
	public List<JobDomain> findByWorkflowInstanceKey(long workflowInstanceKey) {
		return iJobPort.findByWorkflowInstanceKey(workflowInstanceKey);
	}

	@Override
	public JobDomain findByKey(long key) {
		return iJobPort.findByKey(key);
	}

	@Override
	public List<JobDomain> findByStateNotIn(Collection<String> state) {
		return iJobPort.findByStateNotIn(state);
	}

	@Override
	public long countByStateNotIn(Collection<String> state) {
		return iJobPort.countByStateNotIn(state);
	}

	@Override
	public void save(JobDomain jobDomain) {
		iJobPort.save(jobDomain);
		
	}

}
