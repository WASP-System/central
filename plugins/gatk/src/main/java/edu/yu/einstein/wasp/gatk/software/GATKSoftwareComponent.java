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
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.StrategyService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author asmclellan
 * @author jcai
 */
@Transactional("entityManager")
public class GATKSoftwareComponent extends SoftwarePackage {
	
	private static final long serialVersionUID = -7585834500378105920L;

	public final static int NUM_THREADS = 4;
	
	public final static int MEMORY_REQUIRED = 8; // in Gb
	
	@Autowired
	private GatkService gatkService;
	
	@Autowired
	private StrategyService strategyService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private GenomeService genomeService;
	
	
	public GATKSoftwareComponent() {
	}
	
	public String getCreateTarget(Build build, String inputFilename, String intervalFilename) {
		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt " + NUM_THREADS + 
				" -I " + inputFilename + " -R " + genomeService.getReferenceGenomeFastaFile(build) + 
				" -T RealignerTargetCreator -o " + intervalFilename + " -known " + gatkService.getReferenceIndelsVcfFile(build);
		
		logger.debug("Will conduct gatk create target for re-alignment with string: " + command);
		return command;
	}
	
	
	public String getLocalAlign(Build build, String inputFilename, String intervalFilename, String realnBamFilename) {
		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -I " + inputFilename + " -R " + 
				genomeService.getReferenceGenomeFastaFile(build) + " -T  IndelRealigner" + 
				" -targetIntervals " + intervalFilename + " -o " + realnBamFilename + " -known " + gatkService.getReferenceIndelsVcfFile(build);
		
		logger.debug("Will conduct gatk local re-alignment with string: " + command);
		
		return command;
	}
	
	public String getRecaliTable(Build build, String realnBamFilename, String recaliGrpFilename) {
		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -R " + genomeService.getReferenceGenomeFastaFile(build) + 
				" -nct " + NUM_THREADS + " -knownSites " + gatkService.getReferenceSnpsVcfFile(build) + 
				" -I " + realnBamFilename + " -T BaseRecalibrator -o " + recaliGrpFilename;

		logger.debug("Will conduct gatk generating recalibrate table with command: " + command);
		
		return command;
	}
	
	public String getPrintRecali(Build build, String realnBamFilename, String recaliGrpFilename, String recaliBamFilename, String recaliBaiFilename) {
		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -R " + genomeService.getReferenceGenomeFastaFile(build) + 
				" -nct " + NUM_THREADS + " -I " + realnBamFilename + " -T PrintReads -o " + recaliBamFilename +
				" -BQSR " + recaliGrpFilename + " -baq RECALCULATE && mv " + recaliBamFilename + ".bai " + recaliBaiFilename;
		logger.debug("Will conduct gatk recalibrate sequences with command: " + command);
		return command;
	}
	
	public String indexBam(String bamFilename, String baiFilename){
		String command = "java -Xmx4g -jar $PICARD_ROOT/BuildBamIndex.jar I=" + bamFilename + " O=" + baiFilename + 
				" TMP_DIR=. VALIDATION_STRINGENCY=SILENT";
		logger.debug("Will conduct picard indexing of recalibrated bam file with command: " + command);
		return command;
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
			else if (variantCallingMethod.equals(GatkService.UNIFIED_GENOTYPER_CODE) && opt.startsWith(GatkService.UNIFIED_GENOTYPER_CODE))
				key = opt.replace(GatkService.UNIFIED_GENOTYPER_CODE, "");
			else 
				continue;
			gatkOpts += " " + key + " " + jobParameters.get(opt).toString();
		}
		if (wxsIntervalFile != null) // not null if whole exome seq and an interval file is specified
			gatkOpts += " -L " + wxsIntervalFile;
		return gatkOpts;
	}
	
	public WorkUnit getCallVariantsByUnifiedGenotyper(SampleSource cellLibrary, List<FileGroup> fileGroups, Map<String,Object> jobParameters) throws ParameterValueRetrievalException {
		final int NUM_THREADS = 4;
		final int MEMORY_REQUIRED = 8; // in Gb
		Job job = sampleService.getJobOfLibraryOnCell(cellLibrary);
		Build build = genomeService.getGenomeBuild(cellLibrary);
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
		
		String referenceGenomeFile = genomeService.getReferenceGenomeFastaFile(build);
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
	
	public WorkUnit getCallVariantsByHaplotypeCaller(SampleSource cellLibrary, List<FileGroup> fileGroups, Map<String,Object> jobParameters) throws ParameterValueRetrievalException {
		final int NUM_THREADS = 4;
		final int MEMORY_REQUIRED = 8; // in Gb
		Job job = sampleService.getJobOfLibraryOnCell(cellLibrary);
		Build build = genomeService.getGenomeBuild(cellLibrary);
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
		
		String referenceGenomeFile = genomeService.getReferenceGenomeFastaFile(build);
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
		
		Build build = genomeService.getGenomeBuild(cellLibrary);

		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -T GenotypeGVCFs" + 
				" --variant `printf -- '%s\n' ${" + WorkUnit.INPUT_FILE + "[@]} | sed 's/^/-I /g' | tr '\n' ' '` -R " + 
				genomeService.getReferenceGenomeFastaFile(build) + " -o gatk.${" + WorkUnit.JOB_NAME + "}.raw.vcf";
		w.setCommand(command);

		return w;
	}

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
		
		Build build = genomeService.getGenomeBuild(cellLibrary);

		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk." + namePrefix + ".raw.vcf -R " + 
				genomeService.getReferenceGenomeFastaFile(build) + " -T SelectVariants -o gatk.${" + 
				WorkUnit.JOB_NAME + "}.raw_indel.vcf -selectType INDEL";
		w.setCommand(command);

		String command2 = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk." + namePrefix + ".raw.vcf -R " + 
				genomeService.getReferenceGenomeFastaFile(build) + " -T SelectVariants -o gatk.${" + 
				WorkUnit.JOB_NAME + "}.raw_snp.vcf -selectType SNP";
		w.addCommand(command2);
		
		String command3 = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk.${" + WorkUnit.JOB_NAME + "}.raw_indel.vcf -R " + 
				genomeService.getReferenceGenomeFastaFile(build) + " -T VariantFiltration -o gatk.${" + 
				WorkUnit.JOB_NAME + "}.hfilter_indel.vcf --filterName \"GATK_v4_indel_hfilter\" --filterExpression \"QD < 2.0 || FS > 200.0 || ReadPosRankSum < -20.0\"";
		w.addCommand(command3);
					
		
		String command4 = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk.${" + WorkUnit.JOB_NAME + "}.raw_snp.vcf -R " + 
				genomeService.getReferenceGenomeFastaFile(build) + " -T VariantFiltration -o gatk.${" + 
				WorkUnit.JOB_NAME + "}.hfilter_snp.vcf --filterName \"GATK_v4_snp_hfilter\" --filterExpression \"QD < 2.0 || MQ < 40.0 || FS > 60.0 || HaplotypeScore > 13.0 || MQRankSum < -12.5 || ReadPosRankSum < -8.0\"";
		w.addCommand(command4);
							   

		return w;
	}

	
	

}
