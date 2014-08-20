/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.plugins.star.software;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexConfiguration;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.mps.service.ConfigurableReferenceGenomeService;
import edu.yu.einstein.wasp.plugin.mps.software.alignment.ReferenceBasedRNASeqAligner;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Genome;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.software.SoftwarePackage;


/**
 * ReferenceBasedRNASeqAligner implementation of the STAR RNA-seq aligner (https://code.google.com/p/rna-star/).
 * 
 * @author calder
 */
public class Star extends ReferenceBasedRNASeqAligner implements ConfigurableReferenceGenomeService {
	
	public Star() {
		setSoftwareVersion("2.3.1o"); 
	}
	
	/**
	 * Key for the 'sjdbOverhang' option when building reference genome (ideally [read length - 1]) 
	 */
	public static final String STAR_GENOME_OVERLAP_LENGTH_KEY = "overlap";
	
	/**
	 * Key for configuration of GTF file for genome generation.  Mutually exclusive with junctions file
	 */
	public static final String STAR_GENOME_GTF_FILE_KEY = "GTF";
	
	/**
	 * Key for configuration of junctions file.  Mutually exclusive with GTF file.
	 */
	public static final String STAR_GENOME_JUNCTION_FILE_KEY = "junctions";
	
	@Autowired
	private GenomeService genomeService;

	/** 
	 * {@inheritDoc}
	 * 
	 * Star requires configuration with a overlap length (read length - 1) and either a GTF input file or a juctions file.
	 */
	@Override
	public GenomeIndexStatus getGenomeIndexStatus(GridWorkService workService, Build build, GenomeIndexConfiguration<String, String> config) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * {@inheritDoc}
	 * 
	 * Star requires configuration with a overlap length (read length - 1) and either a GTF input file or a juctions file.
	 */
	@Override
	public GenomeIndexStatus getGenomeIndexStatus(GridWorkService workService, String pathToDirectory, Build build,
			GenomeIndexConfiguration<String, String> config) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
