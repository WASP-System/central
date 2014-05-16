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
	
	public abstract String getStageDirectoryName(); 

}
