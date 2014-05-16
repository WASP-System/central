package edu.yu.einstein.wasp.plugin.fileformat.service;

import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.model.FileType;

public interface BamService extends FileTypeService {
	
	public static final String FILE_AREA = "bamFile";
	public static final String BAM_INAME = "bam";
	
	public FileType getBamFileType();

}
