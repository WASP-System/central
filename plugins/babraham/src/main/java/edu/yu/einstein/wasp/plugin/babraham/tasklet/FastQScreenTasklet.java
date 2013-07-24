/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQScreen;
import edu.yu.einstein.wasp.service.FileService;

/**
 * @author calder / asmclellan
 *
 */
public class FastQScreenTasklet extends WaspTasklet {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private BabrahamService babrahamService;
	
	@Autowired
	private FastQScreen fastqscreen;
	
	@Autowired
	private GridHostResolver hostResolver;

	private Integer fileGroupId;
	
	public static final String FASTQSCREEN_PLOT_META_KEY = "fastqscreen_plot";

	/**
	 * 
	 */
	public FastQScreenTasklet() {
		// proxy
	}

	public FastQScreenTasklet(String fileGroupId) {
		Assert.assertParameterNotNull(fileGroupId);
		this.fileGroupId = Integer.valueOf(fileGroupId);
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
			babrahamService.saveJsonForParsedSoftwareOutput(fastqscreen.parseOutput(result.getResultsDirectory()), FASTQSCREEN_PLOT_META_KEY, fastqscreen, fileGroupId);
			return RepeatStatus.FINISHED;
		}
		
		// get work unit
		WorkUnit w = fastqscreen.getFastQScreen(fileGroupId);
		
		// execute it
		GridResult result = hostResolver.execute(w);
		
		//place the grid result in the step context
		storeStartedResult(context, result);
		
		return RepeatStatus.CONTINUABLE;
	}

}
