/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.gatk.service;

import java.util.List;

import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * @author jcai
 * @author asmclellan
 */
public interface GatkService extends WaspService {

		/**
		 * Perform Service
		 * @return String
		 */
		public String performAction();
		
		public Build getGenomeBuild(SampleSource cellLibrary);

		public String getReferenceGenomeFastaFile(Build build);

		public String getReferenceSnpsVcfFile(Build build);

		public String getReferenceIndelsVcfFile(Build build);

		public String getWxsIntervalFile(Job job, Build build);

}
