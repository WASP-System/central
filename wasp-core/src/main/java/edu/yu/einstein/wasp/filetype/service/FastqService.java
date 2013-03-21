package edu.yu.einstein.wasp.filetype.service;

import java.util.UUID;

import edu.yu.einstein.wasp.exception.InvalidFileTypeException;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.WaspService;

public interface FastqService extends WaspService {
	
	public static final String FILE_AREA = "fastqFile";
	
	public static final String FASTQ_READ_SEGMENT_NUMBER = "readSegmentNumber";
	public static final String FASTQ_NUMBER_OF_READS = "numberOfReads";
	public static final String FASTQ_NUMBER_OF_PASS_FILTER_READS = "numberOfPFReads";
	public static final String FASTQ_CONTAINS_FAILED_READS = "containsFailed";
	public static final String FASTQ_LIBRARY_UUID = "libraryUUID";
	
	public static final String FASTQ_INAME = "fastq";
	
	/**
	 * Get FASTQ read number from a FASTQ file.  For Illumina, 1=F 2=R. 
	 * Returns null if not found.
	 * @param file
	 * @return
	 * @throws InvalidFileTypeException
	 */
	public Integer getFastqReadSegmentNumber(FileHandle file);
	
	/**
	 * returns the number of reads observed in the file
	 * @param file
	 * @return
	 * @throws InvalidFileTypeException
	 */
	public Integer getFastqNumberOfReads(FileHandle file);
	
	/**
	 * returns the sequence number of the file
	 * @param file
	 * @return
	 * @throws InvalidFileTypeException
	 */
	public Integer getFastqFileNumber(FileHandle file);
	
	/**
	 * returns the number of PF reads in the file
	 * @param file
	 * @return
	 * @throws InvalidFileTypeException
	 */
	public Integer getFastqNumberOfPassFilterReads(FileHandle file);
	
	/**
	 * returns boolean representing whether or not the file contains fail filter reads
	 * @param file
	 * @return
	 * @throws InvalidFileTypeException
	 */
	public boolean containsFailed(FileHandle file);
	
	/**
	 * returns the originating library's UUID
	 * @param file
	 * @return
	 * @throws InvalidFileTypeException
	 */
	public UUID getLibraryUUID(FileHandle file);
	
	public Sample getLibraryFromFASTQ(FileHandle file);

}
