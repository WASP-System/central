package edu.yu.einstein.wasp.integration.messages.templates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
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
	
	protected String task;
	
	protected String target;
	
	protected User userCreatingMessage; 
	
	protected String comment;
	
	public User getUserCreatingMessage() {
		return userCreatingMessage;
	}
	
	public void setUserCreatingMessageFromSession(UserService userService){
		userCreatingMessage = null;
		try{
			try{
				final String login = SecurityContextHolder.getContext().getAuthentication().getName();
				userCreatingMessage = userService.getUserByLogin(login);
				if (userCreatingMessage.getId() == null)
					userCreatingMessage = userService.getUserDao().getUserByLogin("wasp"); // wasp user (reserved)
			} catch (Exception e){
				userCreatingMessage = userService.getUserDao().getUserByLogin("wasp"); // wasp user (reserved)
			}
		} catch (Exception e){
			logger.debug("not attempting setting last updating user as not able to resolve a user to assign");
		}
	}

	public void setUserCreatingMessage(User userCreatingMessage) {
		this.userCreatingMessage = userCreatingMessage;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}
	
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public WaspMessageTemplate(){
		this.task = WaspTask.NOTIFY_STATUS; // default
		this.target = DEFAULT_TARGET; // default
	}	
	
	public WaspMessageTemplate(Message<?> message){
		if (message.getHeaders().containsKey(TARGET_KEY))
			target = (String) message.getHeaders().get(TARGET_KEY);
		if (message.getHeaders().containsKey(WaspJobTask.HEADER_KEY))
			task = (String) message.getHeaders().get(WaspJobTask.HEADER_KEY);
		if (message.getHeaders().containsKey(USER_KEY))
			userCreatingMessage = (User) message.getHeaders().get(USER_KEY);
		if (message.getHeaders().containsKey(COMMENT_KEY))
			comment = (String) message.getHeaders().get(COMMENT_KEY);
	}	
}
	
