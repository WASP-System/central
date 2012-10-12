/**
 * 
 */
package edu.yu.einstein.wasp.mps.illumina;

import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.Message;

import edu.yu.einstein.wasp.cli.ClientMessageI;
import edu.yu.einstein.wasp.mps.SequenceRunProcessor;

/**
 * ServiceActivatior to handle messages coming into the Illumina Plugin.
 * 
 * @author calder
 *
 */
public class IlluminaService implements ClientMessageI {
	
	private static Log logger = LogFactory.getLog(IlluminaService.class);

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.cli.ClientMessageI#process(org.springframework.integration.Message)
	 */
	@Override
	public Message process(Message m) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
