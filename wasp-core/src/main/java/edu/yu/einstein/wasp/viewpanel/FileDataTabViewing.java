package edu.yu.einstein.wasp.viewpanel;

import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.plugin.WebInterfacing;

public interface FileDataTabViewing extends WebInterfacing{
	
public static enum Status{ COMPLETED, STARTED, PENDING, FAILED, UNKNOWN }
	
	public Status getStatus(FileGroup fileGroup);
	
	public PanelTab getViewPanelTab(FileGroup fileGroup);

}
