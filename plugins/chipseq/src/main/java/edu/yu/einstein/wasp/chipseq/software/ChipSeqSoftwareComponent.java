package edu.yu.einstein.wasp.chipseq.software;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.exception.NullResourceException;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
//import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;

/**
 * @author dubin, based on Lulu's gatk plugin and brent's bwa plugin
 *
 */
public class ChipSeqSoftwareComponent extends SoftwarePackage {
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private GenomeService genomeService;
	
	
	@Autowired
	private RunService runService;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6631761128215948999L;
	
	public ChipSeqSoftwareComponent() {
	}
	public WorkUnit getMergedBam(Map<Sample, List<SampleSource>> approvedSampleApprovedCellLibraryListMap){
		WorkUnit w = prepareWorkUnit(approvedSampleApprovedCellLibraryListMap);
		return w;
	}
	private WorkUnit prepareWorkUnit(Map<Sample, List<SampleSource>> approvedSampleApprovedCellLibraryListMap){
		WorkUnit w = new WorkUnit();
		for (Map.Entry entry : approvedSampleApprovedCellLibraryListMap.entrySet()) {
		    Sample sample = (Sample)entry.getKey();
		    List<SampleSource> approvedCellLibraryList = (List<SampleSource>) entry.getValue();
		}
		return w;
		
	}
	public WorkUnit getChipSeqPeaks(SampleSource treatedCellLibrary, FileGroup treatedFileGroup, SampleSource controlCellLibrary, FileGroup controlFileGroup) {
		
		WorkUnit w = prepareWorkUnit(treatedFileGroup);
		w.setProcessMode(ProcessMode.MAX);

		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		Assert.assertTrue(treatedFileGroup.getFileHandles().size()>0);
		Assert.assertTrue(w.getRequiredFiles().size() == treatedFileGroup.getFileHandles().size() + controlFileGroup.getFileHandles().size());
		String method = "callpeak";
		StringBuilder tempCommand = new StringBuilder();
		tempCommand.append("macs2 " + method);
		
		int indexSoFar = 0;
		for(int i = 0; i < treatedFileGroup.getFileHandles().size(); i++){
			if(i==0){
				tempCommand.append(" -t ");
			}
			tempCommand.append("${" + WorkUnit.INPUT_FILE + "["+indexSoFar+"]} ");
			indexSoFar++;
		}
		if(controlFileGroup!=null && controlFileGroup.getId()!=null){
			for(int i = 0; i < controlFileGroup.getFileHandles().size(); i++){
				if(i==0){
					tempCommand.append(" -c ");
				}
				tempCommand.append("${" + WorkUnit.INPUT_FILE + "["+indexSoFar+"]} ");
				indexSoFar++;
			}
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
	
	private WorkUnit prepareWorkUnit(FileGroup treatedFileGroup, FileGroup controlFileGroup) {
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);//TASK_ARRAY
		
		w.setProcessMode(ProcessMode.MAX);
		
		w.setMemoryRequirements(8);

		int numberOfTasks = 0;
		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		//treatedFileGroup MUST BE ADDED FIRST; controlFileGroup (if any) MUST BE SECOND
		Assert.assertTrue(treatedFileGroup!=null && treatedFileGroup.getId()!=null);
		fhlist.addAll(treatedFileGroup.getFileHandles());
		numberOfTasks = treatedFileGroup.getFileHandles().size(); 
		if(controlFileGroup!=null && controlFileGroup.getId()!=null){
			fhlist.addAll(controlFileGroup.getFileHandles());
			numberOfTasks += treatedFileGroup.getFileHandles().size(); 
		}
		w.setRequiredFiles(fhlist);
		//WHY, if a single task, which this is; I think this is wrong; w.setNumberOfTasks(numberOfTasks);
		w.setNumberOfTasks(1);
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		//sd.add(picard);
		//sd.add(samtools);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(false);
		
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		
		return w;
	}
	
	public WorkUnit getCreatTarget(SampleSource libraryCell, FileGroup fg) {
		WorkUnit w = prepareWorkUnit(fg);
		w.setProcessMode(ProcessMode.MAX);

		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		//
		String command = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt 4 -I ${" + WorkUnit.INPUT_FILE + "} -R " + getGenomeIndexPath(getGenomeBuild(libraryCell)) + "genome.fasta -T RealignerTargetCreator -o gatk.${" + WorkUnit.JOB_NAME + "}.realign.intervals -known /cork/jcai/GATK_bundle_2.2/1000G_phase1.indels.hg19.vcf -known /cork/jcai/GATK_bundle_2.2/Mills_and_1000G_gold_standard.indels.hg19.vcf";
		//
		logger.debug("Will conduct gatk creat target for re-alignment with string: " + command);
		
		w.setCommand(command);
		
		return w;
	}
	
	
	
	public WorkUnit getLocalAlign(SampleSource libraryCell, String scratchDirectory, String namePrefix, FileGroup fg) {
		WorkUnit w = prepareWorkUnit(fg);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setWorkingDirectory(scratchDirectory);

		String command = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar -I ${" + WorkUnit.INPUT_FILE + "} -R " + getGenomeIndexPath(getGenomeBuild(libraryCell)) + "genome.fasta -T  IndelRealigner -targetIntervals gatk." + namePrefix + ".realign.intervals -o gatk.${" + WorkUnit.JOB_NAME + "}.realign.bam -known /cork/jcai/GATK_bundle_2.2/1000G_phase1.indels.hg19.vcf -known /cork/jcai/GATK_bundle_2.2/Mills_and_1000G_gold_standard.indels.hg19.vcf";
		//
		logger.debug("Will conduct gatk local re-alignment with string: " + command);
		
		w.setCommand(command);
		return w;
	}
	
	public WorkUnit getRecaliTable(SampleSource libraryCell, String scratchDirectory, String namePrefix) {
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMemoryRequirements(8);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(false);
		
		w.setWorkingDirectory(scratchDirectory);

		String command = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar -R " + getGenomeIndexPath(getGenomeBuild(libraryCell)) + "genome.fasta -knownSites /cork/jcai/GATK_bundle_2.2/dbsnp_137.hg19.vcf -I gatk." + namePrefix + ".realign.bam -T BaseRecalibrator -o gatk.${" + WorkUnit.JOB_NAME + "}.recali.grp";

		logger.debug("Will conduct gatk generating recalibrate table with string: " + command);
		
		w.setCommand(command);
		return w;
	}
	
	
	public WorkUnit getPrintRecali(SampleSource libraryCell, String scratchDirectory, String namePrefix, String namePrefix2) {
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMemoryRequirements(8);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(false);
		
		w.setWorkingDirectory(scratchDirectory);

		String command = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar -R " + getGenomeIndexPath(getGenomeBuild(libraryCell)) + "genome.fasta -I gatk." + namePrefix + ".realign.bam -T PrintReads -o ${" + WorkUnit.JOB_NAME + "}.recali.bam  -BQSR gatk." + namePrefix2 + ".recali.grp -baq RECALCULATE";
		logger.debug("Will conduct gatk recalibrate sequences with string: " + command);
		
		w.setCommand(command);
		return w;
	}	
	
	private WorkUnit prepareWorkUnit(FileGroup fg) {
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
	
		w.setMemoryRequirements(8);

		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		fhlist.addAll(fg.getFileHandles());
		w.setRequiredFiles(fhlist);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(false);
		
	
		return w;
	}
	
	private Build getGenomeBuild(SampleSource cellLibrary) {
		Build build = null;
		try {
			Sample library = sampleService.getLibrary(cellLibrary);
			logger.debug("looking for genome build associated with sample: " + library.getId());
			build = genomeService.getBuild(library);
			if (build == null) {
				String mess = "cell library does not have associated genome build metadata annotation";
				logger.error(mess);
				throw new NullResourceException(mess);
			}
			logger.debug("genome build: " + build.getGenome().getName() + "::" + build.getName());
		} catch (ParameterValueRetrievalException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return build;
	}
	
	private String getGenomeIndexPath(Build build) {
		String index = genomeService.getRemoteBuildPath(build) + "gatk/";
		return index;
	}

	public WorkUnit getCallVariant(SampleSource sampleSource, List<FileGroup> fileGroups, Map<String,Object> jobParameters) {
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
	
		w.setMemoryRequirements(8);
		w.setProcessMode(ProcessMode.MAX);

		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		for (FileGroup currentFg : fileGroups) {
			fhlist.addAll(currentFg.getFileHandles());
		}
		w.setRequiredFiles(fhlist);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(false);
		
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		//${WASPFILE[@]}
		
		String gatkOpts = "";
		
		for (String opt : jobParameters.keySet()) {
			if (!opt.startsWith("gatk"))
				continue;
			String key = opt.replace("gatk", "");
			if (key.equals("-L"))
				if (jobParameters.get(opt).toString().equals("WES")) {
					gatkOpts += " " + key + " " + getGenomeIndexPath(getGenomeBuild(sampleSource)) + "wes.interval_list";
					continue;
				} else {
					continue;
				}
			gatkOpts += " " + key + " " + jobParameters.get(opt).toString();
		}
		
		
		String command = "java -Djava.io.tmpdir=/oxford/jcai/tmp -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt 4 "+
		"`printf -- '%s\n' ${" + WorkUnit.INPUT_FILE + "[@]} | sed 's/^/-I /g' | tr '\n' ' '` -R " + 
		getGenomeIndexPath(getGenomeBuild(sampleSource)) + "genome.fasta -T UnifiedGenotyper -o gatk.${" + 
		WorkUnit.JOB_NAME + "}.raw.vcf --dbsnp /cork/jcai/GATK_bundle_2.2/dbsnp_137.hg19.vcf" +
		" -l INFO -stand_emit_conf 10.0 -baq CALCULATE_AS_NECESSARY" + gatkOpts +
		" -dt BY_SAMPLE -G Standard -rf BadCigar -A Coverage -A MappingQualityRankSumTest" +
		" -A FisherStrand -A InbreedingCoeff -A ReadPosRankSumTest -A QualByDepth -A HaplotypeScore -A RMSMappingQuality -glm BOTH";

		//
		logger.debug("Will conduct gatk call variant with string: " + command);
		
		w.setCommand(command);
		
		return w;
	}

	public WorkUnit getHardFilter(SampleSource sampleSource, String scratchDirectory, String namePrefix) {
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
	
		w.setMemoryRequirements(2);
		w.setProcessMode(ProcessMode.SINGLE);

		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(false);
		

		w.setWorkingDirectory(scratchDirectory);
		//
		//
		//logger.debug("Will conduct gatk hard filter with string: " + command);
		
		
		String command = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk." + namePrefix + ".raw.vcf -R " + 
		getGenomeIndexPath(getGenomeBuild(sampleSource)) + "genome.fasta -T SelectVariants -o gatk.${" + 
		WorkUnit.JOB_NAME + "}.raw_indel.vcf -selectType INDEL";
		w.setCommand(command);

		String command2 = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk." + namePrefix + ".raw.vcf -R " + 
		getGenomeIndexPath(getGenomeBuild(sampleSource)) + "genome.fasta -T SelectVariants -o gatk.${" + 
		WorkUnit.JOB_NAME + "}.raw_snp.vcf -selectType SNP";
		w.addCommand(command2);
		
		String command3 = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk.${" + WorkUnit.JOB_NAME + "}.raw_indel.vcf -R " + 
		getGenomeIndexPath(getGenomeBuild(sampleSource)) + "genome.fasta -T VariantFiltration -o gatk.${" + 
		WorkUnit.JOB_NAME + "}.hfilter_indel.vcf --filterName \"GATK_v4_snp_hfilter\" --filterExpression \"QD < 2.0 || MQ < 40.0 || FS > 60.0 || HaplotypeScore > 13.0 || MQRankSum < -12.5 || ReadPosRankSum < -8.0\"";
		w.addCommand(command3);
					
		
		String command4 = "java -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk.${" + WorkUnit.JOB_NAME + "}.raw_snp.vcf -R " + 
		getGenomeIndexPath(getGenomeBuild(sampleSource)) + "genome.fasta -T VariantFiltration -o gatk.${" + 
		WorkUnit.JOB_NAME + "}.hfilter_snp.vcf --filterName \"GATK_v4_snp_hfilter\" --filterExpression \"QD < 2.0 || MQ < 40.0 || FS > 60.0 || HaplotypeScore > 13.0 || MQRankSum < -12.5 || ReadPosRankSum < -8.0\"";
		w.addCommand(command4);
							   

		return w;
	}

}


