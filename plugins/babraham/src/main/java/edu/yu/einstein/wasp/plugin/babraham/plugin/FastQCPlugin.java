/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.plugin;

import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.Hyperlink;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.plugin.ViewPanel;

/**
 * 
 */
public class FastQCPlugin extends BabrahamPluginBase{

	private static final long serialVersionUID = -4008147590778610484L;

	private final Logger logger = LoggerFactory.getLogger(getClass());

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
