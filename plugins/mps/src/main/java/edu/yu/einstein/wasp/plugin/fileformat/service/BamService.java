package edu.yu.einstein.wasp.plugin.fileformat.service;

import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.service.WaspService;

public interface BamService extends WaspService {
	
	public static final String FILE_AREA = "bamFile";
	public static final String BAM_INAME = "bam";
	
	public static final String BAM_ATTRIBUTE_SORTED = "sorted";
	public static final String BAM_ATTRIBUTE_DEDUP = "dedup";
	public static final String BAM_ATTRIBUTE_REALN_AROUND_INDELS = "realignAroundIndels";
	public static final String BAM_ATTRIBUTE_RECAL_QC_SCORES = "recalibratedQcScores";
	
	public FileType getBamFileType();

}
