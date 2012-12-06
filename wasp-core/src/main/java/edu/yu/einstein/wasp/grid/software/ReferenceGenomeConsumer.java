/**
 * 
 */
package edu.yu.einstein.wasp.grid.software;

import edu.yu.einstein.wasp.exception.WaspException;

/**
 * @author calder
 *
 */
public interface ReferenceGenomeConsumer {
	
	/**
	 * Test to see if this reference based aligner has built a version of its index for the reference genome 
	 * @param genome
	 * @return
	 */
	public boolean genomeExists(String genome);
	
	/**
	 * Build the reference genome index for this aligner
	 * @param genome
	 * @throws WaspException
	 */
	public void buildGenome(String genome) throws WaspException;

	/**
	 * has associated metadata been built?
	 * @param genome
	 * @return
	 */
	public boolean metadataExists(String genome);
	
	/**
	 * Has metadata been configured for this genome?  Use metadataExists() to test if metadata has been staged.
	 * @param genome
	 * @return
	 */
	public boolean metadataAvailable(String genome);
	
	/**
	 * Obtains information about metadata files from genome metadata.  If available, files are downloaded,
	 * staged and processed.
	 * @param genome
	 * @throws WaspException
	 */
	public void buildMetadata(String genome) throws WaspException;
	
}
