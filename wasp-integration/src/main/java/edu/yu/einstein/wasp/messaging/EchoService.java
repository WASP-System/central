package edu.yu.einstein.wasp.messaging;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.springframework.integration.Message;
import edu.yu.einstein.wasp.messaging.ClientMessageI;

/**
 * 
 * @author calder
 *
 */
public class EchoService implements ClientMessageI {
	
	private static final Logger logger = Logger.getLogger(EchoService.class);

	@Override
	public Message process(Message message) throws RemoteException {
		logger.info(message);
		return message;
	}

}
