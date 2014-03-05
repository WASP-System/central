/**
 * 
 */
package edu.yu.einstein.wasp.cli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.json.JSONObject;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHandlingException;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.rmi.RmiOutboundGateway;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.remoting.RemoteLookupFailureException;

import edu.yu.einstein.wasp.plugin.cli.CliMessagingTask;

/**
 * @author calder
 *
 */
public class Main {
	
	// private final static Logger logger = LoggerFactory.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Parser parser = new Parser(args);
		
		CommandLine cl = parser.getCommandLine();
		
		String host = cl.getOptionValue("H", "localhost");
		String port = cl.getOptionValue("P", "23532");
		String rmiUrl = "rmi://" + host + ":" + port + "/org.springframework.integration.rmiGateway.wasp.channel.remoting.secure.inbound";
		RmiOutboundGateway gw = new RmiOutboundGateway(rmiUrl);
		try {
			
			QueueChannel replychannel = new QueueChannel();
			gw.setReplyChannel(replychannel);
			gw.afterPropertiesSet();
			Message<String> message;
			if (cl.hasOption("l") || cl.hasOption("g") || cl.hasOption("s") || cl.hasOption("c")  || cl.hasOption("r")) {
				if (cl.hasOption("l")) {
					message = getMessage(parser, "cli", CliMessagingTask.LIST_PLUGINS);
					listPlugins(new JSONObject(sendMessageAndParseReply(message, gw)));
				} 
				if (cl.hasOption("g")){
					message = getMessage(parser, "cli", CliMessagingTask.LIST_GENOME_BUILDS);
					listGenomeBuilds(new JSONObject(sendMessageAndParseReply(message, gw)));
				} 
				if (cl.hasOption("s")){
					message = getMessage(parser, "cli", CliMessagingTask.LIST_SAMPLE_SUBTYPES); 
					listSampleSubtypes(new JSONObject(sendMessageAndParseReply(message, gw)));
				} 
				if (cl.hasOption("c")){
					message = getMessage(parser, "cli", CliMessagingTask.LIST_CELL_LIBRARIES); 
					listCellLibraries(new JSONObject(sendMessageAndParseReply(message, gw)));
				} 
			} else {
				String mp = "";
				if (cl.hasOption("m")) mp = cl.getOptionValue("m");
				MessageBuilder<String> m = MessageBuilder.withPayload(mp)
				        .setHeader("user", parser.getUser())
				        .setHeader("password", parser.getPassword());
				
				if(cl.hasOption("T")) m.setHeader("target", "plugin." + cl.getOptionValue("T"));
				if(cl.hasOption("t")) m.setHeader("task", cl.getOptionValue("t"));
				if(cl.hasOption("h")) m.setHeader("help", "true");
				
				message = m.build();
				System.out.println(sendMessageAndParseReply(message, gw));
			}
			
			
		} catch (RemoteLookupFailureException e) {
			System.err.println("ERROR: Unable to connect to server at " + host + ":" + port);
			System.exit(2);
		} catch (MessageHandlingException e) {
			System.err.println("ERROR: Error executing on remote host. Make sure remote host is running. " + e.getCause().toString());
			System.exit(2);
		} 


	}
	
	private static Message<String> getMessage(Parser parser, String target, String payload){
		return (Message<String>) MessageBuilder.withPayload(payload)
				.setHeader("target", target)
				.setHeader("user", parser.getUser())
				.setHeader("password", parser.getPassword())
				.build();
	}
	
	private static String sendMessageAndParseReply(Message<?> m, RmiOutboundGateway gw){
		Message<String> reply = (Message<String>) gw.handleRequestMessage(m);
		if (reply.getHeaders().containsKey("authenticated") && reply.getHeaders().get("authenticated").equals("false")) {
			System.err.println("Failed authentication.");
			System.exit(1);
		}
		if (reply.getHeaders().containsKey("unknown-target") && reply.getHeaders().get("unknown-target").equals("true")) {
			System.err.println("Unknown message target.");
			System.exit(1);
		}
		return reply.getPayload();
	}
	
	public static void listPlugins(JSONObject json) {
		String output = "\n* Registered Wasp System plugins:\n";
		int index = 1;
		List<String> names = new ArrayList<>();
		for (Object key : json.keySet())
			names.add((String) key);
		Collections.sort(names);
		for (String name : names) {
			String description = json.getString(name);
			output += "    " + index++ + " " + name.toString();
			if (description != null && !description.isEmpty()) 
				output += " -> " + description;
			output += "\n";
		}
		System.out.println(output);
	}

	public static void listGenomeBuilds(JSONObject json){
		String output = "\n* List of possible genome builds represented : encoded build -> build description\n";
		List<String> names = new ArrayList<>();
		for (Object key : json.keySet()) 
			names.add((String) key);
		Collections.sort(names);
		for (String name : names) {
			String description = json.getString(name);
			output += "    " + name + " -> " + description + "\n";
		}
		System.out.println(output);
	}
	
	public static void listSampleSubtypes(JSONObject json){
		String output = "\n* List of Sample Subtypes represented: id -> Subtype Name\n";
		List<Integer> ids = new ArrayList<>();
		for (Object key : json.keySet()) 
			ids.add(Integer.parseInt((String) key));
		Collections.sort(ids);
		for (Integer id : ids) {
			String name = json.getString(id.toString());
			output += "    " + id.toString() + " -> " + name + "\n";
		}
		System.out.println(output);
	}
	
	public static void listCellLibraries(JSONObject json){
		String output = "\n* List of Libraries represented: cellLibrary Id -> library name (platform unit name / cell index) :\n";
		List<Integer> ids = new ArrayList<>();
		for (Object key : json.keySet()) 
			ids.add(Integer.parseInt((String) key));
		Collections.sort(ids);
		for (Integer id : ids) {
			String name = json.getString(id.toString());
			output += "    " + id.toString() + " -> " + name + "\n";
		}
		System.out.println(output);
	}

}
