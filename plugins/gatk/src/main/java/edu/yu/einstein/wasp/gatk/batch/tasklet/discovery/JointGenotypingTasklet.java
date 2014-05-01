package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;

public class JointGenotypingTasklet extends WaspRemotingTasklet {

	public JointGenotypingTasklet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		// TODO Auto-generated method stub

	}

}
