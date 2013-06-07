/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package ___package___.___pluginname___.tasklet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.filetype.service.FastqService;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.mps.bwa.BWASoftwareComponent;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author calder
 * 
 */
public class ___Pluginname___Tasklet extends WaspTasklet {
	
	@Autowired
	private JobService jobService;
	
	private Job job;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	

	public ___Pluginname___Tasklet() {
		// proxy
	}

	public ___Pluginname___Tasklet(String jobId) {
		this.job = jobService.getJobById(Integer.parseInt(jobId));
	}

	/**
	 * 
	 * @param contrib
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@Override
	@RetryOnExceptionExponential
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		// if the work has already been started, then check to see if it is finished
		// if not, throw an exception that is caught by the repeat policy.
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (repeatStatus.equals(RepeatStatus.FINISHED))
			return RepeatStatus.FINISHED;
		
		WorkUnit w = new WorkUnit();
		
		//configure
		
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + job.getId());
   
		GridResult result = gridHostResolver.execute(w);
		
		//place the grid result in the step context
		storeStartedResult(context, result);

		return RepeatStatus.CONTINUABLE;
	}
	
}
