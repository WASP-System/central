/**
 * 
 */
package edu.yu.einstein.wasp.plugin.assay;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.JobExplorerWasp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import edu.yu.einstein.wasp.chipseq.service.ChipSeqService;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.interfacing.plugin.WebInterfacing;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.JobDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * @author asmclellan
 * 
 */
public class ChipSeqPlugin extends WaspPlugin implements 
	BatchJobProviding,
	WebInterfacing,
	FileDataTabViewing,
	ClientMessageI,
	JobDataTabViewing {

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
	private SampleService sampleService;
	@Autowired
	private JobService jobService;

	@Autowired
	private MessageChannelRegistry messageChannelRegistry;

	protected JobExplorerWasp batchJobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.batchJobExplorer = (JobExplorerWasp) jobExplorer;
	}

	@Autowired 
	private ChipSeqService chipSeqService;
	
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
		
		logger.debug("******starting launchTestFlow in ChipSeqPlugin for job 3!");
		//m = MessageBuilder.withPayload("{\"id\":\"14\"}").build();
		try {
			//Integer id = getIDFromMessage(m);
			//if (id == null){
			//	return MessageBuilder.withPayload("Unable to determine id from message: " + m.getPayload().toString()).build();
			//}
			
			Map<String, String> jobParameters = new HashMap<String, String>();

			
			//////jobParameters.put("test", new Date().toString());//used for testing only
			
			jobParameters.put(WaspJobParameters.JOB_ID, "3");
			

			waspMessageHandlingService.launchBatchJob(AGGREGATE_ANALYSIS_JOB, jobParameters);
			logger.debug("**Initiating aggregate_analysis_job ChipSeqPlugin: " + AGGREGATE_ANALYSIS_JOB + " on job 3");
			return (Message<String>) MessageBuilder.withPayload("Initiating chipseq test flow: "+AGGREGATE_ANALYSIS_JOB + " for job 3").build();
		} catch (WaspMessageBuildingException e1) {
			logger.warn("WaspMessageBuildingException Unable to build launch batch job 3 from ChipSeqPlugin: " + AGGREGATE_ANALYSIS_JOB);
			return MessageBuilder.withPayload("Unable to launch batch job  3 from ChipSeqPlugin:" + AGGREGATE_ANALYSIS_JOB).build();
		}catch (Exception e1) {
			logger.warn("Exception: Unable to build launch batch job 3 from ChipSeqPlugin: " + AGGREGATE_ANALYSIS_JOB);
			return MessageBuilder.withPayload("Exception: Unable to launch batch job 3 from ChipSeqPlugin:" + AGGREGATE_ANALYSIS_JOB).build();
		}
		
	}
	
	private Message<String> launchTestFlowHelp() {
		String mstr = "\ninside launchTestFlowHelp within the ChipSeqPlugin: launch the test flow.\n" +
				"wasp -T chipseq -t launchTestFlow -m \'{id:\"14\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	public Message<String> launchTestOutput(Message<String> m) throws Exception{
		String mstr = "\ninside launchTestOutput within the ChipSeqPlugin: launch the test output for job 1.\n" +
				"wasp -T chipseq -t launchTestOutput \n";
		Job job = jobService.getJobByJobId(1);
		try{this.getViewPanelTabs(job);}catch(Exception e){mstr = mstr + e.getMessage();}
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
	
	public Status getStatus(Job job){//to make proper use of this call, we must incorporate Brent's new code to deal with the proper ending of this tasklet
		
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new LinkedHashSet<String>();
		jobIdStringSet.add(job.getId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		JobExecution je = batchJobExplorer.getMostRecentlyStartedJobExecutionInList(batchJobExplorer.getJobExecutions(AGGREGATE_ANALYSIS_JOB, parameterMap, false));
		if (je == null)
			return Status.UNKNOWN;
		ExitStatus jobExitStatus = je.getExitStatus();
		if (jobExitStatus.isRunning())
			return Status.STARTED;
		if (jobExitStatus.isCompleted())
			return Status.COMPLETED;
		return Status.FAILED;
	}
	@Override
	public Set<PanelTab> getViewPanelTabs(Job job) throws PanelException{
		try{
			//the summary panelTab is now provided directly from within the web: ResultViewController()					
			Set<PanelTab> panelTabSet = new LinkedHashSet<PanelTab>();
			 if(this.getStatus(job).toString().equals(Status.COMPLETED.toString())){
				JobDataTabViewing peakcallerPlugin = chipSeqService.getPeakcallerPlugin(job);//at this time, only option is macstwo
				Set<PanelTab> downstreamPanelTabSet = peakcallerPlugin.getViewPanelTabs(job);//all the macstwo specific info
				if(downstreamPanelTabSet != null && !downstreamPanelTabSet.isEmpty()){
					panelTabSet.addAll(downstreamPanelTabSet);
				}
			}
			return panelTabSet;
		}catch(Exception e){
			throw new PanelException(e.getMessage());
		}				
	}
}
