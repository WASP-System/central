package edu.yu.einstein.wasp.batch.launch;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * @author asmclellan
 *
 */
public class BatchJobLaunchContext implements Serializable{

	private static final long serialVersionUID = 404143863227754279L;

	private String jobName;
	
	private Map<String, String> jobParameters;
	
	public BatchJobLaunchContext(String jobName, Map<String, String> jobParameters) {
		this.jobName = jobName;
		this.jobParameters = jobParameters;
	}
	
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Map<String, String> getJobParameters() {
		return jobParameters;
	}

	public void setJobParameters(Map<String, String> jobParameters) {
		this.jobParameters = jobParameters;
	}
	
}
