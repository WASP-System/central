package edu.yu.einstein.wasp.integration.messages.templates;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;



/**
 * Class defining the the status message template which may be extended further. A WaspMessageType header may or may not be defined.
 * @author asmclellan
 *
 */
public class WaspStatusMessageTemplate extends WaspMessageTemplate implements StatusMessageTemplate{
	
		
	public static final String EXIT_DESCRIPTION_HEADER = "description";
	
	private static final String JSON_HEADERS_KEY = "w_hdrs";
	
	private static final String JSON_STATUS_KEY = "w_st";
	
	protected WaspStatus status;
	
	public WaspStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (message.getHeaders().containsKey(EXIT_DESCRIPTION_HEADER))
			addHeader(EXIT_DESCRIPTION_HEADER, message.getHeaders().get(EXIT_DESCRIPTION_HEADER));
		status = message.getPayload();
	}
	
	public WaspStatusMessageTemplate(JSONObject json) throws JSONException{
		logger.debug("creating new WaspStatusMessageTemplate object from JSON string: " + json.toString());
		JSONObject jsonForHeaders = json.getJSONObject(JSON_HEADERS_KEY);
		@SuppressWarnings("unchecked")
		Iterator<String> headerIterator = jsonForHeaders.keys();
		while (headerIterator.hasNext()){
			String headerKey = headerIterator.next();
			addHeader(headerKey, jsonForHeaders.get(headerKey));
		}
		setStatus(json.getString(JSON_STATUS_KEY));
	}
	
	public WaspStatusMessageTemplate(){
		super();
		this.status = null;
	}	
	
	public WaspStatusMessageTemplate(String target){
		super(target); 
		this.status = null;
	}
	
	public WaspStatusMessageTemplate(String target, String task){
		super(target, task);
		this.status = null;
	}
	
	public String getExitDescription() {
		return (String) getHeader(EXIT_DESCRIPTION_HEADER);
	}

	public void setExitDescription(String exitDescription) {
		addHeader(EXIT_DESCRIPTION_HEADER, exitDescription);
	}

	@Override
	public WaspStatus getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = WaspStatus.valueOf(status);
	}

	public void setStatus(WaspStatus status) {
		this.status = status;
	}
	
	/**
	 * Build a Spring Integration Message using the ANALYSIS header, task header if not null, and the WaspStatus as payload .
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	@Override
	public Message<WaspStatus> build() throws WaspMessageBuildingException{
		if (this.status == null)
			throw new WaspMessageBuildingException("no status message defined");
		Message<WaspStatus> message = null;
		try {
			message = MessageBuilder.withPayload(status)
						.copyHeaders(getHeaders())
						.setPriority(status.getPriority())
						.build();
		} catch(Exception e){
			throw new WaspMessageBuildingException("build() failed to build message: "+e.getMessage());
		}
		return message;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("WaspStatusMessage:[Status=" + this.status + "][Headers={");
		for (String headerKey : this.getHeaders().keySet()){
			sb.append(headerKey + "=" + this.getHeaders().get(headerKey).toString() + ", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append("}]\n");
		return sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		return actUponMessage(message, task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
		return actUponMessage(message, null);
	}
	
	/**
	 * Takes a message and checks its headers to see if the message should be acted upon or not
	 * @param message
	 * @param task
	 * @return
	 */
	private boolean actUponMessage(Message<?> message, String task ){
		if ( (!message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && headers.containsKey(WaspMessageType.HEADER_KEY)) || 
				(message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && !headers.containsKey(WaspMessageType.HEADER_KEY)) )
			return false;
		
		if ( message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && headers.containsKey(WaspMessageType.HEADER_KEY) && 
						!message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(headers.get(WaspMessageType.HEADER_KEY)) ){
				return false;
		}
		if (task == null)
			return true;
		if (message.getHeaders().containsKey(WaspTask.HEADER_KEY) && message.getHeaders().get(WaspTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}
	
	
	public JSONObject getAsJson() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_HEADERS_KEY, headers);
		json.put(JSON_STATUS_KEY, getStatus());
		return json;
	}
	
	@Override
	public Object getPayload(){
		return getStatus();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null || !getClass().isInstance(obj))
			return false;
		WaspStatusMessageTemplate messageTemplate = (WaspStatusMessageTemplate) obj;
		if (!messageTemplate.getStatus().equals(this.getStatus()))
			return false;
		Set<String> myHeaders = headers.keySet();
		Set<String> objHeaders = messageTemplate.getHeaders().keySet();
		Set<String> commonHeaders = new HashSet<>(myHeaders);
		commonHeaders.retainAll(objHeaders);
		if (myHeaders.size() > commonHeaders.size() || objHeaders.size() > commonHeaders.size())
			return false; // there exists extra headers in one object that are not added by spring integration
		for (String header : commonHeaders){
			if ((headers.get(header) == null && messageTemplate.getHeaders().get(header) != null) || 
					(headers.get(header) != null && messageTemplate.getHeaders().get(header) == null))
				return false;
			if (!headers.get(header).equals(messageTemplate.getHeaders().get(header)))
				return false;
		}
		return true;
	}
	
	@Override
	public int hashCode(){
		int hash = 7;
		hash = 31 * hash + (null == status ? 0 : status.hashCode());
		for (String header : headers.keySet()){
			Object value = headers.get(header);
			hash = 31 * hash + header.hashCode();
			hash = 31 * hash + (null == value ? 0 : value.hashCode());
		}
		return hash;
	}
	
	public WaspStatusMessageTemplate getNewInstance(WaspStatusMessageTemplate messageTemplate){
		WaspStatusMessageTemplate newTemplate = new WaspStatusMessageTemplate();
		copyCommonProperties(messageTemplate, newTemplate);
		return newTemplate;
	}
	
	protected void copyCommonProperties(WaspStatusMessageTemplate source, WaspStatusMessageTemplate target){
		if (source.getComment() != null)
			target.setComment(source.getComment());
		if (source.getExitDescription() != null)
			target.setExitDescription(source.getExitDescription());
		if (source.getHeaders() != null)
			target.setHeaders(source.getHeaders());
		if (source.getStatus() != null)
			target.setStatus(source.getStatus());
		if (source.getTarget() != null)
			target.setTarget(source.getTarget());
		if (source.getTask() != null)
			target.setTask(source.getTask());
		if (source.getUserCreatingMessage() != null)
			target.setUserCreatingMessage(source.getUserCreatingMessage());
	}

}
	
