/**
 * 
 */
package edu.yu.einstein.wasp.helptag.batch.tasklet;

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
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.helptag.software.Helptag;
import edu.yu.einstein.wasp.service.FileService;

/**
 * @author AJ
 *
 */
public class HelptagTasklet extends WaspTasklet {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private HelptagService helptagService;

	@Autowired
	private Helptag helptag;
	
	@Autowired
	private GridHostResolver hostResolver;

	private Integer filegroupId;
	
	/**
	 * 
	 */
	public HelptagTasklet() {
		// proxy
	}

	public HelptagTasklet(String filegroupId) {
		Assert.assertParameterNotNull(filegroupId);
		this.filegroupId = Integer.valueOf(filegroupId);
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
		
		// get work unit
		WorkUnit w = helptag.getHelptag(filegroupId);
		
		// execute it
		GridResult result = hostResolver.execute(w);
		
		//place the grid result in the step context
		storeStartedResult(context, result);
		
		return RepeatStatus.CONTINUABLE;
	}

}
