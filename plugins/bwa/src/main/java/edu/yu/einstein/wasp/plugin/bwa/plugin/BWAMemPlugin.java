	/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.plugin;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;

/**
 * @author asmclellan
 * 
 */
public class BWAMemPlugin extends AbstractBWAPlugin {

	private static final long serialVersionUID = 7953334889300450448L;

	public static final String JOB_NAME = "bwa-mem.alignment";
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public BWAMemPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}

	@Override
	public Message<String> align(Message<String> m) throws Exception {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return alignHelp();
		
		logger.debug("got align");

		Integer cellLibraryId = getCellLibraryIdFromMessage(m);
		if (cellLibraryId == null)
			return MessageBuilder.withPayload("Cell library ID is null").build();
		try{
			bwaService.doLaunchAlign(cellLibraryId, software.getId(), JOB_NAME);
		} catch (Exception e){
			return MessageBuilder.withPayload("Caught exception during launch attempt : " + e.getLocalizedMessage()).build();
		}
		return MessageBuilder.withPayload("Signalled begin of BWA alignment").build();
	}

	@Override
	public Message<String> alignHelp() {
		String mstr = "BWA plugin: align registered fastq files:\n" +
				"wasp -T bwa-mem -t align -m \'{cellLibrary:\"101\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	@Override
	public String getBatchJobName(String BatchJobType) {
		if (BatchJobType.equals(BatchJobTask.ANALYSIS_LIBRARY_PREPROCESS))
			return JOB_NAME;
		return null;
	}

}
