/**
 * 
 */
package edu.yu.einstein.wasp.plugin.assay;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;

import edu.yu.einstein.wasp.Hyperlink;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.WebInterfacing;
import edu.yu.einstein.wasp.plugin.cli.ClientMessageI;

/**
 * @author asmclellan
 * 
 */
public class WaspHelpTagPlugin extends WaspPlugin implements ClientMessageI, WebInterfacing {

	private static Logger logger = LoggerFactory.getLogger(WaspHelpTagPlugin.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -6546551115142070980L;

	public WaspHelpTagPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
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
		return new Hyperlink("waspHelpTag.hyperlink.label", "/wasp-helptag/description.do");
	}

}
