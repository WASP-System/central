/**
 * 
 */
package edu.yu.einstein.wasp.plugin.supplemental.system;

import java.util.Properties;

import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.plugin.WaspPlugin;

/**
 * @author calder
 *
 */
public class SystemPlugin extends WaspPlugin {

	/**
	 * @param pluginName
	 * @param waspSiteProperties
	 * @param channel
	 */
	public SystemPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
		super(pluginName, waspSiteProperties, channel);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

}
