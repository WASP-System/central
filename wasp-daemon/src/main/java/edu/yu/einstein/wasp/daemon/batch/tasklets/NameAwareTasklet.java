package edu.yu.einstein.wasp.daemon.batch.tasklets;

import org.springframework.batch.core.step.tasklet.Tasklet;

public interface NameAwareTasklet extends Tasklet {

	public String getName();
	
}
