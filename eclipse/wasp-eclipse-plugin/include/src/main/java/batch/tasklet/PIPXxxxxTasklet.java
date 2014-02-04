/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package ___package___.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.service.JobService;

/**
 * @author 
 * 
 */
public class ___PluginIName___Tasklet extends WaspRemotingTasklet {
	
	@Autowired
	private JobService jobService;
	
	private Job job;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	

	public ___PluginIName___Tasklet() {
		// proxy
	}

	public ___PluginIName___Tasklet(String jobId) {
		this.job = jobService.getJobByJobId(Integer.parseInt(jobId));
	}
	
	/**
	 * Setup work to be run remotely
	 * @param context
	 * @throws Exception
	 */
	@Override
	public void doExecute(ChunkContext context) throws Exception {
		WorkUnit w = new WorkUnit();
		
		//configure
		
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + job.getId());
   
		GridResult result = gridHostResolver.execute(w);
		
		//place the grid result in the step context
		storeStartedResult(context, result);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (!repeatStatus.isContinuable()) {
			// the work unit is complete, a result is available if required
			GridResult result = getStartedResult(context);
		}
		return repeatStatus;
	}
	
}
