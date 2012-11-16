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
	
	public static final String FLOW_NAME = "wasp-illumina.jobFlow";
	
	public static final String STEP_NOTIFY_RUN_START = "wasp-illumina.mainFlow.notifyRunStart";
	public static final String STEP_LISTEN_FOR_RUN_START = "wasp-illumina.mainFlow.listenForRunStart";
	public static final String STEP_LISTEN_FOR_RUN_COMPLETION = "wasp-illumina.mainFlow.listenForRunCompletion";
	public static final String STEP_LISTEN_FOR_QC = "wasp-illumina.mainFlow.listenForQCCompletion";
	public static final String STEP_CREATE_SAMPLE_SHEET = "wasp-illumina.mainFlow.createSampleSheet";


	public WaspIlluminaPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
		super(pluginName, waspSiteProperties, channel);
	}
	
	public Message bcl2qseq(Message m) {
		return null;
	}

	@Override
	public Set<String> getBatchJobNames() {
		HashSet<String> names = new HashSet<String>();
		names.add(FLOW_NAME);
		return names;
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
