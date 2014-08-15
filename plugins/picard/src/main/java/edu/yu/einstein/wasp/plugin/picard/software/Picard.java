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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.fileformat.service.BamService;
import edu.yu.einstein.wasp.plugin.illumina.IlluminaIndexingStrategy;
import edu.yu.einstein.wasp.plugin.illumina.service.WaspIlluminaService;
import edu.yu.einstein.wasp.plugin.mps.grid.software.Samtools;
import edu.yu.einstein.wasp.plugin.picard.metrics.PicardMetricsParser;
import edu.yu.einstein.wasp.plugin.picard.service.PicardService;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
import edu.yu.einstein.wasp.util.MetaHelper;


/**
 * @author asmclellan
 */
public class Picard extends SoftwarePackage {

	private static final long serialVersionUID = 6817018170220888568L;
	
	
	@Autowired
	PicardService picardService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	private WaspIlluminaService illuminaService;
	
	@Autowired
	private RunService runService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private AdaptorService adaptorService;
	@Value("${wasp.temporary.dir:/tmp}")
	protected String localTempDir;
	
	public static final String BARCODES_DIRECTORY = "BARCODES";
	
	private static final String PICARD_BARCODE_METRICS_AREA = "picardBarcodeMetricsArea";
	
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
			Map <String,String> uniquelyAlignedReadCountMetricMap = samtools.getUniquelyAlignedReadCountMetrics(scratchDirectory, gridHostResolver);
			
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
/* this is apparently no longer used; was moved: see samtools.getUniquelyAlignedReadCountMetrics(String scratchDirectory, GridHostResolver gridHostResolver) in Samtools.java
	public Map<String,String> getUniquelyAlignedReadCountMetrics(String uniquelyAlignedReadCountfilename, String uniquelyAlignedNonRedundantReadCountfilename,String scratchDirectory, GridHostResolver gridHostResolver)throws Exception{
		
		logger.debug("entering getUniquelyAlignedReadCountMetrics");
		
		Map<String,String> uniquelyAlignedReadCountMetricsMap = new HashMap<String,String>();
		
		String uniqueReads = "";
		String uniqueNonRedundantReads = "";
		
		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.SINGLE);
		GridWorkService workService = gridHostResolver.getGridWorkService(w);
		GridTransportConnection transportConnection = workService.getTransportConnection();
		w.setWorkingDirectory(scratchDirectory);
		logger.debug("setting cat command in getPicardDedupMetrics");
		w.addCommand("cat " + uniquelyAlignedReadCountfilename );
		w.addCommand("cat " + uniquelyAlignedNonRedundantReadCountfilename );
		
		GridResult r = transportConnection.sendExecToRemote(w);
		InputStream is = r.getStdOutStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is)); 
		boolean keepReading = true;
		int lineNumber = 0;
		logger.debug("getting ready to read 2 uniquelAlignedMetrics files");
		while (keepReading){
			lineNumber++;
			String line = null;
			line = br.readLine();
			logger.debug("line number = " + lineNumber + " and line = " + line);
			if (line == null)
				keepReading = false;
			if (lineNumber == 1){
				uniqueReads = line.replaceAll("\\n", "");//just in case there is a trailing new line
				uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS, uniqueReads);
				logger.debug("uniqueReads = " + uniqueReads);
			} else if (lineNumber == 2){
				uniqueNonRedundantReads = line.replaceAll("\\n", "");//just in case there is a trailing new line;
				uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS, uniqueNonRedundantReads);
				logger.debug("uniqueNonRedundantReads = " + uniqueNonRedundantReads);
			} else {
				keepReading = false;
			}
			 
		}
		br.close();	
		
		Double fractionUniqueNonRedundant_double = 0.0;
		String fractionUniqueNonRedundant = fractionUniqueNonRedundant_double.toString();
		Integer uniqueReads_integer = Integer.valueOf(uniqueReads);
		Integer uniqueNonRedundantReads_integer = Integer.valueOf(uniqueNonRedundantReads);
		
		if(uniqueReads_integer>0 && uniqueNonRedundantReads_integer>0){
			fractionUniqueNonRedundant_double = (double) uniqueNonRedundantReads_integer / uniqueReads_integer;
			DecimalFormat myFormat = new DecimalFormat("0.000000");
			fractionUniqueNonRedundant = myFormat.format(fractionUniqueNonRedundant_double);						
		}	
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT, fractionUniqueNonRedundant);
		
		logger.debug("leaving getUniquelyAlignedReadCountMetrics");
		return uniquelyAlignedReadCountMetricsMap;
		
	}
*/	
	/**
	 * 
	 * Get a command string for Picard ExtractIlluminaBarcodes.  This command REQUIRES at least ~1500 file descriptors for Illumina HiSeq flowcells.   
	 * Check to see that your submission mechanism allows this on the remote host (eg. qsub -cwd -N testFD -S /bin/bash -j y -b y "ulimit -S -n && ulimit -H -n").
	 * A suggested value would be 10,000 open files to prevent issues.
	 * 
	 * @param run
	 * @param indexedCellMap
	 * @return
	 * @throws MetadataException
	 * @throws SampleTypeException
	 */
	public String getExtractIlluminaBarcodesCmd(Run run) throws WaspException {
		
		String cmd = "rm -rf ./" + BARCODES_DIRECTORY + "\n";
		
		Map<Integer,Sample> indexedCellMap = sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit());
		
		//Document runInfo = illuminaService.getIlluminaRunXml(run);
		
		for (Integer index : indexedCellMap.keySet()) {
			
			Set<IlluminaIndexingStrategy> strategies = new HashSet<IlluminaIndexingStrategy>(); 
			
			Sample cell = indexedCellMap.get(index);
			Set<SampleSource> cellLibraries = new LinkedHashSet<SampleSource>(sampleService.getCellLibrariesForCell(cell));
			
			for (SampleSource ss : sampleService.getCellLibrariesForCell(cell)) {
				try {
					strategies.add(new IlluminaIndexingStrategy(illuminaService.getIndexingStrategy(ss).toString()));
				} catch (WaspException e){
					Sample lib = sampleService.getLibrary(ss);
					if (sampleService.isControlLibrary(lib))
						logger.info("Not able to retrieve indexing strategy for cell-library id=" + ss.getId() + 
								", a control library with id=" + lib.getId() + " (" + lib.getName() + "). Probably no index which is normal");
					else {
						logger.warn("Unable to retrieve indexing strategy for cell-library id=" + ss.getId() + 
								" (library id=" + lib.getId() + ") so not going to process cell library: " + e.getLocalizedMessage());
					}
					cellLibraries.remove(ss); // cannot process this cell library
				}
			}
			
			logger.trace("Lane " + index + ", dealing with " + strategies.size() + " strategies");
			
			for (IlluminaIndexingStrategy s : strategies) {
				logger.debug("going to prepare getExtractIlluminaBarcodes command for run: " + run.getName() + " cell: " + cell.getId() + " (lane " + index + ") strategy: " + s.toString());
				String outputDir = BARCODES_DIRECTORY + "/" +  s.toString() + "/L" + index;
				cmd += "mkdir -p " + outputDir + "\n";
				cmd += getCreateBarcodeFileCmd(outputDir, cellLibraries, s) + "\n";
				cmd += "java -Xmx4g -jar $PICARD_ROOT/ExtractIlluminaBarcodes.jar TMP_DIR=. L=" + index + " B=./Data/Intensities/BaseCalls OUTPUT_DIR=./" + outputDir + 
						" M=./" + outputDir + "/mets.txt RS=" + getReadStructure(run, index, s) + " BARCODE_FILE=./" + BARCODES_DIRECTORY + "/" + s.toString() + "/L" + index + 
						"/barcodes.txt GZIP=true NUM_PROCESSORS=$NTHREADS\n\n###################\n\n";
			}
			
		}
		
		return cmd;
	}
	
	private String getReadStructure(Run run, Integer cellId, IlluminaIndexingStrategy strategy) throws WaspException {
		String rs = null;
		List<Integer> indexLengths = illuminaService.getLengthOfIndexedReads(run);
		List<Integer> segmentLengths = illuminaService.getLengthOfReadSegments(run);
		if (segmentLengths.size() == 0)
			return rs;
		Map<Integer,Sample> cellMap = sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit());
		Sample cell = cellMap.get(cellId);
		List<Sample> libraries = sampleService.getLibrariesOnCell(cell);
		int maxBarcodeLength = 0;
		for (Sample lib : libraries) {
			Adaptor adapter = sampleService.getLibraryAdaptor(lib);
			if (adapter == null){
				if (sampleService.isControlLibrary(lib)){
					logger.info("Ignoring library with id=" + lib.getId() + " as no adapter and is a control");
					continue;
				}
				else {
					throw new WaspException("Library with id=" + lib.getId() + " has no adapter");
				}
			}
			String[] barcodes = adapter.getBarcodesequence().split("-");
			if(barcodes[0].length() > maxBarcodeLength) 
				maxBarcodeLength = barcodes[0].length();
		}
		rs = segmentLengths.get(0) + "T";
		int d = indexLengths.get(0) - maxBarcodeLength;

		// This strategy assumes barcodes of same length
		String barcodeString = maxBarcodeLength + "B";
		if (d > 0) 
			barcodeString += d + "S";
		if (indexLengths.size() == 2 && strategy.equals(IlluminaIndexingStrategy.TRUSEQ_DUAL)) {
			barcodeString += maxBarcodeLength + "B";
			if (d > 0) 
				barcodeString += d + "S"; 
		}
		if (indexLengths.size() == 2 && strategy.equals(IlluminaIndexingStrategy.TRUSEQ)) {
			barcodeString += indexLengths.get(1) + "S";
		}
		rs += barcodeString;
		if (segmentLengths.size() == 2) {
			rs += segmentLengths.get(1) + "T";
		}
		logger.debug("Read Structure string: " + rs);
		
		return rs;
	}
	
	private String getCreateBarcodeFileCmd(String outputDir, Set<SampleSource> cellLibraries, IlluminaIndexingStrategy strategy) throws WaspException {
		String outputFile = "./" + outputDir + "/barcodes.txt";
		String retval = "echo -e \"" + getBarcodeFileHeader() + "\" > " + outputFile + "\n";
		for (SampleSource cellLib : cellLibraries) {
			Sample library = sampleService.getLibrary(cellLib);
			Adaptor a = adaptorService.getAdaptor(library);
			if (strategy.equals(IlluminaIndexingStrategy.TRUSEQ)) {
				retval += "echo -e \"" + a.getBarcodesequence() + "\\t\\t" + a.getName() + "\\t" + library.getName() + "\" >> " + outputFile + "\n";
			} else if (strategy.equals(IlluminaIndexingStrategy.TRUSEQ_DUAL)) {
				int sepIndex = a.getSequence().indexOf("-");
				if (sepIndex == -1) {
					String mess = "Not able to decode TRUSEQ_DUAL barcode " + a.getSequence();
					logger.error(mess);
					throw new WaspException(mess);
				}
				retval += "echo -e \"" + a.getBarcodesequence().substring(0, sepIndex) + "\\t" + a.getBarcodesequence().substring(sepIndex+1) + "\\t" + a.getName() + "\\t" + library.getName() + "\" >> " + outputFile + "\n";
			} else {
				String mess = "Unknown IlluminaIndexingStrategy: " + strategy.toString();
				logger.error(mess);
				throw new WaspException(mess);
			}
			 
		}
		return retval;
	}
	
	private String getBarcodeFileHeader() {
		return "barcode_sequence_1\\tbarcode_sequence_2\\tbarcode_name\\tlibrary_name";
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
	
	@Transactional("entityManager")
	public void registerBarcodeMetadata(Run run, GridResult result) throws WaspException {
		
		Map<Integer,Sample> indexedCellMap = sampleService.getIndexedCellsOnPlatformUnit(run.getPlatformUnit());
		
		//Document runInfo = illuminaService.getIlluminaRunXml(run);
		
		for (Integer index : indexedCellMap.keySet()) {
			
			Set<IlluminaIndexingStrategy> strategies = new HashSet<IlluminaIndexingStrategy>(); 
			
			Sample cell = indexedCellMap.get(index);
			List<SampleSource> cellLibraries = sampleService.getCellLibrariesForCell(cell);
			
			for (SampleSource ss : cellLibraries) {
				try{
					strategies.add(new IlluminaIndexingStrategy(illuminaService.getIndexingStrategy(ss).toString()));
				} catch (WaspException e){
					Sample lib = sampleService.getLibrary(ss);
					if (sampleService.isControlLibrary(lib))
						logger.info("Not able to retrieve indexing strategy for cell-library id=" + ss.getId() + 
								", a control library with id=" + lib.getId() + " (" + lib.getName() + "). Probably no index which is normal");
					else {
						logger.warn("Unable to retrieve indexing strategy for cell-library id=" + ss.getId() + 
								" (library id=" + lib.getId() + ") so not going to process cell library: " + e.getLocalizedMessage());
					}
				}
			}
			
			logger.trace("Lane " + index + ", registering " + strategies.size() + " strategies");
			
			for (IlluminaIndexingStrategy s : strategies) {
				logger.debug("going to register barcode metrics for run: " + run.getName() + " cell: " + cell.getId() + " (lane " + index + ") strategy: " + s.toString());
				String fileName = BARCODES_DIRECTORY + "/" +  s.toString() + "/L" + index + "/mets.txt";
				PicardMetricsParser p = new PicardMetricsParser(hostResolver, result, fileName);
				MetaHelper metahelper = new MetaHelper(PICARD_BARCODE_METRICS_AREA, SampleMeta.class);
				metahelper.setMetaValueByName(s.toString(), p.parseResult().toString());
				sampleService.getSampleMetaDao().setMeta((List<SampleMeta>) metahelper.getMetaList(), cell.getId());
			}
			
		}
		
	}
	
}
