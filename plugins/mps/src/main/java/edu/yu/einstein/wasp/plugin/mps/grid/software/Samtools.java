/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.plugin.mps.grid.software;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.plugin.fileformat.service.BamService;
import edu.yu.einstein.wasp.software.SoftwarePackage;


/**
 * @author dubin
 */

public class Samtools extends SoftwarePackage{
	
	private static final long serialVersionUID = -7208955782745292954L;
	
	//private static final String UNIQUELY_ALIGNED_READ_COUNT_FILENAME = "uniquelyAlignedReadCount.txt";
	//private static final String UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FILENAME = "uniquelyAlignedNonRedundantReadCount.txt";
	/* as of 10-9-14, we (andy,brent) decided to only give NRF from ALL unique reads in the bam file
	private static final String TEMP_SAM_READS_WITHOUT_HEADER_FILENAME = "tempSamFileWithAllReadsAndWithoutHeader.sam";
	
	private static final String TEMP_SAM_2M_READS_FILENAME = "tempSamFileWith2MReads.sam";
	private static final String UNIQUELY_ALIGNED_READ_COUNT_FROM_2M_READS_FILENAME = "uniquelyAlignedReadCountFrom2MReads.txt";
	private static final String UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_2M_READS_FILENAME = "uniquelyAlignedNonRedundantReadCountFrom2MReads.txt";

	private static final String TEMP_SAM_5M_READS_FILENAME = "tempSamFileWith5MReads.sam";
	private static final String UNIQUELY_ALIGNED_READ_COUNT_FROM_5M_READS_FILENAME = "uniquelyAlignedReadCountFrom5MReads.txt";
	private static final String UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_5M_READS_FILENAME = "uniquelyAlignedNonRedundantReadCountFrom5MReads.txt";

	private static final String TEMP_SAM_10M_READS_FILENAME = "tempSamFileWith10MReads.sam";
	private static final String UNIQUELY_ALIGNED_READ_COUNT_FROM_10M_READS_FILENAME = "uniquelyAlignedReadCountFrom10MReads.txt";
	private static final String UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_10M_READS_FILENAME = "uniquelyAlignedNonRedundantReadCountFrom10MReads.txt";

	private static final String TEMP_SAM_20M_READS_FILENAME = "tempSamFileWith20MReads.sam";
	private static final String UNIQUELY_ALIGNED_READ_COUNT_FROM_20M_READS_FILENAME = "uniquelyAlignedReadCountFrom20MReads.txt";
	private static final String UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_20M_READS_FILENAME = "uniquelyAlignedNonRedundantReadCountFrom20MReads.txt";

	private static final String TEMP_SAM_ALL_READS_FILENAME = "tempSamFileWithAllReads.sam";
	*/
	
	private static final String UNIQUELY_ALIGNED_READ_COUNT_FROM_ALL_READS_FILENAME = "uniquelyAlignedReadCountFromAllReads.txt";
	private static final String UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_ALL_READS_FILENAME = "uniquelyAlignedNonRedundantReadCountFromAllReads.txt";

	public Samtools() {
		
	}
	
	public List<String> getCommandsForNonRedundantFraction(String bamFileName){
		List<String> commandList = new ArrayList<String>();
		if(bamFileName==null || bamFileName.isEmpty()){
			return commandList;
		}
		String command1 = "samtools view -c -q 1 " + bamFileName + " > " + UNIQUELY_ALIGNED_READ_COUNT_FROM_ALL_READS_FILENAME;
		commandList.add(command1);
		String command2 = "samtools view -c -q 1 -F 0x584 " + bamFileName + " > " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_ALL_READS_FILENAME;
		commandList.add(command2);
		return commandList;
	}
	
	public Map<String,String> getUniquelyAlignedReadCountMetrics(String scratchDirectory, GridHostResolver gridHostResolver)throws Exception{
		
		logger.debug("entering getUniquelyAlignedReadCountMetrics (newly written for NRF from only ALL reads)");
		
		Map<String,String> uniquelyAlignedReadCountMetricsMap = new HashMap<String,String>();
		
		String uniqueReadsFromAll = "";//uniquely mapped reads
		String uniqueNonRedundantReadsFromAll = "";//non-redundant reads from all
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setProcessMode(ProcessMode.SINGLE);
		GridWorkService workService = gridHostResolver.getGridWorkService(c);
		GridTransportConnection transportConnection = workService.getTransportConnection();
		c.setWorkingDirectory(scratchDirectory);
		logger.debug("setting cat command in getPicardDedupMetrics");
		WorkUnit w = new WorkUnit(c);
		w.addCommand("cat " + UNIQUELY_ALIGNED_READ_COUNT_FROM_ALL_READS_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_ALL_READS_FILENAME );

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
			if (line == null){
				keepReading = false;
			}
			else if (lineNumber == 1){
				uniqueReadsFromAll = line.replaceAll("\\n", "");//just in case there is a trailing new line				
				logger.debug("uniqueReadsFromAll = " + uniqueReadsFromAll);
			} else if (lineNumber == 2){
				uniqueNonRedundantReadsFromAll = line.replaceAll("\\n", "");//just in case there is a trailing new line;				
				logger.debug("uniqueNonRedundantReadsFromAll = " + uniqueNonRedundantReadsFromAll);
			}
			else {
				keepReading = false;
			}
			 
		}
		br.close();	
		
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_ALL, uniqueReadsFromAll);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_ALL, uniqueNonRedundantReadsFromAll);
		
		//ALL (another way)
		Double fractionUniqueNonRedundantFromAll_double = 0.0;
		String fractionUniqueNonRedundantFromAll = fractionUniqueNonRedundantFromAll_double.toString();
		if(!uniqueReadsFromAll.isEmpty() && !uniqueNonRedundantReadsFromAll.isEmpty() ){
			Integer uniqueReadsFromAll_integer = Integer.valueOf(uniqueReadsFromAll);
			Integer uniqueNonRedundantReadsFromAll_integer = Integer.valueOf(uniqueNonRedundantReadsFromAll);		
			if(uniqueReadsFromAll_integer>0 && uniqueNonRedundantReadsFromAll_integer>0){
				fractionUniqueNonRedundantFromAll_double = (double) uniqueNonRedundantReadsFromAll_integer / uniqueReadsFromAll_integer;
				DecimalFormat myFormat = new DecimalFormat("0.000000");
				fractionUniqueNonRedundantFromAll = myFormat.format(fractionUniqueNonRedundantFromAll_double);						
			}	
		}
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_ALL, fractionUniqueNonRedundantFromAll);

		logger.debug("leaving getUniquelyAlignedReadCountMetrics");
		return uniquelyAlignedReadCountMetricsMap;
		
	}
	
	/* as of 10-9-14, we (andy,brent) decided to only give NRF from ALL unique reads in the bam file
	 
	 
	public List<String> getCommandsForNonRedundantFraction(String bamFileName, String alignerSpecificBamTagIndicatingUniqueAlignment){
		List<String> commandList = new ArrayList<String>();
		if(bamFileName==null || bamFileName.isEmpty()){
			return commandList;
		}
		//commandList.addAll(getCommandsForNonRedundantFractionFromAllReads(bamFileName, alignerSpecificBamTagIndicatingUniqueAlignment));
		commandList.addAll(getCommandsForNonRedundantFractionFrom2Mand5Mand10Mand20MandAllReads(bamFileName, alignerSpecificBamTagIndicatingUniqueAlignment));		
		return commandList;
	}
	
	
	
	private List<String> getCommandsForNonRedundantFractionFrom2Mand5Mand10Mand20MandAllReads(String bamFileName, String alignerSpecificBamTagIndicatingUniqueAlignment){
		
		List<String> commandList = new ArrayList<String>();
		if(bamFileName==null || bamFileName.isEmpty()){
			return commandList;
		}
		//this code was added 8-14-14 to replace and improve code that is commented out below
		String command0 = "samtools view -H -o " + TEMP_SAM_ALL_READS_FILENAME + " " + bamFileName;//-H header only; -o is followed by name of output file
		commandList.add(command0);		
		String command1 = "samtools view -H -o " + TEMP_SAM_2M_READS_FILENAME + " " + bamFileName;//-H header only; -o is followed by name of output file
		commandList.add(command1);		
		String command2 = "samtools view -H -o " + TEMP_SAM_5M_READS_FILENAME + " " + bamFileName;
		commandList.add(command2);
		String command3 = "samtools view -H -o " + TEMP_SAM_10M_READS_FILENAME + " " + bamFileName;
		commandList.add(command3);
		String command4 = "samtools view -H -o " + TEMP_SAM_20M_READS_FILENAME + " " + bamFileName;
		commandList.add(command4);
		//next add uniquely mapped reads to the appropriate header-containing file 
		if(alignerSpecificBamTagIndicatingUniqueAlignment != null && !alignerSpecificBamTagIndicatingUniqueAlignment.isEmpty()){
			//here , we first take all reads, then filter AND count with awk in command 6
			String command5 = "samtools view -o "  + TEMP_SAM_READS_WITHOUT_HEADER_FILENAME + " " + bamFileName;//this discrete step appears to be needed on the cluster, as opposed to using a pipe into command 6
			commandList.add(command5);			
			String command6 = "awk 'BEGIN { c=0 } /XT:A:U/ { if(c < 2000000){print >> \"" + TEMP_SAM_2M_READS_FILENAME + "\";} if(c < 5000000){print >> \"" + TEMP_SAM_5M_READS_FILENAME + "\";} if(c < 10000000){print >> \"" + TEMP_SAM_10M_READS_FILENAME + "\";} if(c < 20000000){print >> \"" + TEMP_SAM_20M_READS_FILENAME + "\";}  print >> \"" + TEMP_SAM_ALL_READS_FILENAME + "\"; c++; }' " + TEMP_SAM_READS_WITHOUT_HEADER_FILENAME;
			commandList.add(command6);
		}
		else{
			//by contrast, here, we first filter with samtools view -q 1, then count with awk in command 6
			String command5 = "samtools view -q 1 -o "  + TEMP_SAM_READS_WITHOUT_HEADER_FILENAME + " " + bamFileName;//this discrete step appears to be needed on the cluster, as opposed to using a pipe into command 6
			commandList.add(command5);
			String command6 = "awk 'BEGIN { c=0 } { if(c < 2000000){print >> \"" + TEMP_SAM_2M_READS_FILENAME + "\";} if(c < 5000000){print >> \"" + TEMP_SAM_5M_READS_FILENAME + "\";} if(c < 10000000){print >> \"" + TEMP_SAM_10M_READS_FILENAME + "\";} if(c < 20000000){print >> \"" + TEMP_SAM_20M_READS_FILENAME + "\";} print >> \"" + TEMP_SAM_ALL_READS_FILENAME + "\"; c++; }' " + TEMP_SAM_READS_WITHOUT_HEADER_FILENAME;
			commandList.add(command6);			
		}
		
		//get the total read counts and then the non-redundant read counts into appropriate files for storage
		//so for example: total count in TEMP_SAM_2M_READS_FILENAME should be 2M so UNIQUELY_ALIGNED_READ_COUNT_FROM_2M_READS_FILENAME should contain the number 2000000
		//and UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_2M_READS_FILENAME will contain a number less than 2M (the non-redundant count from those 2M unique reads).
		//2M
		String command11 = "samtools view -S -c  " + TEMP_SAM_2M_READS_FILENAME + " > " + UNIQUELY_ALIGNED_READ_COUNT_FROM_2M_READS_FILENAME;//-c means output a count
		commandList.add(command11);
		String command12 = "samtools view -S -c -F 0x400 " + TEMP_SAM_2M_READS_FILENAME + " > " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_2M_READS_FILENAME;//-F 0x400 remove redundant reads (PCR or optical duplicate)
		commandList.add(command12);
		//5M
		String command13 = "samtools view -S -c  " + TEMP_SAM_5M_READS_FILENAME + " > " + UNIQUELY_ALIGNED_READ_COUNT_FROM_5M_READS_FILENAME;
		commandList.add(command13);
		String command14 = "samtools view -S -c -F 0x400 " + TEMP_SAM_5M_READS_FILENAME + " > " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_5M_READS_FILENAME;
		commandList.add(command14);
		//10M
		String command15 = "samtools view -S -c  " + TEMP_SAM_10M_READS_FILENAME + " > " + UNIQUELY_ALIGNED_READ_COUNT_FROM_10M_READS_FILENAME;
		commandList.add(command15);
		String command16 = "samtools view -S -c -F 0x400 " + TEMP_SAM_10M_READS_FILENAME + " > " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_10M_READS_FILENAME;
		commandList.add(command16);
		//20M
		String command17 = "samtools view -S -c  " + TEMP_SAM_20M_READS_FILENAME + " > " + UNIQUELY_ALIGNED_READ_COUNT_FROM_20M_READS_FILENAME;
		commandList.add(command17);
		String command18 = "samtools view -S -c -F 0x400 " + TEMP_SAM_20M_READS_FILENAME + " > " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_20M_READS_FILENAME;
		commandList.add(command18);
		//ALL
		String command19 = "samtools view -S -c  " + TEMP_SAM_ALL_READS_FILENAME + " > " + UNIQUELY_ALIGNED_READ_COUNT_FROM_ALL_READS_FILENAME;
		commandList.add(command19);
		String command20 = "samtools view -S -c -F 0x400 " + TEMP_SAM_ALL_READS_FILENAME + " > " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_ALL_READS_FILENAME;
		commandList.add(command20);
				
		return commandList;
	}	
	
	public Map<String,String> getUniquelyAlignedReadCountMetrics(String scratchDirectory, GridHostResolver gridHostResolver)throws Exception{
		
		logger.debug("entering getUniquelyAlignedReadCountMetrics");
		
		Map<String,String> uniquelyAlignedReadCountMetricsMap = new HashMap<String,String>();
		
		//String uniqueReads = "";//all the uniquely mapped reads from the bam file
		//String uniqueNonRedundantReads = "";	//all the non-redundant uniquely mapped reads from the bam file	
		String uniqueReadsFrom10M = "";//the first 10M uniquely mapped reads
		String uniqueNonRedundantReadsFrom10M = "";//out of uniqueReadsFrom10M
		String uniqueReadsFrom20M = "";
		String uniqueNonRedundantReadsFrom20M = "";		
		String uniqueReadsFrom2M = "";
		String uniqueNonRedundantReadsFrom2M = "";
		String uniqueReadsFrom5M = "";//the first 5M uniquely mapped reads
		String uniqueNonRedundantReadsFrom5M = "";//non-redundant reads out of the first 5M uniquely mapped reads
		String uniqueReadsFromAll = "";//uniquely mapped reads
		String uniqueNonRedundantReadsFromAll = "";//non-redundant reads from all
		
		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.SINGLE);
		GridWorkService workService = gridHostResolver.getGridWorkService(w);
		GridTransportConnection transportConnection = workService.getTransportConnection();
		w.setWorkingDirectory(scratchDirectory);
		logger.debug("setting cat command in getPicardDedupMetrics");
		
		//w.addCommand("cat " + UNIQUELY_ALIGNED_READ_COUNT_FILENAME );
		//w.addCommand("cat " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_READ_COUNT_FROM_10M_READS_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_10M_READS_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_READ_COUNT_FROM_20M_READS_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_20M_READS_FILENAME );		
		w.addCommand("cat " + UNIQUELY_ALIGNED_READ_COUNT_FROM_2M_READS_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_2M_READS_FILENAME );		
		w.addCommand("cat " + UNIQUELY_ALIGNED_READ_COUNT_FROM_5M_READS_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_5M_READS_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_READ_COUNT_FROM_ALL_READS_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_ALL_READS_FILENAME );

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
			if (line == null){
				keepReading = false;
			}
			//else if (lineNumber == 1){
			//	uniqueReads = line.replaceAll("\\n", "");//just in case there is a trailing new line				
			//	logger.debug("uniqueReads = " + uniqueReads);
			//} else if (lineNumber == 2){
			//	uniqueNonRedundantReads = line.replaceAll("\\n", "");//just in case there is a trailing new line;				
			//	logger.debug("uniqueNonRedundantReads = " + uniqueNonRedundantReads);
			//} 
			else if (lineNumber == 1){//3){
				uniqueReadsFrom10M = line.replaceAll("\\n", "");//just in case there is a trailing new line				
				logger.debug("uniqueReadsFrom10M = " + uniqueReadsFrom10M);
			} else if (lineNumber == 2){//4){
				uniqueNonRedundantReadsFrom10M = line.replaceAll("\\n", "");//just in case there is a trailing new line;				
				logger.debug("uniqueNonRedundantReadsFrom10M = " + uniqueNonRedundantReadsFrom10M);
			} else if (lineNumber == 3){//5){
				uniqueReadsFrom20M = line.replaceAll("\\n", "");//just in case there is a trailing new line				
				logger.debug("uniqueReadsFrom20M = " + uniqueReadsFrom20M);
			} else if (lineNumber == 4){//6){
				uniqueNonRedundantReadsFrom20M = line.replaceAll("\\n", "");//just in case there is a trailing new line;				
				logger.debug("uniqueNonRedundantReadsFrom20M = " + uniqueNonRedundantReadsFrom20M);
			} else if (lineNumber == 5){//7){
				uniqueReadsFrom2M = line.replaceAll("\\n", "");//just in case there is a trailing new line				
				logger.debug("uniqueReadsFrom2M = " + uniqueReadsFrom2M);
			} else if (lineNumber == 6){//8){
				uniqueNonRedundantReadsFrom2M = line.replaceAll("\\n", "");//just in case there is a trailing new line;				
				logger.debug("uniqueNonRedundantReadsFrom2M = " + uniqueNonRedundantReadsFrom2M);
			} else if (lineNumber == 7){//9){
				uniqueReadsFrom5M = line.replaceAll("\\n", "");//just in case there is a trailing new line				
				logger.debug("uniqueReadsFrom5M = " + uniqueReadsFrom5M);
			} else if (lineNumber == 8){//10){
				uniqueNonRedundantReadsFrom5M = line.replaceAll("\\n", "");//just in case there is a trailing new line;				
				logger.debug("uniqueNonRedundantReadsFrom5M = " + uniqueNonRedundantReadsFrom5M);
			} else if (lineNumber == 9){//11){
				uniqueReadsFromAll = line.replaceAll("\\n", "");//just in case there is a trailing new line				
				logger.debug("uniqueReadsFromAll = " + uniqueReadsFromAll);
			} else if (lineNumber == 10){//12){
				uniqueNonRedundantReadsFromAll = line.replaceAll("\\n", "");//just in case there is a trailing new line;				
				logger.debug("uniqueNonRedundantReadsFromAll = " + uniqueNonRedundantReadsFromAll);
			}
			else {
				keepReading = false;
			}
			 
		}
		br.close();	
		
		//uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS, uniqueReads);
		//uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS, uniqueNonRedundantReads);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_10M, uniqueReadsFrom10M);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_10M, uniqueNonRedundantReadsFrom10M);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_20M, uniqueReadsFrom20M);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_20M, uniqueNonRedundantReadsFrom20M);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_2M, uniqueReadsFrom2M);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_2M, uniqueNonRedundantReadsFrom2M);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_5M, uniqueReadsFrom5M);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_5M, uniqueNonRedundantReadsFrom5M);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_ALL, uniqueReadsFromAll);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_ALL, uniqueNonRedundantReadsFromAll);

		
		//all
		//Double fractionUniqueNonRedundant_double = 0.0;
		//String fractionUniqueNonRedundant = fractionUniqueNonRedundant_double.toString();
		//if(!uniqueReads.isEmpty()&&!uniqueNonRedundantReads.isEmpty()){
		///	Integer uniqueReads_integer = Integer.valueOf(uniqueReads);
		//	Integer uniqueNonRedundantReads_integer = Integer.valueOf(uniqueNonRedundantReads);		
		//	if(uniqueReads_integer>0 && uniqueNonRedundantReads_integer>0){
		//		fractionUniqueNonRedundant_double = (double) uniqueNonRedundantReads_integer / uniqueReads_integer;
		//		DecimalFormat myFormat = new DecimalFormat("0.000000");
		//		fractionUniqueNonRedundant = myFormat.format(fractionUniqueNonRedundant_double);						
		//	}
		//}		
		//uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT, fractionUniqueNonRedundant);
		
		
		//10M
		Double fractionUniqueNonRedundantFrom10M_double = 0.0;
		String fractionUniqueNonRedundantFrom10M = fractionUniqueNonRedundantFrom10M_double.toString();
		if(!uniqueReadsFrom10M.isEmpty() && !uniqueNonRedundantReadsFrom10M.isEmpty() ){
			Integer uniqueReadsFrom10M_integer = Integer.valueOf(uniqueReadsFrom10M);
			Integer uniqueNonRedundantReadsFrom10M_integer = Integer.valueOf(uniqueNonRedundantReadsFrom10M);		
			if(uniqueReadsFrom10M_integer>0 && uniqueNonRedundantReadsFrom10M_integer>0){
				fractionUniqueNonRedundantFrom10M_double = (double) uniqueNonRedundantReadsFrom10M_integer / uniqueReadsFrom10M_integer;
				DecimalFormat myFormat = new DecimalFormat("0.000000");
				fractionUniqueNonRedundantFrom10M = myFormat.format(fractionUniqueNonRedundantFrom10M_double);						
			}	
		}
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_10M, fractionUniqueNonRedundantFrom10M);

		//20M
		Double fractionUniqueNonRedundantFrom20M_double = 0.0;
		String fractionUniqueNonRedundantFrom20M = fractionUniqueNonRedundantFrom20M_double.toString();
		if(!uniqueReadsFrom20M.isEmpty() && !uniqueNonRedundantReadsFrom20M.isEmpty() ){
			Integer uniqueReadsFrom20M_integer = Integer.valueOf(uniqueReadsFrom20M);
			Integer uniqueNonRedundantReadsFrom20M_integer = Integer.valueOf(uniqueNonRedundantReadsFrom20M);		
			if(uniqueReadsFrom20M_integer>0 && uniqueNonRedundantReadsFrom20M_integer>0){
				fractionUniqueNonRedundantFrom20M_double = (double) uniqueNonRedundantReadsFrom20M_integer / uniqueReadsFrom20M_integer;
				DecimalFormat myFormat = new DecimalFormat("0.000000");
				fractionUniqueNonRedundantFrom20M = myFormat.format(fractionUniqueNonRedundantFrom20M_double);						
			}	
		}
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_20M, fractionUniqueNonRedundantFrom20M);

		//2M
		Double fractionUniqueNonRedundantFrom2M_double = 0.0;
		String fractionUniqueNonRedundantFrom2M = fractionUniqueNonRedundantFrom2M_double.toString();
		if(!uniqueReadsFrom2M.isEmpty() && !uniqueNonRedundantReadsFrom2M.isEmpty() ){
			Integer uniqueReadsFrom2M_integer = Integer.valueOf(uniqueReadsFrom2M);
			Integer uniqueNonRedundantReadsFrom2M_integer = Integer.valueOf(uniqueNonRedundantReadsFrom2M);		
			if(uniqueReadsFrom2M_integer>0 && uniqueNonRedundantReadsFrom2M_integer>0){
				fractionUniqueNonRedundantFrom2M_double = (double) uniqueNonRedundantReadsFrom2M_integer / uniqueReadsFrom2M_integer;
				DecimalFormat myFormat = new DecimalFormat("0.000000");
				fractionUniqueNonRedundantFrom2M = myFormat.format(fractionUniqueNonRedundantFrom2M_double);						
			}	
		}
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_2M, fractionUniqueNonRedundantFrom2M);
		
		//5M
		Double fractionUniqueNonRedundantFrom5M_double = 0.0;
		String fractionUniqueNonRedundantFrom5M = fractionUniqueNonRedundantFrom5M_double.toString();
		if(!uniqueReadsFrom5M.isEmpty() && !uniqueNonRedundantReadsFrom5M.isEmpty() ){
			Integer uniqueReadsFrom5M_integer = Integer.valueOf(uniqueReadsFrom5M);
			Integer uniqueNonRedundantReadsFrom5M_integer = Integer.valueOf(uniqueNonRedundantReadsFrom5M);		
			if(uniqueReadsFrom5M_integer>0 && uniqueNonRedundantReadsFrom5M_integer>0){
				fractionUniqueNonRedundantFrom5M_double = (double) uniqueNonRedundantReadsFrom5M_integer / uniqueReadsFrom5M_integer;
				DecimalFormat myFormat = new DecimalFormat("0.000000");
				fractionUniqueNonRedundantFrom5M = myFormat.format(fractionUniqueNonRedundantFrom5M_double);						
			}	
		}
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_5M, fractionUniqueNonRedundantFrom5M);

		//ALL (another way)
		Double fractionUniqueNonRedundantFromAll_double = 0.0;
		String fractionUniqueNonRedundantFromAll = fractionUniqueNonRedundantFromAll_double.toString();
		if(!uniqueReadsFromAll.isEmpty() && !uniqueNonRedundantReadsFromAll.isEmpty() ){
			Integer uniqueReadsFromAll_integer = Integer.valueOf(uniqueReadsFromAll);
			Integer uniqueNonRedundantReadsFromAll_integer = Integer.valueOf(uniqueNonRedundantReadsFromAll);		
			if(uniqueReadsFromAll_integer>0 && uniqueNonRedundantReadsFromAll_integer>0){
				fractionUniqueNonRedundantFromAll_double = (double) uniqueNonRedundantReadsFromAll_integer / uniqueReadsFromAll_integer;
				DecimalFormat myFormat = new DecimalFormat("0.000000");
				fractionUniqueNonRedundantFromAll = myFormat.format(fractionUniqueNonRedundantFromAll_double);						
			}	
		}
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_ALL, fractionUniqueNonRedundantFromAll);

		logger.debug("leaving getUniquelyAlignedReadCountMetrics");
		return uniquelyAlignedReadCountMetricsMap;
		
	}
	*/
	
	/* very old; not used
	private List<String> getCommandsForNonRedundantFractionFromAllReads(String bamFileName, String alignerSpecificBamTagIndicatingUniqueAlignment){
		
		List<String> commandList = new ArrayList<String>();
		if(bamFileName==null || bamFileName.isEmpty()){
			return commandList;
		}
		//order doesn't really matter in this case
		commandList.add(this.getUniquelyAlignedReadCountCmd(bamFileName, alignerSpecificBamTagIndicatingUniqueAlignment));
		commandList.add(this.getUniquelyAlignedNonRedundantReadCountCmd(bamFileName, alignerSpecificBamTagIndicatingUniqueAlignment));
		return commandList;
	}
	
	private String getUniquelyAlignedReadCountCmd(String bamFileName, String alignerSpecificBamTagIndicatingUniqueAlignment){
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
	
	private String getUniquelyAlignedNonRedundantReadCountCmd(String bamFileName, String alignerSpecificBamTagIndicatingUniqueAlignment){
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
	*/

}
