/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.plugin.mps.grid.software;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.plugin.fileformat.service.BamService;
import edu.yu.einstein.wasp.software.SoftwarePackage;


/**
 * @author dubin
 */

public class Samtools extends SoftwarePackage{
	
	private static final long serialVersionUID = -7208955782745292954L;
	
	public static final String UNIQUELY_ALIGNED_READ_COUNT_FILENAME = "uniquelyAlignedReadCount.txt";
	public static final String UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FILENAME = "uniquelyAlignedNonRedundantReadCount.txt";

	public Samtools() {
		
	}
	
	public String getUniquelyAlignedReadCountCmd(String bamFileName, String alignerSpecificBamTagIndicatingUniqueAlignment){
		String command = "";
		if(bamFileName==null || bamFileName.isEmpty()){
			return command;
		}
		if(alignerSpecificBamTagIndicatingUniqueAlignment != null && !alignerSpecificBamTagIndicatingUniqueAlignment.isEmpty()){
			command = "samtools view -F 0x4 " + bamFileName + " | awk 'BEGIN { c=0 } /" + alignerSpecificBamTagIndicatingUniqueAlignment + 
					"/ { c++ } END { print c }' > "   + UNIQUELY_ALIGNED_READ_COUNT_FILENAME;//includes duplicates
		}
		else{
			command = "samtools view -c -F 0x4 -q 1 " + bamFileName + " > " + UNIQUELY_ALIGNED_READ_COUNT_FILENAME;//includes duplicates
		}
		return command;
	}
	
	public String getUniquelyAlignedNonRedundantReadCountCmd(String bamFileName, String alignerSpecificBamTagIndicatingUniqueAlignment){
		String command = "";
		if(bamFileName==null || bamFileName.isEmpty()){
			return command;
		}
		if(alignerSpecificBamTagIndicatingUniqueAlignment != null && !alignerSpecificBamTagIndicatingUniqueAlignment.isEmpty()){
			command = "samtools view -F 0x404 " + bamFileName + " | awk 'BEGIN { c=0 } /" + alignerSpecificBamTagIndicatingUniqueAlignment + 
					"/ { c++ } END { print c }' > "   + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FILENAME;//excludes duplicates
		}
		else{
			command = "samtools view -c -F 0x404 -q 1 " + bamFileName + " > " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FILENAME;//excludes duplicates
		}
		return command;
	}
	
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
	

}
