/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;

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

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.work.SoftwareManager#getConfiguration(edu.yu.einstein.wasp.grid.work.WorkUnit)
	 */
	@Override
	public String getConfiguration(WorkUnit w) throws GridExecutionException {
		String result = "";
		
		// configure software dependencies that are loaded via modules
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
		result += "module list" + "\n\n";
		
		// configure the number of processes that will be used.
		int procs = 1;
		for (SoftwareComponent sw : w.getSoftwareDependencies()) {
			if (w.getProcessMode().equals(ProcessMode.SINGLE)) break;
			if (w.getProcessMode().equals(ProcessMode.FIXED)) {
				procs = w.getProcessorRequirements().intValue();
				break;
			}
			String pkey = sw.getName() + ".env.processors";
			if (w.getProcessMode().equals(ProcessMode.MAX)) {
				if (this.containsKey(pkey)) {
					Integer p = new Integer(this.get(pkey));
					if (p > procs) procs = p.intValue();
				}
			}
			
			// sum mode leaves "one for the pot"
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
		
		return result;
	}

	@Override
	public void configure(String s) {
		// TODO Auto-generated method stub

	}

}
