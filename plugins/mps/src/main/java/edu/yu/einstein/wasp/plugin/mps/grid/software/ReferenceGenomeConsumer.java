/**
 * 
 */
package edu.yu.einstein.wasp.plugin.mps.grid.software;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Genome;

/**
 * @author calder
 *
 */
public interface ReferenceGenomeConsumer {
	
	public Genome getGenome(String genome);
	
	/**
	 * Test to see if this reference based aligner has built a version of its index for the reference genome 
	 * @param genome
	 * @return
	 */
	public boolean genomeExists(Genome genome);
	
	/**
	 * Build the reference genome index for this aligner
	 * @param genome
	 * @throws WaspException
	 */
	public void buildGenome(Genome genome) throws WaspException;

	/**
	 * has associated metadata been built?
	 * @param genome
	 * @return
	 */
	public boolean metadataExists(Genome genome);
	
	/**
	 * Has metadata been configured for this genome?  Use metadataExists() to test if metadata has been staged.
	 * @param genome
	 * @return
	 */
	public boolean metadataAvailable(Genome genome);
	
	/**
	 * Obtains information about metadata files from genome metadata.  If available, files are downloaded,
	 * staged and processed.
	 * @param genome
	 * @throws WaspException
	 */
	public void buildMetadata(Genome genome) throws WaspException;
	
}
