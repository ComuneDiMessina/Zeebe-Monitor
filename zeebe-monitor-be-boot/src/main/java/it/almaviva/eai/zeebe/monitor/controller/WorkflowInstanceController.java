package it.almaviva.eai.zeebe.monitor.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.zeebe.client.ZeebeClient;
import it.almaviva.eai.zeebe.monitor.domain.JobDomain;
import it.almaviva.eai.zeebe.monitor.dto.ResolveIncidentDTO;
import it.almaviva.eai.zeebe.monitor.service.JobService;
import it.almaviva.eai.zeebe.monitor.service.WorkflowInstanceService;

@RestController
@RequestMapping("rest/api/instances")
public class WorkflowInstanceController {
	
	@Autowired
	private ZeebeClient zeebeClient;
	
	@Autowired
	private WorkflowInstanceService workflowInstanceService;
	
	@Autowired
	private JobService jobService;

	@RequestMapping(path = "/{key}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String, Object> cancelWorkflowInstance(@RequestHeader("X-Auth-Token") String token,@PathVariable("key") long key) throws Exception {

		Map<String, Object> response = new HashMap<>();
		List<JobDomain> jobList = new ArrayList<JobDomain>();
        boolean checkUserTask = false;
		try {
			jobList = jobService.findByWorkflowInstanceKey(key);
			for (JobDomain jobDomain : jobList) {
				if(jobDomain.getJobType().equalsIgnoreCase("user"))
				{
					checkUserTask = true;
					break;
				}
			}
			if(checkUserTask)
			{
				zeebeClient.newCancelInstanceCommand(key).send().join();
				workflowInstanceService.deleteTasks(token,String.valueOf(key));
			}
			else		
			zeebeClient.newCancelInstanceCommand(key).send().join();

			response.put("status", 200);
		} catch (Exception e) {
			response.put("status", 500);
			response.put("error", e.getMessage());
		}

		return response;
	}

	@RequestMapping(path = "/{key}/set-variables", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String, Object> setVariables(@PathVariable("key") long key, @RequestBody String payload)
			throws Exception {

		Map<String, Object> response = new HashMap<>();

		try {
			zeebeClient.newSetVariablesCommand(key).variables(payload).send().join();
			response.put("status", 200);
		} catch (Exception e) {
			response.put("status", 500);
			response.put("error", e.getMessage());
		}
		return response;
	}

	@RequestMapping(path = "/{key}/set-variables-local", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String, Object> setVariablesLocal(@PathVariable("key") long key, @RequestBody String payload)
			throws Exception {

		Map<String, Object> response = new HashMap<>();

		try {
			zeebeClient.newSetVariablesCommand(key).variables(payload).local(true).send().join();
			response.put("status", 200);
		} catch (Exception e) {
			response.put("status", 500);
			response.put("error", e.getMessage());
		}

		return response;
	}

	@RequestMapping(path = "/{key}/resolve-incident", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String, Object> resolveIncident(@PathVariable("key") long key, @RequestBody ResolveIncidentDTO dto)
			throws Exception {

		Map<String, Object> response = new HashMap<>();

		try {

			if (dto.getJobKey() != null && dto.getJobKey() > 0) {
				zeebeClient.newUpdateRetriesCommand(dto.getJobKey()).retries(dto.getRemainingRetries()).send().join();
			}

			zeebeClient.newResolveIncidentCommand(key).send().join();
			response.put("status", 200);
		} catch (Exception e) {
			response.put("status", 500);
			response.put("error", e.getMessage());
		}
		return response;
	}

}
