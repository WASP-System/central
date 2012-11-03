/**
 * 
 */
package edu.yu.einstein.wasp.plugin.wasp.illumina;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;

/**
 * @author calder
 *
 */
public class WaspIlluminaPlugin extends WaspPlugin {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -654454985142070980L;


	public WaspIlluminaPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
		super(pluginName, waspSiteProperties, channel);
	}
	
	public Message bcl2qseq(Message m) {
		return null;
	}

	@Override
	public Set<String> getBatchJobNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
