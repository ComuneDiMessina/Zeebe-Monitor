package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.JobEntity;
import it.almaviva.eai.zeebe.monitor.data.mapper.IJobMapper;
import it.almaviva.eai.zeebe.monitor.domain.JobDomain;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IJobPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class JobRepository implements IJobPort{
	
	@Autowired
	private SpringDataJobRepository springDataJobRepository;
	
	@Autowired
	private IJobMapper iJobMapper;

	@Override
	public List<JobDomain> findByWorkflowInstanceKey(long workflowInstanceKey) {
		Iterable<JobEntity> byWorkflowInstanceKey = springDataJobRepository.findByWorkflowInstanceKey(workflowInstanceKey);
		return iJobMapper.map(byWorkflowInstanceKey);
	}

	@Override
	public JobDomain findByKey(long key) {
		Optional<JobEntity> byKey = springDataJobRepository.findByKey(key);
		JobEntity jobEntity = byKey.orElseGet(() -> {return null;});
		return iJobMapper.map(jobEntity);
	}

	@Override
	public List<JobDomain> findByStateNotIn(Collection<String> state) {
		Iterable<JobEntity> byStateNotIn = springDataJobRepository.findByStateNotIn(state);
		return iJobMapper.map(byStateNotIn);
	}

	@Override
	public long countByStateNotIn(Collection<String> state) {
		return springDataJobRepository.countByStateNotIn(state);
	}

	@Override
	public void save(JobDomain jobDomain) {
		
		JobEntity jobEntity = new JobEntity();
		jobEntity.setKey(jobDomain.getKey());
		jobEntity.setElementInstanceKey(jobDomain.getElementInstanceKey());
		jobEntity.setJobType(jobDomain.getJobType());
		jobEntity.setRetries(jobDomain.getRetries());
		jobEntity.setState(jobDomain.getState());
		jobEntity.setTimestamp(jobDomain.getTimestamp());
		jobEntity.setWorker(jobDomain.getWorker());
		jobEntity.setWorkflowInstanceKey(jobDomain.getWorkflowInstanceKey());
		springDataJobRepository.save(jobEntity);
	}

}
