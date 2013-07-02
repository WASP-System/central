/**
 * 
 */
package edu.yu.einstein.wasp.fileformat.plugin;

import java.util.Properties;

import org.springframework.integration.MessageChannel;

/**
 * @author asmclellan
 * 
 */
public class WaspBaiPlugin extends WaspBamPlugin {


	private static final long serialVersionUID = 4873013813026401519L;

	public WaspBaiPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
		super(pluginName, waspSiteProperties, channel);
	}

	
}
