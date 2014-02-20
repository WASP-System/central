/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.gatk.plugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
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
 * @author 
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

	public static final String FLOW_NAME = "edu.yu.einstein.wasp.gatk.mainFlow";
	
	public static final String CALL_FLOW_NAME = "edu.yu.einstein.wasp.gatk.mainCallFlow";

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
			jobParameters.put(WaspSoftwareJobParameters.CELL_LIBRARY_ID_LIST, id.toString());
			jobParameters.put(WaspSoftwareJobParameters.GENOME, "10090::GRCm38::70");
			jobParameters.put("test", Long.toString(Calendar.getInstance().getTimeInMillis()));

			waspMessageHandlingService.launchBatchJob(FLOW_NAME, jobParameters);
			return (Message<String>) MessageBuilder.withPayload("Initiating test flow on id " + id).build();
		} catch (WaspMessageBuildingException e1) {
			logger.warn("unable to build message to launch batch job " + FLOW_NAME);
			return MessageBuilder.withPayload("Unable to launch batch job " + FLOW_NAME).build();
		}
		
	}
	
	private Message<String> launchTestFlowHelp() {
		String mstr = "\nGatk plugin: launch the test flow.\n" +
				"wasp -T gatk -t launchTestFlow -m \'{id:\"1\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	
	
	public Message<String> launchCallFlow(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return launchCallFlowHelp();
		
		logger.info("launching call flow");
		
		try {
			List<Integer> ids = getIDSFromMessage(m);
			if (ids.isEmpty())
				return MessageBuilder.withPayload("Unable to determine ids from message: " + m.getPayload().toString()).build();
			
			Map<String, String> jobParameters = new HashMap<String, String>();
			logger.info("Sending launch message with flow " + CALL_FLOW_NAME + " and ids: " + ids);
			jobParameters.put(WaspSoftwareJobParameters.CELL_LIBRARY_ID_LIST, WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(ids));
			jobParameters.put(WaspSoftwareJobParameters.GENOME, "10090::GRCm38::70");
			jobParameters.put("test", Long.toString(Calendar.getInstance().getTimeInMillis()));
			jobParameters.put("gatk-stand_call_conf", "30");
			jobParameters.put("gatk--max_alternate_alleles", "6");
			jobParameters.put("gatk-dcov", "250");
			jobParameters.put("gatk-L", "WGS");

			waspMessageHandlingService.launchBatchJob(CALL_FLOW_NAME, jobParameters);
			return (Message<String>) MessageBuilder.withPayload("Initiating call flow on ids " + ids).build();
		} catch (WaspMessageBuildingException e1) {
			logger.warn("unable to build message to launch batch job " + CALL_FLOW_NAME);
			return MessageBuilder.withPayload("Unable to launch batch job " + CALL_FLOW_NAME).build();
		}
		
	}
	
	private Message<String> launchCallFlowHelp() {
		String mstr = "\nGatk plugin: launch the call flow.\n" +
				"wasp -T gatk -t launchCallFlow -m \'{ids:[\"11\",\"12\"]}\'\n";
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
	
	public List<Integer> getIDSFromMessage(Message<String> m) {
		List<Integer> ids = new ArrayList<>();
		
		JSONObject jo;
		try {
			jo = new JSONObject(m.getPayload().toString());
			if (jo.has("ids")) {
				JSONArray idJsonArray = jo.getJSONArray("ids");
				for (int i=0; i<idJsonArray.length(); i++)
					ids.add(new Integer(idJsonArray.getString(i)));
			} 
		} catch (JSONException e) {
			logger.warn("unable to parse JSON");
		}
		return ids;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	/*public String getBatchJobName(String batchJobType) {
		if (batchJobType.equals(BatchJobTask.GENERIC)) 
			return FLOW_NAME;
		return null;
	}
	*/
	public String getBatchJobName(String BatchJobType) {
		if (BatchJobTask.ANALYSIS_LIBRARY_PREPROCESS.equals(BatchJobType))
			return FLOW_NAME;
		else if (BatchJobTask.ANALYSIS_AGGREGATE.equals(BatchJobType))
			return CALL_FLOW_NAME;
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
	
//	@Override
//	public Software getSoftware() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	
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
}
