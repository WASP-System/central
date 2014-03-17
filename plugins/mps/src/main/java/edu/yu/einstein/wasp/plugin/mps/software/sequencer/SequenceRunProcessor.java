/**
 * 
 */
package edu.yu.einstein.wasp.plugin.mps.software.sequencer;

import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * 
 * Interface defines the required methods for a plugin class that handles data from a sequencer.
 * @author calder
 *
 */
public abstract class SequenceRunProcessor extends SoftwarePackage {

	private static final long serialVersionUID = 8608969134602464549L;
	
	
//	/**
//	 * Called first to set up analysis run.
//	 * 
//	 * @param platformUnit
//	 * @param ghs
//	 * @throws GridExecutionException 
//	 * @throws GridAccessException 
//	 * @throws GridUnresolvableHostException 
//	 */
//	public abstract void preProcess(Run run, GridHostResolver ghs) throws GridUnresolvableHostException, GridAccessException, GridExecutionException;
//	
//	/**
//	 * Using the GridWorkService, run the sequence analysis post-processing steps on the specified host in
//	 * the specified folder.  
//	 *  
//	 * @param platformUnit
//	 * @param ghs
//	 * @throws GridExecutionException 
//	 * @throws GridAccessException 
//	 * @throws GridUnresolvableHostException 
//	 */
//	public abstract void processSequenceRun(Run run, GridHostResolver ghs) throws GridUnresolvableHostException, GridAccessException, GridExecutionException;
//	
//	
//	/**
//	 * completion/clean up phase of analysis
//	 * 
//	 * @param platformUnit
//	 * @param ghs
//	 * @throws GridExecutionException 
//	 * @throws GridAccessException 
//	 * @throws GridUnresolvableHostException 
//	 */
//	public abstract void postProcess(Run run, GridHostResolver ghs) throws GridUnresolvableHostException, GridAccessException, GridExecutionException;
//	
//	/**
//	 * ensure completion of coping results files (FASTQ, instrument specific files, etc.) to the staging area.
//	 * 
//	 * @param platformUnit
//	 * @param ghs
//	 */
//	public abstract void stage(Run run, GridHostResolver ghs) throws GridUnresolvableHostException, GridAccessException, GridExecutionException;

}
