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

			String name = sw.getArea();
			// if the name is configured on the host, override the software
			// package's name
			if (this.containsKey(sw.getArea() + ".name"))
				name = this.get(sw.getArea() + ".name");

			String version = sw.getRequestedVersion();
			// if the version is configured on the host, force that version.
			if (this.containsKey(sw.getArea() + ".version"))
				version = this.get(sw.getArea() + ".version");
			
			if (version == null) {
				logger.error("No version has been set for software: " + name );
				throw new GridExecutionException("Unable to configure software, no version set for " + name );
			}

			result += new StringBuilder().append(
					"module load " + name + "/" + version + "\n").toString();
		}
		result += "module list" + "\n\n";
		
		// configure the number of processes that will be used.
		int procs = 0;
		for (SoftwarePackage sw : w.getSoftwareDependencies()) {
			if (w.getProcessMode().equals(ProcessMode.SINGLE)) {
				procs++;
				break;
			}
			if (w.getProcessMode().equals(ProcessMode.FIXED)) {
				procs = w.getProcessorRequirements().intValue();
				break;
			}
			String pkey = sw.getArea() + ".env.processors";
			if (w.getProcessMode().equals(ProcessMode.MAX)) {
				if (this.containsKey(pkey)) {
					Integer p = new Integer(this.get(pkey));
					if (p > procs) procs = p.intValue();
				}
			}
			
			
			if (w.getProcessMode().equals(ProcessMode.SUM)) {
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

	@Override
	public void configure(String s) {
		// TODO Auto-generated method stub

	}

}
