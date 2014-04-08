package edu.yu.einstein.wasp.integration.messaging;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;

import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;

/**
 * 
 * @author calder
 *
 */
public class EchoService implements ClientMessageI {
	
	private Logger logger = LoggerFactory.getLogger(EchoService.class);

	@Override
	public Message<?> process(Message<?> message) throws RemoteException {
		logger.info(message.toString());
		return message;
	}

}
