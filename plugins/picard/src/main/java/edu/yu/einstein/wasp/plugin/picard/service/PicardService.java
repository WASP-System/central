/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.picard.service;

import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.service.WaspService;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * 
 */
public interface PicardService extends WaspService {

	
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
		public String getUniqueReads(FileGroup fileGroup);		
		public String getUniqueNonRedundantReads(FileGroup fileGroup);		
		public String getNonRedundantReadFraction(FileGroup fileGroup);
		
		public PanelTab getAlignmentMetricsForDisplay(FileGroup fileGroup);
}
