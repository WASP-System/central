package edu.yu.einstein.interfacing.wasp.interfacing.plugin;

import edu.yu.einstein.interfacing.wasp.Hyperlink;

public interface WebInterfacing extends WaspPluginI {
	
	/**
	 * returns the link text and relative URL for the location of a webpage which displays a plugin's description 
	 * @return
	 */
	public Hyperlink getDescriptionPageHyperlink();

}
