/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.plugin;

import java.util.Properties;

import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.Hyperlink;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.web.panel.PanelTab;

/**
 * 
 */
public class FastQCPlugin extends BabrahamPluginBase{

	private static final long serialVersionUID = -4008147590778610484L;

	public static final String FLOW_NAME = "edu.yu.einstein.wasp.plugin.babraham.fastqc.mainFlow";

	public FastQCPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
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
		String mstr = "\nBabraham FastQC plugin: hello world!\n" +
				"wasp -T fastqc -t helloWorld\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	@Override
	protected Message<String> launchTestFlowHelp() {
		String mstr = "\nBabraham FastQC plugin: launch the test flow.\n" +
				"wasp -T fastqc -t launchTestFlow -m \'{id:\"1\"}\'\n";
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
		return new Hyperlink("fastqc.hyperlink.label", "/babraham/fastqc/description.do");
	}

	
	@Override
	public Status getStatus(FileGroup fileGroup) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public PanelTab getViewPanelTab(FileGroup fileGroup) {
		// TODO Auto-generated method stub
		return null;
	}

}
