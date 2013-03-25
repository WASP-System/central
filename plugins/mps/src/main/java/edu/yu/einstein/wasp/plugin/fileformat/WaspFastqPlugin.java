/**
 * 
 */
package edu.yu.einstein.wasp.plugin.fileformat;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.Hyperlink;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.WebInterfacing;
import edu.yu.einstein.wasp.plugin.cli.ClientMessageI;

/**
 * @author asmclellan
 * 
 */
public class WaspFastqPlugin extends WaspPlugin implements ClientMessageI, WebInterfacing {

	private static Logger logger = LoggerFactory.getLogger(WaspFastqPlugin.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -654454985142650980L;

	public WaspFastqPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
		super(pluginName, waspSiteProperties, channel);
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Hyperlink getDescriptionPageHyperlink(){
		return new Hyperlink("waspFastq.hyperlink.label", "/wasp-fastq/description.do");
	}

}
