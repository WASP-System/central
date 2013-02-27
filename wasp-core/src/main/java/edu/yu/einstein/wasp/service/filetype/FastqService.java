package edu.yu.einstein.wasp.service.filetype;

import edu.yu.einstein.wasp.exception.InvalidFileTypeException;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.service.WaspService;

public interface FastqService extends WaspService {
	
	public static final String FILE_AREA = "fastqFile";
	
	public static final String READ_SEGMENT_NUMBER_META_KEY = "readSegmentNumber";
	public static final String NUMBER_OF_READS_META_KEY = "numberOfReads";
	public static final String NUMBER_OF_PASS_FILTER_READS_META_KEY = "numberOfPFReads";
	public static final String CONTAINS_FAILED_READS_META_KEY = "containsFailed";
	
	public static final String FASTQ_INAME = "fastq";
	
	/**
	 * Get FASTQ read number from a FASTQ file.
	 * Returns null if not found.
	 * @return read number
	 */
	public Integer getFastqReadSegmentNumber(File file) throws InvalidFileTypeException;
	
	public Integer getFastqNumberOfReads(File file) throws InvalidFileTypeException;
	
	public Integer getFastqNumberOfPassFilterReads(File file) throws InvalidFileTypeException;
	
	public boolean isReadsMarkedFailed(File file) throws InvalidFileTypeException;
	
	
	
	
	

}
