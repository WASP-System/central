/**
 * 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata.batch.tasklet;

import org.springframework.batch.core.scope.context.ChunkContext;

import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridResultImpl;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;

/**
 * @author calder
 *
 */
public class TestTestForGenomeIndexTasklet extends TestForGenomeIndexTasklet {
	
	private int checks = 0;

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void doExecute(ChunkContext context) throws Exception {
		
		logger.info("normal execution here");
		
		GridResult dummy = new GridResultImpl();
		
		saveGridResult(context, dummy);

	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public GenomeIndexStatus getGenomeIndexStatus() {
		if (checks < 5) {
			logger.info("simulating genome build " + checks++);
			return GenomeIndexStatus.BUILDING;
		}
		logger.info("signaling build genome");
		return GenomeIndexStatus.BUILT;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public WorkUnit prepareWorkUnit() throws Exception {
		return null;
	}

}
