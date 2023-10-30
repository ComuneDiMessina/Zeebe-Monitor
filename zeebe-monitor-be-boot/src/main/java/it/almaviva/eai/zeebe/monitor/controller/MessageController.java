package it.almaviva.eai.zeebe.monitor.controller;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.zeebe.client.ZeebeClient;
import it.almaviva.eai.zeebe.monitor.dto.PublishMessageDTO;

@RestController
@RequestMapping("rest/api/messages")
public class MessageController {
	
	 @Autowired
	 private ZeebeClient zeebeClient;

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public Map<String, Object> publishMessage(@RequestBody PublishMessageDTO dto) {

		Map<String, Object> response = new HashMap<>();

		try {
			zeebeClient.newPublishMessageCommand().messageName(dto.getName())
					.correlationKey(dto.getCorrelationKey()).variables(dto.getPayload())
					.timeToLive(Duration.parse(dto.getTimeToLive())).send().join();
			response.put("status", 200);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("error", e.getMessage());
		}

		return response;

	}

}
