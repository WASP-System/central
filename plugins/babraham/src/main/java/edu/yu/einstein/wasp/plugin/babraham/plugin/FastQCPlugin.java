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

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.JobExplorerWasp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.plugin.babraham.web.service.impl.BabrahamWebServiceImpl;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * 
 */
public class FastQCPlugin extends BabrahamPluginBase{
	
	@Autowired
	BabrahamService babrahamService;
	
	protected JobExplorerWasp batchJobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.batchJobExplorer = (JobExplorerWasp) jobExplorer;
	}
	
	@Autowired
	@Qualifier("fastqc")
	private Software fastqc;

	private static final long serialVersionUID = -4008147590778610484L;

	public static final String FLOW_NAME = "edu.yu.einstein.wasp.plugin.babraham.fastqc.mainFlow";

	public FastQCPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Message<String> helloWorldHelp() {
		String mstr = "\nBabraham FastQC plugin: hello world!\n" +
				"wasp -T fastqc -t helloWorld\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Message<String> launchAnalysisFlowHelp() {
		String mstr = "\nBabraham FastQC plugin: launch the analysis flow with given FileGroup Id.\n" +
				"wasp -T fastqc -t launchAnalysisFlowHelp -m \'{id:\"1\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
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
		return new Hyperlink("fastqc.hyperlink.label", "/babraham/fastqc/description.do");
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
		JobExecution je = batchJobExplorer.getMostRecentlyStartedJobExecutionInList(batchJobExplorer.getJobExecutions(FLOW_NAME, parameterMap, false));
		if (je == null)
			return Status.UNKNOWN;
		ExitStatus jobExitStatus = je.getExitStatus();
		if (jobExitStatus.isRunning())
			return Status.STARTED;
		if (jobExitStatus.isCompleted())
			return Status.COMPLETED;
		return Status.FAILED;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PanelTab getViewPanelTab(FileGroup fileGroup) throws PanelException {
		return ((BabrahamWebServiceImpl) babrahamService).getFastQCDataToDisplay(fileGroup.getId());
	}

}
