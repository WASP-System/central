package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author asmclellan
 *
 */
public class RealignTestControlPairsTasklet extends AbstractGatkTasklet {
	
	public RealignTestControlPairsTasklet(String inputFilegroupIds, String outputFilegroupIds) {
		super(inputFilegroupIds, outputFilegroupIds);
	}
	
	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		
	}

}
