/**
 * 
 */
package edu.yu.einstein.wasp.plugin.illumina.plugin;

import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessagingException;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.interfacing.plugin.RunQcProviding;
import edu.yu.einstein.wasp.interfacing.plugin.SequencingViewProviding;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.illumina.IlluminaIndexingStrategy;
import edu.yu.einstein.wasp.plugin.illumina.software.IlluminaHiseqSequenceRunProcessor;
import edu.yu.einstein.wasp.service.RunService;

/**
 * @author calder
 * 
 */
public class WaspIlluminaHiseqPlugin extends WaspPlugin implements ClientMessageI, RunQcProviding, SequencingViewProviding {

	private static Logger logger = LoggerFactory.getLogger(WaspIlluminaHiseqPlugin.class);

	@Autowired
	private GridHostResolver waspGridHostResolver;

	@Autowired
	private GridFileService waspGridFileService;

	@Autowired
	private RunService runService;

	private IlluminaHiseqSequenceRunProcessor casava;
	
	@Autowired
	private MessageChannelRegistry messageChannelRegistry;

	/**
	 * 
	 */
	private static final long serialVersionUID = -654454985142070980L;

	public static final String FLOW_NAME = "waspIlluminaHiSeq.jobFlow";

	public static final String STEP_NOTIFY_RUN_START = "waspIlluminaHiSeq.mainFlow.notifyRunStart";
	public static final String STEP_LISTEN_FOR_RUN_START = "waspIlluminaHiSeq.mainFlow.listenForRunStart";
	public static final String STEP_LISTEN_FOR_RUN_COMPLETION = "waspIlluminaHiSeq.mainFlow.listenForRunCompletion";
	public static final String STEP_LISTEN_FOR_QC = "waspIlluminaHiSeq.mainFlow.listenForQCCompletion";
	public static final String STEP_CREATE_SAMPLE_SHEET = "waspIlluminaHiSeq.mainFlow.createSampleSheet";

	public WaspIlluminaHiseqPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}

	public Message<String> bcl2fastq(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return bcl2fastqHelp();
		Run run = getRunFromMessage(m);
		try {
			runService.initiateRun(run);
			return (Message<String>) MessageBuilder.withPayload("Initiating Illumina pipeline on run " + run.getName()).build();
		} catch (MessagingException e1) {
			logger.warn("unable to build message for run " + run.getId());
			return MessageBuilder.withPayload("Unable to launch bcl2qseq").build();
		}
	}
		
	public Message<String> getSampleSheet(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return getSampleSheetHelp();
		Run run = getRunFromMessage(m);
		if (run == null)
			return MessageBuilder.withPayload("Unable to determine run from message: " + m.getPayload().toString()).build();
		String ss;
		try {
		        // TODO: change to allow both sample sheet types
			ss = getCasava().getSampleSheet(run, IlluminaIndexingStrategy.TRUSEQ);
		} catch (Exception e) {
			e.printStackTrace();
			String err = "unable to create sample sheet for run " + run.getName();
			logger.error(err);
			return MessageBuilder.withPayload(err).build();
		}
		return MessageBuilder.withPayload(ss).build();
	}
	
//	public Message<String> getAvailablePU(Message<String> m) {
//		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
//			return getFlowcellHelp();
//		Set<Run> runs = runService.getCurrentlyActiveRuns();
//		runs.iterator().next().getPlatformUnit().getName()
//		
//	}
	
	/**
	 * 
	 * @param m
	 * @return Run or null
	 */
	public Run getRunFromMessage(Message<String> m) {
		Run run = null;
		
		JSONObject jo;
		try {
			jo = new JSONObject(m.getPayload().toString());
			String value = "";
			if (jo.has("runId")) {
				value = (String) jo.get("runId");
				run = runService.getRunById(new Integer(value));
			} else if (jo.has("runName")) {
				value = (String) jo.get("runId");
				run = runService.getRunByName(value);
			}
		} catch (JSONException e) {
			logger.warn("unable to parse JSON");
		}
		
		return run;
	}
	
	public String getOverride(Message<String> m) {
		String retval = null;
		JSONObject jo;
		try {
			jo = new JSONObject(m.getPayload().toString());
			if (jo.has("override")) {
				retval = (String) jo.get("override");
			}
		} catch (JSONException e) {
			logger.warn("unable to parse JSON");
		}
		return retval;
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

	private Message<String> bcl2fastqHelp() {
		String mstr = "\nIllumina Pipeline plugin: demultiplex and convert bcl to qseq/fastq:\n" +
				"wasp -T waspIlluminaHiSeq -t bcl2qseq -m \'{runId:\"101\"}\'\n" +
				"wasp -T waspIlluminaHiSeq -t bcl2qseq -m \'{runName:101010_RUN_ID}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	private Message<String> getSampleSheetHelp() {
		String mstr = "\nIllumina Pipeline plugin: output sample sheet for a given run:\n" +
				"wasp -T waspIlluminaHiSeq -t getSampleSheet -m \'{runId:\"101\"}\'\n" +
				"wasp -T waspIlluminaHiSeq -t getSampleSheet -m \'{runName:101010_RUN_ID}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
//	private Message<String> getFlowcellHelp() {
//		String mstr = "\nIllumina Pipeline plugin: output sample sheet for a given run:\n" +
//				"wasp -T waspIlluminaHiSeq -t getAvailablePU";
//		return MessageBuilder.withPayload(mstr).build();
//	}
	
	
	@Override
	public String getBatchJobName(String batchJobType) {
		if (batchJobType.equals(BatchJobTask.GENERIC))
			return FLOW_NAME;
		return null;
	}

	@Override
	public Hyperlink getDescriptionPageHyperlink(){
		return new Hyperlink("waspIlluminaPlugin.hyperlink.label", "/waspIlluminaHiSeq/description.do");
	}
	
	@Override
	public String getShowPlatformUnitViewLink(Integer platformUnitId) {
		return "waspIlluminaHiSeq/flowcell/" + platformUnitId + "/show.do";
	}


	/**
	 * @return the casava
	 */
	public IlluminaHiseqSequenceRunProcessor getCasava() {
		return casava;
	}

	/**
	 * @param casava the casava to set
	 */
	public void setCasava(IlluminaHiseqSequenceRunProcessor casava) {
		this.casava = casava;
	}

	@Override
	public String getRunQcStepName() {
		return STEP_LISTEN_FOR_QC;
	}

}
