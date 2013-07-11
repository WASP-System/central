/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.Hyperlink;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.plugin.ViewPanel;
import edu.yu.einstein.wasp.plugin.ViewPanelProviding;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.service.RunService;

/**
 * 
 */
public class BabrahamPlugin extends WaspPlugin implements BatchJobProviding, ClientMessageI, ViewPanelProviding {

	private static final long serialVersionUID = -4008147590778610484L;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GridHostResolver waspGridHostResolver;

	@Autowired
	private GridFileService waspGridFileService;

	@Autowired
	private RunService runService;
	
	@Autowired
	private MessageChannelRegistry messageChannelRegistry;
	
	@Autowired
	private BabrahamService babrahamService;

	public static final String FLOW_NAME = "edu.yu.einstein.wasp.plugin.babrahamQC.mainFlow";

	public BabrahamPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
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
		
	public Message<String> launchTestFlow(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return launchTestFlowHelp();
		
		Map<String, String> jobParameters = new HashMap<String, String>();
		
		logger.info("launching test flow");
		
		try {
			Integer id = getIDFromMessage(m);
			if (id == null)
				return MessageBuilder.withPayload("Unable to determine id from message: " + m.getPayload().toString()).build();
			
			jobParameters.put(WaspJobParameters.RUN_ID, id.toString());
			
			logger.info("Sending launch message to flow " + FLOW_NAME + " on with id: " + id);
			runService.launchBatchJob(FLOW_NAME, jobParameters);
			
			return (Message<String>) MessageBuilder.withPayload("Initiating test flow on id " + id).build();
		} catch (WaspMessageBuildingException e1) {
			logger.warn("unable to build message around jobParameters: " + jobParameters.toString());
			return MessageBuilder.withPayload("Unable to launch bcl2qseq").build();
		}
		
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

	private Message<String> helloWorldHelp() {
		String mstr = "\nBabraham plugin: hello world!\n" +
				"wasp -T babraham -t helloWorld\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	private Message<String> launchTestFlowHelp() {
		String mstr = "\nBabraham plugin: launch the test flow.\n" +
				"wasp -T babraham -t launchTestFlow -m \'{id:\"1\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	@Override
	public String getBatchJobNameByArea(String batchJobType, String area){
		if (batchJobType.equals(BatchJobTask.GENERIC))
			return FLOW_NAME;
		return null;
	}
	
	@Override
	public String getBatchJobName(String batchJobType) {
		return getBatchJobNameByArea(batchJobType, null);
	}

	@Override
	public Hyperlink getDescriptionPageHyperlink() {
		return new Hyperlink("babraham.hyperlink.label", "/babraham/description.do");
	}

	@Override
	public Status getStatus(Object handle) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Get a set of ViewPanels for a given handle which in this case must be a FileGroup otherwise a ClassCastException is thrown.
	 */
	@Override
	public Set<? extends ViewPanel> getViewPanels(Object handle){
		FileGroup fg;
		if (FileHandle.class.isInstance(handle))
			fg = (FileGroup) handle;
		else
			throw new ClassCastException("Expected 'handle' to be of type FileHandle but it cannot be cast to one");
		return babrahamService.getFastQCDataToDisplay(fg);
	}

}
