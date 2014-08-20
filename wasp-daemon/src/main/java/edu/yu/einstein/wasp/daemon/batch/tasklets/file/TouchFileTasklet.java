/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.tasklets.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridWorkService;

/**
 * @author calder
 *
 */
public class TouchFileTasklet implements Tasklet {
	
	@Autowired
	private GridHostResolver hostResolver;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String hostname;
	private String file;
	
	public TouchFileTasklet(String hostname, String file) {
		this.hostname = hostname;
		this.file = file;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		GridWorkService gws = hostResolver.getGridWorkService(hostname);
		gws.getGridFileService().touch(file);
		return RepeatStatus.FINISHED;
	}

}
