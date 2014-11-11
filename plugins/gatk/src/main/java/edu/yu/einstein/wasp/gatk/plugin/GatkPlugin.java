/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.gatk.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.gatk.service.GatkService;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.interfacing.plugin.WebInterfacing;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * @author jcai
 * @author asmclellan
 */
public class GatkPlugin extends WaspPlugin 
		implements 
			BatchJobProviding,
			WebInterfacing,
			FileDataTabViewing,
			ClientMessageI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4719144342164520649L;

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
	private GATKSoftwareComponent gatk;
	
	@Autowired
	private GatkService gatkService;
		
	public static final String PREPROCESSING_FLOW = "gatk.dataPreprocessing.jobFlow.v1";
	
	public static final String VARIANT_DISCOVERY_FLOW = "gatk.variantDiscovery.jobFlow.v1";

	public GatkPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
		super(pluginName, waspSiteProperties, channel);
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
		String mstr = "\nGatk plugin: hello world!\n" +
				"wasp -T gatk -t helloWorld\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	public Message<String> launchPreprocessingFlow(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return launchPreprocessingFlowHelp();
		
		logger.info("launching gatk data preprocessing flow");
		
		List<Integer> ids = getIDSFromMessage(m);
		String clidl = WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(ids);
		if (ids.isEmpty())
			return MessageBuilder.withPayload("Unable to determine cellLibrary id from message: " + m.getPayload().toString()).build();
		logger.info("Sending launch message with gatk data preprocessing flow " + PREPROCESSING_FLOW + " and cellLibrary ids: " + clidl);
		try{
			gatkService.doLaunchFlow(ids, gatk.getId(), PREPROCESSING_FLOW);
		} catch (Exception e){
			return MessageBuilder.withPayload("Caught exception during launch attempt : " + e.getLocalizedMessage()).build();
		}
		return (Message<String>) MessageBuilder.withPayload("Initiating gatk data preprocessing flow on cellLibrary ids " + clidl).build();
	}
	
	private Message<String> launchPreprocessingFlowHelp() {
		String mstr = "\nLaunch the gatk data preprocessing flow with provided cellLibrary id.\n" +
				"wasp -T gatk -t launchTestFlow -m \'{id:\"1\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	
	
	public Message<String> launchDiscoveryFlow(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return launchDiscoveryFlowHelp();
		
		logger.info("launching gatk variant discovery flow");
		
		List<Integer> ids = getIDSFromMessage(m);
		String clidl = WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(ids);
		if (ids.isEmpty())
			return MessageBuilder.withPayload("Unable to determine cell library ids from message: " + m.getPayload().toString()).build();
		logger.info("Sending launch message with  gatk variant discovery flow " + VARIANT_DISCOVERY_FLOW + " and cellLibrary ids: " + clidl);
		try{
			gatkService.doLaunchFlow(ids, gatk.getId(), VARIANT_DISCOVERY_FLOW);
		} catch (Exception e){
			return MessageBuilder.withPayload("Caught exception during launch attempt : " + e.getLocalizedMessage()).build();
		}
		return (Message<String>) MessageBuilder.withPayload("Initiating gatk variant discovery flow on cellLibrary ids " + clidl).build();
	}
	
	private Message<String> launchDiscoveryFlowHelp() {
		String mstr = "\nLaunch the gatk variant discovery flow with specified cellLibrary id list.\n" +
				"wasp -T gatk -t launchCallFlow -m \'{ids:[\"11\",\"12\"]}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	public List<Integer> getIDSFromMessage(Message<String> m) {
		List<Integer> ids = new ArrayList<>();
		
		JSONObject jo;
		try {
			jo = new JSONObject(m.getPayload().toString());
			if (jo.has("ids")) {
				JSONArray idJsonArray = jo.getJSONArray("ids");
				for (int i=0; i<idJsonArray.length(); i++)
					ids.add(new Integer(idJsonArray.getString(i)));
			} else if (jo.has("id")) {
				ids.add(new Integer(jo.get("id").toString()));
			} 
		} catch (JSONException e) {
			logger.warn("unable to parse JSON");
		}
		return ids;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public String getBatchJobName(String batchJobType) {
		if (BatchJobTask.ANALYSIS_LIBRARY_PREPROCESS.equals(batchJobType))
			return PREPROCESSING_FLOW;
		else if (BatchJobTask.ANALYSIS_AGGREGATE.equals(batchJobType))
			return VARIANT_DISCOVERY_FLOW;
		return null;
	}
	
	/** 
	 * {@inheritDoc} 
	 */
	@Override
	public Hyperlink getDescriptionPageHyperlink() {
		return new Hyperlink("gatk.hyperlink.label", "/gatk/displayDescription.do");
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

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
