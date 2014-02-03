/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.macstwo.software;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.yu.einstein.wasp.Assert;

import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
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
	
	public Macstwo() {
		setSoftwareVersion("2.0.10"); // TODO: Set this value. This default may also be overridden in wasp.site.properties
	}

	//note: test is same as treated, in macs2-speak (from the immunoprecipitated sample)
	public WorkUnit getPeaks(List<FileHandle> testFileHandleList, List<FileHandle> controlFileHandleList, Map<String,Object> jobParameters){
		
		Assert.assertTrue(!testFileHandleList.isEmpty());
		
		WorkUnit w = prepareWorkUnit(testFileHandleList, controlFileHandleList);
		
		StringBuilder tempCommand = new StringBuilder();
		tempCommand.append("macs2");
		String method = " callpeak";//TODO: needs to be a parameter as some point
		tempCommand.append(method);
		
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
		
		tempCommand.append("-f BAM ");
		
		for (String opt : jobParameters.keySet()) {
			if (!opt.startsWith("macsPeakcaller"))
				continue;
			String key = opt.replace("macsPeakcaller.", "");
			if(key.equalsIgnoreCase("pValueCutoff")){
				key = "-p";
			}
			if(key.equalsIgnoreCase("bandwidth")){//should really be sonication size
				key = "--bw";
			}
			if(key.equalsIgnoreCase("genomeSize")){
				key = "-g";
			}
			if(key.equalsIgnoreCase("keepDup") && jobParameters.get(opt).toString().equalsIgnoreCase("no")){
				continue;//take default value,: The default is to keep one tag at the same location. Default: 1
			}
			else if(key.equalsIgnoreCase("keepDup") ){
				key = "--keep-dup";	
				try{
					Integer i = Integer.parseInt(jobParameters.get(opt).toString());
				}
				catch(Exception e){
					tempCommand.append(" " + key + " ");
				}
			}
			tempCommand.append(" " + key + " " + jobParameters.get(opt).toString());
		}

		//
		//String command = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt 4 -I ${" + WorkUnit.INPUT_FILE + "} -R " + getGenomeIndexPath(getGenomeBuild(libraryCell)) + "genome.fasta -T RealignerTargetCreator -o gatk.${" + WorkUnit.JOB_NAME + "}.realign.intervals -known /cork/jcai/GATK_bundle_2.2/1000G_phase1.indels.hg19.vcf -known /cork/jcai/GATK_bundle_2.2/Mills_and_1000G_gold_standard.indels.hg19.vcf";
		
		//String command = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt 4 -I ${" + WorkUnit.INPUT_FILE + "} -R " + getGenomeIndexPath(getGenomeBuild(libraryCell)) + "genome.fasta -T RealignerTargetCreator -o gatk.${" + WorkUnit.JOB_NAME + "}.realign.intervals -known /cork/jcai/GATK_bundle_2.2/1000G_phase1.indels.hg19.vcf -known /cork/jcai/GATK_bundle_2.2/Mills_and_1000G_gold_standard.indels.hg19.vcf";
		//
		
		String command = new String(tempCommand);
		logger.debug("Will execute macs2 for peakcalling with command: " + command);

		w.setCommand(command);
		
		return w;
	}
	private WorkUnit prepareWorkUnit(List<FileHandle> testFileHandleList, List<FileHandle> controlFileHandleList) {
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);		
		w.setProcessMode(ProcessMode.MAX);		
		w.setMemoryRequirements(8);
		w.setNumberOfTasks(1);//?????
		
		List<FileHandle> tempFileHandleList = new ArrayList<FileHandle>();
		tempFileHandleList.addAll(testFileHandleList);//THIS LIST MUST BE ADDED FIRST
		tempFileHandleList.addAll(controlFileHandleList);		
		w.setRequiredFiles(tempFileHandleList);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		//sd.add(picard);
		//sd.add(samtools);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(false);
		
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		
		return w;
	}
}
