/**
 * 
 */
package edu.yu.einstein.wasp.plugin.mps.integration.endpoints;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.FileStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqFileTypeAttribute;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * RunSuccessFastqcSplitter listens for run success messages, obtains all of the FASTQ file groups
 * and sends notification messages for each to announce creation of fastq file
 * 
 * @author asmclellan
 *
 */
public class RunSuccessFastqcSplitter extends AbstractMessageSplitter {
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private RunService runService;
	
	@Autowired
	private FastqService fastqService;
	
	@Autowired
	private FileService fileService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 
	 */
	public RunSuccessFastqcSplitter() {
		//
	}

	/** 
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<Message<WaspStatus>> splitMessage(Message<?> message) {
		List<Message<WaspStatus>> outputMessages = new ArrayList<>();
		if (!RunStatusMessageTemplate.isMessageOfCorrectType(message)){
			logger.warn("Message is not of the correct type (a Run message). Check filter and input channel are correct");
			return outputMessages; // empty list
		}
		RunStatusMessageTemplate runStatusMessageTemplate = new RunStatusMessageTemplate((Message<WaspStatus>) message);
		if (!runStatusMessageTemplate.getStatus().equals(WaspStatus.COMPLETED) || !runStatusMessageTemplate.getTask().equals(WaspTask.NOTIFY_STATUS)){
			logger.warn("Message has the wrong status or payload value. Check filter and input channel are correct");
			return outputMessages; // empty list
		}
		
		Run run = runService.getRunDao().getRunByRunId(runStatusMessageTemplate.getRunId());
		Set<SampleSource> cellLibraries = runService.getCellLibrariesOnSuccessfulRunCells(run);
		
		for (SampleSource cellLib : cellLibraries) {
			Set<FileGroup> fgs = fileService.getFilesForCellLibraryByType(cellLib, fastqService.getFastqFileType());
			for (FileGroup fg : fgs) {
				if (fastqService.hasAttribute(fg, FastqFileTypeAttribute.TRIMMED)) {
					FileStatusMessageTemplate messageTemplate = new FileStatusMessageTemplate(fg.getId());
					messageTemplate.setStatus(WaspStatus.CREATED);
					try {
						outputMessages.add(messageTemplate.build());
					} catch (WaspMessageBuildingException e) {
						throw new MessagingException(e.getLocalizedMessage(), e);
					}
				}
			}
			
		}
		return outputMessages;
	}


}
