/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.plugin;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.Hyperlink;
import edu.yu.einstein.wasp.batch.core.extension.JobExplorerWasp;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * 
 */
public class FastQScreenPlugin extends BabrahamPluginBase{

	
	private static final long serialVersionUID = -7670661469622558826L;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String FLOW_NAME = "edu.yu.einstein.wasp.plugin.babraham.fastqscreen.mainFlow";
	
	protected JobExplorerWasp batchJobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.batchJobExplorer = (JobExplorerWasp) jobExplorer;
	}

	public FastQScreenPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
		super(pluginName, waspSiteProperties, channel);
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
	protected Message<String> helloWorldHelp() {
		String mstr = "\nBabraham FastQ Screen plugin: hello world!\n" +
				"wasp -T fastqscreen -t helloWorld\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	@Override
	protected Message<String> launchTestFlowHelp() {
		String mstr = "\nBabraham FastQ Screen plugin: launch the test flow.\n" +
				"wasp -T fastqscreen -t launchTestFlow -m \'{id:\"1\"}\'\n";
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
		return new Hyperlink("fastqscreen.hyperlink.label", "/babraham/fastqscreen/description.do");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status getStatus(FileGroup fileGroup) {
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> fileGroupIdStringSet = new LinkedHashSet<String>();
		fileGroupIdStringSet.add(fileGroup.getId().toString());
		parameterMap.put(WaspJobParameters.FILE_GROUP_ID, fileGroupIdStringSet);
		JobExecution je = batchJobExplorer.getMostRecentlyStartedJobExecutionInList(batchJobExplorer.getJobExecutions(FLOW_NAME, parameterMap, true));
		if (je == null)
			return Status.UNKNOWN;
		else if (je.getStatus().equals(BatchStatus.STARTED))
			return Status.STARTED;
		else if (je.getStatus().equals(BatchStatus.STOPPED) || je.getStatus().equals(BatchStatus.FAILED) || je.getStatus().equals(BatchStatus.ABANDONED))
			return Status.FAILED;
		else if (je.getStatus().equals(BatchStatus.COMPLETED) )
			return Status.COMPLETED;
		return Status.UNKNOWN;
	}


	@Override
	public PanelTab getViewPanelTab(FileGroup fileGroup) throws PanelException {
		return babrahamService.getFastQScreenDataToDisplay(fileGroup.getId());
	}

	
}
