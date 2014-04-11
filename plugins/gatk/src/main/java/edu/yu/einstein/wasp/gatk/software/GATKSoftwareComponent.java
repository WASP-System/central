package edu.yu.einstein.wasp.gatk.software;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.Strategy.StrategyType;
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
import edu.yu.einstein.wasp.service.StrategyService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author jcai
 * @author asmclellan
 */
public class GATKSoftwareComponent extends SoftwarePackage {
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private GenomeService genomeService;
	
	
	@Autowired
	private RunService runService;
	
	@Autowired
	private StrategyService strategyService;
	
	
	private static final long serialVersionUID = -6631761128215948999L;
	
	public GATKSoftwareComponent() {
		setSoftwareVersion("2.8-1"); // this default may be overridden in wasp.site.properties
	}
	
	public WorkUnit getCreateTarget(SampleSource cellLibrary, FileGroup fg) {
		final int NUM_THREADS = 4;
		final int MEMORY_REQUIRED = 8; // in Gb
		WorkUnit w = prepareWorkUnit(fg);
		w.setProcessMode(ProcessMode.MAX);
		w.setMemoryRequirements(MEMORY_REQUIRED);
		w.setProcessorRequirements(NUM_THREADS);

		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		Build build = getGenomeBuild(cellLibrary);
		
		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt " + NUM_THREADS + 
				" -I ${" + WorkUnit.INPUT_FILE + "} -R " + getReferenceGenomeFastaFile(build) + 
				" -T RealignerTargetCreator -o gatk.${" + WorkUnit.JOB_NAME + 
				"}.realign.intervals -known " + getReferenceIndelsVcfFile(build);
		
		logger.debug("Will conduct gatk create target for re-alignment with string: " + command);
		
		w.setCommand(command);
		
		return w;
	}
	
	
	
	public WorkUnit getLocalAlign(SampleSource cellLibrary, String scratchDirectory, String namePrefix, FileGroup fg) {
		final int MEMORY_REQUIRED = 8; // in Gb
		WorkUnit w = prepareWorkUnit(fg);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMemoryRequirements(MEMORY_REQUIRED);
		w.setWorkingDirectory(scratchDirectory);
		Build build = getGenomeBuild(cellLibrary);

		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -I ${" + WorkUnit.INPUT_FILE + "} -R " + 
				getReferenceGenomeFastaFile(build) + " -T  IndelRealigner -targetIntervals gatk." + namePrefix + 
		".realign.intervals -o gatk.${" + WorkUnit.JOB_NAME + "}.realign.bam -known " + getReferenceIndelsVcfFile(build);
		
		logger.debug("Will conduct gatk local re-alignment with string: " + command);
		
		w.setCommand(command);
		return w;
	}
	
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
		Build build = getGenomeBuild(cellLibrary);

		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -R " + getReferenceGenomeFastaFile(build) + 
				"genome.fasta -knownSites " + getReferenceSnpsVcfFile(build) + " -I gatk." + namePrefix + 
				".realign.bam -T BaseRecalibrator -o gatk.${" + WorkUnit.JOB_NAME + "}.recali.grp";

		logger.debug("Will conduct gatk generating recalibrate table with command: " + command);
		
		w.setCommand(command);
		return w;
	}
	
	
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
		Build build = getGenomeBuild(cellLibrary);

		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -R " + getReferenceGenomeFastaFile(build) + 
				"genome.fasta -I gatk." + namePrefix + ".realign.bam -T PrintReads -o ${" + WorkUnit.OUTPUT_FILE + "[0]}  -BQSR gatk." + namePrefix2 + 
				".recali.grp -baq RECALCULATE";
		logger.debug("Will conduct gatk recalibrate sequences with command: " + command);
		
		w.setCommand(command);
		return w;
	}	
	
	private WorkUnit prepareWorkUnit(FileGroup fg) {
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
	
	private String getReferenceGenomeFastaFile(Build build) {
		String index = genomeService.getRemoteBuildPath(build) + "/" + build.getMetadata("fasta.folder") + "/" + build.getMetadata("fasta.fileName");
		return index;
	}
	
	private String getReferenceSnpsVcfFile(Build build) {
		String index = genomeService.getRemoteBuildPath(build) + "/" + build.getMetadata("vcf.folder") + "/" + build.getMetadata("vcf.snps.fleName");
		return index;
	}
	
	private String getReferenceIndelsVcfFile(Build build) {
		String index = genomeService.getRemoteBuildPath(build) + "/" + build.getMetadata("vcf.folder") + "/" + build.getMetadata("vcf.indels.fleName");
		return index;
	}

	public WorkUnit getCallVariant(SampleSource cellLibrary, List<FileGroup> fileGroups, Map<String,Object> jobParameters) {
		final int NUM_THREADS = 4;
		final int MEMORY_REQUIRED = 8; // in Gb
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.PROCESS);
	
		w.setMemoryRequirements(MEMORY_REQUIRED);
		w.setProcessorRequirements(NUM_THREADS);
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
			gatkOpts += " " + key + " " + jobParameters.get(opt).toString();
		}
		Build build = getGenomeBuild(cellLibrary);
		Strategy strategy = strategyService.getThisJobsStrategy(StrategyType.LIBRARY_STRATEGY, sampleService.getJobOfLibraryOnCell(cellLibrary));
		if (strategy.getStrategy().equals("WXS")) // whole exome seq
			gatkOpts += " -L " + getReferenceGenomeFastaFile(build) + "wes.interval_list"; // TODO: where is this stored????
		String command = "java -Xmx" + MEMORY_REQUIRED + "g -Djava.io.tmpdir=${" + WorkUnit.WORKING_DIRECTORY + "} -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt " + NUM_THREADS +
		" `printf -- '%s\n' ${" + WorkUnit.INPUT_FILE + "[@]} | sed 's/^/-I /g' | tr '\n' ' '` -R " + 
		getReferenceGenomeFastaFile(build) + "genome.fasta -T UnifiedGenotyper -o gatk.${" + 
		WorkUnit.JOB_NAME + "}.raw.vcf --dbsnp " + getReferenceSnpsVcfFile(build) + 
		" -l INFO -stand_emit_conf 10.0 -baq CALCULATE_AS_NECESSARY" + gatkOpts +
		" -dt BY_SAMPLE -G Standard -rf BadCigar -A Coverage -A MappingQualityRankSumTest" +
		" -A FisherStrand -A InbreedingCoeff -A ReadPosRankSumTest -A QualByDepth -A HaplotypeScore -A RMSMappingQuality -glm BOTH";

		//
		logger.debug("Will conduct gatk call variant with string: " + command);
		
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
		
		Build build = getGenomeBuild(cellLibrary);

		String command = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk." + namePrefix + ".raw.vcf -R " + 
		getReferenceGenomeFastaFile(build) + "genome.fasta -T SelectVariants -o gatk.${" + 
		WorkUnit.JOB_NAME + "}.raw_indel.vcf -selectType INDEL";
		w.setCommand(command);

		String command2 = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk." + namePrefix + ".raw.vcf -R " + 
		getReferenceGenomeFastaFile(build) + "genome.fasta -T SelectVariants -o gatk.${" + 
		WorkUnit.JOB_NAME + "}.raw_snp.vcf -selectType SNP";
		w.addCommand(command2);
		
		String command3 = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk.${" + WorkUnit.JOB_NAME + "}.raw_indel.vcf -R " + 
		getReferenceGenomeFastaFile(build) + "genome.fasta -T VariantFiltration -o gatk.${" + 
		WorkUnit.JOB_NAME + "}.hfilter_indel.vcf --filterName \"GATK_v4_indel_hfilter\" --filterExpression \"QD < 2.0 || FS > 200.0 || ReadPosRankSum < -20.0\"";
		w.addCommand(command3);
					
		
		String command4 = "java -Xmx" + MEMORY_REQUIRED + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar --variant gatk.${" + WorkUnit.JOB_NAME + "}.raw_snp.vcf -R " + 
		getReferenceGenomeFastaFile(build) + "genome.fasta -T VariantFiltration -o gatk.${" + 
		WorkUnit.JOB_NAME + "}.hfilter_snp.vcf --filterName \"GATK_v4_snp_hfilter\" --filterExpression \"QD < 2.0 || MQ < 40.0 || FS > 60.0 || HaplotypeScore > 13.0 || MQRankSum < -12.5 || ReadPosRankSum < -8.0\"";
		w.addCommand(command4);
							   

		return w;
	}

	
	

}
