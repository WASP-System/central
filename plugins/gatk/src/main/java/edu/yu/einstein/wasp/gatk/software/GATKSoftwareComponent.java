package edu.yu.einstein.wasp.gatk.software;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.gatk.service.GatkService;
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
	
	public String getCreateTargetCmd(Build build, Set<String> inputFilenames, String intervalFilename, int memRequiredGb, int numProcessors) {
		String command = "java -Xmx" + memRequiredGb + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt " + numProcessors;
		for (String fileName : inputFilenames)
			command += " -I " + fileName;
		command += " -R " + genomeService.getReferenceGenomeFastaFile(build) + 
				" -T RealignerTargetCreator -o " + intervalFilename + " -known " + gatkService.getReferenceIndelsVcfFile(build);
		logger.debug("Will conduct gatk create target for re-alignment with string: " + command);
		return command;
	}
	
	
	public String getLocalAlignCmd(Build build, Set<String> inputFilenames, String intervalFilename, String realnBamFilename, String realnBaiFilename, int memRequiredGb) {
		String command = "java -Xmx" + memRequiredGb + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar";
		for (String fileName : inputFilenames)
			command += " -I " + fileName;
		command += " -R " + 
				genomeService.getReferenceGenomeFastaFile(build) + " -T  IndelRealigner" + 
				" -targetIntervals " + intervalFilename + " -o " + realnBamFilename + " -known " + gatkService.getReferenceIndelsVcfFile(build);
		if (realnBaiFilename != null)
			command += " && mv " + realnBamFilename + ".bai " + realnBaiFilename;
		logger.debug("Will conduct gatk local re-alignment with string: " + command);
		
		return command;
	}
	
	/**
	 * assumes outputfiles are 1:1 with input files (matching order), or if output file list size is double input file list size, 
	 * assumes bai files present, thus bam files have odd number indexes and bai files have even number indexes:
	 * e.g. foo.bam (3rd input file) => foo.realn.bam (5th output file) and foo.realn.bai (6th output file)
	 * @param build
	 * @param inputFilenames
	 * @param intervalFilename
	 * @param memRequiredGb
	 * @return
	 */
	public String getLocalAlignCmd(Build build, Set<String> inputFilenames, String intervalFilename, Set<String> outputFilenames, int memRequiredGb) {
		boolean hasBaiFiles = false;
		if (outputFilenames.size() == inputFilenames.size() * 2)
			hasBaiFiles = true;
		String command = "java -Xmx" + memRequiredGb + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar";
		for (String fileName : inputFilenames)
			command += " -I " + fileName;
		command += " -R " + 
				genomeService.getReferenceGenomeFastaFile(build) + " -T  IndelRealigner --nWayOut .getLocalAlign" + 
				" -targetIntervals " + intervalFilename + " -known " + gatkService.getReferenceIndelsVcfFile(build);
		Iterator<String> outFileIterator = outputFilenames.iterator();
		for (String fileName : inputFilenames){
			if (outFileIterator.hasNext())
				command += " && inFilePath=" + fileName + ";inFile=${inFilePath##*/};tempOutFile=${inFile/.bam/.getLocalAlign};ln -sf $tempOutFile " + 
						outFileIterator.next();
			if (hasBaiFiles && outFileIterator.hasNext())
				command += ";ln -sf ${tempOutFile}.bai " + outFileIterator.next();
		}
			
		logger.debug("Will conduct gatk local re-alignment with string: " + command);
		
		return command;
	}
	
	
	public String getRecaliTableCmd(Build build, String realnBamFilename, String recaliGrpFilename, int memRequiredGb, int numProcessors) {
		String command = "java -Xmx" + memRequiredGb + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -R " + genomeService.getReferenceGenomeFastaFile(build) + 
				" -nct " + numProcessors + " -knownSites " + gatkService.getReferenceSnpsVcfFile(build) + 
				" -I " + realnBamFilename + " -T BaseRecalibrator -o " + recaliGrpFilename;

		logger.debug("Will conduct gatk generating recalibrate table with command: " + command);
		
		return command;
	}
	
	public String getPrintRecaliCmd(Build build, String realnBamFilename, String recaliGrpFilename, String recaliBamFilename, String recaliBaiFilename, int memRequiredGb, int numProcessors) {
		String command = "java -Xmx" + memRequiredGb + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -R " + genomeService.getReferenceGenomeFastaFile(build) + 
				" -nct " + numProcessors + " -I " + realnBamFilename + " -T PrintReads -o " + recaliBamFilename +
				" -BQSR " + recaliGrpFilename + " -baq RECALCULATE";
		if (recaliBaiFilename != null)
			command += " && mv " + recaliBamFilename + ".bai " + recaliBaiFilename;
		logger.debug("Will conduct gatk recalibrate sequences with command: " + command);
		return command;
	}
	
	public String getCallVariantOpts(Map<String,Object> jobParameters) throws ParameterValueRetrievalException{
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
		return gatkOpts;
	}
	
	public String getCallVariantsByHaplotypeCaller(Set<String> inputFileNames, String outputGvcfFile, String referenceGenomeFile, 
			String intervalFile, String additionalOptions, int memRequiredGb, int numProcessors){
		String command = "java -Xmx" + memRequiredGb + "g" +
		" -Djava.io.tmpdir=. -jar $GATK_ROOT/GenomeAnalysisTK.jar -nct " + numProcessors;
		for (String fileName : inputFileNames)
			command += " -I " + fileName;
		command += " -R " + referenceGenomeFile + " -T HaplotypeCaller -o " + outputGvcfFile + 
				" --genotyping_mode DISCOVERY --emitRefConfidence GVCF --variant_index_type LINEAR --variant_index_parameter 128000 " + additionalOptions;
		if (intervalFile != null) 
			command += " -L " + intervalFile;

		logger.debug("Will conduct gatk variant-calling (HaplotypeCaller) with command string: " + command);
		return command;
	}
	
	public String getCallVariantsByUnifiedGenotyper(Set<String> inputFileNames, String outputFileName, String referenceGenomeFile, 
			String intervalFile, String additionalOptions, int memRequiredGb, int numProcessors)  {
		String command = "java -Xmx" + memRequiredGb + "g" +
		" -Djava.io.tmpdir=. -jar $GATK_ROOT/GenomeAnalysisTK.jar -nt " + numProcessors;
		for (String fileName : inputFileNames)
			command += " -I " + fileName;
		command += " -R " + referenceGenomeFile + " -T UnifiedGenotyper -o " + outputFileName + 
		" -l INFO -baq CALCULATE_AS_NECESSARY -dt BY_SAMPLE -G Standard -rf BadCigar -A Coverage -A MappingQualityRankSumTest" +
		" -A FisherStrand -A InbreedingCoeff -A ReadPosRankSumTest -A QualByDepth -A HaplotypeScore -A RMSMappingQuality -glm BOTH " + additionalOptions;
		if (intervalFile != null) 
			command += " -L " + intervalFile;
		//
		logger.debug("Will conduct gatk variant-calling (UnifiedGenotyper) with command string: " + command);
		return command;
	}
	
	public String genotypeGVCFs(Set<String> inputFileNames, String outputFileName, String referenceGenomeFile, int memRequiredGb, int numProcessors){
		String command = "java -Xmx" + memRequiredGb + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -T GenotypeGVCFs -nt " + numProcessors;
		for (String fileName : inputFileNames)
			command += " -V " + fileName;
		command += " -R " + referenceGenomeFile + " -o " + outputFileName;
		logger.debug("Will conduct gatk genotyping with command string: " + command);
		return command;
	}
	
	public String selectSnpsFromVariantsFile(String inputVariantsFileName, String snpsOutFileName, String referenceGenomeFile, String intervalFile, int memRequiredGb){
		String command = "java -Xmx" + memRequiredGb + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -T SelectVariants";
		command += " -R " + referenceGenomeFile + " -V " + inputVariantsFileName + " -selectType SNP"  + " -o " + snpsOutFileName;
		if (intervalFile != null) 
			command += " -L " + intervalFile;
		logger.debug("Will conduct gatk SelectVariants for snps with command string: " + command);
		return command;
	}
	
	public String selectIndelsFromVariantsFile(String inputVariantsFileName, String indelsOutFileName, String referenceGenomeFile, String intervalFile, int memRequiredGb){
		String command = "java -Xmx" + memRequiredGb + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -T SelectVariants";
		command += " -R " + referenceGenomeFile + " -V " + inputVariantsFileName + " -selectType INDEL"  + " -o " + indelsOutFileName;
		if (intervalFile != null) 
			command += " -L " + intervalFile;
		logger.debug("Will conduct gatk SelectVariants for indels with command string: " + command);
		return command;
	}
	
	public String applyGenericHardFilterForSnps(String snpsInputFileName, String filteredSnpsOutFileName, String referenceGenomeFile, int memRequiredGb){
		String command = "java -Xmx" + memRequiredGb + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -T VariantFiltration";
		command += " -R " + referenceGenomeFile + " -V " + snpsInputFileName + " -o " + filteredSnpsOutFileName;
		command += " --filterExpression 'QD < 2.0 || FS > 60.0 || MQ < 40.0 || HaplotypeScore > 13.0 || MappingQualityRankSum < -12.5 || ReadPosRankSum < -8.0'";
		command += " --filterName 'waspBasicSnpFilter'";
		logger.debug("Will conduct gatk VariantFiltration for snps with command string: " + command);
		return command;
	}
	
	public String applyGenericHardFilterForIndels(String indelsInputFileName, String filteredIndelsOutFileName, String referenceGenomeFile, int memRequiredGb){
		String command = "java -Xmx" + memRequiredGb + "g -jar $GATK_ROOT/GenomeAnalysisTK.jar -T VariantFiltration";
		command += " -R " + referenceGenomeFile + " -V " + indelsInputFileName + " -o " + filteredIndelsOutFileName;
		command += " --filterExpression 'QD < 2.0 || FS > 200.0 || ReadPosRankSum < -20.0'";
		command += " --filterName 'waspBasicIndelFilter'";
		logger.debug("Will conduct gatk VariantFiltration for indels with command string: " + command);
		return command;
	}

}
