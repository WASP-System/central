package edu.yu.einstein.wasp.chipseq.software;


import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
//import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;

/**
 * @author dubin, based on Lulu's gatk plugin and brent's bwa plugin
 *
 */
public class ChipSeqSoftwareComponent extends SoftwarePackage {
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private GenomeService genomeService;
	
	
	@Autowired
	private RunService runService;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6631761128215948999L;
	
	public ChipSeqSoftwareComponent() {
	}
	
}


