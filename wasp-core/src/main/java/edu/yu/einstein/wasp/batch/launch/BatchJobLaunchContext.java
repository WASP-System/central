package edu.yu.einstein.wasp.batch.launch;

import java.util.Map;

public class BatchJobLaunchContext {

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
