package edu.yu.einstein.wasp.integration.messages.templates;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.UserService;



/**
 * Abstract base class defining common attributes.
 * @author asmclellan
 *
 */
public abstract class WaspMessageTemplate implements MessageTemplate{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String TARGET_KEY = "target";
	
	public static final String MESSAGE_TYPE_KEY = "messagetype";
	
	public static final String DEFAULT_TARGET = "batch";
	
	public static final String USER_KEY = "user";
	
	public static final String COMMENT_KEY = "comment";
	
	protected Map<String, Object> headers = new HashMap<>();
	
	
	/**
	 * Default constructor. No target or task specified so defaults will be used (target = batch, task = WaspTask.NOTIFY_STATUS)
	 */
	public WaspMessageTemplate(){
		setTask(WaspTask.NOTIFY_STATUS); // default
		setTarget(DEFAULT_TARGET); // default
	}
	
	/**
	 * No task specified so default will be used (WaspTask.NOTIFY_STATUS)
	 * @param target
	 */
	public WaspMessageTemplate(String target){
		setTask(WaspTask.NOTIFY_STATUS); // default
		setTarget(target); 
	}
	
	public WaspMessageTemplate(String target, String task){
		setTask(task); 
		setTarget(target); 
	}
	
	public WaspMessageTemplate(Message<?> message){
		if (message.getHeaders().containsKey(TARGET_KEY))
			setTarget((String) message.getHeaders().get(TARGET_KEY));
		if (message.getHeaders().containsKey(WaspJobTask.HEADER_KEY))
			setTask((String) message.getHeaders().get(WaspJobTask.HEADER_KEY));
		if (message.getHeaders().containsKey(USER_KEY))
			setUserCreatingMessage((User) message.getHeaders().get(USER_KEY));
		if (message.getHeaders().containsKey(COMMENT_KEY))
			setComment((String) message.getHeaders().get(COMMENT_KEY));
		headers.putAll(getSanitizedHeaders(message.getHeaders()));
	}	
	
	public User getUserCreatingMessage() {
		return (User) headers.get(USER_KEY);
	}
	
	public void setUserCreatingMessageFromSession(UserService userService){
		User userCreatingMessage = null;
		try{
			try{
				final String login = SecurityContextHolder.getContext().getAuthentication().getName();
				userCreatingMessage = userService.getUserByLogin(login);
				if (userCreatingMessage.getId() == null)
					userCreatingMessage = userService.getUserDao().getUserByLogin("wasp"); // wasp user (reserved)
			} catch (Exception e){
				userCreatingMessage = userService.getUserDao().getUserByLogin("wasp"); // wasp user (reserved)
			}
			headers.put(USER_KEY, userCreatingMessage);
		} catch (Exception e){
			logger.debug("not attempting setting last updating user as not able to resolve a user to assign");
		}
	}

	public void setUserCreatingMessage(User userCreatingMessage) {
		headers.put(USER_KEY, userCreatingMessage);
	}

	public String getComment() {
		return (String) headers.get(COMMENT_KEY);
	}

	public void setComment(String comment) {
		headers.put(COMMENT_KEY, comment);
	}

	public String getTask() {
		return (String) headers.get(WaspJobTask.HEADER_KEY);
	}

	public void setTask(String task) {
		headers.put(WaspJobTask.HEADER_KEY, task);
	}
	
	public String getTarget() {
		return (String) headers.get(TARGET_KEY);
	}

	public void setTarget(String target) {
		headers.put(TARGET_KEY, target);
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}
	
	public Object getHeader(String key){
		return headers.get(key);
	}
	
	public void addHeader(String key, Object value){
		headers.put(key, value);
	}
	
	/**
	 * Remove spring integration headers (specified in {@link MessageHeaders}) from header set (if any)
	 * @param headers
	 * @return
	 */
	protected HashMap<String, Object> getSanitizedHeaders(final MessageHeaders headers){
		HashMap<String, Object> sanitizedHeaders = new HashMap<>(headers);
		sanitizedHeaders.remove(MessageHeaders.CONTENT_TYPE);
		sanitizedHeaders.remove(MessageHeaders.CORRELATION_ID);
		sanitizedHeaders.remove(MessageHeaders.ERROR_CHANNEL);
		sanitizedHeaders.remove(MessageHeaders.EXPIRATION_DATE);
		sanitizedHeaders.remove(MessageHeaders.ID);
		sanitizedHeaders.remove(MessageHeaders.POSTPROCESS_RESULT);
		sanitizedHeaders.remove(MessageHeaders.PRIORITY);
		sanitizedHeaders.remove(MessageHeaders.REPLY_CHANNEL);
		sanitizedHeaders.remove(MessageHeaders.SEQUENCE_DETAILS);
		sanitizedHeaders.remove(MessageHeaders.SEQUENCE_NUMBER);
		sanitizedHeaders.remove(MessageHeaders.SEQUENCE_SIZE);
		sanitizedHeaders.remove(MessageHeaders.TIMESTAMP);
		return sanitizedHeaders;
	}

}
	
