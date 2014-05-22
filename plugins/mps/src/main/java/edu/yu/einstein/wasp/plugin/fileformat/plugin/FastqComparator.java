/**
 * 
 */
package edu.yu.einstein.wasp.plugin.fileformat.plugin;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import edu.yu.einstein.wasp.filetype.FileHandleComparator;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;

/**
 * FileHandle comparator specifically for ordering FASTQ files.  Will provide a natural order for FASTQ files by:
 * 
 * 1. sample ID
 * 2. file number
 * 3. read segment number
 * 
 * e.g.
 * 
 * 1_R1_001.fq.gz
 * 1_R2_001.fq.gz
 * 1_R1_002.fq.gz
 * 1_R2_002.fq.gz
 * 2_R1_001.fq.gz
 * 2_R2_001.fq.gz
 * 
 * @author calder
 *
 */
public class FastqComparator extends FileHandleComparator {
	
	private FileType fq = null;
	
	private FastqService fastqService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public FastqComparator(FastqService fastqService) {
		this.fastqService = fastqService;
	}
	
	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.filetype.FileHandleComparator#compare(edu.yu.einstein.wasp.model.FileHandle, edu.yu.einstein.wasp.model.FileHandle)
	 */
	@Override
	public int compare(FileHandle arg0, FileHandle arg1) {
		
		if (fq == null)
			fq = fastqService.getFastqFileType();
		Assert.isTrue(arg0.getFileType().equals(fq));
		Assert.isTrue(arg1.getFileType().equals(fq));
		
		return new CompareToBuilder()
			.append(fastqService.getLibraryFromFASTQ(arg0).getId(), fastqService.getLibraryFromFASTQ(arg1).getId())
			.append(fastqService.getFastqFileNumber(arg0), fastqService.getFastqFileNumber(arg1))
			.append(fastqService.getFastqReadSegmentNumber(arg0), fastqService.getFastqReadSegmentNumber(arg1))
			.toComparison();
		
	}

}
