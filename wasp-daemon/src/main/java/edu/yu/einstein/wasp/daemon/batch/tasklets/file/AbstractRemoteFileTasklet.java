/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.tasklets.file;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;

/**
 * @author calder
 *
 */
public abstract class AbstractRemoteFileTasklet extends WaspRemotingTasklet {
	
	public static final String FILE_DELIMITER = ",";
	public static final String FILE_TRANSFER_BEGUN_SEMAPHORE = "transfer_started.txt";
	public static final String FILE_TRANSFER_COMPLETE_SEMAPHORE= "transfer_complete.txt";

}
