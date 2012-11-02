package edu.yu.einstein.wasp.batch.launch;

public interface BatchJobLauncher {
	
	public void launch(BatchJobLaunchContext batchJobLaunchContext) throws Exception;

}
