/**
 * 
 */
package edu.yu.einstein.wasp.mps.illumina;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.interfaces.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.service.RunService;

/**
 * ServiceActivatior to handle messages coming into the Illumina Plugin.
 * 
 * @author calder
 *
 */
@Component
public class IlluminaService implements ClientMessageI {
	
	private static Logger logger = LoggerFactory.getLogger(IlluminaService.class);
	
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
			if ((! jo.has("id")) && (! jo.has("name"))) return MessageBuilder.withPayload("\"id\" or \"name\" is required").build();
			
			String task = (String) jo.get("task");
			String id = "";
			String name = "";
			if (jo.has("id")) id = (String) jo.get("id");
			if (jo.has("name")) name = (String) jo.get("name");
			
			Assert.assertParameterNotNull(id);
			Assert.assertParameterNotNull(name);
			
			if (task.equals("bcl2fastq")) {
				try {
					Run run;
					if (id == "") {
						run = runService.getRunByName(name);
					} else { 
						run = runService.getRunDao().findById(new Integer(id));	
					}
					logger.debug("going to try to initiate processing on " + run.getName());
					illuminaSequenceRunProcessor.preProcess(run, waspGridHostResolver);
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
						"wasp -t wasp-illumina -m \'{\"task\":\"bcl2fastq\",\"id\":\"run_id\"}\'\n" +
						"wasp -t wasp-illumina -m \'{\"task\":\"bcl2fastq\",\"name\":\"run_name\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

}
