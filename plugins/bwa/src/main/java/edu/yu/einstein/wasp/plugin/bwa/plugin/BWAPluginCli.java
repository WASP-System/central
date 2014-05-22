package edu.yu.einstein.wasp.plugin.bwa.plugin;

import org.springframework.messaging.Message;

public interface BWAPluginCli {
	
	public Message<String> align(Message<String> m) throws Exception;

	public Message<String> alignHelp();

}
