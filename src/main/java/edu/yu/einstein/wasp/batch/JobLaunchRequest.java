package edu.yu.einstein.wasp.batch;

import java.util.Collections;
import java.util.Map;

import org.springframework.batch.core.JobParameter;

public class JobLaunchRequest {

    private final String jobName;
    private final Map<String, String> jobParameters;

    /**
     * Request job with name jobName to start
     * 
     * @param jobName
     */
    @SuppressWarnings("unchecked")
    public JobLaunchRequest(String jobName) {
        this(jobName, Collections.EMPTY_MAP);
    }

    /**
     * Request job with name jobName and parameters jobParams to start
     * 
     * @param jobName
     * @param jobParams
     */
    public JobLaunchRequest(String jobName, Map<String, String> jobParams) {
        super();
        this.jobName = jobName;
        jobParameters = jobParams;
    }

    /**
     * getter for jobName
     * 
     * @return jobName
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * get the parameter for the job execution request {@link JobParameter}
     * 
     * @return man containing parameters of job {@link JobParameter}
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getJobParameters() {
        return jobParameters == null ? Collections.EMPTY_MAP :
                        Collections.unmodifiableMap(jobParameters);
    }

}