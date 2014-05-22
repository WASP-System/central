/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.picard.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.JobExplorerWasp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software; 
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.picard.service.PicardService;
import edu.yu.einstein.wasp.interfacing.Hyperlink;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;
import edu.yu.einstein.wasp.viewpanel.FileDataTabViewing;
import edu.yu.einstein.wasp.viewpanel.GridColumn;
import edu.yu.einstein.wasp.viewpanel.GridContent;
import edu.yu.einstein.wasp.viewpanel.GridDataField;
import edu.yu.einstein.wasp.viewpanel.GridPanel;
import edu.yu.einstein.wasp.viewpanel.PanelTab;
import edu.yu.einstein.wasp.viewpanel.DataTabViewing.Status;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;

/**
 * @author 
 */
public class PicardPlugin extends WaspPlugin 
		implements 
			ClientMessageI, FileDataTabViewing {

	
	private static final long serialVersionUID = 1988113569229047484L;

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
	PicardService picardService;
	
	protected JobExplorerWasp batchJobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.batchJobExplorer = (JobExplorerWasp) jobExplorer;
	}

	@Autowired
	@Qualifier("picard")
	private Software picard;
	

	public PicardPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
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
		String mstr = "\nPicard plugin: hello world!\n" +
				"wasp -T picard -t helloWorld\n";
		return MessageBuilder.withPayload(mstr).build();
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
	public Status getStatus(FileGroup fileGroup) {
		if(picardService.alignmentMetricsExist(fileGroup)){
			return Status.COMPLETED;
		}
		//return Status.UNKNOWN;
		return Status.COMPLETED; //MUST REMOVE THIS LINE AND UNCOMMENT LINE ABOVE
		/*
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
		*/
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PanelTab getViewPanelTab(FileGroup fileGroup) throws PanelException {
		
		
		//return picardService.getAlignmentMetricsForDisplay(fileGroup);
		
		logger.debug("------------THIS IS A ROB TEST in picard");
		PanelTab panelTab = new PanelTab();
		panelTab.setName("TEST alignmentMetrics Panel Tab");
		panelTab.setNumberOfColumns(1);
		//return panelTab;
		WebPanel wp = new WebPanel();
		WebContent wc = new WebContent();
		wc.setHtmlCode("this is a text code");
		wp.setContent(wc);
		panelTab.addPanel(wp);
		return panelTab;
		
		
		
	}

	@Override
	public Hyperlink getDescriptionPageHyperlink() {
		// TODO Auto-generated method stub
		//something like: return new Hyperlink("chipSeq.hyperlink.label", "/chipSeq/description.do");
		return null;
	}
	
}
