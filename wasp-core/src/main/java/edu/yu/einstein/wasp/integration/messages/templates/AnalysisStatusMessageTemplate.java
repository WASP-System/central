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
	
	public AnalysisStatusMessageTemplate(Integer jobId, Integer cellLibraryId){
		super();
		addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.ANALYSIS);
		setCellLibraryId(cellLibraryId);
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
		if (message.getHeaders().containsKey(WaspJobParameters.CELL_LIBRARY_ID))
			setCellLibraryId((Integer) message.getHeaders().get(WaspJobParameters.CELL_LIBRARY_ID));
		if (message.getHeaders().containsKey(WaspJobParameters.JOB_ID))
			setJobId((Integer) message.getHeaders().get(WaspJobParameters.JOB_ID));
	}
	
	@Autowired
	public void setSampleService(SampleService sampleService){
		this.sampleService = sampleService;
	}
	
	public Integer getCellLibraryId() {
		return (Integer) getHeader(WaspJobParameters.CELL_LIBRARY_ID);
	}

	public Integer getJobId() {
		return (Integer) getHeader(WaspJobParameters.JOB_ID);
	}

	public void setJobId(Integer jobId) {
		addHeader(WaspJobParameters.JOB_ID, jobId);
	}
	
	public void setCellLibraryId(Integer cellLibraryId){
		SampleSource cellLibrary = sampleService.getSampleSourceDao().getSampleSourceBySampleSourceId(cellLibraryId);
		addHeader(WaspJobParameters.CELL_LIBRARY_ID, cellLibraryId);
		setJobId(sampleService.getJobOfLibraryOnCell(cellLibrary).getId());
	}
	

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		
		if (task == null){
			if (getCellLibraryId() == null)
				return actUponMessage(message, getJobId());
			else 
				return actUponMessage(message, getJobId(), getCellLibraryId());
		}
		if (getCellLibraryId() == null)
			return actUponMessage(message, getJobId(), task);
		else 
			return actUponMessage(message, getJobId(), getCellLibraryId(), task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
		if (getCellLibraryId() == null)
			return actUponMessage(message, getJobId(), (String) null);
		else 
			return actUponMessage(message, getJobId(), getCellLibraryId(), null);
	}
	
	// Statics.........
	
	/**
	 * Takes a message and checks its headers against the supplied JobId (and CellLibraryId if not null) value to see if the message should be acted upon or not.
	 * Ignores CellLibraryId if null
	 * @param message
	 * @param jobId 
	 * @param CellLibraryId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer jobId, Integer CellLibraryId ){
		if ( !(message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && 
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.ANALYSIS)) )
			return false;
		if ( CellLibraryId != null && jobId != null &&
				message.getHeaders().containsKey(WaspJobParameters.LIBRARY_ID) && 
				((Integer) message.getHeaders().get(WaspJobParameters.LIBRARY_ID)).equals(CellLibraryId) &&
				message.getHeaders().containsKey(WaspJobParameters.JOB_ID) && 
				((Integer) message.getHeaders().get(WaspJobParameters.JOB_ID)).equals(jobId) )
			return true;
		if ( jobId != null && CellLibraryId == null &&
				message.getHeaders().containsKey(WaspJobParameters.JOB_ID) && 
				((Integer) message.getHeaders().get(WaspJobParameters.JOB_ID)).equals(jobId) )
			return true;
		if ( jobId == null && CellLibraryId != null &&
				message.getHeaders().containsKey(WaspJobParameters.LIBRARY_ID) && 
				((Integer) message.getHeaders().get(WaspJobParameters.LIBRARY_ID)).equals(CellLibraryId) )
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
	 * Takes a message and checks its headers against the supplied jobId and CellLibraryId value and task to see if the message should be acted upon or not
	 * @param message
	 * @param jobId 
	 * @param CellLibraryId 
	 * @param task
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Integer jobId, Integer CellLibraryId, String task ){
		if (! actUponMessage(message, jobId, CellLibraryId) )
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
	
	@Override
	public AnalysisStatusMessageTemplate getNewInstance(WaspStatusMessageTemplate messageTemplate){
		AnalysisStatusMessageTemplate newTemplate = new AnalysisStatusMessageTemplate(((AnalysisStatusMessageTemplate) messageTemplate).getJobId(),
				((AnalysisStatusMessageTemplate) messageTemplate).getCellLibraryId());
		copyCommonProperties(messageTemplate, newTemplate);
		return newTemplate;
	}
	
}
	
