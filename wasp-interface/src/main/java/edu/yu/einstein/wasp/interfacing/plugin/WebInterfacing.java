package edu.yu.einstein.wasp.interfacing.plugin;

import edu.yu.einstein.wasp.interfacing.Hyperlink;

public interface WebInterfacing extends WaspPluginI {
	
	/**
	 * returns the link text and relative URL for the location of a webpage which displays a plugin's description 
	 * @return
	 */
	public Hyperlink getDescriptionPageHyperlink();

}
