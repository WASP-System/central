/**
 * 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata.batch.tasklet;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridResultImpl;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
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
	public GridResult doExecute(ChunkContext context) throws Exception {
		
		logger.info("normal execution here");
		
		return new GridResultImpl();

	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public GenomeIndexStatus getGenomeIndexStatus(StepExecution stepExecution) {
		if (checks < 5) {
			logger.info("simulating genome build " + checks++);
			return GenomeIndexStatus.BUILDING;
		}
		logger.info("signaling build genome");
		return GenomeIndexStatus.BUILT;
	}

	@Override
	public WorkUnitGridConfiguration configureWorkUnit(StepExecution stepExecution) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkUnit buildWorkUnit(StepExecution stepExecution) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doCleanupBeforeRestart(ChunkContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

}
