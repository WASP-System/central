/**
 * 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata.batch.tasklet;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		
		logger.info("normal execution here");
		
		GridResult dummy = new GridResultImpl();
		
		saveGridResult(context, dummy);

	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
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
	@Transactional("entityManager")
	public WorkUnit prepareWorkUnit() throws Exception {
		return null;
	}

}
