package edu.yu.einstein.wasp.mps.tools.plugin;

import java.util.Properties;

import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.mps.tools.Picard;
import edu.yu.einstein.wasp.mps.tools.SAMTools;
import edu.yu.einstein.wasp.plugin.WaspPlugin;

public class MPSToolsPlugin extends WaspPlugin {

	public MPSToolsPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
		super(pluginName, waspSiteProperties, channel);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1668181496726966963L;
	
	private Picard picard;
	private SAMTools samtools;
	
	/**
	 * @return the picard
	 */
	public Picard getPicard() {
		return picard;
	}

	/**
	 * @param picard the picard to set
	 */
	public void setPicard(Picard picard) {
		this.picard = picard;
	}

	/**
	 * @return the samtools
	 */
	public SAMTools getSamtools() {
		return samtools;
	}

	/**
	 * @param samtools the samtools to set
	 */
	public void setSamtools(SAMTools samtools) {
		this.samtools = samtools;
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
