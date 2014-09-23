package edu.yu.einstein.wasp.plugin.mps.genomebrowser;

import edu.yu.einstein.wasp.interfacing.plugin.WaspPluginI;

public interface GenomeBrowserProviding extends WaspPluginI {
	
	
	
	public boolean isDisplayable(Integer fileGroupId);
	public String getIcon();
	public String getLink(Integer fileGroupId);
	
	//public Action getAction(Integer fileGroupId);
}
