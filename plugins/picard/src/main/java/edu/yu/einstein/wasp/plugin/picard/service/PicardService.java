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
		public void setAlignmentMetrics(SampleSource cellLib, JSONObject json)throws MetadataException  ;
		
		public Map<String,String> getPicardDedupMetrics(String dedupMetricsFilename, String scratchDirectory, GridHostResolver gridHostResolver)throws Exception;

		public Map<String,String> getUniquelyAlignedReadCountMetrics(String uniqelyAlignedReadCountfilename, String uniqelyAlignedNonRedundantReadCountfilename, String scratchDirectory, GridHostResolver gridHostResolver)throws Exception;
	
}
