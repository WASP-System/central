/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.picard.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.picard.service.PicardService;

import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

@Service
@Transactional("entityManager")
public class PicardServiceImpl extends WaspServiceImpl implements PicardService {
	@Autowired
	SampleService sampleService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}

	public void setAlignmentMetrics(SampleSource cellLib, JSONObject json)throws MetadataException{
		sampleService.setLibraryOnCellMeta(cellLib, "bamFile", "alignmentMetrics", json.toString());
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
		
		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.SINGLE);
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
		String percentDuplication = "";//this value is really a fraction, so store in fractionDuplicated
		String fractionDuplicated = "";//identical to percentDuplication, just provided with a more descriptive name
				
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
				picardDedupMetricsMap.put("unpairedReads", unpairedReadsExamined);//unpairedReadsExamined
				readPairsExamined	= stringArray[2];
				picardDedupMetricsMap.put("pairedReads", readPairsExamined);//readPairsExamined
				unmappedReads = stringArray[3];
				picardDedupMetricsMap.put("unmappedReads", unmappedReads);//unmappedReads
				unpairedReadDuplicates = stringArray[4];
				picardDedupMetricsMap.put("unpairedReadDuplicates", unpairedReadDuplicates);//unpairedReadDuplicates
				readPairDuplicates	= stringArray[5];
				picardDedupMetricsMap.put("pairedReadDuplicates", readPairDuplicates);//readPairDuplicates
				readPairOpticalDuplicates = stringArray[6];
				picardDedupMetricsMap.put("pairedReadOpticalDuplicates", readPairOpticalDuplicates);//readPairOpticalDuplicates
				percentDuplication = stringArray[7];
				fractionDuplicated = percentDuplication;//percentDuplication is actually a fraction duplicated
				picardDedupMetricsMap.put("percentDuplicated", percentDuplication);//percentDuplication
				picardDedupMetricsMap.put("fractionDuplicated", fractionDuplicated);//fractionDuplicated
				logger.debug("finished reading from the dedupMetrics file");
				
				//work up derived values
				Integer mappedReads_integer = Integer.valueOf(unpairedReadsExamined) + Integer.valueOf(readPairsExamined);
				mappedReads = mappedReads_integer.toString();
				picardDedupMetricsMap.put("mappedReads", mappedReads);
				Integer unmappedReads_integer = Integer.valueOf(unmappedReads);
				Integer totalReads_integer = mappedReads_integer + unmappedReads_integer;
				totalReads = totalReads_integer.toString();
				picardDedupMetricsMap.put("totalReads", totalReads);
				
				Double fractionMapped_double = 0.0;
				fractionMapped = fractionMapped_double.toString();
				if(mappedReads_integer>0 && totalReads_integer>0){
					fractionMapped_double = (double) mappedReads_integer / totalReads_integer;
					DecimalFormat myFormat = new DecimalFormat("0.000000");
					fractionMapped = myFormat.format(fractionMapped_double);						
				}					
				picardDedupMetricsMap.put("fractionMapped", fractionMapped);
				
				Integer duplicateReads_integer = Integer.valueOf(unpairedReadDuplicates) + Integer.valueOf(readPairDuplicates);
				duplicateReads = duplicateReads_integer.toString();
				picardDedupMetricsMap.put("duplicateReads", duplicateReads);

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

	public Map<String,String> getUniquelyAlignedReadCountMetrics(String uniquelyAlignedReadCountfilename, String uniquelyAlignedNonRedundantReadCountfilename,String scratchDirectory, GridHostResolver gridHostResolver){
		Map<String,String> uniquelyAlignedReadCountMetricsMap = new HashMap<String,String>();
		return uniquelyAlignedReadCountMetricsMap;
		
	}
	

}
