package edu.yu.einstein.wasp.viewpanel;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.model.FileGroup;

public interface FileDataTabViewing extends DataTabViewing {
	
	public Status getStatus(FileGroup fileGroup);
	
	public PanelTab getViewPanelTab(FileGroup fileGroup) throws PanelException;

}
