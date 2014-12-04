/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.plugin;

import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.bwa.service.BwaService;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;

/**
 * @author calder / asmclellan
 * 
 */
public abstract class AbstractBWAPlugin extends WaspPlugin implements ClientMessageI, BWAPluginCli, BatchJobProviding {

	private static final long serialVersionUID = 4158608859447480863L;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	protected BwaService bwaService;
	
	@Autowired
	private GenomeService genomeService;
	
	protected Software software;
	
	public AbstractBWAPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}

	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}

	protected Integer getCellLibraryIdFromMessage(Message<String> m) {
		Integer clId = null;

		JSONObject jo;
		try {
			jo = new JSONObject(m.getPayload().toString());
			String value = "";
			if (jo.has("cellLibrary")) {
				value = (String) jo.get("cellLibrary");
				clId = new Integer(value);
			}
		} catch (JSONException e) {
			logger.warn("unable to parse JSON");
		}

		return clId;
	}
	
	

	@Override
	public void afterPropertiesSet() throws Exception {	}

	@Override
	public void destroy() throws Exception { }
	
	@Override
	public Message<String> buildIndex(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return buildIndexHelp();

		logger.info("launching FASTA build ");

		Build b;
		GridWorkService host;
		String hostname = "";

		try {
			JSONObject jo = new JSONObject(m.getPayload().toString());

			try {
				Integer organism = jo.getInt("organism");
				String genome = jo.getString("genome");
				String build = jo.getString("build");
				b = genomeService.getBuild(organism, genome, build);
			} catch (Exception e) {
				return MessageBuilder.withPayload("Unable to determine build from message: " + m.getPayload().toString()).build();
			}

			try {
				hostname = jo.getString("hostname");
				host = gridHostResolver.getGridWorkService(hostname);
			} catch (Exception e) {
				return MessageBuilder.withPayload("Unable to determine host from message: " + m.getPayload().toString()).build();
			}

			GenomeIndexStatus status = bwaService.getGenomeIndexStatus(host, b);

			return MessageBuilder.withPayload("BWA index for " + b.getGenomeBuildNameString() + " has status " + status.toString()).build();

		} catch (Exception e1) {
			String mess = "unable to build message to launch batch job for building BWA index on " + hostname;
			logger.warn(mess);
			return MessageBuilder.withPayload(mess).build();
		}

	}
	
	@Override
	public Message<String> buildIndexHelp() {
		String mstr = "\ngenome metadata plugin: launch a BWA index build.\n"
				+ "wasp -T bwa-mem -t buildIndex -m \'{organism:\"10090\",genome:GRCm38,build:\"75\",hostname:remote.host.org}\'\n";
		return MessageBuilder.withPayload(mstr).build();
		
	}

}
