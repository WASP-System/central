package edu.yu.einstein.wasp.integration.messages;

/**
 * @author calder
 *
 */
public class WaspPlatformUnitTask extends WaspTask {
	
	
	public static final String RUN_ID_KEY = "runId";
	
	/**
	 * Platform Unit is registered.
	 */
	public static final String CREATE = "createPU";
	
	/**
	 * Platform unit can no longer have samples assigned. 
	 */
	public static final String CLOSE = "closePU";

}
