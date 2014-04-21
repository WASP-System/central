package edu.yu.einstein.wasp.gatk.software;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.Strategy.StrategyType;
import edu.yu.einstein.wasp.gatk.service.GatkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.StrategyService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author asmclellan
 * @author jcai
 */
public class GATKSoftwareComponent extends SoftwarePackage {
	
	@Autowired
	private GatkService gatkService;
	
	@Autowired
	private StrategyService strategyService;
	
	@Autowired
	private SampleService sampleService;
	
	
	private static final long serialVersionUID = -6631761128215948999L;
	
	public GATKSoftwareComponent() {
		setSoftwareVersion("3.0-0"); // this default may be overridden in wasp.site.properties
	}
	
	@Transactional("entityManager")
	public WorkUnit getCreateTarget(SampleSource cellLibrary, FileGroup fg) {
		final int NUM_THREADS = 4;
		final int MEMORY_REQUIRED = 8; // in Gb
		WorkUnit w = prepareWorkUnit(fg);
		w.setProcessMode(ProcessMode.MAX);
		w.setMemoryRequirements(MEMORY_REQUIRED);
		w.setProcessorRequirements(NUM_THREADS);

		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		Build build = gatkService.getGenomeBuild(cellLibrary);
		
		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt " + NUM_THREADS + 
				" -I ${" + WorkUnit.INPUT_FILE + "} -R " + gatkService.getReferenceGenomeFastaFile(build) + 
				" -T RealignerTargetCreator -o gatk.${" + WorkUnit.JOB_NAME + 
				"}.realign.intervals -known " + gatkService.getReferenceIndelsVcfFile(build);
		
		logger.debug("Will conduct gatk create target for re-alignment with string: " + command);
		
		w.setCommand(command);
		
		return w;
	}
	
	
	@Transactional("entityManager")
	public WorkUnit getLocalAlign(SampleSource cellLibrary, String scratchDirectory, String namePrefix, FileGroup fg) {
		final int MEMORY_REQUIRED = 8; // in Gb
		WorkUnit w = prepareWorkUnit(fg);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMemoryRequirements(MEMORY_REQUIRED);
		w.setWorkingDirectory(scratchDirectory);
		Build build = gatkService.getGenomeBuild(cellLibrary);

		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -I ${" + WorkUnit.INPUT_FILE + "} -R " + 
				gatkService.getReferenceGenomeFastaFile(build) + " -T  IndelRealigner -targetIntervals gatk." + namePrefix + 
		".realign.intervals -o gatk.${" + WorkUnit.JOB_NAME + "}.realign.bam -known " + gatkService.getReferenceIndelsVcfFile(build);
		
		logger.debug("Will conduct gatk local re-alignment with string: " + command);
		
		w.setCommand(command);
		return w;
	}
	
	@Transactional("entityManager")
	public WorkUnit getRecaliTable(SampleSource cellLibrary, String scratchDirectory, String namePrefix) {
		final int MEMORY_REQUIRED = 8; // in Gb
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMemoryRequirements(MEMORY_REQUIRED);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(false);
		
		w.setWorkingDirectory(scratchDirectory);
		Build build = gatkService.getGenomeBuild(cellLibrary);

		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -R " + gatkService.getReferenceGenomeFastaFile(build) + 
				" -knownSites " + gatkService.getReferenceSnpsVcfFile(build) + " -I gatk." + namePrefix + 
				".realign.bam -T BaseRecalibrator -o gatk.${" + WorkUnit.JOB_NAME + "}.recali.grp";

		logger.debug("Will conduct gatk generating recalibrate table with command: " + command);
		
		w.setCommand(command);
		return w;
	}
	
	@Transactional("entityManager")
	public WorkUnit getPrintRecali(SampleSource cellLibrary, String scratchDirectory, String namePrefix, String namePrefix2) {
		final int MEMORY_REQUIRED = 8; // in Gb
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMemoryRequirements(MEMORY_REQUIRED);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(true);
		
		w.setWorkingDirectory(scratchDirectory);
		Build build = gatkService.getGenomeBuild(cellLibrary);

		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -R " + gatkService.getReferenceGenomeFastaFile(build) + 
				" -I gatk." + namePrefix + ".realign.bam -T PrintReads -o ${" + WorkUnit.OUTPUT_FILE + "[0]}  -BQSR gatk." + namePrefix2 + 
				".recali.grp -baq RECALCULATE";
		logger.debug("Will conduct gatk recalibrate sequences with command: " + command);
		
		w.setCommand(command);
		return w;
	}	
	
	@Transactional("entityManager")
	public WorkUnit prepareWorkUnit(FileGroup fg) {
		final int MEMORY_REQUIRED = 8; // in Gb
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
	
		w.setMemoryRequirements(MEMORY_REQUIRED);

		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		fhlist.addAll(fg.getFileHandles());
		w.setRequiredFiles(fhlist);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(false);

		return w;
	}
	
	private String getCallVariantOpts(Map<String,Object> jobParameters, String wxsIntervalFile) throws ParameterValueRetrievalException{
		if (!jobParameters.containsKey("variantCallingMethod"))
			throw new ParameterValueRetrievalException("Unable to determine variant calling method from job parameters");
		String variantCallingMethod = (String) jobParameters.get("variantCallingMethod");
		String gatkOpts = "";
		for (String opt : jobParameters.keySet()) {
			String key;
			if (opt.startsWith("gatk"))
				key = opt.replace("gatk", "");
			else if (variantCallingMethod.equals("ug") && opt.startsWith("ug"))
				key = opt.replace("ug", "");
			else 
				continue;
			gatkOpts += " " + key + " " + jobParameters.get(opt).toString();
		}
		if (wxsIntervalFile != null) // not null if whole exome seq and an interval file is specified
			gatkOpts += " -L " + wxsIntervalFile;
		return gatkOpts;
	}
	
	@Transactional("entityManager")
	public WorkUnit getCallVariantsByUnifiedGenotyper(SampleSource cellLibrary, List<FileGroup> fileGroups, Map<String,Object> jobParameters) throws ParameterValueRetrievalException {
		final int NUM_THREADS = 4;
		final int MEMORY_REQUIRED = 8; // in Gb
		Job job = sampleService.getJobOfLibraryOnCell(cellLibrary);
		Build build = gatkService.getGenomeBuild(cellLibrary);
		Strategy strategy = strategyService.getThisJobsStrategy(StrategyType.LIBRARY_STRATEGY, sampleService.getJobOfLibraryOnCell(cellLibrary));
		String wxsIntervalFile = null;
		if (strategy.getStrategy().equals("WXS"))
			wxsIntervalFile = gatkService.getWxsIntervalFile(job, build);
		String gatkOpts = getCallVariantOpts(jobParameters, wxsIntervalFile);
		
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
	
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
		
		String referenceGenomeFile = gatkService.getReferenceGenomeFastaFile(build);
		String snpFile = gatkService.getReferenceSnpsVcfFile(build);
		w.setMemoryRequirements(MEMORY_REQUIRED);
		w.setProcessorRequirements(NUM_THREADS);
		
		String command = "java -Xmx" + MEMORY_REQUIRED + "g" +
		" -Djava.io.tmpdir=${" + WorkUnit.WORKING_DIRECTORY + "} -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt " + NUM_THREADS +
		" `printf -- '%s\n' ${" + WorkUnit.INPUT_FILE + "[@]} | sed 's/^/-I /g' | tr '\n' ' '` -R " + referenceGenomeFile +
		" -T UnifiedGenotyper -o gatk.${" + WorkUnit.JOB_NAME + "}.raw.vcf --dbsnp " + snpFile + 
		" -l INFO -baq CALCULATE_AS_NECESSARY" + gatkOpts +
		" -dt BY_SAMPLE -G Standard -rf BadCigar -A Coverage -A MappingQualityRankSumTest" +
		" -A FisherStrand -A InbreedingCoeff -A ReadPosRankSumTest -A QualByDepth -A HaplotypeScore -A RMSMappingQuality -glm BOTH";

		//
		logger.debug("Will conduct gatk call variants with command string: " + command);
		
		w.setCommand(command);
		
		return w;
	}
	
	@Transactional("entityManager")
	public WorkUnit getCallVariantsByHaplotypeCaller(SampleSource cellLibrary, List<FileGroup> fileGroups, Map<String,Object> jobParameters) throws ParameterValueRetrievalException {
		final int NUM_THREADS = 4;
		final int MEMORY_REQUIRED = 8; // in Gb
		Job job = sampleService.getJobOfLibraryOnCell(cellLibrary);
		Build build = gatkService.getGenomeBuild(cellLibrary);
		Strategy strategy = strategyService.getThisJobsStrategy(StrategyType.LIBRARY_STRATEGY, sampleService.getJobOfLibraryOnCell(cellLibrary));
		String wxsIntervalFile = null;
		if (strategy.getStrategy().equals("WXS"))
			wxsIntervalFile = gatkService.getWxsIntervalFile(job, build);
		String gatkOpts = getCallVariantOpts(jobParameters, wxsIntervalFile);
		
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
	
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
		
		String referenceGenomeFile = gatkService.getReferenceGenomeFastaFile(build);
		String snpFile = gatkService.getReferenceSnpsVcfFile(build);
		w.setMemoryRequirements(MEMORY_REQUIRED);
		w.setProcessorRequirements(NUM_THREADS);
		
		String command = "java -Xmx" + MEMORY_REQUIRED + "g" +
		" -Djava.io.tmpdir=${" + WorkUnit.WORKING_DIRECTORY + "} -jar $GATK_ROOT/GenomeAnalysisTK.jar -nct " + NUM_THREADS +
		" `printf -- '%s\n' ${" + WorkUnit.INPUT_FILE + "[@]} | sed 's/^/-I /g' | tr '\n' ' '` -R " + referenceGenomeFile +
		" -T HaplotypeCaller -o gatk.${" + WorkUnit.JOB_NAME + "}.gvcf.vcf --dbsnp " + snpFile + " --genotyping_mode DISCOVERY" + 
		" --emitRefConfidence GVCF --variant_index_type LINEAR --variant_index_parameter 12800" + gatkOpts;

		logger.debug("Will conduct gatk call variants with command string: " + command);
		
		w.setCommand(command);
		
		return w;
	}
	
	@Transactional("entityManager")
	public WorkUnit genotypeGVCFs(SampleSource cellLibrary, String scratchDirectory, List<FileGroup> fileGroups){
		final int MEMORY_REQUIRED = 2; // in Gb
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
	
		w.setMemoryRequirements(MEMORY_REQUIRED);
		w.setProcessMode(ProcessMode.SINGLE);

		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(false);
		

		w.setWorkingDirectory(scratchDirectory);
		
		Build build = gatkService.getGenomeBuild(cellLibrary);

		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -T GenotypeGVCFs" + 
				" --variant `printf -- '%s\n' ${" + WorkUnit.INPUT_FILE + "[@]} | sed 's/^/-I /g' | tr '\n' ' '` -R " + 
				gatkService.getReferenceGenomeFastaFile(build) + " -o gatk.${" + WorkUnit.JOB_NAME + "}.raw.vcf";
		w.setCommand(command);

		return w;
	}

	@Transactional("entityManager")
	public WorkUnit getHardFilter(SampleSource cellLibrary, String scratchDirectory, String namePrefix) {
		final int MEMORY_REQUIRED = 2; // in Gb
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
	
		w.setMemoryRequirements(MEMORY_REQUIRED);
		w.setProcessMode(ProcessMode.SINGLE);

		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(false);
		

		w.setWorkingDirectory(scratchDirectory);
		
		Build build = gatkService.getGenomeBuild(cellLibrary);

		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk." + namePrefix + ".raw.vcf -R " + 
				gatkService.getReferenceGenomeFastaFile(build) + " -T SelectVariants -o gatk.${" + 
				WorkUnit.JOB_NAME + "}.raw_indel.vcf -selectType INDEL";
		w.setCommand(command);

		String command2 = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk." + namePrefix + ".raw.vcf -R " + 
				gatkService.getReferenceGenomeFastaFile(build) + " -T SelectVariants -o gatk.${" + 
				WorkUnit.JOB_NAME + "}.raw_snp.vcf -selectType SNP";
		w.addCommand(command2);
		
		String command3 = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk.${" + WorkUnit.JOB_NAME + "}.raw_indel.vcf -R " + 
				gatkService.getReferenceGenomeFastaFile(build) + " -T VariantFiltration -o gatk.${" + 
				WorkUnit.JOB_NAME + "}.hfilter_indel.vcf --filterName \"GATK_v4_indel_hfilter\" --filterExpression \"QD < 2.0 || FS > 200.0 || ReadPosRankSum < -20.0\"";
		w.addCommand(command3);
					
		
		String command4 = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk.${" + WorkUnit.JOB_NAME + "}.raw_snp.vcf -R " + 
				gatkService.getReferenceGenomeFastaFile(build) + " -T VariantFiltration -o gatk.${" + 
				WorkUnit.JOB_NAME + "}.hfilter_snp.vcf --filterName \"GATK_v4_snp_hfilter\" --filterExpression \"QD < 2.0 || MQ < 40.0 || FS > 60.0 || HaplotypeScore > 13.0 || MQRankSum < -12.5 || ReadPosRankSum < -8.0\"";
		w.addCommand(command4);
							   

		return w;
	}

	
	

}
