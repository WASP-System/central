package edu.yu.einstein.wasp.integration.endpoints;

import org.springframework.integration.Message;

public interface CliSupporting {
	
	public Message<String> listPlugins();
	
	public Message<String> listGenomeBuilds();
	
	public Message<String> listSampleSubtypes();

}
