/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.software;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.babraham.charts.BabrahamQCParseModule;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.software.SoftwarePackage;



/**
 * @author calder / asmclellan
 *
 */
@Transactional("entityManager")
public class TrimGalore extends SoftwarePackage {

    
        public static final String TRIM_GALORE_FLOW_NAME = "edu.yu.einstein.wasp.plugin.babraham.trim_galore.mainFlow";
	
	@Autowired
	private FastqService fastqService;
	
	@Autowired
	private MessageService messageService;
	
	// cannot autowire as IlluminaHiseqSequenceRunProcessor here which is all we really need. Beans referenced by base type so must
	// as Software and use @Qualifier to specify the casava bean. 
	// Seems to be an issue for batch but not Web which accepts IlluminaHiseqSequenceRunProcessor.
	@Autowired
	@Qualifier("casava")
	private Software casava;
	
	public static String TRIM_GALORE_RESULTS_KEY = "TRIM_GALORE_SIZES";
	
	@Autowired
	BabrahamService babrahamService;
	
	@Autowired
	FileService fileService;
	
	/**
	 * 
	 */
	public TrimGalore() {
		setSoftwareVersion("0.3.3"); // this default may be overridden in wasp.site.properties
	}
	
	/**
	 * This method takes a grid result of a successfully run FastQC job, gets the working directory
	 * and uses it to parse the <em>fastqc_data.txt</em> file into a Map which contains
	 * static Strings defining the output keys (see above) and JSONObjects representing the
	 * data.  
	 * @param result
	 * @return
	 * @throws GridException
	 * @throws BabrahamDataParseException
	 * @throws JSONException 
	 */
	public Map<String,JSONObject> parseOutput(String resultsDir) throws GridException, BabrahamDataParseException, JSONException {
		Map<String,JSONObject> output = new LinkedHashMap<String, JSONObject>();
		Map<String, FastQCDataModule> mMap = babrahamService.parseFastQCOutput(resultsDir);
//		output.put(PlotType.QC_RESULT_SUMMARY, BabrahamQCParseModule.getParsedQCResults(mMap, messageService));
//		output.put(PlotType.BASIC_STATISTICS, BabrahamQCParseModule.getParsedBasicStatistics(mMap, messageService));
//		output.put(PlotType.PER_BASE_QUALITY, BabrahamQCParseModule.getParsedPerBaseQualityData(mMap, messageService));
//		output.put(PlotType.PER_SEQUENCE_QUALITY, BabrahamQCParseModule.getPerSequenceQualityScores(mMap, messageService));
//		output.put(PlotType.PER_BASE_SEQUENCE_CONTENT, BabrahamQCParseModule.getPerBaseSequenceContent(mMap, messageService));
//		output.put(PlotType.PER_BASE_GC_CONTENT, BabrahamQCParseModule.getPerBaseGcContent(mMap, messageService));
//		output.put(PlotType.PER_SEQUENCE_GC_CONTENT, BabrahamQCParseModule.getPerSequenceGcContent(mMap, messageService));
//		output.put(PlotType.PER_BASE_N_CONTENT, BabrahamQCParseModule.getPerBaseNContent(mMap, messageService));
//		output.put(PlotType.SEQUENCE_LENGTH_DISTRIBUTION, BabrahamQCParseModule.getSequenceLengthDist(mMap, messageService));
//		output.put(PlotType.DUPLICATION_LEVELS, BabrahamQCParseModule.getSequenceDuplicationLevels(mMap, messageService));
//		output.put(PlotType.OVERREPRESENTED_SEQUENCES, BabrahamQCParseModule.getOverrepresentedSequences(mMap, messageService));
//		output.put(PlotType.KMER_PROFILES, BabrahamQCParseModule.getOverrepresentedKmers(mMap, messageService));
		return output;
	}
	



}
