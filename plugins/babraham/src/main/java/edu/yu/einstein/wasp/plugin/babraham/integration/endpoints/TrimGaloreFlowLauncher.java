/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.integration.endpoints;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.MessagingException;
import org.springframework.integration.annotation.ServiceActivator;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.RunStatusMessageTemplate;
import edu.yu.einstein.wasp.plugin.babraham.integration.messages.TrimGaloreStatusMessageTemplate;
import edu.yu.einstein.wasp.plugin.babraham.software.TrimGalore;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author calder
 *
 */
public class TrimGaloreFlowLauncher {
    
    private static final Logger logger = LoggerFactory.getLogger(TrimGaloreFlowLauncher.class);
    
    public TrimGaloreFlowLauncher() {
        //cglib
    }

    @ServiceActivator
    public Message<BatchJobLaunchContext> launchTrimGaloreFlowJob(Message<?> message){ 
            if (!TrimGaloreStatusMessageTemplate.isMessageOfCorrectType(message)){
                    logger.warn("Message is not of the correct type (a TrimGalore message). Check filter and input channel are correct");
                    return null; 
            }
            
            TrimGaloreStatusMessageTemplate trimGaloreMessage = new TrimGaloreStatusMessageTemplate((Message<WaspStatus>) message);
            if (!trimGaloreMessage.getStatus().equals(WaspStatus.CREATED) ){
                    logger.debug("Message has the wrong status, this service activator launches on WaspStatus.CREATED.");
                    return null;
            }
            
            String runName = trimGaloreMessage.getRunName();
            Integer fileGroupId = trimGaloreMessage.getFileGroupId();
            Integer fileNumber = trimGaloreMessage.getFileNumber();
            Integer readSegments = trimGaloreMessage.getNumberOfReadSegments();
            
            logger.debug("Processing run message: fileGroupId=" + fileGroupId);
            logger.debug("Processing run message: fileNumber=" + fileNumber);
            logger.debug("Processing run message: readSegments=" + readSegments);
            
            String flowName = TrimGalore.TRIM_GALORE_FLOW_NAME;
            
            // all checks out so create the batch job launch message
            Map<String, String> jobParameters = new HashMap<String, String>();
            jobParameters.put(WaspJobParameters.RUN_NAME, runName);
            jobParameters.put(WaspJobParameters.FILE_GROUP_ID, fileGroupId.toString());
            jobParameters.put(FileTypeService.FILETYPE_FILE_NUMBER_META_KEY, fileNumber.toString());
            jobParameters.put(FastqService.FASTQ_GROUP_NUMBER_OF_READ_SEGMENTS, readSegments.toString());
            
            BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate(new BatchJobLaunchContext(flowName, jobParameters) );
            try {
                    if (message.getHeaders().containsKey(MessageHeaders.REPLY_CHANNEL))
                            batchJobLaunchMessageTemplate.getHeaders().put(MessageHeaders.REPLY_CHANNEL, message.getHeaders().get(MessageHeaders.REPLY_CHANNEL));
                    Message<BatchJobLaunchContext> launchMessage = batchJobLaunchMessageTemplate.build();
                    logger.debug("preparing new message to send: " + launchMessage);
                    return launchMessage;
            } catch (WaspMessageBuildingException e) {
                    throw new MessagingException(e.getLocalizedMessage(), e);
            }
    
    }
}
