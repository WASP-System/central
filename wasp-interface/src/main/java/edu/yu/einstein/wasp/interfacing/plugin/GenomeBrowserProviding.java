package edu.yu.einstein.wasp.interfacing.plugin;

public interface GenomeBrowserProviding extends WaspPluginI {
	
	
	
	public boolean isDisplayable(Integer fileGroupId);
	public String getIcon();
	public String getLink(Integer fileGroupId);
}
