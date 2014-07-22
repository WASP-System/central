/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.plugin;

import java.util.Calendar;
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
import org.springframework.batch.core.explore.wasp.WaspJobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.plugin.babraham.software.TrimGalore;
import edu.yu.einstein.wasp.plugin.babraham.web.service.impl.BabrahamWebServiceImpl;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * 
 */
public class TrimGalorePlugin extends WaspPlugin implements ClientMessageI, FileDataTabViewing {

    /**
     * 
     */
    private static final long serialVersionUID = 8094367515193248876L;
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    public static final String TRIM_GALORE_PLOT_KEY = "size-plot";

    @Autowired
    BabrahamService babrahamService;
    
    @Autowired
	protected RunService runService;

    protected WaspJobExplorer batchJobExplorer;

    @Autowired
    void setJobExplorer(JobExplorer jobExplorer) {
	this.batchJobExplorer = (WaspJobExplorer) jobExplorer;
    }

    @Autowired
    @Qualifier("trim_galore")
    private Software trim_galore;

    public TrimGalorePlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
	super(iName, waspSiteProperties, channel);
    }

    /**
     * Wasp plugins implement InitializingBean to give authors an opportunity to
     * initialize at runtime.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
	// TODO Auto-generated method stub

    }

    /**
     * Wasp plugins implement DisposableBean to give authors the ability to tear
     * down on shutdown.
     */
    @Override
    public void destroy() throws Exception {
	// TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Hyperlink getDescriptionPageHyperlink() {
	return new Hyperlink("trim_galore.hyperlink.label", "/babraham/trim_galore/description.do");
    }

    /**
     * Trimming is happening prior to returning the file to the user,
     * so the status of analysis of a fastq file is always complete
     * when the user wants to visualize any information.
     * {@inheritDoc}
     */
    @Override
    public Status getStatus(FileGroup fileGroup) {
    	Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Run run = babrahamService.getRunForFileGroup(fileGroup);
		if (run == null){
			logger.warn("Unable to determine status as failed to obtain a run for filegroup with id = " + fileGroup.getId());
			return Status.UNKNOWN;
		}
		Set<String> runIdStringSet = new LinkedHashSet<String>();
		runIdStringSet.add(run.getId().toString());
		parameterMap.put(WaspJobParameters.RUN_ID, runIdStringSet);
		try{
			if (!getViewPanelTab(fileGroup).getPanels().isEmpty())
				return Status.COMPLETED;
		} catch (PanelException e){}
		
		JobExecution je = batchJobExplorer.getMostRecentlyStartedJobExecutionInList(batchJobExplorer.getJobExecutions(TrimGalore.FLOW_NAME, parameterMap, false));
		if (je == null){
			logger.info("No TrimGalore batch jobs found for FileGroup id=" + fileGroup.getId());
			return Status.UNKNOWN;
		}
		ExitStatus jobExitStatus = je.getExitStatus();
		if (jobExitStatus.isFailed())
			return Status.FAILED; 
		if (jobExitStatus.isRunning())
			return Status.STARTED; // trumps previously set status of COMPLETED
		return Status.UNKNOWN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public PanelTab getViewPanelTab(FileGroup fileGroup) throws PanelException {
		return ((BabrahamWebServiceImpl) babrahamService).getTrimGaloreDataToDisplay(fileGroup.getId());
	}
    
    public Message<String> trim(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return trimHelp();
		
		Map<String, String> jobParameters = new HashMap<String, String>();
		
		logger.info("launching TrimGalore flow");
		
		try {
			Integer id = getIDFromMessage(m);
			if (id == null)
				return MessageBuilder.withPayload("Unable to determine run id from message: " + m.getPayload().toString()).build();
			
			jobParameters.put(WaspJobParameters.RUN_ID, id.toString());
			jobParameters.put(WaspJobParameters.BEAN_NAME, "casava");
			jobParameters.put("uniqCode", Long.toString(Calendar.getInstance().getTimeInMillis())); // overcomes limitation of job being run only once
			logger.info("Sending launch message to flow '" + TrimGalore.FLOW_NAME + "' on run with id=" + id);
			runService.launchBatchJob(TrimGalore.FLOW_NAME, jobParameters);
			
			return (Message<String>) MessageBuilder.withPayload("Initiating TrimGalore flow on run with id=" + id).build();
		} catch (WaspMessageBuildingException e1) {
			logger.warn("unable to build message around jobParameters: " + jobParameters.toString());
			return MessageBuilder.withPayload("Unable to launch TrimGalore").build();
		}
		
	}
	
	private Message<String> trimHelp() {
		String mstr = "\nBabraham Trim Galore plugin: launch the trim flow with given run Id.\n" +
				"wasp -T trim_galore -t trim -m \'{id:\"1\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	private Integer getIDFromMessage(Message<String> m) {
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

}
