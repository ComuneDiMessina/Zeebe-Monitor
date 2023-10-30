package it.almaviva.eai.zeebe.monitor.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "UserTask", url = "${usertask.endpoint}")
public interface IUserTaskClient {


	@RequestMapping(value = "/usertask/admin/tasks/workflows/{key}", method = RequestMethod.DELETE, produces = "application/json")
	void deleteTasks(@RequestHeader(value = "X-Auth-Token", required = true) String authorizationHeader,@PathVariable("key") String key);

}
