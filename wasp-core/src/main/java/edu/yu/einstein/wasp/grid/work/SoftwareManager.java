/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

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
	public String getConfiguration(WorkUnit w) throws GridExecutionException;
	
	/**
	 * On setup, configure software manager.  Should log events and report failures.
	 * @param s
	 */
	public void configure(String s);

}
