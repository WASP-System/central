package edu.yu.einstein.wasp.integration.messages.templates;

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
 * Handling Wasp Analysis Status Messages. If not task is defined the default is WaspTask.NOTIFY_STATUS
 * @author asmclellan
 *
 */
public class AnalysisStatusMessageTemplate extends  WaspStatusMessageTemplate{
	
	protected SampleService sampleService;
	
	public AnalysisStatusMessageTemplate(Integer jobId, Integer libraryId){
		super();
		addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.ANALYSIS);
		setLibraryId(libraryId);
		setJobId(jobId);
	}
	
	public AnalysisStatusMessageTemplate(Integer jobId){
		super();
		addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.ANALYSIS);
		setJobId(jobId);
	}
	
	public AnalysisStatusMessageTemplate(){
		super();
		addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.ANALYSIS);
	}
	
		
	public AnalysisStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.LIBRARY_ID))
			setLibraryId((Integer) message.getHeaders().get(WaspJobParameters.LIBRARY_ID));
		if (message.getHeaders().containsKey(WaspJobParameters.JOB_ID))
			setJobId((Integer) message.getHeaders().get(WaspJobParameters.JOB_ID));
	}
	
	@Autowired
	public void setSampleService(SampleService sampleService){
		this.sampleService = sampleService;
	}
	
	public Integer getLibraryId() {
		return (Integer) getHeader(WaspJobParameters.LIBRARY_ID);
	}

	public void setLibraryId(Integer libraryId) {
		addHeader(WaspJobParameters.LIBRARY_ID, libraryId);
	}
	
	public Integer getJobId() {
		return (Integer) getHeader(WaspJobParameters.JOB_ID);
	}

	public void setJobId(Integer jobId) {
		addHeader(WaspJobParameters.JOB_ID, jobId);
	}
	
	public void setCellLibraryId(Integer cellLibraryId){
		SampleSource libraryCell = sampleService.getSampleSourceDao().getSampleSourceBySampleSourceId(cellLibraryId);
		setLibraryId(sampleService.getLibrary(libraryCell).getId());
		setJobId(sampleService.getJobOfLibraryOnCell(libraryCell).getId());
	}
	

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		
		if (task == null){
			if (getLibraryId() == null)
				return actUponMessage(message, getJobId());
			else 
				return actUponMessage(message, getJobId(), getLibraryId());
		}
		if (getLibraryId() == null)
			return actUponMessage(message, getJobId(), task);
		else 
			return actUponMessage(message, getJobId(), getLibraryId(), task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
		if (getLibraryId() == null)
			return actUponMessage(message, getJobId(), (String) null);
		else 
			return actUponMessage(message, getJobId(), getLibraryId(), null);
	}
	
	// Statics.........
	
	/**
	 * Takes a message and checks its headers against the supplied JobId (and libraryId if not null) value to see if the message should be acted upon or not.
	 * Ignores libraryId if null
	 * @param message
	 * @param jobId 
	 * @param libraryId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer jobId, Integer libraryId ){
		if ( !(message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && 
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.ANALYSIS)) )
			return false;
		if ( libraryId != null && jobId != null &&
				message.getHeaders().containsKey(WaspJobParameters.LIBRARY_ID) && 
				((Integer) message.getHeaders().get(WaspJobParameters.LIBRARY_ID)).equals(libraryId) &&
				message.getHeaders().containsKey(WaspJobParameters.JOB_ID) && 
				((Integer) message.getHeaders().get(WaspJobParameters.JOB_ID)).equals(jobId) )
			return true;
		if ( jobId != null && libraryId == null &&
				message.getHeaders().containsKey(WaspJobParameters.JOB_ID) && 
				((Integer) message.getHeaders().get(WaspJobParameters.JOB_ID)).equals(jobId) )
			return true;
		if ( jobId == null && libraryId != null &&
				message.getHeaders().containsKey(WaspJobParameters.LIBRARY_ID) && 
				((Integer) message.getHeaders().get(WaspJobParameters.LIBRARY_ID)).equals(libraryId) )
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks its headers against the supplied jobId value to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer jobId ){
		return actUponMessage(message, jobId, (Integer) null);
	}
	
	/**
	 * Takes a message and checks its headers against the supplied jobId and libraryId value and task to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @param libraryId 
	 * @param task
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer jobId, Integer libraryId, String task ){
		if (! actUponMessage(message, jobId, libraryId) )
			return false;
		if (task == null)
			return true;
		if (message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) && message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks its headers against the supplied jobId value and task to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @param task
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer jobId, String task ){
		if (! actUponMessage(message, jobId) )
			return false;
		if (task == null)
			return true;
		if (message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) && message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}

	/**
	 * Returns true is the message is of the correct WaspMessageType
	 * @param message
	 * @return
	 */
	public static boolean isMessageOfCorrectType(Message<?> message) {
		return message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && 
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.ANALYSIS);
	}
	
}
	
