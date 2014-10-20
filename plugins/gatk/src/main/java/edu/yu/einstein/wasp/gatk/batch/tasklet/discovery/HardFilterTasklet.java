package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.Strategy.StrategyType;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.filetype.FileTypeAttribute;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.gatk.service.GatkService;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.VcfFileTypeAttribute;
import edu.yu.einstein.wasp.plugin.mps.grid.software.SnpEff;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.StrategyService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

public class HardFilterTasklet extends WaspRemotingTasklet {
	
	private static Logger logger = LoggerFactory.getLogger(HardFilterTasklet.class);
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileType vcfFileType;
	
	@Autowired
	private GatkService gatkService;
	
	@Autowired
	private StrategyService strategyService;
	
	@Autowired
	protected GenomeService genomeService;
	
	@Autowired
	@Qualifier("fileTypeServiceImpl")
	private FileTypeService fileTypeService;
	
	@Autowired
	private GATKSoftwareComponent gatk;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	private Integer jobId;
	
	public HardFilterTasklet(Integer jobId) {
		this.jobId = jobId;
	}

	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		ExecutionContext stepExecutionContext = context.getStepContext().getStepExecution().getExecutionContext();
		ExecutionContext jobExecutionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		FileGroup combinedGenotypedVcfFg = null;
		if (jobExecutionContext.containsKey("combinedGenotypedVcfFgId"))
			combinedGenotypedVcfFg = fileService.getFileGroupById(Integer.parseInt(jobExecutionContext.getString("combinedGenotypedVcfFgId")));
		Build build = null;
		
		Set<FileHandle> outFiles = new LinkedHashSet<FileHandle>();
		
		Job job = jobService.getJobByJobId(jobId);
		Set<FileTypeAttribute> attributes = new HashSet<>(fileTypeService.getAttributes(combinedGenotypedVcfFg));
		attributes.add(VcfFileTypeAttribute.FILTERED);
		
		String filteredSnpVcfOutFileName = fileService.generateUniqueBaseFileName(job) + "snps.filtered.vcf";
		FileGroup filteredSnpVcfOutG = new FileGroup();
		FileHandle filteredSnpVcfOut = new FileHandle();
		filteredSnpVcfOut.setFileName(filteredSnpVcfOutFileName);
		filteredSnpVcfOut.setFileType(vcfFileType);
		filteredSnpVcfOutG.setIsActive(0);
		filteredSnpVcfOutG.addFileHandle(filteredSnpVcfOut);
		filteredSnpVcfOutG.setFileType(vcfFileType);
		filteredSnpVcfOutG.setDescription(filteredSnpVcfOutFileName);
		filteredSnpVcfOutG.setSoftwareGeneratedById(gatk.getId());
		filteredSnpVcfOutG.addDerivedFrom(combinedGenotypedVcfFg);
		filteredSnpVcfOutG = fileService.saveInDiscreteTransaction(filteredSnpVcfOutG, attributes);
		outFiles.add(filteredSnpVcfOut);
		stepExecutionContext.putString("filteredSnpsVcfFgId", filteredSnpVcfOutG.getId().toString());
		
		String filteredIndelVcfOutFileName = fileService.generateUniqueBaseFileName(job) + "indels.filtered.vcf";
		FileGroup filteredIndelVcfOutG = new FileGroup();
		FileHandle filteredIndelVcfOut = new FileHandle();
		filteredIndelVcfOut.setFileName(filteredIndelVcfOutFileName);
		filteredIndelVcfOut.setFileType(vcfFileType);
		filteredIndelVcfOutG.setIsActive(0);
		filteredIndelVcfOutG.addFileHandle(filteredIndelVcfOut);
		filteredIndelVcfOutG.setFileType(vcfFileType);
		filteredIndelVcfOutG.setDescription(filteredIndelVcfOutFileName);
		filteredIndelVcfOutG.setSoftwareGeneratedById(gatk.getId());
		filteredIndelVcfOutG.addDerivedFrom(combinedGenotypedVcfFg);
		filteredIndelVcfOutG = fileService.saveInDiscreteTransaction(filteredIndelVcfOutG, attributes);
		outFiles.add(filteredIndelVcfOut);
		stepExecutionContext.putString("filteredIndelsVcfFgId", filteredIndelVcfOutG.getId().toString());
				
		WorkUnit w = new WorkUnit();
		w.setMode(ExecutionMode.PROCESS);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMemoryRequirements(AbstractGatkTasklet.MEMORY_GB_4);
		w.setSecureResults(true);
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, gatk));
		w.setResultFiles(outFiles);
		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		build = gatkService.getBuildForFg(combinedGenotypedVcfFg);
		fhlist.addAll(combinedGenotypedVcfFg.getFileHandles());
		w.setRequiredFiles(fhlist);
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		SnpEff snpEff = (SnpEff) gatk.getSoftwareDependencyByIname("snpEff");
		sd.add(gatk);
		sd.add(snpEff);
		w.setSoftwareDependencies(sd);
		String wxsIntervalFile = null; 
		Strategy strategy = strategyService.getThisJobsStrategy(StrategyType.LIBRARY_STRATEGY, job);
		if (strategy.getStrategy().equals("WXS"))
			wxsIntervalFile = gatkService.getWxsIntervalFile(job, build);
		String rawVariantsFileName = "${" + WorkUnit.INPUT_FILE + "[0]}";
		String rawSnpsFileName = "snps.${" + WorkUnit.OUTPUT_FILE + "[0]}";
		String rawIndelsFileName = "indels.${" + WorkUnit.OUTPUT_FILE + "[1]}";
		String filteredSnpVcfFileName = "snps.filtered.${" + WorkUnit.OUTPUT_FILE + "[0]}";
		String filteredIndelVcfFileName = "indels.filtered.${" + WorkUnit.OUTPUT_FILE + "[1]}";
		String referenceGenomeFileName = genomeService.getReferenceGenomeFastaFile(build);
		w.addCommand(gatk.selectSnpsFromVariantsFile(rawVariantsFileName, rawSnpsFileName, referenceGenomeFileName, wxsIntervalFile, AbstractGatkTasklet.MEMORY_GB_4));
		w.addCommand(gatk.selectIndelsFromVariantsFile(rawVariantsFileName, rawIndelsFileName, referenceGenomeFileName, wxsIntervalFile, AbstractGatkTasklet.MEMORY_GB_4));
		w.addCommand(gatk.applyGenericHardFilterForSnps(rawSnpsFileName, filteredSnpVcfFileName, referenceGenomeFileName, AbstractGatkTasklet.MEMORY_GB_4));
		w.addCommand(gatk.applyGenericHardFilterForIndels(rawIndelsFileName, filteredIndelVcfFileName, referenceGenomeFileName, AbstractGatkTasklet.MEMORY_GB_4));
		
		// We will now add snp and indel database ids
		String snpFile = gatkService.getReferenceSnpsVcfFile(build);
		String indelsFile = gatkService.getReferenceIndelsVcfFile(build);
		String filteredSnpWithIdsVcfFileName = "${" + WorkUnit.OUTPUT_FILE + "[0]}";
		String filteredIndelWithIdsVcfFileName = "${" + WorkUnit.OUTPUT_FILE + "[1]}";
		w.addCommand(snpEff.getAnnotateIdsCommand(filteredSnpVcfFileName, snpFile, filteredSnpWithIdsVcfFileName));
		w.addCommand(snpEff.getAnnotateIdsCommand(filteredIndelVcfFileName, indelsFile, filteredIndelWithIdsVcfFileName));
				
		GridResult result = gridHostResolver.execute(w);
		
		//place the grid result in the step context
		saveGridResult(context, result);
	}
	
	@Transactional("entityManager")
	@Override
	public void doPreFinish(ChunkContext context) throws Exception {
		ExecutionContext stepExecutionContext = context.getStepContext().getStepExecution().getExecutionContext();
		if (stepExecutionContext.containsKey("filteredSnpsVcfFgId")){
			Integer filteredSnpsVcfFgId = Integer.parseInt(stepExecutionContext.getString("filteredSnpsVcfFgId"));
			logger.debug("Setting as active FileGroup with id=: " + filteredSnpsVcfFgId);
			fileService.getFileGroupById(filteredSnpsVcfFgId).setIsActive(1);
		}
		if (stepExecutionContext.containsKey("filteredIndelsVcfFgId")){
			Integer filteredIndelsVcfFgId = Integer.parseInt(stepExecutionContext.getString("filteredIndelsVcfFgId"));
			logger.debug("Setting as active FileGroup with id=: " + filteredIndelsVcfFgId);
			fileService.getFileGroupById(filteredIndelsVcfFgId).setIsActive(1);
		}
	}
	

}
