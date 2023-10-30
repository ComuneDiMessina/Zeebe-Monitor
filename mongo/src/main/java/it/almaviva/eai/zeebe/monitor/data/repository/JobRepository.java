package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.JobDocument;
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
		Iterable<JobDocument> byWorkflowInstanceKey = springDataJobRepository.findByWorkflowInstanceKey(workflowInstanceKey);
		return iJobMapper.map(byWorkflowInstanceKey);
	}

	@Override
	public JobDomain findByKey(long key) {
		Optional<JobDocument> byKey = springDataJobRepository.findById(key);
		JobDocument JobDocument = byKey.orElseGet(() -> {return null;});
		return iJobMapper.map(JobDocument);
	}

	@Override
	public List<JobDomain> findByStateNotIn(Collection<String> state) {
		Iterable<JobDocument> byStateNotIn = springDataJobRepository.findByStateNotIn(state);
		return iJobMapper.map(byStateNotIn);
	}

	@Override
	public long countByStateNotIn(Collection<String> state) {
		return springDataJobRepository.countByStateNotIn(state);
	}

	@Override
	public void save(JobDomain jobDomain) {
		
		JobDocument JobDocument = new JobDocument();
		JobDocument.setKey(jobDomain.getKey());
		JobDocument.setElementInstanceKey(jobDomain.getElementInstanceKey());
		JobDocument.setJobType(jobDomain.getJobType());
		JobDocument.setRetries(jobDomain.getRetries());
		JobDocument.setState(jobDomain.getState());
		JobDocument.setTimestamp(jobDomain.getTimestamp());
		JobDocument.setWorker(jobDomain.getWorker());
		JobDocument.setWorkflowInstanceKey(jobDomain.getWorkflowInstanceKey());
		springDataJobRepository.save(JobDocument);
	}

}
