/**
 * 
 */
package edu.yu.einstein.wasp.mps.illumina;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.cli.ClientMessageI;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.service.RunService;

/**
 * ServiceActivatior to handle messages coming into the Illumina Plugin.
 * 
 * @author calder
 *
 */
@Component
public class IlluminaService implements ClientMessageI {
	
	private static Log logger = LogFactory.getLog(IlluminaService.class);
	
	@Autowired
	private GridHostResolver waspGridHostResolver;
	
	@Autowired
	private GridFileService waspGridFileService;
	
	@Autowired
	private RunService runService;
	
	@Autowired
	private IlluminaSequenceRunProcessor illuminaSequenceRunProcessor;

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.cli.ClientMessageI#process(org.springframework.integration.Message)
	 */
	@Override
	public Message process(Message m) throws RemoteException {
		if (m.getPayload().toString().equals("help"))
			return help();
		try {
			JSONObject jo = new JSONObject(m.getPayload().toString());
			
			if (! jo.has("task")) return MessageBuilder.withPayload("\"task\" is required").build();
			if (! jo.has("id")) return MessageBuilder.withPayload("\"id\" is required").build();
			
			String task = (String) jo.get("task");
			String id = (String) jo.get("id");
			
			if (task.equals("bcl2fastq")) {
				try {
					illuminaSequenceRunProcessor.preProcess(runService.getRunByName(id), waspGridHostResolver);
				} catch (GridUnresolvableHostException e) {
					logger.error("Unable to locate the correct host to process bcl2fastq: " + e.getMessage());
					return MessageBuilder.withPayload("Unable to locate the correct host to process bcl2fastq").build();
				} catch (GridAccessException e) {
					logger.error("There was an error in grid submission: " + e.getMessage());
					return MessageBuilder.withPayload("There was an error in grid submission.").build();
				} catch (GridExecutionException e) {
					logger.error("There was a failure in generating sample sheet: " + e.getMessage());
					return MessageBuilder.withPayload("There was a failure in generating sample sheet.").build();
				}
			} else {
				return MessageBuilder.withPayload("Unknown task \"" + task + "\"" ).build();
			}
			
		} catch (JSONException e) {
			return MessageBuilder.withPayload("Unable to parse request").build();
		}
		
		return null;
	}
	
	private Message<String> help() {
		String mstr = "\nIllumina CASAVA Pipeline plugin\n" +
						"-------------------------------\n" +
						"java -jar wasp -t illumina -m \'{\"task\":\"bcl2fastq\",\"id\":\"run_id\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

}
