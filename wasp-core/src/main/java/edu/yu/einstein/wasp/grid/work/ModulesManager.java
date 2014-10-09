/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * Called to get the configuration string for loading software modules.
 * 
 * @author calder
 * 
 */
public class ModulesManager extends HashMap<String, String> implements SoftwareManager {

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

	/**
	 * {@inheritDoc}
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
		if (w.getMode().equals(ExecutionMode.TASK_ARRAY))
			result += "if [ \"$" + WorkUnit.TASK_ARRAY_ID + "\" -eq \"1\" ]; then\n";
		result += "module list 2> ${" + WorkUnit.JOB_NAME + "}.sw\n";
		if (w.getMode().equals(ExecutionMode.TASK_ARRAY))
			result +="fi\n";
		
		
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getConfiguredSetting(String key) {
		if (this.containsKey(key)) {
			return this.get(key);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * In this case output of executing 'module list' at the command line:
	 * <pre>Currently Loaded Modulefiles:
  	 * 1) intel/11.1.072            3) mysql/intel/5.1.41
  	 * 2) gsl/intel/1.15            4) python/intel/2.7.3</pre>
	 */
	@Override
	public Set<String> parseSoftwareListFromText(String data) {
		Set<String> sw = new TreeSet<>();
		for (String line : data.split("\n")){
			line = line.trim();
			if (line.isEmpty() || line.startsWith("Currently Loaded Modulefiles"))
				continue; // filter lines
			String[] elements = line.split("\\s*\\d+\\)\\s+");
			for (String element : elements){
				element = element.trim();
				if (element.isEmpty())
					continue;
				sw.add(element);
			}
		}
		return sw;
	}

}
