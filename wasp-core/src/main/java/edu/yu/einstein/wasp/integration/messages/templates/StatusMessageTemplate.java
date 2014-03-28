package edu.yu.einstein.wasp.integration.messages.templates;

import java.util.Map;

import edu.yu.einstein.wasp.integration.messages.WaspStatus;



/**
 * Interface defining status message templates
 * @author asmclellan
 *
 */
public interface StatusMessageTemplate extends MessageTemplate{

	public WaspStatus getStatus();
	
	public Map<String, Object> getHeaders();
	
}
