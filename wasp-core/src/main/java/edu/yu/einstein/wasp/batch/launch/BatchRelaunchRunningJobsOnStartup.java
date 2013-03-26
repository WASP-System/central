package edu.yu.einstein.wasp.batch.launch;

/**
 * Interface for launching running jobs on batch restart
 * @author asmclellan
 *
 */
public interface BatchRelaunchRunningJobsOnStartup {
		
	
	/**
	 * Relaunch all batch jobs in state BatchStatus.STARTING and BatchStatus.STARTED
	 */
	public void doLaunchAllRunningJobs();
	
}
