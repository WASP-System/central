/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.macstwo.software;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Assert;

import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.Sample;
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

	@Autowired
	private FileService fileService;
	
	public Macstwo() {
		setSoftwareVersion("2.0.10"); // TODO: Set this value. This default may also be overridden in wasp.site.properties
	}

	//note: test is same as treated, in macs2-speak (from the immunoprecipitated sample)
	public WorkUnit getPeaks(String prefixForFileName, List<FileHandle> testFileHandleList, List<FileHandle> controlFileHandleList, Map<String,Object> jobParameters){
		
		Assert.assertTrue(!testFileHandleList.isEmpty());
		
		WorkUnit w = prepareWorkUnit(testFileHandleList, controlFileHandleList);
		
		StringBuilder tempCommand = new StringBuilder();
		tempCommand.append("macs2");
		String method = " callpeak";//TODO: needs to be a parameter as some point
		tempCommand.append(method);
		
		//macs2 can handle merging multiple test and/or multiple control files
		int indexSoFar = 0;
		for(int i = 0; i < testFileHandleList.size(); i++){
			if(i==0){
				tempCommand.append(" -t ");
			}
			tempCommand.append("${" + WorkUnit.INPUT_FILE + "["+indexSoFar+"]} ");
			indexSoFar++;
		}
		for(int i = 0; i < controlFileHandleList.size(); i++){
			if(i==0){
				tempCommand.append(" -c ");
			}
			tempCommand.append("${" + WorkUnit.INPUT_FILE + "["+indexSoFar+"]} ");
			indexSoFar++;
		}
		
		//tempCommand.append("-f BAM ");//this could actually be figured out by macs2
		
		for (String key : jobParameters.keySet()) {
		//for(JobMeta jobMeta : jobMetaList){//could use the jobPar
			//String opt = jobMeta.getK();
		
			//if (!opt.startsWith("macsPeakcaller")){
			//	continue;
			//}
			//String key = opt.replace("macsPeakcaller.", "");
			String opt = "";
			if(key.equalsIgnoreCase("pValueCutoff")){
				opt = "--pvalue";
				try{
					Double i = Double.parseDouble(jobParameters.get(key).toString());
				}
				catch(Exception e){
					continue;//not a number so accept default and get out
				}
			}
			if(key.equalsIgnoreCase("bandwidth")){//should really be sonication size
				opt = "--bw";
				try{
					Integer i = Integer.parseInt(jobParameters.get(key).toString());//Integer.parseInt(jobParameters.get(opt).toString());
				}
				catch(Exception e){//not a number so accept default and get out
					continue;
				}
			}
			if(key.equalsIgnoreCase("genomeSize")){
				opt = "--gsize";
				try{
					Double i = Double.parseDouble(jobParameters.get(key).toString());//use double since this will be in scientific notation
				}
				catch(Exception e){//not a number so accept default (size of human genome) and get out
					continue;
				}
			}
			if(key.equalsIgnoreCase("keepDup") ){
				opt = "--keep-dup";	
				if(jobParameters.get(key).toString().equalsIgnoreCase("no")){   //jobParameters.get(opt).toString().equalsIgnoreCase("no")){
					tempCommand.append(" " + key + " 1");
					continue;
				}
				else if(jobParameters.get(key).toString().equalsIgnoreCase("yes")   //jobParameters.get(opt).toString().equalsIgnoreCase("yes") 
						|| 
						//jobParameters.get(opt).toString().equalsIgnoreCase("all")){
						jobParameters.get(key).toString().equalsIgnoreCase("all")){
					tempCommand.append(" " + key + " all");
					continue;
				}
				else if(jobParameters.get(key).toString().equalsIgnoreCase("auto")){//jobParameters.get(opt).toString().equalsIgnoreCase("auto")){
					tempCommand.append(" " + key + " auto");
					continue;
				}
				else{
					try{
						Integer i = Integer.parseInt(jobParameters.get(key).toString());//Integer.parseInt(jobParameters.get(opt).toString());
					}
					catch(Exception e){//not a number, so accept default
						continue;
					}
				}
			}
			if(!opt.isEmpty()){
				tempCommand.append(" " + opt + " " + jobParameters.get(key).toString());
			}
		}
		
		tempCommand.append(" --name " + prefixForFileName);//The name string of the experiment. MACS will use this string NAME to create output files like 'NAME_peaks.xls', etc.
		
		tempCommand.append(" --bdg");//generates two bedGraph files
		
		//String command = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt 4 -I ${" + WorkUnit.INPUT_FILE + "} -R " + getGenomeIndexPath(getGenomeBuild(libraryCell)) + "genome.fasta -T RealignerTargetCreator -o gatk.${" + WorkUnit.JOB_NAME + "}.realign.intervals -known /cork/jcai/GATK_bundle_2.2/1000G_phase1.indels.hg19.vcf -known /cork/jcai/GATK_bundle_2.2/Mills_and_1000G_gold_standard.indels.hg19.vcf";
		//String command = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt 4 -I ${" + WorkUnit.INPUT_FILE + "} -R " + getGenomeIndexPath(getGenomeBuild(libraryCell)) + "genome.fasta -T RealignerTargetCreator -o gatk.${" + WorkUnit.JOB_NAME + "}.realign.intervals -known /cork/jcai/GATK_bundle_2.2/1000G_phase1.indels.hg19.vcf -known /cork/jcai/GATK_bundle_2.2/Mills_and_1000G_gold_standard.indels.hg19.vcf";

		String command = new String(tempCommand);
		logger.debug("---- Will execute macs2 for peakcalling with command: ");
		logger.debug("---- "+command);
		
		w.setCommand(command);
		
		logger.debug("----command has been set to workunit");		
		return w;
	}
	private WorkUnit prepareWorkUnit(List<FileHandle> testFileHandleList, List<FileHandle> controlFileHandleList) {
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);		
		w.setProcessMode(ProcessMode.MAX);		
		w.setMemoryRequirements(8);
		//w.setNumberOfTasks(1);//?????only important when ExecutionMode.Task_Array
		
		List<FileHandle> tempFileHandleList = new ArrayList<FileHandle>();
		tempFileHandleList.addAll(testFileHandleList);//THIS LIST MUST BE ADDED FIRST
		tempFileHandleList.addAll(controlFileHandleList);		
		w.setRequiredFiles(tempFileHandleList);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		//sd.add(picard);
		//sd.add(samtools);
		w.setSoftwareDependencies(sd);
		//w.setResultFiles(resultFiles);
		w.setSecureResults(true);
		
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		
		return w;
	}
}
