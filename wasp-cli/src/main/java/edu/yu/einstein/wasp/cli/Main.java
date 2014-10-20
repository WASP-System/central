/**
 * 
 */
package edu.yu.einstein.wasp.cli;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.cli.CommandLine;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.rmi.RmiOutboundGateway;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.remoting.RemoteLookupFailureException;

import edu.yu.einstein.wasp.interfacing.plugin.cli.CliMessagingTask;

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
			if (cl.hasOption("l") || cl.hasOption("g") || cl.hasOption("G") || cl.hasOption("i")){
				if (cl.hasOption("g")){
					try{
						Path path = Paths.get(cl.getOptionValue("g"));
						TemplateFileHandler.createTemplateFile(path, true);
					} catch (InvalidPathException e){
						logger.debug("ERROR: not a valid path " + e.getMessage());
						System.exit(2);
					} catch (IOException e1){
						logger.debug("ERROR: " + e1.getMessage());
						System.exit(2);
					}
				} else if (cl.hasOption("G")){
					try{
						Path path = Paths.get(cl.getOptionValue("G"));
						TemplateFileHandler.createTemplateFile(path, false);
					} catch (InvalidPathException e){
						logger.debug("ERROR: not a valid path " + e.getMessage());
						System.exit(2);
					} catch (IOException e1){
						logger.debug("ERROR: " + e1.getMessage());
						System.exit(2);
					}
				} else if (cl.hasOption("i")){
					try{
						Path path = Paths.get(cl.getOptionValue("i"));
						List<List<String>> data = TemplateFileHandler.importTemplateFileData(path);
						JSONObject jsonObj = new JSONObject();
						
						// cannot simply add nested list to JSONObject constructor. Fails to work so follwing code required:
						Integer count = 0;
						for (List<String> list : data)
							jsonObj.put((count++).toString(), list);
						
						message = getMessage(parser, "cli", jsonObj.toString());
						String result = sendMessageAndParseReply(message, gw);
						logger.debug(result);
						
					} catch (InvalidPathException e){
						logger.debug("ERROR: not a valid path " + e.getMessage());
						System.exit(2);
					} catch (IOException e1){
						logger.debug("ERROR: " + e1.getMessage());
						System.exit(2);
					}
				} 
				if (cl.hasOption("l")) {
					boolean match = false;
					String listOption = cl.getOptionValue("l", "plugins");
					if (listOption.contains("plugins")) {
						message = getMessage(parser, "cli", CliMessagingTask.LIST_PLUGINS);
						listPlugins(new JSONObject(sendMessageAndParseReply(message, gw)));
						match = true;
					} 
					if (listOption.contains("builds")){
						message = getMessage(parser, "cli", CliMessagingTask.LIST_GENOME_BUILDS);
						listGenomeBuilds(new JSONObject(sendMessageAndParseReply(message, gw)));
						match = true;
					} 
					if (listOption.contains("sampleSubtypes")){
						message = getMessage(parser, "cli", CliMessagingTask.LIST_SAMPLE_SUBTYPES); 
						listSampleSubtypes(new JSONObject(sendMessageAndParseReply(message, gw)));
						match = true;
					} 
					if (listOption.contains("cellLibraries")){
						message = getMessage(parser, "cli", CliMessagingTask.LIST_CELL_LIBRARIES); 
						listCellLibraries(new JSONObject(sendMessageAndParseReply(message, gw)));
						match = true;
					}
					if (listOption.contains("fileTypes")){
						message = getMessage(parser, "cli", CliMessagingTask.LIST_FILE_TYPES); 
						listFileTypes(new JSONObject(sendMessageAndParseReply(message, gw)));
						match = true;
					}
					if (listOption.contains("workflows")){
						message = getMessage(parser, "cli", CliMessagingTask.LIST_ASSAY_WORKFLOWS); 
						listWorkflows(new JSONObject(sendMessageAndParseReply(message, gw)));
						match = true;
					}
					if (listOption.contains("users")){
						message = getMessage(parser, "cli", CliMessagingTask.LIST_USERS); 
						listUsers(new JSONObject(sendMessageAndParseReply(message, gw)));
						match = true;
					}
					if (listOption.contains("software")){
						message = getMessage(parser, "cli", CliMessagingTask.LIST_SOFTWARE); 
						listSoftware(new JSONObject(sendMessageAndParseReply(message, gw)));
						match = true;
					}
					if (!match) {
						System.err.println("ERROR: unknown list option value '" + listOption + "'");
						parser.formatHelp();
						System.exit(2);
					}
				} 
			}else {
				String mp = "";
				if (cl.hasOption("m")) mp = cl.getOptionValue("m");
				MessageBuilder<String> m = MessageBuilder.withPayload(mp)
				        .setHeader("user", parser.getUser())
				        .setHeader("password", parser.getPassword());
				
				if(cl.hasOption("T")) m.setHeader("target", "plugin." + cl.getOptionValue("T"));
				if(cl.hasOption("t")) m.setHeader("task", cl.getOptionValue("t"));
				if(cl.hasOption("h")) m.setHeader("help", "true");
				
				message = m.build();
				logger.debug(sendMessageAndParseReply(message, gw));
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
	
	private static void listPlugins(JSONObject json) {
		String output = "\n* Registered Wasp System plugins\n";
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
		logger.debug(output);
	}

	private static void listGenomeBuilds(JSONObject json){
		String output = "\n* List of possible genome builds [represented : build string -> build description]\n";
		List<String> names = new ArrayList<>();
		for (Object key : json.keySet()) 
			names.add((String) key);
		Collections.sort(names);
		for (String name : names) {
			String description = json.getString(name);
			output += "    " + name + " -> " + description + "\n";
		}
		logger.debug(output);
	}
	
	private static void listSampleSubtypes(JSONObject json){
		String output = "\n* List of sample subtypes [represented: sampleSubtype id -> Sample Subtype Name]\n";
		List<Integer> ids = new ArrayList<>();
		for (Object key : json.keySet()) 
			ids.add(Integer.parseInt((String) key));
		Collections.sort(ids);
		for (Integer id : ids) {
			String name = json.getString(id.toString());
			output += "    " + id.toString() + " -> " + name + "\n";
		}
		logger.debug(output);
	}
	
	private static void listCellLibraries(JSONObject json){
		String output = "\n* List of libraries [represented: cellLibrary id -> library name / library UUID (platform unit name / cell index) ]\n";
		List<Integer> ids = new ArrayList<>();
		for (Object key : json.keySet()) 
			ids.add(Integer.parseInt((String) key));
		Collections.sort(ids);
		for (Integer id : ids) {
			String name = json.getString(id.toString());
			output += "    " + id.toString() + " -> " + name + "\n";
		}
		logger.debug(output);
	}
	
	private static void listFileTypes(JSONObject json){
		String output = "\n* List of file types [represented: fileType id -> name]\n";
		List<Integer> ids = new ArrayList<>();
		for (Object key : json.keySet()) 
			ids.add(Integer.parseInt((String) key));
		Collections.sort(ids);
		for (Integer id : ids) {
			String name = json.getString(id.toString());
			output += "    " + id.toString() + " -> " + name + "\n";
		}
		logger.debug(output);
	}
	
	private static void listWorkflows(JSONObject json){
		String output = "\n* List of workflows [represented: workflow id -> name]\n";
		List<Integer> ids = new ArrayList<>();
		for (Object key : json.keySet()) 
			ids.add(Integer.parseInt((String) key));
		Collections.sort(ids);
		for (Integer id : ids) {
			String name = json.getString(id.toString());
			output += "    " + id.toString() + " -> " + name + "\n";
		}
		logger.debug(output);
	}
	
	private static void listSoftware(JSONObject json){
		String output = "\n* List of software [represented: software id -> name]\n";
		List<Integer> ids = new ArrayList<>();
		for (Object key : json.keySet()) 
			ids.add(Integer.parseInt((String) key));
		Collections.sort(ids);
		for (Integer id : ids) {
			String name = json.getString(id.toString());
			output += "    " + id.toString() + " -> " + name + "\n";
		}
		logger.debug(output);
	}
	
	private static void listUsers(JSONObject json){
		String output = "\n* List users [represented: user id -> user name (lab)] (ordered by user name)\n";
		Map<String, String> usersTreeMap = new TreeMap<>();
		for (Object key : json.keySet()) 
			usersTreeMap.put(json.getString(key.toString()), key.toString());
		for (String details : usersTreeMap.keySet()) {
			output += "    " + usersTreeMap.get(details) + " -> " + details + "\n";
		}
		logger.debug(output);
	}
	
	public static boolean isPareseableToInteger(String s){
		try{
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e){
			return false;
		}
	}

}
