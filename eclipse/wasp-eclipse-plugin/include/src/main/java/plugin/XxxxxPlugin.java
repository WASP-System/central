/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package ___package___.plugin;

import java.util.Calendar;
import java.util.HashMap; ///// PIP
import java.util.Map; ///// PIP
import java.util.Properties;

import org.json.JSONException; ///// PIP
import org.json.JSONObject; ///// PIP
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import edu.yu.einstein.wasp.Hyperlink;  ///// FORMVIZ
import edu.yu.einstein.wasp.exception.PanelException; ///// VIZ
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException; ///// PIP 
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters; ///// PIP
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;  ///// PIP
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.FileGroup; ///// VIZ
import edu.yu.einstein.wasp.model.Software;  ///// RES
import edu.yu.einstein.wasp.plugin.BatchJobProviding;  ///// PIP 
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.WebInterfacing; ///// FORM
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing; ///// VIZ
import edu.yu.einstein.wasp.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;
import edu.yu.einstein.wasp.viewpanel.PanelTab; ///// VIZ

/**
 * @author 
 */
public class ___PluginIName___Plugin extends WaspPlugin 
		implements 
			BatchJobProviding,	///// PIP
			WebInterfacing, ///// FORM
			FileDataTabViewing, ///// VIZ
			ClientMessageI {

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
	
	////> RES
	@Autowired
	@Qualifier("___pluginIName___")
	private Software ___pluginIName___;
	
	////<
	public static final String FLOW_NAME = "___package___.mainFlow"; ///// PIP

	public ___PluginIName___Plugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
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
		String mstr = "\n___PluginIName___ plugin: hello world!\n" +
				"wasp -T ___pluginIName___ -t helloWorld\n";
		return MessageBuilder.withPayload(mstr).build();
	}

////> PIP
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
			jobParameters.put("uniqCode", Long.toString(Calendar.getInstance().getTimeInMillis())); // overcomes limitation of job being run only once
			waspMessageHandlingService.launchBatchJob(FLOW_NAME, jobParameters);
			return (Message<String>) MessageBuilder.withPayload("Initiating test flow on id " + id).build();
		} catch (WaspMessageBuildingException e1) {
			logger.warn("unable to build message to launch batch job " + FLOW_NAME);
			return MessageBuilder.withPayload("Unable to launch batch job " + FLOW_NAME).build();
		}
		
	}
	
	private Message<String> launchTestFlowHelp() {
		String mstr = "\n___PluginIName___ plugin: launch the test flow.\n" +
				"wasp -T ___pluginIName___ -t launchTestFlow -m \'{id:\"1\"}\'\n";
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
	
////<
////> VIZ FORM
	/** 
	 * {@inheritDoc} 
	 */
	@Override
	public Hyperlink getDescriptionPageHyperlink() {
		return new Hyperlink("___pluginIName___.hyperlink.label", "/___pluginIName___/displayDescription.do");
	}
	
////<
////> VIZ	
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
	
////<
	
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
