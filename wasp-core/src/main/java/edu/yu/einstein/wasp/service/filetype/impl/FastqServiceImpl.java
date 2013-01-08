package edu.yu.einstein.wasp.service.filetype.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.FileMetaDao;
import edu.yu.einstein.wasp.dao.WorkflowMetaDao;
import edu.yu.einstein.wasp.dao.impl.WorkflowMetaDaoImpl;
import edu.yu.einstein.wasp.exception.InvalidFileTypeException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.FileMeta;
import edu.yu.einstein.wasp.service.filetype.FastqService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
@Transactional("entityManager")
public class FastqServiceImpl extends FileTypeServiceImpl implements FastqService {
	
	public static final String READ_SEGMENT_NUMBER_META_KEY = "fastqReadSegmentNumber";
	
	public static final String NUMBER_OF_READS_META_KEY = "fastqNumberOfReads";
	
	public static final String NUMBER_OF_PASS_FILTER_READS_META_KEY = "fastqNumberOfPFReads";
	
	public static final String CONTAINS_FAILED_READS_META_KEY = "fastqContainsFailed";
	
	public static final String FILE_AREA = "workflow";
	
	private static final String DELIMITER = ";";
	
	private FileMetaDao fileMetaDao;
	
	@Autowired
	public void setWorkflowMetaDao(FileMetaDao fileMetaDao) {
		this.fileMetaDao = fileMetaDao;
	}

	@Override
	public Integer getFastqReadSegmentNumber(File file) throws InvalidFileTypeException {
				
		Integer readSegmentNumber = new Integer(getMeta(file, FILE_AREA, READ_SEGMENT_NUMBER_META_KEY));
		return readSegmentNumber;
	}

	protected void setFastqReadSegmentNumber(File file, Integer number) throws InvalidFileTypeException {
		setMeta(file, FILE_AREA, READ_SEGMENT_NUMBER_META_KEY, number.toString());
	}

	@Override
	public Integer getFastqNumberOfReads(File file) throws InvalidFileTypeException {
		Integer nor = new Integer(getMeta(file, FILE_AREA, NUMBER_OF_READS_META_KEY));
		return nor;
	}
	
	protected void setFastqNumberOfReads(File file, Integer number) throws InvalidFileTypeException {
		setMeta(file, FILE_AREA, NUMBER_OF_READS_META_KEY, number.toString());
	}

	@Override
	public Integer getFastqNumberOfPassFilterReads(File file) throws InvalidFileTypeException {
		Integer nopfr = new Integer(getMeta(file, FILE_AREA, NUMBER_OF_PASS_FILTER_READS_META_KEY));
		return nopfr;
	}
	
	protected void setFastqNumberOfPassFilterReads(File file, Integer number) throws InvalidFileTypeException {
		setMeta(file, FILE_AREA, NUMBER_OF_PASS_FILTER_READS_META_KEY, number.toString());
	}

	@Override
	public boolean containsReadsMarkedFailed(File file) throws InvalidFileTypeException {
		String fails = getMeta(file, FILE_AREA, CONTAINS_FAILED_READS_META_KEY);
		Boolean b = new Boolean(fails);
		return b.booleanValue();
	}
	
	protected void containsReadsMarkedFailed(File file, boolean fail) throws InvalidFileTypeException {
		Boolean b = new Boolean(fail);
		setMeta(file, FILE_AREA, CONTAINS_FAILED_READS_META_KEY, b.toString());
	}
	

}
