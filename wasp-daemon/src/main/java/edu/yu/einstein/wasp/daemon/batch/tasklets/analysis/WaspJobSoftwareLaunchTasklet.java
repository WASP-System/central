package edu.yu.einstein.wasp.daemon.batch.tasklets.analysis;

import java.util.List;
import java.util.Map;

import org.springframework.batch.core.step.tasklet.Tasklet;

import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * We need to program to this interface otherwise if an implemented class is declared to be @Transactional, such a class cannot be autowired due to proxying
 * (the proxy needs to have a common interface with concrete class)
 * @author asmclellan
 *
 */
public interface WaspJobSoftwareLaunchTasklet extends Tasklet {

	public void setWaspPluginRegistry(WaspPluginRegistry waspPluginRegistry);

	public void setJobService(JobService jobService);
	
	public void setSampleService(SampleService sampleService);

	public void setCellLibraryId(Integer cellLibraryId);

	public void setCellLibraryIds(List<Integer> cellLibraryIds);

	public void setJobId(Integer jobId);

	public void setTask(String task);

	public Map<String, String> getAdditionalJobParameters();

	public void setAdditionalJobParameters(Map<String, String> additionalJobParameters);

}
