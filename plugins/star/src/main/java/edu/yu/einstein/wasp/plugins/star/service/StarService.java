/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugins.star.service;

import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.plugin.mps.service.ConfigurableReferenceGenomeService;
import edu.yu.einstein.wasp.plugins.star.StarGenomeIndexConfiguration;

/**
 * 
 * STAR genome building occurs in two phases, normal system-wide genome building
 * and then a per-comparison secondary build. It utilizes a
 * {@link GenomeIndexConfiguration} object to hold key-value pairs necessary for
 * sending information necessary for the build.
 * 
 * The first phase requires a build GTF file relative to the FASTA reference
 * genome.
 * 
 * The following GenomeIndexConfiguration key-value pairs are valid
 * 
 * twoPass : required, true or false sjdbOverhang : required, in base pairs
 * (read-length - 1). If it is not present or set to 0, splice junction
 * annotation will be disabled. gtfVersion : recommended. If not set and
 * two-pass is true, this will be set to the value marked as default in the
 * genomes.properties file.
 * 
 * @author calder
 * 
 */
public interface StarService extends ConfigurableReferenceGenomeService {
	
	/**
	 * Perform Service
	 * 
	 * @return String
	 */
	public String performAction();
	
	public String getPrefixedStarIndexPath(GridWorkService workService, StarGenomeIndexConfiguration config) throws ParameterValueRetrievalException;

}
