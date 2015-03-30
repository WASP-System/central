/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.rnaseq.plugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.interfacing.Hyperlink; 
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask; 
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.Software; 
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding; 
import edu.yu.einstein.wasp.interfacing.plugin.PluginSpecificDataForDisplay;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.rnaseq.service.RnaseqService;
import edu.yu.einstein.wasp.interfacing.plugin.WebInterfacing;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * @author 
 */
public class RnaseqPlugin extends WaspPlugin 
		implements 
			BatchJobProviding,
			WebInterfacing,
			FileDataTabViewing,
			ClientMessageI,
			PluginSpecificDataForDisplay {

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
	private JobService jobService;
	
	@Autowired
	private SampleService sampleService;

	@Autowired
	private RnaseqService rnaseqService;

	@Autowired
	private MessageServiceWebapp messageService;

	@Autowired
	@Qualifier("rnaseq")
	private Software rnaseq;
	
	public static final String FLOW_NAME = "edu.yu.einstein.wasp.plugin.rnaseq.mainFlow";

	public RnaseqPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
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
		String mstr = "\nRnaseq plugin: hello world!\n" +
				"wasp -T rnaseq -t helloWorld\n";
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
		String mstr = "\nRnaseq plugin: launch the test flow.\n" +
				"wasp -T rnaseq -t launchTestFlow -m \'{id:\"1\"}\'\n";
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
	 * {@inheritDoc} 
	 */
	@Override
	public Hyperlink getDescriptionPageHyperlink() {
		return new Hyperlink("rnaseq.hyperlink.label", "/rnaseq/displayDescription.do");
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
	public Map<String,String> getPlugInSpecificSampleDataForDisplay(Integer jobId, Integer sampleId){
		
		Map<String,String> m = new LinkedHashMap<String,String>();
		Job job = jobService.getJobByJobId(jobId);
		Sample sample = sampleService.getSampleById(sampleId);		
		if(sample != null && sample.getId()!=null){
			if(job==null ||  job.getId()==null || !job.getSample().contains(sample)){
				return m;
			}
			if(sample.getSampleType().getIName().toLowerCase().equals("rna")){
				  String rnaFraction = rnaseqService.getRNAFraction(sample);
				  if(rnaFraction!=null && !rnaFraction.isEmpty()){
					  m.put(messageService.getMessage("rnaseq.fraction.label"), rnaFraction);	
				  }
				  String rnaDirectionality = rnaseqService.getDirectionality(sample);
				  if(rnaDirectionality!=null && !rnaDirectionality.isEmpty()){
					  m.put(messageService.getMessage("rnaseq.requestedDirectionality.label"), rnaDirectionality);
				  }
			}
			if(sample.getSampleType().getIName().toLowerCase().equals("cdna")){
				  String ribosomeDepleteionMethod = rnaseqService.getRibosomeDepletionMethod(sample);
				  if(ribosomeDepleteionMethod!=null && !ribosomeDepleteionMethod.isEmpty()){
					  m.put(messageService.getMessage("rnaseq.ribosomeDepletion.label"), ribosomeDepleteionMethod);	
				  }
				  String rnaDirectionality = rnaseqService.getDirectionality(sample);
				  if(rnaDirectionality!=null && !rnaDirectionality.isEmpty()){
					  m.put(messageService.getMessage("rnaseq.directionality2.label"), rnaDirectionality);
				  }
			}
			if(sample.getSampleType().getIName().toLowerCase().endsWith("library")){//user-submitted and facility library  
				  String ribosomeDepleteionMethod = rnaseqService.getRibosomeDepletionMethod(sample);
				  if(ribosomeDepleteionMethod!=null && !ribosomeDepleteionMethod.isEmpty()){
					  m.put(messageService.getMessage("rnaseq.ribosomeDepletion.label"), ribosomeDepleteionMethod);	
				  }
				  String rnaDirectionality = rnaseqService.getDirectionality(sample);
				  if(rnaDirectionality!=null && !rnaDirectionality.isEmpty()){
					  m.put(messageService.getMessage("rnaseq.directionality2.label"), rnaDirectionality);	
				  }
			}
		}				
		return m;
	}
}
