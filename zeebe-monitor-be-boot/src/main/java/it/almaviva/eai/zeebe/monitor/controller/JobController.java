package it.almaviva.eai.zeebe.monitor.controller;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.ActivatedJob;
import it.almaviva.eai.zeebe.monitor.domain.JobDomain;
import it.almaviva.eai.zeebe.monitor.dto.ThrowErrorDTO;
import it.almaviva.eai.zeebe.monitor.port.incoming.IJobUseCase;

@RestController
@RequestMapping("rest/api/jobs")
public class JobController {
	
	private static final String WORKER_NAME = "zeebe-simple-monitor";

	 @Autowired
	 private ZeebeClient zeebeClient;
	
	@Autowired
	private IJobUseCase iJobUseCase;
	
	@RequestMapping(path = "/{key}/complete", method = RequestMethod.PUT)
	public Map<String, Object> completeJob(@PathVariable("key") long key, @RequestBody String variables) {

		Map<String, Object> response = new HashMap<>();

		try {
			zeebeClient.newCompleteCommand(key).variables(variables).send().join();
			response.put("status", 200);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("error", e.getMessage());
		}

		return response;
	}

	@RequestMapping(path = "/{key}/fail", method = RequestMethod.PUT)
	public Map<String, Object> failJob(@PathVariable("key") long key) {

		Map<String, Object> response = new HashMap<>();

		try {
			final ActivatedJob activatedJob = activateJob(key, zeebeClient);
		    zeebeClient
		        .newFailCommand(activatedJob.getKey())
		        .retries(0)
		        .errorMessage("Failed by user.").send()
		        .join();
			response.put("status", 200);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("error", e.getMessage());
		}

		return response;

	}

	/*new method*/
	@RequestMapping(path = "/{key}/throw-error", method = RequestMethod.PUT)
	public Map<String, Object> throwError(@PathVariable("key") long key, @RequestBody ThrowErrorDTO dto) {

		Map<String, Object> response = new HashMap<>();
		
		try {
			zeebeClient.newThrowErrorCommand(key).errorCode(dto.getErrorCode()).send().join();
			response.put("status", 200);
		}catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("error", e.getMessage());
		}

		return response;
		
	}
	
	private ActivatedJob activateJob(long key, final ZeebeClient client) {
		final JobDomain job = getJob(key);
		final String jobType = job.getJobType();

		return activateJob(client, key, jobType);
	}

	private JobDomain getJob(long key) {
		return Optional.of(iJobUseCase.findByKey(key)).orElseThrow(() -> new RuntimeException("no job found with key: " + key));
	}

	private ActivatedJob activateJob(final ZeebeClient client, long key, final String jobType) {

		final List<ActivatedJob> jobs = client.newActivateJobsCommand().jobType(jobType).maxJobsToActivate(10)
				.timeout(Duration.ofSeconds(10)).workerName(WORKER_NAME).send().join().getJobs();

		if (jobs.isEmpty()) {
			throw new RuntimeException("no activatable job found with key: " + key);
		} else {
			return jobs.stream().filter(activatedJob -> activatedJob.getKey() == key).findFirst()
					.orElseGet(() -> activateJob(client, key, jobType));
		}
	}

}
