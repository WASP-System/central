/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;

/**
 * 
 */
public abstract class BabrahamPluginBase extends WaspPlugin implements BatchJobProviding, ClientMessageI, FileDataTabViewing {

	private static final long serialVersionUID = -1918438365161307850L;


	public BabrahamPluginBase(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected GridHostResolver waspGridHostResolver;

	@Autowired
	protected GridFileService waspGridFileService;

	@Autowired
	protected RunService runService;
	
	@Autowired
	protected MessageChannelRegistry messageChannelRegistry;
	
	@Autowired
	protected BabrahamService babrahamService;


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
			
			jobParameters.put(WaspJobParameters.FILE_GROUP_ID, id.toString());
			
			logger.info("Sending launch message to flow " + getBatchJobName(BatchJobTask.GENERIC) + " on with id: " + id);
			runService.launchBatchJob(getBatchJobName(BatchJobTask.GENERIC), jobParameters);
			
			return (Message<String>) MessageBuilder.withPayload("Initiating test flow on id " + id).build();
		} catch (WaspMessageBuildingException e1) {
			logger.warn("unable to build message around jobParameters: " + jobParameters.toString());
			return MessageBuilder.withPayload("Unable to launch FastQC").build();
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


	protected abstract Message<String> helloWorldHelp();
	
	protected abstract Message<String> launchTestFlowHelp();



}
