package edu.yu.einstein.wasp.plugin.fileformat.service.impl;

import java.util.UUID;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.FileTypeDao;
import edu.yu.einstein.wasp.exception.InvalidFileTypeException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.filetype.service.impl.FileTypeServiceImpl;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.service.SampleService;

@Service
@Transactional("entityManager")
public class FastqServiceImpl extends FileTypeServiceImpl implements FastqService {
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private FileType fastqFileType;
	
	@Autowired
	private FileTypeDao fileTypeDao;

	@Override
	public Integer getFastqReadSegmentNumber(FileHandle file) {
		String metaValue = getMeta(file, FILE_AREA, FASTQ_READ_SEGMENT_NUMBER);
		if (metaValue == null)
			return null;
		return Integer.parseInt(metaValue);
	}

	public void setFastqReadSegmentNumber(FileHandle file, Integer number) throws InvalidFileTypeException, MetadataException {
		setMeta(file, FILE_AREA, FASTQ_READ_SEGMENT_NUMBER, number.toString());
	}

	@Override
	public Integer getFastqNumberOfReads(FileHandle file) {
		String metaValue = getMeta(file, FILE_AREA, FASTQ_NUMBER_OF_READS);
		if (metaValue == null)
			return null;
		return Integer.parseInt(metaValue);
	}
	
	public void setFastqNumberOfReads(FileHandle file, Integer number) throws InvalidFileTypeException, MetadataException {
		setMeta(file, FILE_AREA, FASTQ_NUMBER_OF_READS, number.toString());
	}

	@Override
	public Integer getFastqNumberOfPassFilterReads(FileHandle file) {
		String metaValue = (getMeta(file, FILE_AREA, FASTQ_NUMBER_OF_PASS_FILTER_READS));
		if (metaValue == null)
			return null;
		return Integer.parseInt(metaValue);
	}
	
	public void setFastqNumberOfPassFilterReads(FileHandle file, Integer number) throws MetadataException {
		setMeta(file, FILE_AREA, FASTQ_NUMBER_OF_PASS_FILTER_READS, number.toString());
	}

	@Override
	public boolean containsFailed(FileHandle file)  {
		String metaValue = (getMeta(file, FILE_AREA, FASTQ_CONTAINS_FAILED_READS));
		if (metaValue == null)
			return false;
		return Boolean.parseBoolean(metaValue);
	}
	
	public void setContainsFailed(FileHandle file, boolean fail) throws MetadataException {
		Boolean b = new Boolean(fail);
		setMeta(file, FILE_AREA, FASTQ_CONTAINS_FAILED_READS, b.toString());
	}

	@Override
	public Integer getFastqFileNumber(FileHandle file) {
		Integer fileNumber = new Integer(getMeta(file, FILETYPE_AREA, FileTypeService.FILETYPE_FILE_NUMBER_META_KEY));
		return fileNumber;
	}
	
	public void setFastqFileNumber(FileHandle file, Integer number) throws InvalidFileTypeException, MetadataException {
		setMeta(file, FILETYPE_AREA, FileTypeService.FILETYPE_FILE_NUMBER_META_KEY, number.toString());
	}

	@Override
	public UUID getLibraryUUID(FileHandle file) {
		UUID uuid = UUID.fromString(getMeta(file, FILE_AREA, FASTQ_LIBRARY_UUID));
		return uuid;
	}
	
	public void setLibraryUUID(FileHandle file, Sample library) throws MetadataException {
		Assert.assertTrue(sampleService.isLibrary(library), "sample must be of type library");
		Assert.assertParameterNotNull(library.getUUID(), "library must be persisted");
		setMeta(file, FILE_AREA, FASTQ_LIBRARY_UUID, library.getUUID().toString());
	}

	@Override
	public Sample getLibraryFromFASTQ(FileHandle fastq) {
		Assert.assertTrue(fastq.getFileType().equals(fastqFileType), "file must be of type");
		TypedQuery<Sample> query = fileTypeDao.getEntityManager().createQuery("select s from Sample s where s.uuid = :fqLibUUID", Sample.class);
		query.setParameter("fqLibUUID", getLibraryUUID(fastq));
		Sample result = query.getSingleResult();
		Assert.assertParameterNotNull(result);
		return result;
	}

	@Override
	public Integer getNumberOfReadSegments(FileGroup filegroup) {
		Assert.assertParameterNotNull(filegroup);
		Integer numberOfSegments = 1;
		try{
			numberOfSegments = new Integer(getMeta(filegroup, FILE_AREA, FASTQ_GROUP_NUMBER_OF_READ_SEGMENTS));
		} catch (NumberFormatException e){
			logger.warn("Unable to obtain number of read segments from meta so defaulting to 1");
		}
		return numberOfSegments;
	}
	
	public void setNumberOfReadSegments(FileGroup filegroup, Integer number) throws MetadataException {
		Assert.assertParameterNotNull(filegroup);
		Assert.assertParameterNotNull(number);
		setMeta(filegroup, FILE_AREA, FASTQ_GROUP_NUMBER_OF_READ_SEGMENTS, number.toString());
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public FileType getFastqFileType() {
		return fastqFileType;
	}
	
	@Override
	public void copyFastqFileGroupMetadata(FileGroup origin, FileGroup target) throws MetadataException {
	    this.copyMetaByArea(origin, target, FILE_AREA);
	}

	@Override
	public void copyFastqFileHandleMetadata(FileHandle origin, FileHandle target) throws MetadataException {
	    this.copyMetaByArea(origin, target, FILE_AREA);	    
	}

	
	

}
