package it.almaviva.eai.zeebe.monitor.service;

import it.almaviva.eai.zeebe.monitor.domain.TimerDomain;
import it.almaviva.eai.zeebe.monitor.port.incoming.ITimerUseCase;
import it.almaviva.eai.zeebe.monitor.port.outgoing.ITimerPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimerService implements ITimerUseCase{
	
	@Autowired
	private ITimerPort iTimerPort;

	@Override
	public TimerDomain findById(long id) {
		return iTimerPort.findById(id);
	}

	@Override
	public List<TimerDomain> findByWorkflowInstanceKey(Long workflowInstanceKey) {
		return iTimerPort.findByWorkflowInstanceKey(workflowInstanceKey);
	}

	@Override
	public List<TimerDomain> findByWorkflowKeyAndWorkflowInstanceKeyIsNull(Long workflowInstanceKey) {
		return iTimerPort.findByWorkflowKeyAndWorkflowInstanceKeyIsNull(workflowInstanceKey);
	}

	@Override
	public void save(TimerDomain timerDomain) {
		iTimerPort.save(timerDomain);
	}

}
