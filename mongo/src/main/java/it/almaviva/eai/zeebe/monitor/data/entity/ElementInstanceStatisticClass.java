package it.almaviva.eai.zeebe.monitor.data.entity;

import it.almaviva.eai.zeebe.monitor.domain.ElementInstanceStatistics;

public class ElementInstanceStatisticClass implements ElementInstanceStatistics {

	private String elementId;
	private long count;
	
	@Override
	public String getElementId() {
		// TODO Auto-generated method stub
		return elementId;
	}

	@Override
	public long getCount() {
		// TODO Auto-generated method stub
		return count;
	}

}
