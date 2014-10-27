/**
 * 
 */
package edu.yu.einstein.wasp.helptag.batch.tasklet;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.helptag.software.Helptag;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author AJ
 *
 */
public class HelptagTasklet extends WaspRemotingTasklet  implements StepExecutionListener {

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

	private StepExecution stepExecution;
	
	private Integer cellLibraryId;
	
	/**
	 * 
	 */
	public HelptagTasklet() {
		// proxy
	}

	public HelptagTasklet(String cellLibraryId) {
		Assert.assertParameterNotNull(cellLibraryId);
		this.cellLibraryId = Integer.valueOf(cellLibraryId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		
/*
		SampleSource cellLib = sampleService.getSampleSourceDao().findById(cellLibraryId);
		
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
		WorkUnit w = helptag.getHelptag(cellLibraryId);

//		w.setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, helptag));
   
		GridResult result = gridHostResolver.execute(w);
		
		//place the grid result in the step context
		saveGridResult(context, result);
		
		// place scratch directory in step execution context, to be promoted
		// to the job context at run time.
/*		stepContext.put("scrDir", result.getWorkingDirectory());
		stepContext.put("createHcountName", result.getId());
*/

	}
	
	public static void doWork(int cellLibraryId) {
		
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
		logger.debug("StepExecutionListener beforeStep saving StepExecution");
		this.stepExecution = stepExecution;
		
	}

	@Override
	public void doCleanupBeforeRestart(ChunkContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
