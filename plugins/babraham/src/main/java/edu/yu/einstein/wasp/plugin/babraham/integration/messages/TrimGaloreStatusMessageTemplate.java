/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.integration.messages;

import org.springframework.integration.Message;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;

/**
 * @author calder
 * 
 */
public class TrimGaloreStatusMessageTemplate extends SimpleBabrahamStatusMessageTemplate {

    public TrimGaloreStatusMessageTemplate(String runName, Integer fileGroupId, Integer fileNumber, Integer numberOfReadSegments) {
        super();
        addHeader(WaspMessageType.HEADER_KEY, BabrahamMessageType.TRIM_GALORE);
        addHeader(WaspJobParameters.RUN_NAME, runName);
        addHeader(WaspJobParameters.FILE_GROUP_ID, fileGroupId);
        addHeader(FileTypeService.FILETYPE_FILE_NUMBER_META_KEY, fileNumber);
        addHeader(FastqService.FASTQ_GROUP_NUMBER_OF_READ_SEGMENTS, numberOfReadSegments);
    }
    
    public String getRunName() {
        return (String) this.getHeader(WaspJobParameters.RUN_NAME);
    }

    public Integer getFileGroupId() {
        return (Integer) this.getHeader(WaspJobParameters.FILE_GROUP_ID);
    }

    public Integer getFileNumber() {
        return (Integer) this.getHeader(FileTypeService.FILETYPE_FILE_NUMBER_META_KEY);
    }

    public Integer getNumberOfReadSegments() {
        return (Integer) this.getHeader(FastqService.FASTQ_GROUP_NUMBER_OF_READ_SEGMENTS);
    }

    public TrimGaloreStatusMessageTemplate(Message<WaspStatus> message) {
        super(message);
        if (!isMessageOfCorrectType(message))
            throw new WaspMessageInitializationException("message is not of the correct type");
    }

    public static boolean isMessageOfCorrectType(Message<?> message) {
        return message.getHeaders().containsKey(WaspMessageType.HEADER_KEY)
                && message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(BabrahamMessageType.TRIM_GALORE);
    }

    /**
     * Test to see if the message should be acted upon. This is the only such
     * valid method for this class.
     * 
     * @param message
     * @param fileGroupId
     * @return
     */
    public static boolean actUponMessage(Message<?> message, Integer fileGroupId) {
        return isMessageOfCorrectType(message) && message.getHeaders().containsKey(WaspJobParameters.FILE_GROUP_ID)
                && message.getHeaders().get(WaspJobParameters.FILE_GROUP_ID).equals(fileGroupId);
    }

    /**
     * false, requires more information
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean actUponMessage(Message<?> message) {
        return false;
    }

    /**
     * false, requires more information
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean actUponMessageIgnoringTask(Message<?> message) {
        return false;
    }

}
