/**
 * 
 */
package edu.yu.einstein.wasp.plugin.assay;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.Hyperlink;
import edu.yu.einstein.wasp.interfaces.WebInterfacing;
import edu.yu.einstein.wasp.interfaces.cli.ClientMessageI;
import edu.yu.einstein.wasp.plugin.WaspPlugin;

/**
 * @author asmclellan
 * 
 */
public class WaspChipSeqPlugin extends WaspPlugin implements ClientMessageI, WebInterfacing {

	private static Logger logger = LoggerFactory.getLogger(WaspChipSeqPlugin.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -6546554985142070980L;

	public WaspChipSeqPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
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
		return new Hyperlink("waspChipSeq.hyperlink.label", "/wasp-chipseq/description.do");
	}

}
