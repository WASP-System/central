package edu.yu.einstein.wasp.plugin.fileformat.service;

import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.model.FileType;

public interface BamService extends FileTypeService {
	
	public static final String FILE_AREA = "bamFile";
	public static final String BAM_INAME = "bam";
	
	public static final String BAMFILE_ALIGNMENT_METRICS_META_KEY = "bamFile.alignmentMetrics";

	public static final String BAMFILE_ALIGNMENT_METRIC_UNPAIRED_READS = "unpairedReads";	
	public static final String BAMFILE_ALIGNMENT_METRIC_PAIRED_READS = "pairedReads";	
	public static final String BAMFILE_ALIGNMENT_METRIC_UNMAPPED_READS = "unmappedReads";	
	public static final String BAMFILE_ALIGNMENT_METRIC_UNPAIRED_READ_DUPLICATES = "unpairedReadDuplicates";	
	public static final String BAMFILE_ALIGNMENT_METRIC_PAIRED_READ_DUPLICATES = "pairedReadDuplicates";	
	public static final String BAMFILE_ALIGNMENT_METRIC_PAIRED_READ_OPTICAL_DUPLICATES = "pairedReadOpticalDuplicates";	
	public static final String BAMFILE_ALIGNMENT_METRIC_FRACTION_MAPPED = "fractionMapped";	
	public static final String BAMFILE_ALIGNMENT_METRIC_MAPPED_READS = "mappedReads";	
	public static final String BAMFILE_ALIGNMENT_METRIC_TOTAL_READS = "totalReads";	
	public static final String BAMFILE_ALIGNMENT_METRIC_FRACTION_DUPLICATED = "fractionDuplicated"; // duplicateReads / mappedReads	
	public static final String BAMFILE_ALIGNMENT_METRIC_DUPLICATE_READS = "duplicateReads";//this is duplicates of the set of mapped reads
	public static final String BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS = "uniqueReads";//map to single location	
	public static final String BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS = "uniqueNonRedundantReads";//map to single location (duplicates removed)
	public static final String BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT = "fractionUniqueNonRedundant"; // uniqueNonRedundantReads / uniqueReads
	
	public FileType getBamFileType();

}
