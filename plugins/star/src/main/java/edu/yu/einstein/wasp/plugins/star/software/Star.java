/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.plugins.star.software;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.plugin.genomemetadata.service.GenomeMetadataService;
import edu.yu.einstein.wasp.plugin.mps.software.alignment.ReferenceBasedRNASeqAligner;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugins.star.StarGenomeIndexConfiguration;
import edu.yu.einstein.wasp.plugins.star.service.StarService;
import edu.yu.einstein.wasp.service.GenomeService;

/**
 * ReferenceBasedRNASeqAligner implementation of the STAR RNA-seq aligner
 * (https://code.google.com/p/rna-star/).
 * 
 * @author calder
 */
public class Star extends ReferenceBasedRNASeqAligner {

	public Star() {
		setSoftwareVersion("2.4.0d");
	}

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String STAR_INDEX_FLOW = "star.index";

	/**
	 * Key for the 'sjdbOverhang' option when building reference genome (ideally
	 * [read length - 1])
	 */
	public static final String STAR_SJDBOVERHANG_KEY = "sjdbOverhang";

	/**
	 * Key for configuration of GTF file for genome generation. Mutually
	 * exclusive with junctions file
	 */
	public static final String STAR_GENOME_GTF_FILE_KEY = "GTF";

	/**
	 * Key for configuration of junctions file. Mutually exclusive with GTF
	 * file.
	 */
	public static final String STAR_GENOME_JUNCTION_FILE_KEY = "junctions";

	@Autowired
	private GenomeService genomeService;

	@Autowired
	private GenomeMetadataService genomeMetadataService;
	
	@Autowired 
	private StarService starService;

	public String getStarGenomeBuildString(GridWorkService workService, StarGenomeIndexConfiguration config) throws ParameterValueRetrievalException {

		Build build = genomeService.getBuild(config.getOrganism(), config.getGenome(), config.getBuild());

		String command = "STAR --runMode genomeGenerate --runThreadN $" + WorkUnitGridConfiguration.NUMBER_OF_THREADS + " --genomeFastaFiles "
				+ genomeMetadataService.getPrefixedGenomeFastaPath(workService, build) + " --genomeDir " + starService.getPrefixedStarIndexPath(workService, config);

		if (!config.isSecond()) {
			command += " --sjdbGTFfile " + genomeMetadataService.getPrefixedGtfPath(workService, build, config.getGtfVersion()) + " --sjdbOverhang "
					+ config.getSjdbOverhang();
		} else {
			command = "cat " + config.getPathToJunctions() + "* | sort -u -k1,1 -k2,2n -k3,3n -k4,4 - > mergedSjdbFileChrStartEnd.txt \n" + command
					+ " --sjdbFileChrStartEnd mergedSjdbFileChrStartEnd.txt --sjdbOverhang " + config.getSjdbOverhang();
		}
		
		logger.trace(command);

		return command;
	}

}
