/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.plugin.picard.software;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;


/**
 * @author asmclellan
 */
public class Picard extends SoftwarePackage{

	private static final long serialVersionUID = 6817018170220888568L;
	
	@Autowired
	SampleService sampleService;
	
	public Picard() {}
	
	/**
	 * Gets command for running picard MarkDuplicates.
	 * @param inputBamFilename
	 * @param dedupBamFilename
	 * @param dedupBaiFilename (optional: may be null)
	 * @param dedupMetricsFilename
	 * @return
	 */
	public String getMarkDuplicatesCmd(String inputBamFilename, String dedupBamFilename, String dedupBaiFilename, String dedupMetricsFilename, int memRequiredGb){
		boolean createIndex = false;
		if (dedupBaiFilename != null)
			createIndex = true;
		String command = "java -Xmx" + memRequiredGb + "g -jar $PICARD_ROOT/MarkDuplicates.jar I=" + inputBamFilename + " O=" + dedupBamFilename +
				" REMOVE_DUPLICATES=false METRICS_FILE=" + dedupMetricsFilename + 
				" TMP_DIR=. CREATE_INDEX=" + createIndex + " VALIDATION_STRINGENCY=SILENT";
		if (createIndex)
			 command += " && mv " + dedupBamFilename + ".bai " + dedupBaiFilename;
		logger.debug("Will conduct picard MarkDuplicates with command: " + command);
		return command;
	}
	
	/**
	 * Indexes given bam file
	 * @param bamFilename
	 * @param baiFilename
	 * @return
	 */
	public String getIndexBamCmd(String bamFilename, String baiFilename, int memRequiredGb){
		String command = "java -Xmx" + memRequiredGb + "g -jar $PICARD_ROOT/BuildBamIndex.jar I=" + bamFilename + " O=" + baiFilename + 
				" TMP_DIR=. VALIDATION_STRINGENCY=SILENT";
		logger.debug("Will conduct picard indexing of bam file with command: " + command);
		return command;
	}	
	
	/**
	 * merges given bam files. 
	 * @param inputBamFilenames
	 * @param mergedBamFilename
	 * @param mergedBaiFilename (optional: may be null)
	 * @return
	 */
	public String getMergeBamCmd(Set<String> inputBamFilenames, String mergedBamFilename, String mergedBaiFilename, int memRequiredGb) {
		boolean createIndex = false;
		if (mergedBaiFilename != null)
			createIndex = true;
		String command = "java -Xmx" + memRequiredGb + "g -jar $PICARD_ROOT/MergeSamFiles.jar";
		for (String fileName : inputBamFilenames)
			command += " I=" + fileName;
		command += " O=" + mergedBamFilename + " SO=coordinate TMP_DIR=. CREATE_INDEX=" + createIndex + " VALIDATION_STRINGENCY=SILENT";
		if (createIndex)
			 command += " && mv " + mergedBamFilename + ".bai " + mergedBaiFilename;
		logger.debug("Will conduct picard MergeSamFiles with command: " + command);
		return command;
	}
	
	/**
	 * Merge filenames represented as a glob e.g. '*' or '*.bam' or '*.sam' (location is relative to the scratch directory).
	 * @param inputBamFilenamesGlob
	 * @param mergedBamFilename
	 * @param mergedBaiFilename (optional: may be null)
	 * @return
	 */
	public String getMergeBamCmd(String inputBamFilenamesGlob, String mergedBamFilename, String mergedBaiFilename, int memRequiredGb) {
		boolean createIndex = false;
		if (mergedBaiFilename != null)
			createIndex = true;
		String command = "java -Xmx" + memRequiredGb + "g -jar $PICARD_ROOT/MergeSamFiles.jar $(printf 'I=%s ' " + inputBamFilenamesGlob + ")" + 
		" O=" + mergedBamFilename + " SO=coordinate TMP_DIR=. CREATE_INDEX=" + createIndex + " VALIDATION_STRINGENCY=SILENT";
		if (createIndex)
			 command += " && mv " + mergedBamFilename + ".bai " + mergedBaiFilename;
		logger.debug("Will conduct picard MergeSamFiles with command: " + command);
		return command;
	}
	
	/**
	 * save Picard dedup metrics (if exists) to cellLibrary Meta 
	 * @param SampleSource cellLib
	 * @param FileGroup metricsG
	 * @param String scratchDirectory
	 * @return void
	 */
	public void savePicardDedupMetrics(SampleSource cellLib, String dedupMetricsFilename, String scratchDirectory, GridHostResolver gridHostResolver){
			
		/*
		 http://picard.sourceforge.net/picard-metric-definitions.shtml#DuplicationMetrics
		Column Definitions of DuplicationMetrics (output stats) from Picard mark duplicates
		LIBRARY: The library on which the duplicate marking was performed.
		UNPAIRED_READS_EXAMINED: The number of mapped reads examined which did not have a mapped mate pair, either because the read is unpaired, or the read is paired to an unmapped mate.
		READ_PAIRS_EXAMINED: The number of mapped read pairs examined.
		UNMAPPED_READS: The total number of unmapped reads examined.
		UNPAIRED_READ_DUPLICATES: The number of fragments that were marked as duplicates.
		READ_PAIR_DUPLICATES: The number of read pairs that were marked as duplicates.
		READ_PAIR_OPTICAL_DUPLICATES: The number of read pairs duplicates that were caused by optical duplication. Value is always < READ_PAIR_DUPLICATES, which counts all duplicates regardless of source.
		PERCENT_DUPLICATION: The percentage of mapped sequence that is marked as duplicate.
		ESTIMATED_LIBRARY_SIZE: The estimated number of unique molecules in the library based on PE duplication.
		ExtractIlluminaBarcodes.BarcodeMetric				
		Metrics produced by the ExtractIlluminaBarcodes program that is used to parse data in the basecalls directory and determine to which barcode each read should be assigned.
		
		header in output file:
		//LIBRARY 	UNPAIRED_READS_EXAMINED 	READ_PAIRS_EXAMINED	UNMAPPED_READS	UNPAIRED_READ_DUPLICATES	READ_PAIR_DUPLICATES	READ_PAIR_OPTICAL_DUPLICATES	PERCENT_DUPLICATION	 ESTIMATED_LIBRARY_SIZE
			
		*/
		logger.debug("starting savePicardDedupMetrics");

		if(cellLib==null || dedupMetricsFilename==null || dedupMetricsFilename.isEmpty() || scratchDirectory==null || scratchDirectory.isEmpty()){
			return;
		}
						
		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.SINGLE);
		String UNPAIRED_READS_EXAMINED = "";
		String READ_PAIRS_EXAMINED	= "";
		String UNMAPPED_READS = "";
		String UNPAIRED_READ_DUPLICATES	= "";
		String READ_PAIR_DUPLICATES	= "";
		String READ_PAIR_OPTICAL_DUPLICATES = "";
		String PERCENT_DUPLICATION = "";			
		JSONObject json = new JSONObject();
		
		try {
			GridWorkService workService = gridHostResolver.getGridWorkService(w);
			GridTransportConnection transportConnection = workService.getTransportConnection();
			w.setWorkingDirectory(scratchDirectory);
			w.addCommand("cat " + dedupMetricsFilename + " | grep '.' | tail -1");//grep '.' excludes blank lines; tail to get the data
			
			GridResult r = transportConnection.sendExecToRemote(w);
			InputStream is = r.getStdOutStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is)); 
			boolean keepReading = true;
			int lineNumber = 0;
			while (keepReading){
				lineNumber++;
				String line = null;
				line = br.readLine();
				logger.debug("line number = " + lineNumber + " and line = " + line);
				if (line == null || lineNumber > 1)
					keepReading = false;
				else if (lineNumber == 1){
					String [] stringArray = line.split("\\t");							
					UNPAIRED_READS_EXAMINED = stringArray[1];
					json.put("unpairedReadsExamined", UNPAIRED_READS_EXAMINED);
					READ_PAIRS_EXAMINED	= stringArray[2];
					json.put("readPairsExamined", READ_PAIRS_EXAMINED);
					UNMAPPED_READS = stringArray[3];
					json.put("unmappedReads", UNMAPPED_READS);
					UNPAIRED_READ_DUPLICATES = stringArray[4];
					json.put("unpairedReadDuplicates", UNPAIRED_READ_DUPLICATES);
					READ_PAIR_DUPLICATES	= stringArray[5];
					json.put("readPairDuplicates", READ_PAIR_DUPLICATES);
					READ_PAIR_OPTICAL_DUPLICATES = stringArray[6];
					json.put("readPairOpticalDuplicates", READ_PAIR_OPTICAL_DUPLICATES);
					PERCENT_DUPLICATION = stringArray[7];
					json.put("percentDuplication", PERCENT_DUPLICATION);
					logger.debug("dedupMetrics:  =  UNPAIRED_READS_EXAMINED: " + UNPAIRED_READS_EXAMINED + "; READ_PAIRS_EXAMINED: " + READ_PAIRS_EXAMINED + "; UNMAPPED_READS_String: "+ UNMAPPED_READS + "; UNPAIRED_READ_DUPLICATES: "+ UNPAIRED_READ_DUPLICATES + "; READ_PAIR_DUPLICATES: "+ READ_PAIR_DUPLICATES + "; READ_PAIR_OPTICAL_DUPLICATES: "+ READ_PAIR_OPTICAL_DUPLICATES + "; PERCENT_DUPLICATION_String: "+ PERCENT_DUPLICATION);
					logger.debug("dedupMetrics via json.toString():  =  " + json.toString());
					sampleService.setLibraryOnCellMeta(cellLib, "picard", "dedupMetrics", json.toString());
					logger.debug("successfully saved the picard dedup metrics");
				} 
			}
			br.close();					
		} catch (Exception e) {
			logger.debug("unable to read from dedupMetricsFilename: " + dedupMetricsFilename );
		} 
		
		logger.debug("ending savePicardDedupMetrics");
	}
}
