package it.almaviva.eai.zeebe.monitor.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.command.DeployWorkflowCommandStep1;
import io.zeebe.client.api.response.DeploymentEvent;
import io.zeebe.client.api.response.WorkflowInstanceEvent;
import it.almaviva.eai.zeebe.monitor.dto.DeploymentDTO;
import it.almaviva.eai.zeebe.monitor.dto.FileDTO;

@RestController
@RequestMapping(path = "rest/api/workflows")
public class WorkflowController {
	
	@Autowired
	private ZeebeClient zeebeClient;

	@RequestMapping(path = "/{workflowKey}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String, Object> createWorkflowInstance(@PathVariable("workflowKey") long workflowKey,
			@RequestBody String payload) {

		Map<String, Object> response = new HashMap<>();
		try {
			WorkflowInstanceEvent event = zeebeClient.newCreateInstanceCommand().workflowKey(workflowKey)
					.variables(payload).send().join();
			if (Objects.isNull(event.getWorkflowInstanceKey()) == false) {
				response.put("status", 200);
				response.put("event", event);
			} else
				throw new RuntimeException("error creating instances");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			response.put("status", 500);
			response.put("error", e.getMessage());
		}

		return response;
	}

	@RequestMapping(path = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String, Object> uploadModel(@RequestBody DeploymentDTO deployment) throws UnsupportedEncodingException {

		Map<String, Object> response = new HashMap<>();

		try {

			final List<FileDTO> files = deployment.getFiles();
			if (files.isEmpty()) {
				throw new RuntimeException("no resources to deploy");
			}

			final FileDTO firstFile = files.get(0);

			final DeployWorkflowCommandStep1.DeployWorkflowCommandBuilderStep2 cmd = zeebeClient
		            .newDeployCommand()
		            .addResourceBytes(firstFile.getContent(), firstFile.getFilename());

			for (FileDTO file : files.subList(1, files.size())) {
				cmd.addResourceBytes(file.getContent(), file.getFilename());
			}

			DeploymentEvent event = cmd.send().join();
			if (event.getWorkflows().size() > 0)
				response.put("status", 200);
			else
				throw new RuntimeException("deploy error");

		} catch (Exception e) {
			System.err.println(e.getMessage());
			response.put("status", 500);
			response.put("error", e.getMessage());
		}

		return response;
	}

}
