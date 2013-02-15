package edu.yu.einstein.wasp.service.filetype.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.yu.einstein.wasp.exception.InvalidFileTypeException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.service.filetype.FastqService;

@Service
@Transactional("entityManager")
public class FastqServiceImpl extends FileTypeServiceImpl implements FastqService {

	@Override
	public Integer getFastqReadSegmentNumber(FileHandle file) throws InvalidFileTypeException {
				
		Integer readSegmentNumber = new Integer(getMeta(file, FILE_AREA, READ_SEGMENT_NUMBER_META_KEY));
		return readSegmentNumber;
	}

	public void setFastqReadSegmentNumber(FileHandle file, Integer number) throws InvalidFileTypeException, MetadataException {
		setMeta(file, FILE_AREA, READ_SEGMENT_NUMBER_META_KEY, number.toString());
	}

	@Override
	public Integer getFastqNumberOfReads(FileHandle file) throws InvalidFileTypeException {
		Integer nor = new Integer(getMeta(file, FILE_AREA, NUMBER_OF_READS_META_KEY));
		return nor;
	}
	
	public void setFastqNumberOfReads(FileHandle file, Integer number) throws InvalidFileTypeException, MetadataException {
		setMeta(file, FILE_AREA, NUMBER_OF_READS_META_KEY, number.toString());
	}

	@Override
	public Integer getFastqNumberOfPassFilterReads(FileHandle file) throws InvalidFileTypeException {
		Integer nopfr = new Integer(getMeta(file, FILE_AREA, NUMBER_OF_PASS_FILTER_READS_META_KEY));
		return nopfr;
	}
	
	public void setFastqNumberOfPassFilterReads(FileHandle file, Integer number) throws InvalidFileTypeException, MetadataException {
		setMeta(file, FILE_AREA, NUMBER_OF_PASS_FILTER_READS_META_KEY, number.toString());
	}

	@Override
	public boolean isReadsMarkedFailed(FileHandle file) throws InvalidFileTypeException {
		String fails = getMeta(file, FILE_AREA, CONTAINS_FAILED_READS_META_KEY);
		Boolean b = new Boolean(fails);
		return b.booleanValue();
	}
	
	public void setReadsMarkedFailed(FileHandle file, boolean fail) throws InvalidFileTypeException, MetadataException {
		Boolean b = new Boolean(fail);
		setMeta(file, FILE_AREA, CONTAINS_FAILED_READS_META_KEY, b.toString());
	}
	

}
