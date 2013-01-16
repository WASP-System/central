/**
 * 
 */
package edu.yu.einstein.wasp.plugin.standard;

import java.util.Properties;
import java.util.Set;

import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.plugin.WaspPlugin;

/**
 * Plugin to perform file operations on registered files (file objects), including
 * registration and de-registration (and deletion) through messaging.
 * 
 * @author calder
 *
 */
public class FilePlugin extends WaspPlugin {

	/**
	 * @param pluginName
	 * @param waspSiteProperties
	 * @param channel
	 */
	public FilePlugin(String pluginName, Properties waspSiteProperties,
			MessageChannel channel) {
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

	@Override
	public String getBatchJobNameByArea(String BatchJobType, String area) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getBatchJobName(String BatchJobType) {
		// TODO Auto-generated method stub
		return null;
	}


}
