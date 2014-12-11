package edu.yu.einstein.wasp.software;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.model.Software;

/**
 * Simple class to bind software to its parameters.
 * @author asmclellan
 *
 */
public class SoftwareConfiguration implements Serializable {
	
	private static final Logger logger = LoggerFactory.getLogger(SoftwareConfiguration.class);

	private static final long serialVersionUID = -2582562680785424878L;

	private Map<String, String> parameterMap;
	
	private Software software;
	
	public SoftwareConfiguration(Software software) {
		logger.debug("Creating for SoftwareConfiguration for software with iname=" + software.getIName());
		this.software = software;
		this.parameterMap = new HashMap<String, String>();
	}
	
	public SoftwareConfiguration(Software software, Map<String, String> parameterMap) {
		logger.debug("Creating for SoftwareConfiguration for software with iname=" + software.getIName() + " and parameters: " + parameterMap );
		this.software = software;
		this.parameterMap = parameterMap;
	}
	
	public Map<String, String> getParameters() {
		return this.parameterMap;
	}
	
	public String getParameter(String name) {
		return this.parameterMap.get(name);
	}

	public void setParameters(Map<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}
	
	public Software getSoftware(){
		return this.software;
	}
	
	public void putParameter(String name, String value){
		this.parameterMap.put(name, value);
	}
	
	public void removeParameter(String name){
		if (this.parameterMap.containsKey(name))
			this.parameterMap.remove(name);
	}
	
}