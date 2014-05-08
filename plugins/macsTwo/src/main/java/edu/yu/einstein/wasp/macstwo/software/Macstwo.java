/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.macstwo.software;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.plugin.mps.grid.software.R;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
// Un-comment the following if using the plugin service
// import org.springframework.beans.factory.annotation.Autowired;
// import package edu.yu.einstein.wasp.macstwo.service. MacstwoService;




/**
 * dubin; see macs2: https://github.com/taoliu/MACS/ 
 */
public class Macstwo extends SoftwarePackage{

	// Un-comment the following if using the plugin service
	//@Autowired
	//MacstwoService  macstwoService;

	private static final long serialVersionUID = 6657173203004836355L;

	@Autowired
	private FileService fileService;
	
	public Macstwo() {
		setSoftwareVersion("2.0.10"); // TODO: Set this value. This default may also be overridden in wasp.site.properties
	}

	//note: test is same as treated, in macs2-speak (from the immunoprecipitated sample)
	public WorkUnit getPeaks(String prefixForFileName, List<FileHandle> testFileHandleList, List<FileHandle> controlFileHandleList, Map<String,Object> jobParametersMap){
		
		Assert.assertTrue(!testFileHandleList.isEmpty());
		
		WorkUnit w = prepareWorkUnit();	
		
		List<FileHandle> tempFileHandleList = new ArrayList<FileHandle>();
		tempFileHandleList.addAll(testFileHandleList);//THIS LIST MUST BE ADDED FIRST
		tempFileHandleList.addAll(controlFileHandleList);		
		w.setRequiredFiles(tempFileHandleList);
		
		StringBuilder tempCommand;
		
		String mergedTestBamFile = "";
		
		if(testFileHandleList.size()==1){
			mergedTestBamFile = "${" + WorkUnit.INPUT_FILE + "[0]}";
		}
		else if(testFileHandleList.size() > 1){
			mergedTestBamFile = "mergedTESTBamFile.bam";
			
			tempCommand = new StringBuilder();
			tempCommand.append("samtools merge " + mergedTestBamFile + " ");//mergedTESTBamFile.bam is the output of the merge; note that merge requires sorted files
			
			for(int i = 0; i < testFileHandleList.size(); i++){				
				tempCommand.append("${" + WorkUnit.INPUT_FILE + "["+i+"]} ");				
			}
			String command1 = new String(tempCommand);
			logger.debug("---- Will execute samtools merge mergedTESTBamFile.bam in1.bam in2.bam [....] for merging bams with command: ");
			logger.debug("---- "+command1);
			
			w.addCommand(command1);
			
		}
		/* moved immediately up, so this is no longer needed
		if(testFileHandleList.size()>1){
			mergedTestBamFile = "mergedTESTBamFile.bam";
		}
		else{
			mergedTestBamFile = "${" + WorkUnit.INPUT_FILE + "[0]}";
		}
		
		if(testFileHandleList.size()>1){
			tempCommand = new StringBuilder();
			tempCommand.append("samtools merge " + mergedTestBamFile + " ");//mergedTESTBamFile.bam is the output of the merge; note that merge requires sorted files
			
			for(int i = 0; i < testFileHandleList.size(); i++){				
				tempCommand.append("${" + WorkUnit.INPUT_FILE + "["+i+"]} ");				
			}
			String command1 = new String(tempCommand);
			logger.debug("---- Will execute samtools merge for merging bams with command: ");
			logger.debug("---- "+command1);
			
			w.addCommand(command1);
		}
		*/
		
		String totalCountMappedReads = "totalCountMappedReads.txt"; //output in this file will be a single number 
		
		tempCommand = new StringBuilder();
		tempCommand.append("samtools view -c -F 0x04 " + mergedTestBamFile + " > " + totalCountMappedReads);//count reads but skip unmapped reads
		String command2 = new String(tempCommand);
		logger.debug("---- Will execute samtools view -c -F 0x04 xxx.bam (or mergedTESTBamFile.bam) > totalCountMappedReads.txt to get total count of mapped reads  with command: ");
		logger.debug("---- "+command2);
		w.addCommand(command2);
		
		tempCommand = new StringBuilder();
		tempCommand.append("macs2 callpeak -t " + mergedTestBamFile);
				
		//macs2 can handle merging multiple test and/or multiple control files
		
		/////don't need this anymore, the -t mergedTestBamFile takes care of this
		/////for(int i = 0; i < testFileHandleList.size(); i++){
		/////	tempCommand.append("${" + WorkUnit.INPUT_FILE + "["+i+"]} ");
		/////}
		for(int i = testFileHandleList.size(); i < testFileHandleList.size() + controlFileHandleList.size(); i++){
			if(i==testFileHandleList.size()){
				tempCommand.append(" -c ");
			}
			tempCommand.append("${" + WorkUnit.INPUT_FILE + "["+i+"]} ");
		}
		
		for (String key : jobParametersMap.keySet()) {
	
			String opt = "";
			/*
			if(key.equalsIgnoreCase("broadPeakExpected")){//TODO: not yet an option on the forms
				opt = "--broad";
				if(jobParametersMap.get(key).toString().equalsIgnoreCase("yes"))
				{   
					tempCommand.append(" " + opt);
					continue;
				}
				
			}
			*/
			if(key.equalsIgnoreCase("pValueCutoff")){
				opt = "--pvalue";
				try{
					Double i = Double.parseDouble(jobParametersMap.get(key).toString());
				}
				catch(Exception e){
					continue;//not a number so accept default and get out
				}
			}
			if(key.equalsIgnoreCase("bandwidth")){//should really be sonication size
				opt = "--bw";
				try{
					Integer i = Integer.parseInt(jobParametersMap.get(key).toString());//Integer.parseInt(jobParameters.get(opt).toString());
				}
				catch(Exception e){//not a number so accept default and get out
					continue;
				}
			}
			if(key.equalsIgnoreCase("genomeSize")){
				opt = "--gsize";
				try{
					Double i = Double.parseDouble(jobParametersMap.get(key).toString());//use double since this will be in scientific notation
				}
				catch(Exception e){//not a number so accept default (size of human genome) and get out
					continue;
				}
			}
			if(key.equalsIgnoreCase("keepDup") ){//only yes or no are currently permitted on the forms
				opt = "--keep-dup";	
				if(jobParametersMap.get(key).toString().equalsIgnoreCase("no")){   //jobParameters.get(opt).toString().equalsIgnoreCase("no")){
					tempCommand.append(" " + opt + " 1");
					continue;
				}
				else if(jobParametersMap.get(key).toString().equalsIgnoreCase("yes")   //jobParameters.get(opt).toString().equalsIgnoreCase("yes") 
						|| 
						jobParametersMap.get(key).toString().equalsIgnoreCase("all")){
					tempCommand.append(" " + opt + " all");
					continue;
				}
				else if(jobParametersMap.get(key).toString().equalsIgnoreCase("auto")){//jobParameters.get(opt).toString().equalsIgnoreCase("auto")){
					tempCommand.append(" " + opt + " auto");
					continue;
				}
				else{
					try{
						Integer i = Integer.parseInt(jobParametersMap.get(key).toString());//Integer.parseInt(jobParameters.get(opt).toString());
					}
					catch(Exception e){//not a number, so accept default
						continue;
					}
				}
			}
			if(!opt.isEmpty()){
				tempCommand.append(" " + opt + " " + jobParametersMap.get(key).toString());
			}
		}
		
		tempCommand.append(" --name " + prefixForFileName);//The name string of the experiment. MACS will use this string NAME to create output files like 'NAME_peaks.xls', etc.
		
		tempCommand.append(" --bdg");//generates two bedGraph files
		
		String command3 = new String(tempCommand);
		logger.debug("---- Will execute macs2 for peakcalling with command: ");
		logger.debug("---- "+command3);
		
		w.addCommand(command3);
		
		String peaksFromMacs = prefixForFileName+"_peaks.narrowPeak"; //one of the output files from macs (bed6+4)
		String peaksInBed4Format = prefixForFileName+"_peaksBed4Format.narrowPeak"; //since bedtools coverage cannot deal with bed6+4 format (which the peaks.narrowPeak bed file is in), we must first convert, so lets convert to bed4
		
		tempCommand = new StringBuilder();
		tempCommand.append("awk -v OFS='\t' '{print $1, $2, $3, $4}' " + peaksFromMacs + " > " + peaksInBed4Format);
		String command4 = new String(tempCommand);
		logger.debug("---- Will execute awk to convert bed6+4 to bed4 using command: ");
		logger.debug("---- "+command4);
		w.addCommand(command4);
	
		String mappedReadsInPeaks = "mappedReadsInPeaks.bed";//column 5 will be the one we need (depth)
		tempCommand = new StringBuilder();
		//bedtools coverage appears to only use reads that are mapped for determining coverage - which is what we want (dubin observation)
		tempCommand.append("bedtools coverage -counts -abam " + mergedTestBamFile + " -b " + peaksInBed4Format + " > " + mappedReadsInPeaks);
		String command5 = new String(tempCommand);
		logger.debug("---- Will execute bedtools coverage to get coverage of number of reads in each peak using command: ");
		logger.debug("---- "+command5);
		w.addCommand(command5);
	
		String totalCountMappedReadsInPeaks = "totalCountMappedReadsInPeaks.txt";//output in this file will be a single number 
		tempCommand = new StringBuilder();
		tempCommand.append("awk '{sum += $5} END {print sum}' " + mappedReadsInPeaks + " > " + totalCountMappedReadsInPeaks);
		String command6 = new String(tempCommand);
		logger.debug("---- Will execute awk to sum up column 5 (mapped reads in each peak) from mappedReadsInPeaks.bed using command: ");
		logger.debug("---- "+command6);
		w.addCommand(command6);		
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		sd.add(this.getSoftwareDependencyByIname("samtools"));
		sd.add(this.getSoftwareDependencyByIname("bedtools"));
		w.setSoftwareDependencies(sd);

		logger.debug("----command has been set to workunit in getPeaks()");		
		return w;
	}
	public WorkUnit getModelPdf(FileHandle modelScriptFileHandle, String pdfFileName, String pngFileName){//as of 33-25-14, also will generate a .png from the resulting .pdf
		
		Assert.assertTrue(modelScriptFileHandle != null);
		
		WorkUnit w = prepareWorkUnit();
		
		List<FileHandle> tempFileHandleList = new ArrayList<FileHandle>();
		tempFileHandleList.add(modelScriptFileHandle);		
		w.setRequiredFiles(tempFileHandleList);
		
		String command = "Rscript ${" + WorkUnit.INPUT_FILE + "[0]}";
		logger.debug("---- Will execute RScript to convert model.r to model.pdf using command: ");
		logger.debug("---- "+command);
		
		w.setCommand(command);
		
		//String command2 = "convert ${" + WorkUnit.OUTPUT_FILE + "[0]} -append " + pngFileName; //this fails; convert needs to see .pdf, we think
		String command2 = "convert " +  pdfFileName + " -append " + pngFileName; //this may work; will test it now
		logger.debug("---- And Will subsequently execute ImageMagick.convert to convert model.pdf to model.png using command: ");
		logger.debug("---- "+command2);
		
		w.addCommand(command2);
		
		//w.setSoftwareDependencies(getSoftwareDependencies());
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this.getSoftwareDependencyByIname("imagemagick"));
		sd.add(this.getSoftwareDependencyByIname("rPackage"));
		w.setSoftwareDependencies(sd);
		
		logger.debug("----command has been set to workunit in getModelPdf");		
		return w;
	}
	private WorkUnit prepareWorkUnit() {
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);		
		w.setProcessMode(ProcessMode.MAX);		
		w.setMemoryRequirements(8);
		//w.setNumberOfTasks(1);//?????only important when ExecutionMode.Task_Array
				
		//w.setResultFiles(resultFiles);//may not be needed
		w.setSecureResults(true);
		
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		
		return w;
	}
}
