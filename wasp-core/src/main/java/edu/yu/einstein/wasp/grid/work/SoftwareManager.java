/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.util.Set;

import edu.yu.einstein.wasp.grid.GridExecutionException;

/**
 * @author calder
 *
 */
public interface SoftwareManager {
	
	/**
	 * Get configuration string from software manager.  For example, if the Modules manager determines
	 * that a WorkUnit uses bzip2 and BWA, it might return the string "module load bzip2/v1\nmodule load bwa/v1".
	 * @param w
	 * @return
	 */
	public abstract String getConfiguration(WorkUnit w) throws GridExecutionException;
	
	/**
	 * Get configured software properties.
	 * @param s
	 */
	public abstract String getConfiguredSetting(String key);
	
	/**
	 * Return a string representation of a software configuration.
	 * @param data
	 * @return
	 */
	public Set<String> parseSoftwareListFromText(String data);

}
