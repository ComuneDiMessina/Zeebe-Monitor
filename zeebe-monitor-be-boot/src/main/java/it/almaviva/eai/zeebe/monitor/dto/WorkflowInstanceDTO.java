package it.almaviva.eai.zeebe.monitor.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkflowInstanceDTO {
	
	  private int partitionId;

	  private long workflowInstanceKey;

	  private String bpmnProcessId;
	  private long workflowKey;
	  private int version;

	  private String state;
	  private boolean isRunning;

	  private String startTime = "";
	  private String endTime = "";

	  private Long parentWorkflowInstanceKey;
	  private String parentBpmnProcessId = "";

	  private List<VariableEntry> variables = new ArrayList<>();
	  private List<ActiveScope> activeScopes = new ArrayList<>();

	  private List<ElementInstanceState> elementInstances = new ArrayList<>();

	  private List<AuditLogEntry> auditLogEntries = new ArrayList<>();

	  private List<String> activeActivities = new ArrayList<>();
	  private List<String> incidentActivities = new ArrayList<>();
	  private List<String> takenSequenceFlows = new ArrayList<>();

	  private List<IncidentDTO> incidents = new ArrayList<>();
	  private List<JobDTO> jobs = new ArrayList<>();
	  private List<MessageSubscriptionDTO> messageSubscriptions = new ArrayList<>();
	  private List<TimerDTO> timers = new ArrayList<>();
	  private List<CalledWorkflowInstanceDTO> calledWorkflowInstances = new ArrayList<>();

	  private List<BpmnElementInfo> bpmnElementInfos = new ArrayList<>();

}
