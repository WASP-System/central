/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.grid.GridExecutionException;

/**
 * Called to get the configuration string for loading software modules.
 * 
 * @author calder
 * 
 */
public class ModulesManager extends HashMap<String, String> implements
		SoftwareManager {

	private static final Logger logger = Logger.getLogger(SgeWorkService.class);

	// wasp instance properties
	@Autowired
	private Properties waspLocalProperties;

	private String name;

	/**
	 * 
	 */
	public ModulesManager(String name) {
		this.name = name;
		String prefix = this.name + ".software.";
		for (String key : waspLocalProperties.stringPropertyNames()) {
			if (key.startsWith(prefix)) {
				String newKey = key.replaceFirst(prefix, "");
				String value = waspLocalProperties.getProperty(key);
				this.put(newKey, value);
				logger.debug("Configured software property for host \""
						+ this.name + "\": " + newKey + "=" + value);
			}
		}
	}

	@Override
	public String getConfiguration(WorkUnit w) throws GridExecutionException {
		String result = "";
		for (SoftwareComponent sw : w.getSoftwareDependencies()) {

			String name = sw.getName();
			// if the name is configured on the host, override the software
			// package's name
			if (this.containsKey(sw.getName() + ".name"))
				name = sw.getName();

			String version = sw.getVersion();
			// if the version is configured on the host, force that version.
			if (this.containsKey(sw.getVersion() + ".version"))
				version = sw.getName();
			
			if ((name == null) || (version == null)) {
				logger.error("null value in software configuration: " + name + "/" + version);
				throw new GridExecutionException("unable to configure software");
			}

			result += new StringBuilder().append(
					"module load " + name + "/" + version + "\n").toString();
		}
		result += "module list" + "\n";
		return result;
	}

	@Override
	public void configure(String s) {
		// TODO Auto-generated method stub

	}

}
