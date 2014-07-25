/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.plugin.picard.software;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.plugin.fileformat.service.BamService;
import edu.yu.einstein.wasp.plugin.mps.grid.software.Samtools;
import edu.yu.einstein.wasp.plugin.picard.service.PicardService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.software.SoftwarePackage;


/**
 * @author asmclellan
 */
public class Picard extends SoftwarePackage{

	private static final long serialVersionUID = 6817018170220888568L;
	
	
	@Autowired
	PicardService picardService;
	
	@Autowired
	FileService fileService;
	
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
				" TMP_DIR=${" + WorkUnit.TMP_DIRECTORY + "} CREATE_INDEX=" + createIndex + " VALIDATION_STRINGENCY=SILENT";
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
				" TMP_DIR=${" + WorkUnit.TMP_DIRECTORY + "} VALIDATION_STRINGENCY=SILENT";
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
		command += " O=" + mergedBamFilename + " SO=coordinate TMP_DIR=${" + WorkUnit.TMP_DIRECTORY + "} CREATE_INDEX=" + createIndex + " VALIDATION_STRINGENCY=SILENT";
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
		" O=" + mergedBamFilename + " SO=coordinate TMP_DIR=${" + WorkUnit.TMP_DIRECTORY + "} CREATE_INDEX=" + createIndex + " VALIDATION_STRINGENCY=SILENT";
		if (createIndex)
			 command += " && mv " + mergedBamFilename + ".bai " + mergedBaiFilename;
		logger.debug("Will conduct picard MergeSamFiles with command: " + command);
		return command;
	}
	
	
	
	/**
	 * save alignment metrics, including Picard dedup metrics and unqielyAligned Metrics
	 * @param SampleSource cellLib
	 * @param FileGroup metricsG
	 * @param String scratchDirectory
	 * @return void
	 */
	public void saveAlignmentMetrics(Integer fileGroupId, String dedupMetricsFilename, String scratchDirectory, GridHostResolver gridHostResolver){

		logger.debug("starting saveAlignmentMetrics");

		if(fileGroupId==null || dedupMetricsFilename==null || dedupMetricsFilename.isEmpty() || scratchDirectory==null || scratchDirectory.isEmpty()){
			logger.debug("major problem in starting saveAlignmentMetrics: something is unexpectedly null or empty");
			return;
		}
		
		JSONObject json = new JSONObject();
		try{
			Samtools samtools = (Samtools) getSoftwareDependencyByIname("samtools");
			Map <String,String> picardDedupMetricsMap = this.getPicardDedupMetrics(dedupMetricsFilename, scratchDirectory, gridHostResolver);
			logger.debug("size of dedupMetrics in saveAlignmentMetrics: " + picardDedupMetricsMap.size());
			Map <String,String> uniquelyAlignedReadCountMetricMap = samtools.getUniquelyAlignedReadCountMetrics(Samtools.UNIQUELY_ALIGNED_READ_COUNT_FILENAME, Samtools.UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FILENAME, scratchDirectory, gridHostResolver);
			
			//capture and add and print out jsaon
			logger.debug("dedupMetrics output:");
			for (String key : picardDedupMetricsMap.keySet()) {
				logger.debug(key + " : " + picardDedupMetricsMap.get(key));
				json.put(key, picardDedupMetricsMap.get(key));
			}
			logger.debug("uniquelyAlignedMetrics output:");
			for (String key : uniquelyAlignedReadCountMetricMap.keySet()) {
				logger.debug(key + " : " + uniquelyAlignedReadCountMetricMap.get(key));
				json.put(key, uniquelyAlignedReadCountMetricMap.get(key));
			}
			
			logger.debug("json output from saveAlignmentMetrics to filegroupmeta: ");
			logger.debug(json.toString());
			
			setAlignmentMetricsToFileGroupMeta(fileGroupId, json);
			logger.debug("successfully saved the picard dedup metrics to filegroupmeta");
			
		} catch (Exception e) {
			logger.debug("exception in saveAlignmentMetrics to filegroupmeta: " + e.getMessage());
		} 		
		logger.debug("ending saveAlignmentMetrics");
	}
	public Map<String,String> getPicardDedupMetrics(String dedupMetricsFilename, String scratchDirectory, GridHostResolver gridHostResolver)throws Exception{
		
		/*
		Column Definitions in the stats file generated from Picard.DuplicationMetrics 
		taken from http://picard.sourceforge.net/picard-metric-definitions.shtml#DuplicationMetrics
		LIBRARY: The library on which the duplicate marking was performed.
		unpairedReadsExamined: The number of mapped reads examined which did not have a mapped mate pair, either because the read is unpaired, or the read is paired to an unmapped mate.
		readPairsExamined: The number of mapped read pairs examined.
		unmappedReads: The total number of unmapped reads examined.
		unpairedReadDuplicates: The number of fragments that were marked as duplicates.
		readPairDuplicates: The number of read pairs that were marked as duplicates.
		readPairOpticalDuplicates: The number of read pairs duplicates that were caused by optical duplication. Value is always < readPairDuplicates, which counts all duplicates regardless of source.
		percentDuplication: The percentage of mapped sequence that is marked as duplicate.
		ESTIMATED_LIBRARY_SIZE: The estimated number of unique molecules in the library based on PE duplication.
		ExtractIlluminaBarcodes.BarcodeMetric				
		Metrics produced by the ExtractIlluminaBarcodes program that is used to parse data in the basecalls directory and determine to which barcode each read should be assigned.
		
		header in output file:
		//LIBRARY 	unpairedReadsExamined 	readPairsExamined	unmappedReads	unpairedReadDuplicates	readPairDuplicates	readPairOpticalDuplicates	percentDuplication	 ESTIMATED_LIBRARY_SIZE
			
		*/
		logger.debug("entering getPicardDedupMetrics");
		
		Map<String,String> picardDedupMetricsMap = new HashMap<String,String>();		
		
		String unpairedReadsExamined = "";
		String readPairsExamined	= "";
		String mappedReads = "";//derived: unpairedReadsExamined + readPairsExamined
		String unmappedReads = "";
		String totalReads = "";//derived: mappedReads + unmappedReads
		String fractionMapped = "";//derived: mappedReads / totalReads
		String unpairedReadDuplicates	= "";
		String readPairDuplicates	= "";
		String duplicateReads = "";//derived: unpairedReadDuplicates +  readPairDuplicates (these are mapped duplicates!)
		String readPairOpticalDuplicates = "";
		String percentDuplication = "";//this value is really a fraction, so store in fractionDuplicated; it's duplicated mapped reads / total mapped reads
		String fractionDuplicated = "";//identical to percentDuplication, just provided with a more descriptive name; it's duplicated mapped reads / total mapped reads
		
		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.SINGLE);
		GridWorkService workService = gridHostResolver.getGridWorkService(w);
		GridTransportConnection transportConnection = workService.getTransportConnection();
		w.setWorkingDirectory(scratchDirectory);
		logger.debug("setting cat command in getPicardDedupMetrics");
		w.addCommand("cat " + dedupMetricsFilename + " | grep '.' | tail -1");//grep '.' excludes blank lines; tail to get the data
		
		GridResult r = transportConnection.sendExecToRemote(w);
		InputStream is = r.getStdOutStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is)); 
		boolean keepReading = true;
		int lineNumber = 0;
		logger.debug("getting ready to read picardDedupMetrics file");
		while (keepReading){
			lineNumber++;
			String line = null;
			line = br.readLine();
			logger.debug("line number = " + lineNumber + " and line = " + line);
			if (line == null)
				keepReading = false;
			else if (lineNumber == 1){
				String [] stringArray = line.split("\\t");							
				unpairedReadsExamined = stringArray[1];
				picardDedupMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNPAIRED_READS, unpairedReadsExamined);//unpairedReadsExamined (mapped)
				readPairsExamined	= stringArray[2];
				picardDedupMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_PAIRED_READS, readPairsExamined);//readPairsExamined (mapped)
				unmappedReads = stringArray[3];
				picardDedupMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNMAPPED_READS, unmappedReads);//unmappedReads
				unpairedReadDuplicates = stringArray[4];
				picardDedupMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNPAIRED_READ_DUPLICATES, unpairedReadDuplicates);//unpairedReadDuplicates
				readPairDuplicates	= stringArray[5];
				picardDedupMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_PAIRED_READ_DUPLICATES, readPairDuplicates);//readPairDuplicates
				readPairOpticalDuplicates = stringArray[6];
				picardDedupMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_PAIRED_READ_OPTICAL_DUPLICATES, readPairOpticalDuplicates);//readPairOpticalDuplicates
				percentDuplication = stringArray[7];
				fractionDuplicated = percentDuplication;//percentDuplication is actually a fraction duplicated
				picardDedupMetricsMap.put("percentDuplicated", percentDuplication);//percentDuplication
				picardDedupMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_DUPLICATED, fractionDuplicated);//fractionDuplicated
				logger.debug("finished reading from the dedupMetrics file");
				
				//work up derived values
				Integer mappedReads_integer = Integer.valueOf(unpairedReadsExamined) + Integer.valueOf(readPairsExamined);
				mappedReads = mappedReads_integer.toString();
				picardDedupMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_MAPPED_READS, mappedReads);
				Integer unmappedReads_integer = Integer.valueOf(unmappedReads);
				Integer totalReads_integer = mappedReads_integer + unmappedReads_integer;
				totalReads = totalReads_integer.toString();
				picardDedupMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_TOTAL_READS, totalReads);
				
				Double fractionMapped_double = 0.0;
				fractionMapped = fractionMapped_double.toString();
				if(mappedReads_integer>0 && totalReads_integer>0){
					fractionMapped_double = (double) mappedReads_integer / totalReads_integer;
					DecimalFormat myFormat = new DecimalFormat("0.000000");
					fractionMapped = myFormat.format(fractionMapped_double);						
				}					
				picardDedupMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_MAPPED, fractionMapped);
				
				Integer duplicateReads_integer = Integer.valueOf(unpairedReadDuplicates) + Integer.valueOf(readPairDuplicates);
				duplicateReads = duplicateReads_integer.toString();
				picardDedupMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_DUPLICATE_READS, duplicateReads);

				logger.debug("output of gathered alignment metrics (key:value) :");
				for (String key : picardDedupMetricsMap.keySet()) {
					logger.debug(key + " : " + picardDedupMetricsMap.get(key));
				}
			} 
		}
		br.close();					
		
		logger.debug("exiting getPicardDedupMetrics");
		return picardDedupMetricsMap;
	}

	private void setAlignmentMetricsToFileGroupMeta(Integer fileGroupId, JSONObject json)throws MetadataException{
		FileGroup fileGroup = fileService.getFileGroupById(fileGroupId);
		List<FileGroupMeta> fileGroupMetaList = fileGroup.getFileGroupMeta();
		FileGroupMeta fgm = new FileGroupMeta();
		fgm.setFileGroup(fileGroup);
		fgm.setK(BamService.BAMFILE_ALIGNMENT_METRICS_META_KEY);
		fgm.setV(json.toString());
		fileGroupMetaList.add(fgm);
		fileService.saveFileGroupMeta(fileGroupMetaList, fileGroup);		
	}
}
