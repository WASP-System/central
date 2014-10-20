/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.batch.tasklet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
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
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.bwa.service.BwaService;
import edu.yu.einstein.wasp.plugin.bwa.software.BWABacktrackSoftwareComponent;
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
public class BWAalnTasklet extends TestForGenomeIndexTasklet implements StepExecutionListener {

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
	
	private boolean skip = false;
	
	@Autowired
	private BWABacktrackSoftwareComponent bwa;
	
	@Autowired
	private BwaService bwaService;
	
	private Job job;
	private Set<FileGroup> fileGroups;
	private SampleSource cellLib;
	private FileGroup fg;

	public BWAalnTasklet() {
		// proxy
	}

	public BWAalnTasklet(String cellLibraryIds) {
		List<Integer> cids = WaspSoftwareJobParameters.getCellLibraryIdList(cellLibraryIds);
		Assert.assertTrue(cids.size() == 1);
		this.cellLibraryId = cids.get(0);
	}

	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		ExecutionContext stepExecutionContext = context.getStepContext().getStepExecution().getExecutionContext();
				
		logger.trace("obtained fastq file groups of size " + fileGroups.size() + " for CellLibrary" + cellLib.getId());
		
		if (fileGroups.size() != 1) {
		    for (FileGroup fg : fileGroups) {
		        if (!fastqService.hasAttribute(fg, FastqFileTypeAttribute.TRIMMED)) {
		            logger.trace("Removing untrimmed file group " + fg.getId());
		            fileGroups.remove(fg);
		        }
		    }
		}
		
		logger.debug("file group: " + fg.getId() + ":" + fg.getDescription());
		
		Map<String,Object> jobParameters = context.getStepContext().getJobParameters();
		
		for (String key : jobParameters.keySet()) {
			logger.debug("Key: " + key + " Value: " + jobParameters.get(key).toString());
		}
		
		try {
			WorkUnit w = bwa.getAln(cellLib, fg, jobParameters);
		
			GridResult result = executeWorkUnit(w);
		
			//place the grid result in the step context
			saveGridResult(context, result);
		
			// place properties for use in later steps into the step execution context, to be promoted
			// to the job context at run time.
			stepExecutionContext.put("cellLibId", cellLib.getId()); 
			stepExecutionContext.put("scrDir", result.getWorkingDirectory());
			stepExecutionContext.put("alnName", result.getId());
			stepExecutionContext.put("alnStr", w.getCommand());
			stepExecutionContext.put("method", "backtrack");
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
		if (cellLib == null) {
			cellLib = sampleService.getSampleSourceDao().findById(cellLibraryId);
			job = sampleService.getJobOfLibraryOnCell(cellLib);
			
			logger.debug("Beginning BWA aln step for cellLibrary " + cellLib.getId() + " from job " + job.getId());
			
			fileGroups = fileService.getFilesForCellLibraryByType(cellLib, fastqFileType);
			Assert.assertTrue(fileGroups.size() == 1);
			fg = fileGroups.iterator().next();
		}
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
	public GenomeIndexStatus getGenomeIndexStatus() {
		try {
			Build build = bwa.getGenomeBuild(cellLib);
			return bwaService.getGenomeIndexStatus(getGridWorkService(), build);
		} catch (ParameterValueRetrievalException | GridUnresolvableHostException e) {
			String mess = "Unable to determine build or build status " + e.getLocalizedMessage();
			logger.error(mess);
			throw new WaspRuntimeException(mess);
		}
	}

	@Override
	@Transactional("entityManager")
	public WorkUnit prepareWorkUnit() {
		return bwa.prepareWorkUnit(fg);
	}

}
