package it.almaviva.eai.zeebe.monitor.service;

import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceDomain;
import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceStatistics;
import it.almaviva.eai.zeebe.monitor.port.incoming.IElementInstanceUseCase;
import it.almaviva.eai.zeebe.monitor.port.outgoing.IElementInstancePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ElementInstanceService implements IElementInstanceUseCase {
	
	@Autowired
	private IElementInstancePort iElementInstancePort;

	@Override
	public Iterable<ElementInstanceDomain> findByWorkflowInstanceKey(long workflowInstanceKey) {
		return iElementInstancePort.findByWorkflowInstanceKey(workflowInstanceKey);
	}

	@Override
	public boolean existsById(long position) {
		return iElementInstancePort.existsById(position);
	}

	@Override
	public void save(ElementInstanceDomain elementInstanceDomain) {
		iElementInstancePort.save(elementInstanceDomain);
	}

	@Override
	public List<ElementInstanceStatistics> getElementInstanceStatisticsByKeyAndIntentIn(long key,
			Collection<String> intents, Collection<String> excludeElementTypes) {
		return iElementInstancePort.getElementInstanceStatisticsByKeyAndIntentIn(key, intents, excludeElementTypes);
	}

}
