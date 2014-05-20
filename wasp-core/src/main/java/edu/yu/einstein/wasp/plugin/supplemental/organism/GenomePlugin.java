/**
 * 
 */
package edu.yu.einstein.wasp.plugin.supplemental.organism;

import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;

/**
 * @author calder
 * 
 */
public class GenomePlugin extends WaspPlugin {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FileService fileService;

	@Autowired
	private GridHostResolver hostResolver;

	@Autowired
	private MessageChannelRegistry messageChannelRegistry;

	@Autowired
	private GenomeService genomeService;

	/**
	 * @param iName
	 * @param waspSiteProperties
	 * @param channel
	 */
	public GenomePlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}

	public Message<String> list(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return listHelp();
		
		Set<Organism> orgs = genomeService.getOrganisms();
		String retval = "\n";
		for (Organism o : orgs) {
			retval += o.getNcbiID() + ": " + o.getName() + "\n";
			for (Genome g : o.getGenomes().values()) {
				for (Build b : g.getBuilds().values()) {
					retval += "\t" + g.getName() + ": " + b.getVersion() + "\n\t\t" + b.getDescription() + "\n"; 
				}
			}
		}

		return MessageBuilder.withPayload(retval).build();
	}


	public Message<String> listHelp() {
		String mstr = "\nGenome Plugin: List available genome information\n" +
				"wasp -T genome -t list\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

}
