/**
 * 
 */
package edu.yu.einstein.wasp.plugin.assay;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.Hyperlink;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.WebInterfacing;
import edu.yu.einstein.wasp.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * @author asmclellan
 * 
 */
public class ChipSeqPlugin extends WaspPlugin implements 
	BatchJobProviding,
	WebInterfacing,
	FileDataTabViewing,
	ClientMessageI {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final long serialVersionUID = -6546554985142070980L;

	@Autowired
	@Qualifier("waspMessageHandlingServiceImpl") // more than one class of type WaspMessageHandlingService so must specify
	private WaspMessageHandlingService waspMessageHandlingService;

	@Autowired
	private GridHostResolver waspGridHostResolver;

	@Autowired
	private GridFileService waspGridFileService;

	@Autowired
	private MessageChannelRegistry messageChannelRegistry;

	public static final String PREPROCESS_ANALYSIS_JOB = "chipSeq.library.preProcess.jobflow.v1";
	
	public static final String AGGREGATE_ANALYSIS_JOB = "chipSeq.library.aggregate.jobflow.v1";
	
	public ChipSeqPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
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
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help")){
			return helloWorldHelp();
		}
		
		logger.info("******************************public method: Hello World from ChipSeqPlugin!");
			
		return (Message<String>) MessageBuilder.withPayload("sent a Hello World message from the wasp_chip_seq_plugin").build();
	}
	
	private Message<String> helloWorldHelp() {
		String mstr = "\nthe message string set in chipseq helloWorldHelp: hello world, HELP!\n" +
				"wasp -T chipseq -t helloWorld\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	public Message<String> launchTestFlow(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return launchTestFlowHelp();
		
		logger.debug("******starting launchTestFlow in ChipSeqPlugin!");
		//m = MessageBuilder.withPayload("{\"id\":\"14\"}").build();
		try {
			//Integer id = getIDFromMessage(m);
			//if (id == null){
			//	return MessageBuilder.withPayload("Unable to determine id from message: " + m.getPayload().toString()).build();
			//}
			
			Map<String, String> jobParameters = new HashMap<String, String>();
			//////logger.info("Sending launch message with flow " + PREP_FLOW_NAME + " and id: " + id);
//			jobParameters.put(WaspSoftwareJobParameters.LIBRARY_CELL_ID_LIST, id.toString());
//			jobParameters.put(WaspSoftwareJobParameters.GENOME, "10090::GRCm38::70");
			jobParameters.put("test", new Date().toString());
			
			//jobParameters.put(WaspJobParameters.LIBRARY_CELL_ID, id.toString());
			//String cellLibraryIdListAsString = "37,38,39,40";//comma delimited list is how they will appear
			String cellLibraryIdListAsString = "37,38,39,40";//comma delimited list is how they will appear
			jobParameters.put(WaspSoftwareJobParameters.LIBRARY_CELL_ID_LIST, cellLibraryIdListAsString);
			waspMessageHandlingService.launchBatchJob(AGGREGATE_ANALYSIS_JOB, jobParameters);
			logger.debug("**Initiating aggregate_analysis_job ChipSeqPlugin: " + AGGREGATE_ANALYSIS_JOB + " on cellLibraryIds " + cellLibraryIdListAsString);
			return (Message<String>) MessageBuilder.withPayload("Initiating chipseq test flow: "+AGGREGATE_ANALYSIS_JOB + " on cellLibraryIds " + cellLibraryIdListAsString).build();
		} catch (WaspMessageBuildingException e1) {
			logger.warn("WaspMessageBuildingException Unable to build launch batch job from ChipSeqPlugin: " + AGGREGATE_ANALYSIS_JOB);
			return MessageBuilder.withPayload("Unable to launch batch job from ChipSeqPlugin:" + AGGREGATE_ANALYSIS_JOB).build();
		}catch (Exception e1) {
			logger.warn("Exception: Unable to build launch batch job from ChipSeqPlugin: " + AGGREGATE_ANALYSIS_JOB);
			return MessageBuilder.withPayload("Exception: Unable to launch batch job from ChipSeqPlugin:" + AGGREGATE_ANALYSIS_JOB).build();
		}
		
	}
	
	private Message<String> launchTestFlowHelp() {
		String mstr = "\ninside launchTestFlowHelp within the ChipSeqPlugin: launch the test flow.\n" +
				"wasp -T chipseq -t launchTestFlow -m \'{id:\"14\"}\'\n";
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
			logger.warn("*************unable to parse JSON");
		}
		return id;
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
		return new Hyperlink("chipSeq.hyperlink.label", "/chipSeq/description.do");
	}

	@Override
	public String getBatchJobName(String BatchJobType) {
		if (BatchJobTask.ANALYSIS_LIBRARY_PREPROCESS.equals(BatchJobType))
			return PREPROCESS_ANALYSIS_JOB;
		else if (BatchJobTask.ANALYSIS_AGGREGATE.equals(BatchJobType))
			return AGGREGATE_ANALYSIS_JOB;
		return null;
	}


	@Override
	public Status getStatus(FileGroup fileGroup) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public PanelTab getViewPanelTab(FileGroup fileGroup) throws PanelException {
		// TODO Auto-generated method stub
		return null;
	}
}
