/**
 * 
 */
package edu.yu.einstein.wasp.cli;

import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHandlingException;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.rmi.RmiOutboundGateway;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.remoting.RemoteLookupFailureException;

/**
 * @author calder
 *
 */
public class Main {
	
	private final static Logger logger = LoggerFactory.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("cli-context.xml");
		
		Parser parser = new Parser(args);
		
		CommandLine cl = parser.getCommandLine();
		
		String host = cl.getOptionValue("H", "localhost");
		String port = cl.getOptionValue("P", "23532");
		String rmiUrl = "rmi://" + host + ":" + port + "/org.springframework.integration.rmiGateway.wasp.channel.rmi.secure.inbound0";
		RmiOutboundGateway gw = new RmiOutboundGateway(rmiUrl);
		try {
			
			QueueChannel replychannel = new QueueChannel();
			gw.setReplyChannel(replychannel);
			gw.afterPropertiesSet();
			Message<String> message;
			if (cl.hasOption("l")) {
				message = (Message<String>) MessageBuilder.withPayload("list")
						.setHeader("target", "pluginRegistry")
						.setHeader("user", parser.getUser())
						.setHeader("password", parser.getPassword())
						.build();
			} else {
				message = (Message<String>) MessageBuilder.withPayload(cl.getOptionValue("m"))
			        .setHeader("target", cl.getOptionValue("t"))
			        .setHeader("user", parser.getUser())
			        .setHeader("password", parser.getPassword())
			        .build();
			}
			
			Message<String> reply = (Message<String>) gw.handleRequestMessage(message);
			
			if (reply.getHeaders().containsKey("authenticated") && reply.getHeaders().get("authenticated").equals("false")) {
				System.err.println("Failed authentication.");
				System.exit(1);
			}
			if (reply.getHeaders().containsKey("unknown-target") && reply.getHeaders().get("unknown-target").equals("true")) {
				System.err.println("Unknown message target.");
				System.exit(1);
			}
			
			System.out.println(reply.getPayload());
		} catch (RemoteLookupFailureException e) {
			System.err.println("ERROR: Unable to connect to server at " + host + ":" + port);
			System.exit(2);
		} catch (MessageHandlingException e) {
			System.err.println("ERROR: Error executing on remote host. " + e.getCause().toString());
			System.exit(2);
		} 
		
//		Message m = new Message();
//		m.sendMessage(parser);

	}

}
