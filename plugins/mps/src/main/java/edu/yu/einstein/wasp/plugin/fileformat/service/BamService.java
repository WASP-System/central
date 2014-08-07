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
	
	//for mammalian genomes (point chip-seq)
	public static final String BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_10M = "uniqueReadsFrom10M";//the first 10M uniquely mapped reads in the bam file (so should be 10 Million)	
	public static final String BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_10M = "uniqueNonRedundantReadsFrom10M";//duplicates removed  from uniqueReadsFrom10M 
	public static final String BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_10M = "fractionUniqueNonRedundantFrom10M"; // uniqueNonRedundantReadsFrom10M / uniqueReadsFrom10M
	//for mammalian genomes (broad chip-seq)
	public static final String BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_20M = "uniqueReadsFrom20M";//the first 20M uniquely mapped reads in the bam file (so should be 20 Million)	
	public static final String BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_20M = "uniqueNonRedundantReadsFrom20M";//duplicates removed  from uniqueReadsFrom20M 
	public static final String BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_20M = "fractionUniqueNonRedundantFrom20M"; // uniqueNonRedundantReadsFrom20M / uniqueReadsFrom20M
	//for modENCODE genomes (point chip-seq)
	public static final String BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_2M = "uniqueReadsFrom2M";//the first 2M uniquely mapped reads in the bam file (so should be 2 Million)	
	public static final String BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_2M = "uniqueNonRedundantReadsFrom2M";//duplicates removed  from uniqueReadsFrom2M 
	public static final String BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_2M = "fractionUniqueNonRedundantFrom2M"; // uniqueNonRedundantReadsFrom2M / uniqueReadsFrom2M
	//for modENCODE genomes (broad chip-seq)
	public static final String BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_5M = "uniqueReadsFrom5M";//the first 5M uniquely mapped reads in the bam file (so should be 5 Million)	
	public static final String BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_5M = "uniqueNonRedundantReadsFrom5M";//duplicates removed  from uniqueReadsFrom5M 
	public static final String BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_5M = "fractionUniqueNonRedundantFrom5M"; // uniqueNonRedundantReadsFrom5M / uniqueReadsFrom5M

	public FileType getBamFileType();

}
