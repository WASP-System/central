/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.picard.service;

import java.util.Map;

import org.json.JSONObject;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * 
 */
public interface PicardService extends WaspService {

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
		 * @param SampleSource cellLib
		 * @aparam JSONObject json
		 * @return void
		 */
		public void setAlignmentMetrics(SampleSource cellLib, JSONObject json)throws MetadataException;
		
		public String getUnpairedMappedReads(SampleSource cellLib);
		public String getPairedMappedReads(SampleSource cellLib);
		public String getUnmappedReads(SampleSource cellLib);
		public String getUnpairedMappedReadDuplicates(SampleSource cellLib);
		public String getPairedMappedReadDuplicates(SampleSource cellLib);
		public String getPairedMappedReadOpticalDuplicates(SampleSource cellLib);		
		public String getFractionMapped(SampleSource cellLib);
		public String getMappedReads(SampleSource cellLib);
		public String getTotalReads(SampleSource cellLib);
		public String getFractionMappedAsCalculation(SampleSource cellLib);
		public String getFractionDuplicated(SampleSource cellLib);
		public String getDuplicateReads(SampleSource cellLib);
		public String getFractionDuplicatedAsCalculation(SampleSource cellLib);
		public String getFractionUniqueNonRedundant(SampleSource cellLib);
		public String getUniqueReads(SampleSource cellLib);
		public String getUniqueNonRedundantReads(SampleSource cellLib);
		public String getFractionUniqueNonRedundantAsCalculation(SampleSource cellLib);
}
