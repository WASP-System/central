/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.picard.service;

import java.util.Map;

import org.json.JSONObject;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * 
 */
public interface PicardService extends WaspService {

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
	
		/**
		 * Perform Service
		 * @return String
		 */
		public String performAction();

		/**
		 * @param FileGroup fileGroup
		 * @aparam JSONObject json
		 * @return void
		 */
		public boolean alignmentMetricsExist(FileGroup fileGroup);
		public String getUnpairedMappedReads(FileGroup fileGroup);
		public String getPairedMappedReads(FileGroup fileGroup);
		public String getUnmappedReads(FileGroup fileGroup);
		public String getUnpairedMappedReadDuplicates(FileGroup fileGroup);
		public String getPairedMappedReadDuplicates(FileGroup fileGroup);
		public String getPairedMappedReadOpticalDuplicates(FileGroup fileGroup);		
		public String getFractionMapped(FileGroup fileGroup);
		public String getMappedReads(FileGroup fileGroup);
		public String getTotalReads(FileGroup fileGroup);
		public String getFractionMappedAsCalculation(FileGroup fileGroup);
		public String getFractionDuplicated(FileGroup fileGroup);
		public String getDuplicateReads(FileGroup fileGroup);
		public String getFractionDuplicatedAsCalculation(FileGroup fileGroup);
		public String getFractionUniqueNonRedundant(FileGroup fileGroup);
		public String getUniqueReads(FileGroup fileGroup);
		public String getUniqueNonRedundantReads(FileGroup fileGroup);
		public String getFractionUniqueNonRedundantAsCalculation(FileGroup fileGroup);
}
