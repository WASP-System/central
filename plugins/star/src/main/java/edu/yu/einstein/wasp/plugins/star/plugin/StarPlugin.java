/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugins.star.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.service.GenomeMetadataService;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugins.star.StarGenomeIndexConfiguration;
import edu.yu.einstein.wasp.plugins.star.service.StarService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;

/**
 * @author
 */
public class StarPlugin extends WaspPlugin implements BatchJobProviding, ClientMessageI {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("waspMessageHandlingServiceImpl")
	// more than one class of type WaspMessageHandlingService so must specify
	private WaspMessageHandlingService waspMessageHandlingService;

	@Autowired
	private GridHostResolver gridHostResolver;

	@Autowired
	private GridFileService waspGridFileService;

	@Autowired
	private MessageChannelRegistry messageChannelRegistry;

	@Autowired
	private GenomeService genomeService;

	@Autowired
	private GenomeMetadataService genomeMetadataService;
	
	@Autowired
	private StarService starService;

	@Autowired
	@Qualifier("star")
	private Software star;

	public static final String FLOW_NAME = "edu.yu.einstein.wasp.plugins.star.mainFlow";

	public StarPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}

	/**
	 * Methods with the signature: Message<String> methodname(Message<String> m)
	 * are automatically accessible to execution by the command line. Messages
	 * sent are generally free text or JSON formatted data. These methods should
	 * not implement their own functionality, rather, they should either return
	 * information in a message (text) or trigger events through integration
	 * messaging (e.g. launch a job).
	 * 
	 * @param m
	 * @return
	 */
	public Message<String> helloWorld(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return helloWorldHelp();

		logger.info("Hello World!");

		return (Message<String>) MessageBuilder.withPayload("sent a Hello World").build();
	}

	private Message<String> helloWorldHelp() {
		String mstr = "\nStar plugin: hello world!\n" + "wasp -T star -t helloWorld\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	public Message<String> launchTestFlow(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return launchTestFlowHelp();

		logger.info("launching test flow");

		try {
			Integer id = getIDFromMessage(m);
			if (id == null)
				return MessageBuilder.withPayload("Unable to determine id from message: " + m.getPayload().toString()).build();

			Map<String, String> jobParameters = new HashMap<String, String>();
			logger.info("Sending launch message with flow " + FLOW_NAME + " and id: " + id);
			jobParameters.put(WaspJobParameters.TEST_ID, id.toString());
			waspMessageHandlingService.launchBatchJob(FLOW_NAME, jobParameters);
			return (Message<String>) MessageBuilder.withPayload("Initiating test flow on id " + id).build();
		} catch (WaspMessageBuildingException e1) {
			logger.warn("unable to build message to launch batch job " + FLOW_NAME);
			return MessageBuilder.withPayload("Unable to launch batch job " + FLOW_NAME).build();
		}

	}

	private Message<String> launchTestFlowHelp() {
		String mstr = "\nStar plugin: launch the test flow.\n" + "wasp -T star -t launchTestFlow -m \'{id:\"1\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	/**
	 * 
	 * @param m
	 * @return
	 */
	public Integer getIDFromMessage(Message<String> m) {
		Integer id = null;

		JSONObject jo;
		try {
			jo = new JSONObject(m.getPayload().toString());
			if (jo.has("id")) {
				id = new Integer(jo.get("id").toString());
			}
		} catch (JSONException e) {
			logger.warn("unable to parse JSON");
		}
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBatchJobName(String batchJobType) {
		if (batchJobType.equals(BatchJobTask.GENERIC))
			return FLOW_NAME;
		return null;
	}

	/**
	 * Wasp plugins implement InitializingBean to give authors an opportunity to
	 * initialize at runtime.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * Wasp plugins implement DisposableBean to give authors the ability to tear
	 * down on shutdown.
	 */
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

	public Message<String> buildIndex(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return buildIndexHelp();

		logger.info("launching FASTA build ");

		Build b;
		String gtfVersion;
		Integer sjdbOverhang;
		GridWorkService host;
		String hostname = "";

		try {
			JSONObject jo = new JSONObject(m.getPayload().toString());

			try {
				Integer organism = jo.getInt("organism");
				String genome = jo.getString("genome");
				String build = jo.getString("build");
				gtfVersion = jo.getString("gtfVersion");
				sjdbOverhang = jo.getInt("sjdbOverhang");
				b = genomeService.getBuild(organism, genome, build);
			} catch (Exception e) {
				return MessageBuilder.withPayload("Unable to determine build from message: " + m.getPayload().toString()).build();
			}
			
			StarGenomeIndexConfiguration config;

			try {
				hostname = jo.getString("hostname");
				host = gridHostResolver.getGridWorkService(hostname);
			} catch (Exception e) {
				return MessageBuilder.withPayload("Unable to determine host from message: " + m.getPayload().toString()).build();
			}
			
			config = new StarGenomeIndexConfiguration(b, gtfVersion, sjdbOverhang, false);
			
			config.setDirectory(starService.getPrefixedStarIndexPath(host, config));
			config.setPathToJunctions(genomeMetadataService.getPrefixedGtfPath(host, b, gtfVersion));

			GenomeIndexStatus status = starService.getGenomeIndexStatus(host, b, config);

			return MessageBuilder.withPayload("BWA index for " + b.getGenomeBuildNameString() + " has status " + status.toString()).build();

		} catch (Exception e1) {
			String mess = "unable to build message to launch batch job for building BWA index on " + hostname;
			logger.warn(mess);
			return MessageBuilder.withPayload(mess).build();
		}

	}

	public Message<String> buildIndexHelp() {
		String mstr = "\nSTAR plugin: launch a STAR index build.\n"
				+ "wasp -T bwa-mem -t buildIndex -m \'{organism:\"10090\",genome:GRCm38,build:\"75\",gtfVersion:version,sjdbOverhang:\"99\",hostname:remote.host.org}\'\n";
		return MessageBuilder.withPayload(mstr).build();

	}

}
