package edu.yu.einstein.wasp.batch.launch;

import org.springframework.integration.Message;

import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;

public interface BatchJobLauncher {
	
	public Message<WaspStatus> launch(BatchJobLaunchContext batchJobLaunchContext) throws Exception;

}
