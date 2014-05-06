package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author asmclellan
 *
 */
public class MergeSampleBamFilesTasklet extends AbstractGatkTasklet {
	
	public MergeSampleBamFilesTasklet(String inputFilegroupIds, Integer outputFilegroupId) {
		super(inputFilegroupIds, outputFilegroupId.toString());
	}
	
	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		
	}

}
