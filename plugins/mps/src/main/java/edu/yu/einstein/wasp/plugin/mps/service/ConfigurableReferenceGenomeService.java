/**
 * 
 */
package edu.yu.einstein.wasp.plugin.mps.service;

import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.plugin.mps.GenomeIndexConfiguration;
import edu.yu.einstein.wasp.plugin.mps.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;

/**
 * @author calder
 *
 */
public interface ConfigurableReferenceGenomeService extends ReferenceGenomeService {
	
	/**
	 * Look for (and build if necessary) the genome for a given reference based aligner on a given GridWorkService.
	 * This method preforms indexing in the default remote metadata location and is shared between all jobs.
	 * If the genome index is not built, but can be, return GenomeIndexStatus.BUILDING, launch the build job
	 * and return immediately.
	 * 
	 * This particular method allows for an implementation specific Map of configuration settings to specialize the genome index.
	 * 
	 * This method must be called in a singleton service and be synchronized.
	 * 
	 * @param workService
	 * @param build
	 * @param config
	 * @return
	 */
	public GenomeIndexStatus getGenomeIndexStatus(GridWorkService workService, Build build, GenomeIndexConfiguration<String,String> config);
	
	/**
	 * Look for (and build if necessary) the genome for a given reference based aligner on a given GridWorkService.
	 * It will create the index in the directory indicated by the pathToDirectory variable (usually intended to a 
	 * temporary index).
	 * This method preforms indexing in the default remote metadata location and is shared between all jobs.
	 * If the genome index is not built, but can be, return GenomeIndexStatus.BUILDING, launch the build job
	 * and return immediately.
	 * 
	 * This particular method allows for an implementation specific Map of configuration settings to specialize the genome index.
	 * 
	 * This method must be called in a singleton service and be synchronized.
	 *
	 * @param workService
	 * @param pathToDirectory
	 * @param build
	 * @param config
	 * @return
	 */
	public GenomeIndexStatus getGenomeIndexStatus(GridWorkService workService, String pathToDirectory, Build build, GenomeIndexConfiguration<String,String> config);

}
