/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.integration.messages;

import org.springframework.integration.Message;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;

/**
 * @author calder
 * 
 */
public class TrimGaloreStatusMessageTemplate extends SimpleBabrahamStatusMessageTemplate {

    public TrimGaloreStatusMessageTemplate() {
        super();
        addHeader(WaspMessageType.HEADER_KEY, BabrahamMessageType.BABRAHAM);
    }

    public TrimGaloreStatusMessageTemplate(Message<WaspStatus> message) {
        super(message);
        if (!isMessageOfCorrectType(message))
            throw new WaspMessageInitializationException("message is not of the correct type");
    }

    public static boolean isMessageOfCorrectType(Message<?> message) {
        return message.getHeaders().containsKey(WaspMessageType.HEADER_KEY)
                && message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(BabrahamMessageType.BABRAHAM);
    }
    /**
     * This message should be acted upon if the message is of type BABRAHAM and the task is TRIM_GALORE.
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean actUponMessage(Message<?> message) {
        return isMessageOfCorrectType(message) &&
                message.getHeaders().containsKey(WaspTask.HEADER_KEY) &&
                message.getHeaders().get(WaspTask.HEADER_KEY).equals(BabrahamBatchJobTask.TRIM_GALORE);
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
