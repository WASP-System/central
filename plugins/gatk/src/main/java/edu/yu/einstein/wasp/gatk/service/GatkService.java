/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.gatk.service;

import java.util.List;
import java.util.Set;

import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.BamFileTypeAttribute;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * @author jcai
 * @author asmclellan
 */
public interface GatkService extends WaspService {
	
		public static final String UNIFIED_GENOTYPER_CODE="ug";
		
		public static final String HAPLOTYPE_CALLER_CODE="hc";
		
		public Set<BamFileTypeAttribute> getCompleteGatkPreprocessBamFileAttributeSet(boolean isDedup);
		
		public String performAction();
		
		@Deprecated
		public String getWxsIntervalFile(Job job, Build build);

		public Build getBuildForFg(FileGroup fileGroup);

		public void doLaunchFlow(List<Integer> cellLibraryIds, Integer softwareId,	String flowName) throws Exception;


}
