package edu.yu.einstein.wasp.plugin.fileformat.service;

import java.util.UUID;

import edu.yu.einstein.wasp.exception.InvalidFileTypeException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Sample;

public interface FastqService extends FileTypeService {
	
	public static final String FILE_AREA = "fastqFile";
	
	public static final String FASTQ_READ_SEGMENT_NUMBER = "readSegmentNumber";
	public static final String FASTQ_NUMBER_OF_READS = "numberOfReads";
	public static final String FASTQ_NUMBER_OF_PASS_FILTER_READS = "numberOfPFReads";
	public static final String FASTQ_CONTAINS_FAILED_READS = "containsFailed";
	public static final String FASTQ_LIBRARY_UUID = "libraryUUID";
	
	public static final String FASTQ_GROUP_NUMBER_OF_READ_SEGMENTS = "numberOfReadSegments";
	
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
	
	public Integer getNumberOfReadSegments(FileGroup filegroup);
	
	public FileType getFastqFileType();
	
	public void setFastqReadSegmentNumber(FileHandle file, Integer number) throws InvalidFileTypeException, MetadataException;

	public void setFastqNumberOfReads(FileHandle file, Integer number) throws InvalidFileTypeException, MetadataException;

	public void setFastqNumberOfPassFilterReads(FileHandle file, Integer number) throws MetadataException;

	public void setContainsFailed(FileHandle file, boolean fail) throws MetadataException;

	public void setLibraryUUID(FileHandle file, Sample library) throws MetadataException;

	public void setNumberOfReadSegments(FileGroup filegroup, Integer number) throws MetadataException;

	public void copyFastqFileGroupMetadata(FileGroup origin, FileGroup target) throws MetadataException;

	public void copyFastqFileHandleMetadata(FileHandle origin, FileHandle target) throws MetadataException;

}
