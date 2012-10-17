/**
 * 
 */
package edu.yu.einstein.wasp.mps;

import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.model.Sample;

/**
 * 
 * Interface defines the required methods for a plugin class that handles data from a sequencer.
 * @author calder
 *
 */
public interface SequenceRunProcessor {
	
	
	/**
	 * Called first to set up analysis run.
	 * 
	 * @param platformUnit
	 * @param pathToData
	 * @param outputFolder
	 * @param gws
	 */
	public void preProcess(Sample platformUnit, String pathToData, String outputFolder, GridWorkService gws);
	
	/**
	 * Using the GridWorkService, run the sequence analysis post-processing steps on the specified host in
	 * the specified folder.  
	 *  
	 * @param platformUnit 
	 * @param pathToData
	 * @param outputFolder
	 * @param gws
	 */
	public void processSequenceRun(Sample platformUnit, String pathToData, String outputFolder, GridWorkService gws);
	
	
	/**
	 * completion/clean up phase of analysis
	 * 
	 * @param platformUnit
	 * @param pathToData
	 * @param outputFolder
	 * @param gws
	 */
	public void postProcess(Sample platformUnit, String pathToData, String outputFolder, GridWorkService gws);
	
	/**
	 * ensure completion of coping results files (FASTQ, instrument specific files, etc.) to the staging area.
	 * 
	 * @param platformUnit
	 * @param pathToData
	 * @param outputFolder
	 * @param gws
	 */
	public void stage(Sample platformUnit, String pathToData, String outputFolder, GridWorkService gws);

}
