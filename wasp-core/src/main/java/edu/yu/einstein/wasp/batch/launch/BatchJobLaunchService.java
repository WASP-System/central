package edu.yu.einstein.wasp.batch.launch;

import org.springframework.messaging.Message;

import edu.yu.einstein.wasp.integration.messages.WaspStatus;

/**
 * 
 * @author asmclellan
 *
 */
public interface BatchJobLaunchService {
	
	/**
	 * Launch batch job
	 * @param batchJobLaunchContext
	 * @return
	 * @throws Exception
	 */
	public Message<WaspStatus> launch(BatchJobLaunchContext batchJobLaunchContext) throws Exception;

}
