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
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.SampleService;
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
	@Autowired
	private GenomeService genomeService;
	@Autowired
	private SampleService sampleService;

	public Macstwo() {
	}

	//note: test is same as treated, in macs2-speak (from the immunoprecipitated sample)
	public WorkUnit getPeaks(String peakType, int shortestReadLengthFromAllTestRuns, Sample ipSample, Sample controlSample, String prefixForFileName, List<FileHandle> testFileHandleList, List<FileHandle> controlFileHandleList, 
			Map<String,Object> jobParametersMap, String modelFileName, String pdfFileName, String pngFileName){
		
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
		
		tempCommand.append(" --name " + prefixForFileName);//The name string of the experiment. MACS will use this string NAME to create output files like 'NAME_peaks.xls', etc.
		
		tempCommand.append(" --bdg");//generates two bedGraph files
		//must test --trackline out first
		//tempCommand.append(" --trackline");//could be problem without control sample//--trackline  Tells MACS to include trackline with bedGraph files. (However Macs2 author suggestion is to convert bedGraph to bigWig, then show the smaller and faster binary bigWig file at UCSC genome browser, as well as downstream analysis.)
		
		//mappable or "effective" genome size
		//really should have this with the build , at least for those that are not hs, mm, dm, and ce (and sc, sp, tg). See below
		String mappableGenomeSize = this.getMappableGenomeSize(ipSample);
		if(mappableGenomeSize.isEmpty()){
			logger.debug("genomeSize cannot be empty for macs2 - this will cause an assert to fail");
		}
		Assert.assertTrue(!mappableGenomeSize.isEmpty());
		tempCommand.append(" --gsize " + mappableGenomeSize);
		
		//bandwidth : sonication size if macromolecule; insert size if library  (no, it's not 1/2 of this value, this according to Dayou; also, it's the fragment/sonication size of the IP that is important (no need to consider the control for this parameter)
		//old wasp used library insert; here we use a user-provided fragmentSize
		String fragmentSize = this.getFragmentSize(ipSample);
		if(!fragmentSize.isEmpty()){//if is empty, accept default value
			tempCommand.append(" --bw " + fragmentSize);
		}
		
		//tag size :  currently taken as smallest readLength from all IP's runs' readLengths. Would be better to have actual, average, readLength, but don't yet have that available.
		//Dayou suggested use only IP data, not include controls here
		if(shortestReadLengthFromAllTestRuns>0){//if 0 (indicating problem), simply allow MACS to calculate
			String tagSize = Integer.valueOf(shortestReadLengthFromAllTestRuns).toString();
			tempCommand.append(" --tsize " + tagSize);//size of sequencing tags
		}
		
		if( !peakType.equalsIgnoreCase("punctate") ){//not punctate, so must be broad or mixed peakType
			tempCommand.append(" --broad --broad-cutoff 0.1");//set for broad peaks and use default cutoff of 0.1
		}
		
		
		//this is now part of the parameters in the jobParametersMap
		//default q (minimum FDR) is 0.01; Genome Center requested 0.05; not sure what to do; this value will be used if p NOT set
		///tempCommand.append(" --qvalue 0.01 ");//currently take the default of q = 0.01 and p is empty
			
		for (String key : jobParametersMap.keySet()) {//example of key: macs2--qvalue (note that while the jobMeta key is macstwo.macs2--qvalue, the key in jobParameterMap is simply macs2--qvalue)
			if(key.startsWith("macs2")){//
				String parameterName = key.trim().replaceFirst("macs2", "");//parameterName might be set to, for example, --qvalue
				if(parameterName.isEmpty()){//should never occur
					continue;
				}
				String parameterValue = jobParametersMap.get(key).toString().trim();
				if(parameterValue.isEmpty()){//a parameter that does not have a value is not being used (for example, --pvalue; --extsize, which the user has not given a value and thus does not want to use) 
					continue;
				}
				else if(parameterValue.equals("_SET_OFF_")){//parameter is to be set off (which is default), such as --nomodel (similar to linux command: ls -a). So, continue.
					continue;
				}
				else if(parameterValue.equals("_SET_ON_")){//to set this parameter on, just use paramater's name
					tempCommand.append(" " + parameterName);
				}
				else{//typical parameter, such as --qvalue 0.01
					tempCommand.append(" " + parameterName + " " + parameterValue);
				}
				
			}
		}
			/* not option on forms
			if(key.equalsIgnoreCase("broadPeakExpected")){//TODO: not yet an option on the forms
				opt = "--broad";
				if(jobParametersMap.get(key).toString().equalsIgnoreCase("yes"))
				{   
					tempCommand.append(" " + opt);
					continue;
				}
				
			}
			
			//on the form, so dealt with above
			if(key.equalsIgnoreCase("pValueCutoff")){
				opt = "--pvalue";
				try{
					Double i = Double.parseDouble(jobParametersMap.get(key).toString());
				}
				catch(Exception e){
					continue;//not a number so accept default and get out
				}
			}
			
			/* as of 8-14-14, this is now dealt with above
			if(key.equalsIgnoreCase("genomeSize")){
				opt = "--gsize";
				try{
					Double i = Double.parseDouble(jobParametersMap.get(key).toString());//use double since this will be in scientific notation
				}
				catch(Exception e){//not a number so accept default (size of human genome) and get out
					continue;
				}
			}
			
			//on the form, so dealt with above
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
			*/
		
		
		
		String command3 = new String(tempCommand);
		logger.debug("---- Will execute macs2 for peakcalling with command: ");
		logger.debug("---- "+command3);
		
		w.addCommand(command3);
		
		String peaksFromMacs = "";
		String peaksInBed4Format = "";
		if(peakType.equalsIgnoreCase("punctate")){
			peaksFromMacs = prefixForFileName+"_peaks.narrowPeak"; //one of the output files from macs (bed6+4)
			peaksInBed4Format = prefixForFileName+"_peaksBed4Format.narrowPeak";//since bedtools coverage cannot deal with bed6+4 format (which the peaks.narrowPeak bed file is in), we must first convert, so lets convert to bed4
		}
		else{//broad peaks
			peaksFromMacs = prefixForFileName+"_peaks.broadPeak";
			peaksInBed4Format = prefixForFileName+"_peaksBed4Format.broadPeak"; //since bedtools coverage cannot deal with bed6+3 format (which the peaks.broadPeak bed file is in), we must first convert, so lets convert to bed4
		}
		
		tempCommand = new StringBuilder();
		tempCommand.append("awk -v OFS='\t' '{print $1, $2, $3, $4}' " + peaksFromMacs + " > " + peaksInBed4Format);
		String command4 = new String(tempCommand);
		logger.debug("---- Will execute awk to convert bed6+4 (or if not punctate, bed6+3) to bed4 using command: ");
		logger.debug("---- "+command4);
		w.addCommand(command4);
	
		String mappedReadsInPeaks = prefixForFileName+"_mappedReadsInPeaks.bed";//column 5 will be the one we need (depth)
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
		
		
		
		
		sd.add(this.getSoftwareDependencyByIname("imagemagick"));
		sd.add(this.getSoftwareDependencyByIname("rPackage"));
		//String new_command_1 = "Rscript " + prefixForFileName + "_model.r";
		String new_command_1 = "Rscript " + modelFileName;
		w.addCommand(new_command_1);
		//String new_command2 = "convert " +  prefixForFileName + "_model.pdf" + " -append " + prefixForFileName + "_model.png";
		String new_command2 = "convert " +  pdfFileName + " -append " + pngFileName;
		w.addCommand(new_command2);
			
		
		
		
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
		w.setSecureResults(true);
		//this line is irrelevant, as I'm writing over it in MacstwoTasklet.java
		//w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		
	/*	probably it's wise to use this, but not really needed, as it will automatically be set
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
	*/
		
		return w;
	}
	private String getMappableGenomeSize(Sample ipSample)  {
		/*	from MACS2: https://github.com/taoliu/MACS/
		 	-g/--gsize
			PLEASE assign this parameter to fit your needs!
			It's the mappable genome size or effective genome size which is defined as the genome size 
			which can be sequenced. Because of the repetitive features on the chromsomes, the actual 
			mappable genome size will be smaller than the original size, about 90% or 70% of the genome size. 
			The default hs -- 2.7e9 is recommended for UCSC human hg18 assembly. 
			Here are all precompiled parameters for effective genome size:
			hs = 2.7e9
			mm = 1.87e9
			ce = 9e7
			dm = 1.2e8
		 */
		//might be interesting: http://www.nature.com/nbt/journal/v27/n1/fig_tab/nbt.1518_T1.html
		//THIS IS IMPORTANT; you cannot simply use hs as a default genome size.
		String retValue = "";
		try{
			Build build = genomeService.getBuild(ipSample);
			String speciesName = build.getGenome().getOrganism().getName().replaceAll("\\s+", "").toLowerCase();//Homo sapiens to Homosapiens to homosapiens			
			if("homosapiens".equals(speciesName) || speciesName.contains("sapiens")){
				retValue = "hs";
			}
			else if("musmusculus".equals(speciesName) || speciesName.contains("musculus")){
				retValue = "mm";
			}
			else if("caenorhabditiselegans".equals(speciesName) || speciesName.contains("elegans") ){
				retValue = "ce";
			}
			else if("drosophilamelanogaster".equals(speciesName) || speciesName.contains("melanogaster")){
				retValue = "dm";
			}
			else if("saccharomycescerevisiae".equals(speciesName) || speciesName.contains("cerevisiae")){
				retValue = "1.1e7";  //taken from old WASP
			}
			else if("schizosaccharomycespombe".equals(speciesName) || speciesName.contains("pombe")){
				retValue = "1.2e7"; //taken from old WASP
			}
			else if("toxoplasmagondii".equals(speciesName) || speciesName.contains("gondii")){
				retValue = "6e7";  //taken from old WASP
			}			
		}catch(Exception e){logger.debug("exception getting build in Macstwo.java method getMappableGenomeSize()");}
		
		return retValue;
	}
	private String getFragmentSize(Sample ipSample)  {
		String fragmentSize = "";
		for(SampleMeta sm : ipSample.getSampleMeta()){
			if(sm.getK().equalsIgnoreCase("chipseqDna.fragmentSize")){
				fragmentSize = sm.getV();
			}
		}
		return fragmentSize;
	}
	
}
