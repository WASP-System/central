package edu.yu.einstein.wasp.cli;


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

