/**
 * 
 */
package edu.yu.einstein.wasp.filetype;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import edu.yu.einstein.wasp.filetype.service.FastqService;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.service.FileService;

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
	
	@Autowired
	private FileType fastqFileType;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FastqService fastqService;

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.filetype.FileHandleComparator#compare(edu.yu.einstein.wasp.model.FileHandle, edu.yu.einstein.wasp.model.FileHandle)
	 */
	@Override
	public int compare(FileHandle arg0, FileHandle arg1) {
		Assert.isTrue(arg0.getFileType().equals(fastqFileType));
		Assert.isTrue(arg1.getFileType().equals(fastqFileType));
		
		
		
		return new CompareToBuilder()
			.append(fastqService.getLibraryFromFASTQ(arg0).getId(), fastqService.getLibraryFromFASTQ(arg1).getId())
			.append(fastqService.getFastqFileNumber(arg0), fastqService.getFastqFileNumber(arg1))
			.append(fastqService.getFastqReadSegmentNumber(arg0), fastqService.getFastqReadSegmentNumber(arg1))
			.toComparison();
		
	}

}
