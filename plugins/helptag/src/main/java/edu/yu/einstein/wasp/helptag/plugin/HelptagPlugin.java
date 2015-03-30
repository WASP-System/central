/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag.plugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
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
import org.springframework.ui.ModelMap;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.interfacing.plugin.PluginSpecificDataForDisplay;
import edu.yu.einstein.wasp.interfacing.plugin.WebInterfacing;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.JobDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * @author
 */
public class HelptagPlugin extends WaspPlugin implements BatchJobProviding,
		WebInterfacing, FileDataTabViewing, JobDataTabViewing, ClientMessageI, PluginSpecificDataForDisplay {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1505200481435526198L;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("waspMessageHandlingServiceImpl")
	// more than one class of type WaspMessageHandlingService so must specify
	private WaspMessageHandlingService waspMessageHandlingService;

	@Autowired
	private GridHostResolver waspGridHostResolver;

	@Autowired
	private GridFileService waspGridFileService;

	@Autowired
	private HelptagService helptagService;

	@Autowired
	private MessageChannelRegistry messageChannelRegistry;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private SampleService sampleService;

	@Autowired
	private MessageServiceWebapp messageService;

	protected JobExplorerWasp batchJobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.batchJobExplorer = (JobExplorerWasp) jobExplorer;
	}

	public static final String PREPROCESS_ANALYSIS_JOB = "helptag.library.preProcess.job";
	public static final String AGGREGATE_ANALYSIS_JOB = "helptag.library.aggrFlow.job";

	public HelptagPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
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
		String mstr = "\nHelptag plugin: hello world!\n" +
				"wasp -T helptag -t helloWorld\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	public Message<String> launchHpaiiCountingFlow(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return launchHpaiiCountingFlowHelp();
		
		logger.info("Launching Hpaii Counting flow for HELPtag pipeline");
		
		try {
			Integer id = getIDFromMessage(m);
			if (id == null)
				return MessageBuilder.withPayload("Unable to determine id from message: " + m.getPayload().toString()).build();
			
			Map<String, String> jobParameters = new HashMap<String, String>();
			logger.info("Sending launch message with flow " + PREPROCESS_ANALYSIS_JOB + " and id: " + id);
			jobParameters.put(WaspJobParameters.CELL_LIBRARY_ID, id.toString());
//			jobParameters.put(WaspSoftwareJobParameters.CELL_LIBRARY_ID_LIST, id.toString());
//			jobParameters.put(WaspSoftwareJobParameters.GENOME, "10090::GRCm38::70");

			jobParameters.put("uniqCode", Long.toString(Calendar.getInstance().getTimeInMillis())); // overcomes limitation of job being run only once

			waspMessageHandlingService.launchBatchJob(PREPROCESS_ANALYSIS_JOB, jobParameters);
			return (Message<String>) MessageBuilder.withPayload("Initiating HELPtag HpaII Counting flow on cell-lib id " + id).build();
		} catch (WaspMessageBuildingException e1) {
			logger.warn("Unable to build message to launch batch job " + PREPROCESS_ANALYSIS_JOB);
			return MessageBuilder.withPayload("Unable to launch batch job " + PREPROCESS_ANALYSIS_JOB).build();
		}
		
	}
	
	private Message<String> launchHpaiiCountingFlowHelp() {
		String mstr = "\nHelptag plugin: launch the Hpaii Counting flow.\n";
		mstr += "wasp -T helptag -t launchHpaiiCountingFlow -m \'{id:\"1\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	public Message<String> launchAngleMakerFlow(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return launchAngleMakerFlowHelp();

		logger.info("Launching Angle Maker flow for HELPtag pipeline");

		try {
			Integer id = getIDFromMessage(m);
			if (id == null)
				return MessageBuilder.withPayload("Unable to determine job id from message: " + m.getPayload().toString()).build();

			Map<String, String> jobParameters = new HashMap<String, String>();
			logger.info("Sending launch message with flow " + AGGREGATE_ANALYSIS_JOB + " and job id: " + id);
			jobParameters.put(WaspJobParameters.JOB_ID, id.toString());

			jobParameters.put("uniqCode", Long.toString(Calendar.getInstance().getTimeInMillis())); // overcomes limitation of job being run only once

			waspMessageHandlingService.launchBatchJob(AGGREGATE_ANALYSIS_JOB, jobParameters);
			return (Message<String>) MessageBuilder.withPayload("Initiating HELPtag Angle Maker flow on job id " + id).build();
		} catch (WaspMessageBuildingException e1) {
			logger.warn("Unable to build message to launch batch job " + AGGREGATE_ANALYSIS_JOB);
			return MessageBuilder.withPayload("Unable to launch batch job " + AGGREGATE_ANALYSIS_JOB).build();
		}

	}

	private Message<String> launchAngleMakerFlowHelp() {
		String mstr = "\nHelptag plugin: launch angle maker flow with provided job id.\n";
		mstr += "wasp -T helptag -t launchAngleMakerFlow -m \'{id:\"1\"}\'\n";
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
		if (batchJobType.equals(BatchJobTask.ANALYSIS_LIBRARY_PREPROCESS)) 
			return PREPROCESS_ANALYSIS_JOB;
		else if (batchJobType.equals(BatchJobTask.ANALYSIS_AGGREGATE))
			return AGGREGATE_ANALYSIS_JOB;
		return null;
	}
	
	/** 
	 * {@inheritDoc} 
	 */
	@Override
	public Hyperlink getDescriptionPageHyperlink() {
		return new Hyperlink("helptag.hyperlink.label", "/helptag/displayDescription.do");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status getStatus(FileGroup fileGroup) {
		return Status.UNKNOWN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PanelTab getViewPanelTab(FileGroup fileGroup) throws PanelException {
		return null;
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
	@Override
	public Status getStatus(Job job) {
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new LinkedHashSet<String>();
		jobIdStringSet.add(job.getId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		JobExecution je = batchJobExplorer.getMostRecentlyStartedJobExecutionInList(batchJobExplorer.getJobExecutions(AGGREGATE_ANALYSIS_JOB, parameterMap,
																													  false));
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
	public Set<PanelTab> getViewPanelTabs(Job job) throws PanelException {
		try {
			// the summary panelTab is now provided directly from within the web: ResultViewController()
			Set<PanelTab> panelTabSet = new LinkedHashSet<PanelTab>();
			if (this.getStatus(job).toString().equals(Status.COMPLETED.toString())) {
				JobDataTabViewing angleMakerPlugin = helptagService.getHAMPlugin(job);
				Set<PanelTab> downstreamPanelTabSet = angleMakerPlugin.getViewPanelTabs(job);
				if (downstreamPanelTabSet != null && !downstreamPanelTabSet.isEmpty()) {
					panelTabSet.addAll(downstreamPanelTabSet);
				}
			}
			return panelTabSet;
		} catch (Exception e) {
			throw new PanelException(e.getMessage());
		}
	}
	
	@Override
	public Map<String,String> getPlugInSpecificSampleDataForDisplay(Integer jobId, Integer sampleId){
		
		Map<String,String> m = new LinkedHashMap<String,String>();
		Job job = jobService.getJobByJobId(jobId);
		Sample sample = sampleService.getSampleById(sampleId);		
		if(sample != null && sample.getId()!=null){
			if(job==null ||  job.getId()==null || !job.getSample().contains(sample)){
				return m;
			}
			if(sample.getSampleType().getIName().toLowerCase().equals("dna")){//HELP macromolecule
				  String typeOfHelpLibraryRequested = helptagService.getTypeOfHelpLibraryRequestedForMacromolecule(sample);
				  if(typeOfHelpLibraryRequested!=null && !typeOfHelpLibraryRequested.isEmpty()){
					  m.put(messageService.getMessage("helptag.typeOfHelpLibraryRequested.label"), typeOfHelpLibraryRequested);					  
				  } 
			  }
			  else{//library
				  String typeOfHelpLibrary = helptagService.getTypeOfHelpLibrary(sample);
				  if(typeOfHelpLibrary!=null && !typeOfHelpLibrary.isEmpty()){
					  m.put(messageService.getMessage("helptag.typeOfHelpLibrary.label"), typeOfHelpLibrary);
				  }
			  }
		}				
		return m;
	}
	
	@Override
	public void getPlugInSpecificSamplePairDataForDisplay(Integer jobId, ModelMap m){
		
		    Job job = jobService.getJobByJobId(jobId);
			List<Sample> submittedSamplesList = jobService.getSubmittedSamples(job);
			List<Sample> controlList = new ArrayList<Sample>();
			List<Sample> testWithPairList = new ArrayList<Sample>();
			
			//m.addAttribute("submittedSamplesList", submittedSamplesList);
			Map<Sample, List<Sample>> samplePairsMap = new HashMap<Sample, List<Sample>>();
			Set<SampleSource> sampleSourceSet = sampleService.getSamplePairsByJob(job);
			for(Sample submittedSample : submittedSamplesList){
				List<Sample> list = new ArrayList<Sample>();
				for(SampleSource ss : sampleSourceSet){
					Sample test = ss.getSample();//HpaII or beta-GT-MspI
					Sample control = ss.getSourceSample();
					//logger.debug("----control = " + control.getName() + " AND test = " + test.getName());
					if(submittedSample == control){
						list.add(test);
						testWithPairList.add(test);
					}
				}
				if(!list.isEmpty()){
					samplePairsMap.put(submittedSample, list);//submittedSample is MspI
					controlList.add(submittedSample);
				}
			}
			
			//if any test submitted sample (HpaII or betaGT-MspI) is NOT PAIRED WITH SOME CONTROL SAMPLE,
			//then display as paired with standard reference
			List<Sample> testsPairedWithUniversalReference = new ArrayList<Sample>();
			for(Sample submittedSample : submittedSamplesList){
				if(testWithPairList.contains(submittedSample)){
					continue;//this test (HpaII or beta-GT-MspI) already paired up
				}
				else{
					if(helptagService.isHpaII(submittedSample)||helptagService.isBetaGTMspI(submittedSample)){
						testsPairedWithUniversalReference.add(submittedSample);
					}
				}
			}
			if(!testsPairedWithUniversalReference.isEmpty()){
				Sample standardReferenceControl = new Sample();
				standardReferenceControl.setName(messageService.getMessage("helptag.helptagStandardReference.label"));
				samplePairsMap.put(standardReferenceControl, testsPairedWithUniversalReference);
				controlList.add(standardReferenceControl);
			}
			
			m.addAttribute("samplePairsMap", samplePairsMap);
			m.addAttribute("controlListForSamplePairs", controlList);
			m.addAttribute("SamplePairLabel_Left", messageService.getMessage("helptag.helptagMspI.label"));
			m.addAttribute("SamplePairLabel_Right", messageService.getMessage("helptag.helptagHpaIIorBetaGTMspI.label"));
	}
}
