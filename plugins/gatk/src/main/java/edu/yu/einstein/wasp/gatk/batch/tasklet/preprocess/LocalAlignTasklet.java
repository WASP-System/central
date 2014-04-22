package edu.yu.einstein.wasp.gatk.batch.tasklet.preprocess;




/**
 * 
 */

import java.util.HashSet;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.BamFileTypeAttribute;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;


/**
 * @author jcai
 * @author asmclellan
 */
public class LocalAlignTasklet extends WaspRemotingTasklet implements StepExecutionListener {

	
	@Autowired
	private SampleService sampleService;

	@Autowired
	private FileService fileService;

	@Autowired
	private GridHostResolver gridHostResolver;

	@Autowired
	private FileType bamFileType;
	
	@Autowired
	private FileType fastqFileType;

	@Autowired
	private GATKSoftwareComponent gatk;
	
	public LocalAlignTasklet() {
		// proxy
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		
		// retrieve stored properties
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		ExecutionContext jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
		String scratchDirectory = jobExecutionContext.getString("scrDir");
		String createTargetJobName = jobExecutionContext.getString("createTargetName");
		
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(jobExecutionContext.getInt("cellLibId"));

		Job job = sampleService.getJobOfLibraryOnCell(cellLib);

		logger.debug("Beginning GATK local re-alignment step for cellLibrary " + cellLib.getId() + " from job " + job.getId());
		logger.debug("Starting from previously target creat'd scratch directory " + scratchDirectory);

		Set<BamFileTypeAttribute> attributes = new HashSet<>();
		attributes.add(BamFileTypeAttribute.SORTED);
		attributes.add(BamFileTypeAttribute.DEDUP);
		Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLib, bamFileType, attributes, true);

		Assert.assertTrue(fileGroups.size() == 1, "The number of filegroups (" + fileGroups.size() + ") is not equal to 1");
		FileGroup fg = fileGroups.iterator().next();

		logger.debug("file group: " + fg.getId() + ":" + fg.getDescription());

		WorkUnit w = gatk.getLocalAlign(cellLib, scratchDirectory, createTargetJobName, fg);
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + job.getId());

		GridResult result = gridHostResolver.execute(w);

		// place the grid result in the step context
		storeStartedResult(context, result);

		// place localAlignName in execution context, to be promoted
		// to the job context at run time.
		stepExecution.getExecutionContext().put("localAlignName", result.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return super.afterStep(stepExecution);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		super.beforeStep(stepExecution);
	}
}

