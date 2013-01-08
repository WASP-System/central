/**
 * 
 */
package edu.yu.einstein.wasp.plugin.illumina;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.integration.messages.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfaces.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.mps.illumina.IlluminaSequenceRunProcessor;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.service.RunService;

/**
 * @author calder
 * 
 */
public class WaspIlluminaPlugin extends WaspPlugin implements ClientMessageI {

	private static Logger logger = LoggerFactory.getLogger(WaspIlluminaPlugin.class);

	@Autowired
	private GridHostResolver waspGridHostResolver;

	@Autowired
	private GridFileService waspGridFileService;

	@Autowired
	private RunService runService;

	@Autowired
	private IlluminaSequenceRunProcessor casava;
	
	
	@Autowired
	private MessageChannelRegistry messageChannelRegistry;

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

	public Message<String> bcl2fastq(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return bcl2fastqHelp();

		Map<String, String> jobParameters = new HashMap<String, String>();
		
		try {
			Run run = getRunFromMessage(m);
			if (run == null)
				return MessageBuilder.withPayload("Unable to determine run from message: " + m.getPayload().toString()).build();
			
			jobParameters.put(WaspJobParameters.RUN_ID, run.getRunId().toString());
			jobParameters.put(WaspJobParameters.RUN_NAME, run.getName());
			
			logger.debug("Sending launch message to run " + FLOW_NAME + " on jobId: " + run.getRunId());
			runService.launchBatchJob(FLOW_NAME, jobParameters);
			
			return (Message<String>) MessageBuilder.withPayload("Initiating Illumina pipeline on run " + run.getName()).build();
		} catch (WaspMessageBuildingException e1) {
			logger.warn("unable to build message around jobParameters: " + jobParameters.toString());
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
			ss = casava.getSampleSheet(run);
		} catch (Exception e) {
			e.printStackTrace();
			String err = "unable to create sample sheet for run " + run.getName();
			logger.error(err);
			return MessageBuilder.withPayload(err).build();
		}
		return MessageBuilder.withPayload(ss).build();
	}
	
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

	private Message<String> bcl2fastqHelp() {
		String mstr = "\nIllumina Pipeline plugin: demultiplex and convert bcl to qseq/fastq:\n" +
				"wasp -T wasp-illumina -t bcl2qseq -m \'{runId:\"101\"}\'\n" +
				"wasp -T wasp-illumina -t bcl2qseq -m \'{runName:101010_RUN_ID}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	private Message<String> getSampleSheetHelp() {
		String mstr = "\nIllumina Pipeline plugin: output sample sheet for a given run:\n" +
				"wasp -T wasp-illumina -t getSampleSheet -m \'{runId:\"101\"}\'\n" +
				"wasp -T wasp-illumina -t getSampleSheet -m \'{runName:101010_RUN_ID}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	@Override
	public String getFlowNameFromArea(String area) {
		return FLOW_NAME;
	}

}
