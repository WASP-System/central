package edu.yu.einstein.wasp.service.filetype;

import java.util.List;

import edu.yu.einstein.wasp.exception.InvalidFileTypeException;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.WaspService;

public interface FastqService extends WaspService {
	
	/**
	 * Get FASTQ read number from a FASTQ file.
	 * Returns null if not found.
	 * @return read number
	 */
	public Integer getFastqReadSegmentNumber(File file) throws InvalidFileTypeException;
	
	public Integer getFastqNumberOfReads(File file) throws InvalidFileTypeException;
	
	public Integer getFastqNumberOfPassFilterReads(File file) throws InvalidFileTypeException;
	
	public boolean containsReadsMarkedFailed(File file) throws InvalidFileTypeException;
	
	
	
	
	

}
