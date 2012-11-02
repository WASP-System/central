package edu.yu.einstein.wasp.integration.messages;

import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;


/**
 * Interface defining status message templates
 * @author andymac
 *
 */
public interface StatusMessageTemplate extends MessageTemplate{

	public WaspStatus getStatus();
	
}
