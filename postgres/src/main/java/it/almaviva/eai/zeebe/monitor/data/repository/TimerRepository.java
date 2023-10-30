package it.almaviva.eai.zeebe.monitor.data.repository;

import it.almaviva.eai.zeebe.monitor.data.entity.TimerEntity;
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
		Optional<TimerEntity> byId = springDataTimerRepository.findById(id);
		TimerEntity timerEntity = byId.orElseGet(() -> {return null;});
		return iTimerMapper.map(timerEntity);
	}

	@Override
	public List<TimerDomain> findByWorkflowInstanceKey(Long workflowInstanceKey) {
		List<TimerEntity> byWorkflowInstanceKey = springDataTimerRepository.findByWorkflowInstanceKey(workflowInstanceKey);
		return iTimerMapper.map(byWorkflowInstanceKey);
	}

	@Override
	public List<TimerDomain> findByWorkflowKeyAndWorkflowInstanceKeyIsNull(Long workflowInstanceKey) {
		List<TimerEntity> byWorkflowKeyAndWorkflowInstanceKeyIsNull = springDataTimerRepository.findByWorkflowKeyAndWorkflowInstanceKeyIsNull(workflowInstanceKey);
		return iTimerMapper.map(byWorkflowKeyAndWorkflowInstanceKeyIsNull);
	}

	@Override
	public void save(TimerDomain timerDomain) {
		TimerEntity timerEntity = new TimerEntity();
		timerEntity.setKey(timerDomain.getKey());
		timerEntity.setDueDate(timerDomain.getDueDate());
		timerEntity.setElementInstanceKey(timerDomain.getElementInstanceKey());
		timerEntity.setRepetitions(timerDomain.getRepetitions());
		timerEntity.setState(timerDomain.getState());
		timerEntity.setTargetFlowNodeId(timerDomain.getTargetFlowNodeId());
		timerEntity.setTimestamp(timerDomain.getTimestamp());
		timerEntity.setWorkflowInstanceKey(timerDomain.getWorkflowInstanceKey());
		timerEntity.setWorkflowKey(timerDomain.getWorkflowKey());
		springDataTimerRepository.save(timerEntity);
	}

}
