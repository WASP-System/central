package edu.yu.einstein.wasp.util;

import java.util.Map;

import edu.yu.einstein.wasp.model.Software;

/**
 * Simple class to bind software to its parameters.
 * @author andymac
 *
 */
public class SoftwareConfiguration {

	private Map<String, String> parameterMap;
	
	private Software software;
	
	
	public SoftwareConfiguration(Software software, Map<String, String> parameterMap) {
		this.software = software;
		this.parameterMap = parameterMap;
	}
	
	public Map<String, String> getParameters(){
		return this.parameterMap;
	}
	
	public Software getSoftware(){
		return this.software;
	}
	
}
