/**
 * 
 */
package edu.yu.einstein.wasp.helptag.batch.tasklet;

import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.helptag.software.Helptag;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author AJ
 *
 */
public class HelptagTasklet extends WaspTasklet  implements StepExecutionListener {

	@Autowired
	private FileService fileService;

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private HelptagService helptagService;

	@Autowired
	private Helptag helptag;
	
	@Autowired
	private GridHostResolver gridHostResolver;

	@Autowired
	private FileType fastqFileType;

	private StepExecution stepExecution;
	private Integer libraryCellId;
	
	/**
	 * 
	 */
	public HelptagTasklet() {
		// proxy
	}

	public HelptagTasklet(String libraryCellId) {
		Assert.assertParameterNotNull(libraryCellId);
		this.libraryCellId = Integer.valueOf(libraryCellId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@RetryOnExceptionExponential
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		// if the work has already been started, then check to see if it is finished
		// if not, throw an exception that is caught by the repeat policy.
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (repeatStatus.equals(RepeatStatus.FINISHED)) {
			// the work unit is complete, parse output
			GridResult result = getStartedResult(context);
			// parse and save output
			return RepeatStatus.FINISHED;
		}
		
/*
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(libraryCellId);
		
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		stepContext.put("cellLibId", cellLib.getId()); //place in the step context
		
		Job job = sampleService.getJobOfLibraryOnCell(cellLib);
		
		logger.debug("Beginning HelpTagging step 1 (generate hcount file) for cellLibrary " + cellLib.getId() + " from job " + job.getId());
		
		Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLib, fastqFileType); // TODO: change to bamFileType later
		
		logger.debug("ffileGroups.size()="+fileGroups.size());
		Assert.assertTrue(fileGroups.size() == 1);
		FileGroup fg = fileGroups.iterator().next();
		
		logger.debug("file group: " + fg.getId() + ":" + fg.getDescription());
		
		Map<String,Object> jobParameters = context.getStepContext().getJobParameters();
		
		for (String key : jobParameters.keySet()) {
			logger.debug("Key: " + key + " Value: " + jobParameters.get(key).toString());
		}
		
		WorkUnit w = helptag.getHelptag(cellLib, fg);
*/
		// get work unit
		WorkUnit w = helptag.getHelptag(libraryCellId);

//		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + job.getId());
   
		GridResult result = gridHostResolver.execute(w);
		
		//place the grid result in the step context
		storeStartedResult(context, result);
		
		// place scratch directory in step execution context, to be promoted
		// to the job context at run time.
/*		stepContext.put("scrDir", result.getWorkingDirectory());
		stepContext.put("createHcountName", result.getId());
*/

		return RepeatStatus.CONTINUABLE;
	}
	
	public static void doWork(int cellLibraryId) {
		
	}
	
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.debug("StepExecutionListener beforeStep saving StepExecution");
		this.stepExecution = stepExecution;
		
	}

}
