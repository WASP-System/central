package edu.yu.einstein.wasp.cli;

import edu.yu.einstein.wasp.cli.EchoService;

/**
 * 
 * @author calder
 *
 */
public class EchoService {
	
	public String echo(String message) {
		System.out.println(message);
		return "success";
	}

}

