package it.almaviva.eai.zeebe.monitor.port.incoming;

import it.almaviva.eai.zeebe.monitor.domain.TimerDomain;

import java.util.List;

public interface ITimerUseCase {
	
	  TimerDomain findById(long id);
	
	  List<TimerDomain> findByWorkflowInstanceKey(Long workflowInstanceKey);

	  List<TimerDomain> findByWorkflowKeyAndWorkflowInstanceKeyIsNull(Long workflowInstanceKey);
	  
	  void save(TimerDomain timerDomain);

}
