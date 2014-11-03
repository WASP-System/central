/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.batch.tasklet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.bwa.service.BwaService;
import edu.yu.einstein.wasp.plugin.bwa.software.BWAMemSoftwareComponent;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqFileTypeAttribute;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.batch.tasklet.TestForGenomeIndexTasklet;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author calder
 * 
 */
public class BWAmemTasklet extends TestForGenomeIndexTasklet implements StepExecutionListener {

	private Integer cellLibraryId;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FastqService fastqService;

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private FileType fastqFileType;
	
	@Autowired
	private BWAMemSoftwareComponent bwa;
	
	@Autowired
	private BwaService bwaService;
	
	private boolean skip = false;
	
	private Integer fgId;
	
	

	public BWAmemTasklet() {
		// proxy
	}

	public BWAmemTasklet(String cellLibraryIds) {
		List<Integer> cids = WaspSoftwareJobParameters.getCellLibraryIdList(cellLibraryIds);
		Assert.assertTrue(cids.size() == 1);
		this.cellLibraryId = cids.get(0);
	}

	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		ExecutionContext stepExecutionContext = context.getStepContext().getStepExecution().getExecutionContext();
		try{
			WorkUnit w = buildWorkUnit(context.getStepContext().getStepExecution());
			GridResult result = executeWorkUnit(context, w);
			
			//place the grid result in the step context
			saveGridResult(context, result);
			
			// place properties for use in later steps into the step execution context, to be promoted
			// to the job context at run time.
			stepExecutionContext.put("cellLibId", cellLibraryId); 
			stepExecutionContext.put("scrDir", result.getWorkingDirectory());
			stepExecutionContext.put("method", "mem");
		} catch (ParameterValueRetrievalException e) {
			logger.info("BWA aln requested but got ParameterValueRetrievalException: " + e.getLocalizedMessage() + ". Assume alignment not possible, returning SKIP.");
		    skip = true;
		}
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void beforeStep(StepExecution stepExecution){
		
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibraryId);
		Job job = sampleService.getJobOfLibraryOnCell(cellLib);
		
		logger.debug("Beginning BWA mem step for cellLibrary " + cellLib.getId() + " from job " + job.getId());
		
		 Set<FileGroup> fileGroups = new HashSet<FileGroup>();;
		
	    for (FileGroup fg : fileService.getFilesForCellLibraryByType(cellLib, fastqFileType)) {
	        if (!fastqService.hasAttribute(fg, FastqFileTypeAttribute.TRIMMED)) {
	            logger.trace("Ignoring untrimmed file group " + fg.getId());
	            continue;
	        }
	        fileGroups.add(fg);
	    }
		Assert.assertTrue(fileGroups.size() == 1, "No filegroups returned for cellLibrary " + cellLib.getId() + " from job " + job.getId());
		fgId = fileGroups.iterator().next().getId();
		
		super.beforeStep(stepExecution);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (skip == true) {
			return new ExitStatus("SKIP");
		}
		return super.afterStep(stepExecution);
	}

	@Override
	@Transactional("entityManager")
	public GenomeIndexStatus getGenomeIndexStatus(StepExecution stepExecution) {
		try {
			SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibraryId);
			Build build = bwa.getGenomeBuild(cellLib);
			return bwaService.getGenomeIndexStatus(getGridWorkService(getStepExecutionContext(stepExecution)), build);
		} catch (ParameterValueRetrievalException | GridUnresolvableHostException e) {
			String mess = "Unable to determine build or build status " + e.getLocalizedMessage();
			logger.error(mess);
			throw new WaspRuntimeException(mess);
		}
	}

	@Override
	@Transactional("entityManager")
	public WorkUnit buildWorkUnit(StepExecution stepExecution) throws Exception {
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibraryId);
		FileGroup fg = fileService.getFileGroupById(fgId);
		logger.debug("file group: " + fg.getId() + ":" + fg.getDescription());
		
		Map<String, JobParameter> jobParameters = stepExecution.getJobExecution().getJobParameters().getParameters();
		if (logger.isDebugEnabled()){
			for (String key : jobParameters.keySet()) {
				logger.debug("Key: " + key + " Value: " + jobParameters.get(key).getValue().toString());
			}
		}
		WorkUnit w = bwa.getMem(cellLib, fg, jobParameters);
		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		fhlist.addAll(fg.getFileHandles());
		Collections.sort(fhlist, new FastqComparator(fastqService));
		w.setRequiredFiles(fhlist);
		w.setSecureResults(false);
		return w;
	}

	@Override
	@Transactional("entityManager")
	public WorkUnitGridConfiguration configureWorkUnit(StepExecution stepExecution) throws Exception {
		FileGroup fg = fileService.getFileGroupById(fgId);
		return bwa.prepareWorkUnitConfiguration(fg);
	}

}
