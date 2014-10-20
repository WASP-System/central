package edu.yu.einstein.wasp.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author calder
 *
 */
public class EchoService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String echo(String message) {
		logger.debug(message);
		return "success";
	}

}

