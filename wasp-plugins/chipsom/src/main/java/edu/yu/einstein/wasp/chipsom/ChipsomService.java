/**
 * 
 */
package edu.yu.einstein.wasp.chipsom;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.MessageBuilder;

import org.springframework.integration.Message;

import edu.yu.einstein.wasp.cli.ClientMessageI;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridResultImpl;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.messaging.EchoService;

/**
 * @author calder
 *
 */
public class ChipsomService implements ClientMessageI {
	
	@Autowired
	private GridWorkService waspGridWorkService;
	
	@Autowired
	private GridFileService waspGridFileService;
	
	@Value("${chipsom.init}")
	public String chipsomInit;
	
	@Value("${chipsom.exec}")
	public String chipsomExec;
	
	private static final Logger logger = Logger.getLogger(ChipsomService.class);

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.cli.ClientMessageI#process(org.springframework.integration.Message)
	 */
	@Override
	public Message process(Message m) throws RemoteException {
		if (m.getPayload().toString().equals("help"))
			return help();
		
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory("chipsom");
		w.setProcessorRequirements(72);
		w.setMode(ExecutionMode.MPI);
		w.setCommand(chipsomInit);
		w.addCommand(chipsomExec);
		GridResult g = new GridResultImpl();
		try {
			g = waspGridWorkService.execute(w);
			Thread.sleep(1000);
			logger.info(g.toString());
			return MessageBuilder.withPayload(g.toString()).build();
		} catch (GridAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteException(e.getCause().toString());
		} catch (GridUnresolvableHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteException(e.getCause().toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteException(e.getCause().toString());
		}
		
		
	}
	
	private Message<String> help() {
		String mstr = "\nExecute the chipsom pipeline\n" +
						 "----------------------------\n" +
						 "-t chipsom -m \"{input=/path/to/file,genome=hg19}\"\n";
		Message<String> help = MessageBuilder.withPayload(mstr)
				.build();
				
		return help;
	}

}
