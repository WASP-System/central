/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.picard.plugin;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.JobExplorerWasp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.picard.service.PicardService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.PanelTab;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;

/**
 * @author 
 */
public class PicardPlugin extends WaspPlugin 
		implements 
			ClientMessageI, FileDataTabViewing {

	
	private static final long serialVersionUID = 1988113569229047484L;
	
	public static final String EXTRACT_ILLUMINA_BARCODES_FLOW = "picard.extractIlluminaBarcodes";

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("waspMessageHandlingServiceImpl") // more than one class of type WaspMessageHandlingService so must specify
	private WaspMessageHandlingService waspMessageHandlingService;

	@Autowired
	private GridHostResolver waspGridHostResolver;

	@Autowired
	private GridFileService waspGridFileService;

	@Autowired
	private MessageChannelRegistry messageChannelRegistry;
	
	@Autowired
	PicardService picardService;
	
	protected JobExplorerWasp batchJobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.batchJobExplorer = (JobExplorerWasp) jobExplorer;
	}

	@Autowired
	@Qualifier("picard")
	private Software picard;
	
	@Autowired
	private RunService runService;
	

	public PicardPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}

	/**
	 * Methods with the signature: Message<String> methodname(Message<String> m)
	 * are automatically accessible to execution by the command line.  Messages sent are generally
	 * free text or JSON formatted data.  These methods should not implement their own functionality,
	 * rather, they should either return information in a message (text) or trigger events through
	 * integration messaging (e.g. launch a job).
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
		String mstr = "\nPicard plugin: hello world!\n" +
				"wasp -T picard -t helloWorld\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	public Message<String> extractIlluminaBarcodes(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return extractIlluminaBarcodesHelp();
		
		logger.trace(m.getPayload());
		
		try {
			Integer id = getIDFromMessage(m);
			Run run = runService.getRunById(id);
			if (id == null)
				return MessageBuilder.withPayload("Unable to determine cellLibrary id from message: " + m.getPayload().toString()).build();
			
			Map<String, String> jobParameters = new HashMap<String, String>();
			logger.info("Sending launch message to extract Illumina barcodes for " + run.getName() + " with ID: " + id);
			jobParameters.put(WaspSoftwareJobParameters.RUN_ID, id.toString());
			jobParameters.put("testId", Long.toString(Calendar.getInstance().getTimeInMillis())); // overcomes limitation of job being run only once

			waspMessageHandlingService.launchBatchJob(EXTRACT_ILLUMINA_BARCODES_FLOW, jobParameters);
			return (Message<String>) MessageBuilder.withPayload("Initiating gatk data preprocessing flow on cellLibrary id " + id).build();
		} catch (WaspMessageBuildingException | JSONException e1) {
			logger.warn("unable to build message to launch batch job " + EXTRACT_ILLUMINA_BARCODES_FLOW);
			return MessageBuilder.withPayload("Unable to launch batch job " + EXTRACT_ILLUMINA_BARCODES_FLOW).build();
		}
	}
	
	private Message<String> extractIlluminaBarcodesHelp() {
		String resp = "\nextractIlluminaBarcodes: Extract Illumina barcode sequences in the run folder (run_name/BARCODES).  Both single and dual.\n" +
				"wasp -T picard -t extractIlluminaBarcodes -m '{runId:\"101\"}'\n" +
				"wasp -T picard -t extractIlluminaBarcodes -m '{runName:150101_SN111_0111_B11ABCACXX}'\n";
		return MessageBuilder.withPayload(resp).build();
	}
	
	private Integer getIDFromMessage(Message<String> m) throws JSONException {
		JSONObject jr = new JSONObject(m.getPayload());
		Integer retval = null;
		if (jr.has("runId")) {
			retval = runService.getRunById(jr.getInt("runId")).getId();
		} else {
			retval = runService.getRunByName(jr.getString("runName")).getId();
		}
		Assert.assertParameterNotNull(retval);
		return retval;
	}

	
	/**
	 * Wasp plugins implement InitializingBean to give authors an opportunity to initialize at runtime.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * Wasp plugins implement DisposableBean to give authors the ability to tear down on shutdown.
	 */
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status getStatus(FileGroup fileGroup) {
		if(picardService.alignmentMetricsExist(fileGroup)){
			return Status.COMPLETED;
		}
		return Status.UNKNOWN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PanelTab getViewPanelTab(FileGroup fileGroup) throws PanelException {
		return picardService.getAlignmentMetricsForDisplay(fileGroup);
	}

	@Override
	public Hyperlink getDescriptionPageHyperlink() {
		// TODO Auto-generated method stub
		//something like: return new Hyperlink("chipSeq.hyperlink.label", "/chipSeq/description.do");
		return null;
	}
	
}
