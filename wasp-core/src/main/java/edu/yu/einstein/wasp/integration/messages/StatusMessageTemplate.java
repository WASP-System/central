package edu.yu.einstein.wasp.integration.messages;



/**
 * Interface defining status message templates
 * @author andymac
 *
 */
public interface StatusMessageTemplate extends MessageTemplate{

	public WaspStatus getStatus();
	
}
