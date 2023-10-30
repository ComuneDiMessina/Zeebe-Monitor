package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.TimerDocument;
import it.almaviva.eai.zeebe.monitor.data.mapper.ITimerMapper;
import it.almaviva.eai.zeebe.monitor.domain.TimerDomain;
import it.almaviva.eai.zeebe.monitor.port.outgoing.ITimerPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TimerRepository implements ITimerPort{
	
	@Autowired
	private SpringDataTimerRepository springDataTimerRepository;
	
	@Autowired
	private ITimerMapper iTimerMapper;

	@Override
	public TimerDomain findById(long id) {
		Optional<TimerDocument> byId = springDataTimerRepository.findById(id);
		TimerDocument TimerDocument = byId.orElseGet(() -> {return null;});
		return iTimerMapper.map(TimerDocument);
	}

	@Override
	public List<TimerDomain> findByWorkflowInstanceKey(Long workflowInstanceKey) {
		List<TimerDocument> byWorkflowInstanceKey = springDataTimerRepository.findByWorkflowInstanceKey(workflowInstanceKey);
		return iTimerMapper.map(byWorkflowInstanceKey);
	}

	@Override
	public List<TimerDomain> findByWorkflowKeyAndWorkflowInstanceKeyIsNull(Long workflowInstanceKey) {
		List<TimerDocument> byWorkflowKeyAndWorkflowInstanceKeyIsNull = springDataTimerRepository.findByWorkflowKeyAndWorkflowInstanceKeyIsNull(workflowInstanceKey);
		return iTimerMapper.map(byWorkflowKeyAndWorkflowInstanceKeyIsNull);
	}

	@Override
	public void save(TimerDomain timerDomain) {
		TimerDocument TimerDocument = new TimerDocument();
		TimerDocument.setKey(timerDomain.getKey());
		TimerDocument.setDueDate(timerDomain.getDueDate());
		TimerDocument.setElementInstanceKey(timerDomain.getElementInstanceKey());
		TimerDocument.setRepetitions(timerDomain.getRepetitions());
		TimerDocument.setState(timerDomain.getState());
		TimerDocument.setTargetFlowNodeId(timerDomain.getTargetFlowNodeId());
		TimerDocument.setTimestamp(timerDomain.getTimestamp());
		TimerDocument.setWorkflowInstanceKey(timerDomain.getWorkflowInstanceKey());
		TimerDocument.setWorkflowKey(timerDomain.getWorkflowKey());
		springDataTimerRepository.save(TimerDocument);
	}

}
