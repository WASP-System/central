package edu.yu.einstein.wasp.integration.messages.templates;

import edu.yu.einstein.wasp.integration.messages.WaspStatus;



/**
 * Interface defining status message templates
 * @author andymac
 *
 */
public interface StatusMessageTemplate extends MessageTemplate{

	public WaspStatus getStatus();
	
}
