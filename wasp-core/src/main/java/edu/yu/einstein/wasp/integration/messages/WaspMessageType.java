package edu.yu.einstein.wasp.integration.messages;

/**
 * 
 * @author asmclellan
 *
 */
public class WaspMessageType {
	
	// constant for use with message headers
	public static final String HEADER_KEY = "messagetype";
	
	public static final String JOB = "job"; 
	public static final String PLUGIN = "plugin";
	public static final String RUN = "run";
	public static final String SAMPLE = "sample";
	public static final String LIBRARY = "library";
	public static final String ANALYSIS = "analysis";
	public static final String GENERIC = "generic";
	public static final String FILE = "file";
	public static final String HIBERNATION = "hibernation";
	public static final String LAUNCH_BATCH_JOB = "launchBatchJob";
	public static final String ACTION_BATCH_JOB = "actionBatchJob";
	public static final String REPLY = "reply";
	public static final String MANY = "many";
}
