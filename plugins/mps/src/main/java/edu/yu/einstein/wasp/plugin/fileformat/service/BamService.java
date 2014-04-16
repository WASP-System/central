package edu.yu.einstein.wasp.plugin.fileformat.service;

import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.service.WaspService;

public interface BamService extends WaspService {
	
	public static final String FILE_AREA = "bamFile";
	public static final String BAM_INAME = "bam";
	
	public FileType getBamFileType();

}
