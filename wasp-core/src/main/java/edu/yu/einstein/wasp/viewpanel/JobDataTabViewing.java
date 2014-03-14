package edu.yu.einstein.wasp.viewpanel;

import java.util.Set;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.model.Job;

public interface JobDataTabViewing extends DataTabViewing {

	public Status getStatus(Job job);
	
	public Set<PanelTab> getViewPanelTabs(Job job) throws PanelException;
	
}
