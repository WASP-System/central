package edu.yu.einstein.wasp.plugin.fileformat.service;

import java.util.UUID;

import edu.yu.einstein.wasp.exception.InvalidFileTypeException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.WaspService;

public interface BamService extends WaspService {
	
	public static final String FILE_AREA = "bamFile";
		
	public static final String BAM_INAME = "bam";
	
	public FileType getBamFileType();

}
