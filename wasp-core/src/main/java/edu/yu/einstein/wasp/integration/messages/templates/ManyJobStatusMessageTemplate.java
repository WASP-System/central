package edu.yu.einstein.wasp.integration.messages.templates;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * Message template for completion of jobs launched by LaunchManyJobsTasklet
 * 
 * @author asmclellan & calder
 * 
 */
public class ManyJobStatusMessageTemplate extends WaspStatusMessageTemplate {

    public ManyJobStatusMessageTemplate(UUID parentId, Integer childId) {
        super();
        addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.MANY);
        setParentId(parentId);
        setChildId(childId);
    }

    public ManyJobStatusMessageTemplate(Message<WaspStatus> message) {
        super(message);
        addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.MANY);
        if (!isMessageOfCorrectType(message))
            throw new WaspMessageInitializationException("message is not of the correct type");
        if (message.getHeaders().containsKey(WaspMessageTemplate.PARENT_ID))
            setParentId(UUID.fromString((String) message.getHeaders().get(WaspMessageTemplate.PARENT_ID)));
        if (message.getHeaders().containsKey(WaspMessageTemplate.CHILD_MESSAGE_ID))
            setChildId((Integer) message.getHeaders().get(WaspMessageTemplate.CHILD_MESSAGE_ID));
    }

    public UUID getParentId() {
        return UUID.fromString((String) getHeader(WaspMessageTemplate.PARENT_ID));
    }

    public void setParentId(UUID parentId) {
        addHeader(WaspMessageTemplate.PARENT_ID, parentId.toString());
    }
    
    public void setParentId(String parentId) {
        setParentId(UUID.fromString(parentId));
    }

    public Integer getChildId() {
        return (Integer) getHeader(WaspMessageTemplate.CHILD_MESSAGE_ID);
    }

    public void setChildId(Integer childId) {
        addHeader(WaspMessageTemplate.CHILD_MESSAGE_ID, childId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean actUponMessage(Message<?> message) {
        return actUponMessage(message, getParentId(), getChildId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
	    logger.warn("should this method ever be called?");
	    return false;
	}

    // Statics.........

    /**
     * Takes a message and checks its headers against the supplied JobId (and
     * libraryId if not null) value to see if the message should be acted upon
     * or not. Ignores libraryId if null
     * 
     * @param message
     * @param jobId
     * @param libraryId
     * @return
     */
    public static boolean actUponMessage(Message<?> message, UUID parentId, Integer childId) {
        if (!(message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && ((String) message.getHeaders().get(WaspMessageType.HEADER_KEY))
                .equals(WaspMessageType.MANY)))
            return false;
        if (parentId != null && childId != null 
                && message.getHeaders().containsKey(WaspMessageTemplate.PARENT_ID)
                && ((String) message.getHeaders().get(WaspMessageTemplate.PARENT_ID)).equals(parentId.toString())
                && message.getHeaders().containsKey(WaspMessageTemplate.CHILD_MESSAGE_ID) 
                && ((Integer) message.getHeaders().get(WaspMessageTemplate.CHILD_MESSAGE_ID)).equals(childId))
            return true;
        return false;
    }

    /**
     * Returns true is the message is of the correct WaspMessageType
     * 
     * @param message
     * @return
     */
    public static boolean isMessageOfCorrectType(Message<?> message) {
        return message.getHeaders().containsKey(WaspMessageType.HEADER_KEY)
                && ((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.MANY);
    }

    @Override
    public ManyJobStatusMessageTemplate getNewInstance(WaspStatusMessageTemplate messageTemplate) {
        ManyJobStatusMessageTemplate newTemplate = new ManyJobStatusMessageTemplate(((ManyJobStatusMessageTemplate) messageTemplate).getParentId(),
                ((ManyJobStatusMessageTemplate) messageTemplate).getChildId());
        copyCommonProperties(messageTemplate, newTemplate);
        return newTemplate;
    }

}
