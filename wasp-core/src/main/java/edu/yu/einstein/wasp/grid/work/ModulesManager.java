/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * Called to get the configuration string for loading software modules.
 * 
 * @author calder
 * 
 */
public class ModulesManager extends HashMap<String, String> implements
		SoftwareManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5307186653702281648L;

	private Logger logger = LoggerFactory.getLogger(ModulesManager.class);

	// wasp instance properties
	private Properties waspSiteProperties;

	private String name;

	/**
	 * 
	 */
	public ModulesManager(String name, Properties waspSiteProperties) {
		this.name = name;
		this.waspSiteProperties = waspSiteProperties;
		String prefix = this.name + ".software.";
		for (String key : this.waspSiteProperties.stringPropertyNames()) {
			if (key.startsWith(prefix)) {
				String newKey = key.replaceFirst(prefix, "");
				String value = this.waspSiteProperties.getProperty(key);
				this.put(newKey, value);
				logger.debug("Configured software property for host \""
						+ this.name + "\": " + newKey + "=" + value);
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.work.SoftwareManager#getConfiguration(edu.yu.einstein.wasp.grid.work.WorkUnit)
	 */
	@Override
	public String getConfiguration(WorkUnit w) throws GridExecutionException {
		String result = "";
		
		// configure software dependencies that are loaded via modules
		for (SoftwarePackage sw : w.getSoftwareDependencies()) {

			String name = sw.getSoftwareName();
			String remoteName = name;
			// if the name is configured on the host, override the software
			// package's name
			if (this.containsKey(name + ".name"))
				remoteName = this.get(name + ".name");

			String version = sw.getSoftwareVersion();
			// if the version is configured on the host, force that version.
			if (this.containsKey(name + ".version"))
				version = this.get(name + ".version");
			
			if (version == null) {
				logger.error("No version has been set for software: " + name );
				throw new GridExecutionException("Unable to configure software, no version set for " + name );
			}

			result += new StringBuilder().append(
					"module load " + remoteName + "/" + version + "\n").toString();
		}
		result += "echo #### begin modules info 1>&2\n" + 
				"module list\n" + 
				"echo #### end modules info 1>&2\n\n";
		
		// configure the number of processes that will be used.
		// TODO: clean up this logic
		int procs = 0;
		for (SoftwarePackage sw : w.getSoftwareDependencies()) {
			if (w.getProcessMode().equals(ProcessMode.SINGLE)) {
				procs++;
				break;
			}
			if (w.getProcessMode().equals(ProcessMode.FIXED) || w.getProcessMode().equals(ProcessMode.MAX)) {
				procs = w.getProcessorRequirements().intValue();
				break;
			}
			
			String pkey = sw.getSoftwareName() + ".env.processors";
			if (w.getProcessMode().equals(ProcessMode.SUM)) {
				
				// With this logic, SUM could exceed absolute maxiumum
				// this application is made after setting by GridWorkService
				
				if (this.containsKey(pkey)) {
					Integer p = new Integer(this.get(pkey));
					procs += p.intValue();
				} else {
					procs++;
				}
			}
			
			w.setProcessorRequirements(new Integer(procs));
		}
		
		logger.debug("Configured work unit with mode " + w.getProcessMode().toString() + " and " + procs + " processors.");
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.work.SoftwareManager#getConfiguredSetting(java.lang.String)
	 */
	@Override
	public String getConfiguredSetting(String key) {
		if (this.containsKey(key)) {
			return this.get(key);
		}
		return null;
	}

}
