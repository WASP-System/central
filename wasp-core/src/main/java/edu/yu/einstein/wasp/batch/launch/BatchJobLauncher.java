package edu.yu.einstein.wasp.batch.launch;

import org.springframework.integration.Message;

import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;

public interface BatchJobLauncher {
	
	/**
	 * Launch batch job
	 * @param batchJobLaunchContext
	 * @return
	 * @throws Exception
	 */
	public Message<WaspStatus> launch(BatchJobLaunchContext batchJobLaunchContext) throws Exception;

}
