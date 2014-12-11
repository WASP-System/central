package edu.yu.einstein.wasp.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.exception.JobContextInitializationException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.software.SoftwareConfiguration;

/**
 * Class to provide easy access to job information including resource and selected software information 
 * @author asmclellan
 *
 */
public class WaspJobContext {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Job job; // contains userid. labid, workflowid
	
	private Map<ResourceType, SoftwareConfiguration> configuredSoftwareByType;
	
	public WaspJobContext() {}
	
	public WaspJobContext(Job job) throws JobContextInitializationException {
		setJob(job);
		setContext();
	}
	
	public WaspJobContext(Integer jobId, JobService jobService) throws JobContextInitializationException {
		setJob(jobId, jobService);
		setContext();
	}
	
	public void setContext() throws JobContextInitializationException{
		try{
			configuredSoftwareByType = new HashMap<ResourceType, SoftwareConfiguration>();
			List<JobSoftware> swl = this.job.getJobSoftware();
			logger.debug("software length: " + swl.size());
			for (JobSoftware js: swl){
				Software software = js.getSoftware();
				logger.debug("Working with software: " + software.getIName());
				ResourceType softwareType = software.getResourceType();
				MetaHelper metaHelper = new MetaHelper(software.getIName(), JobMeta.class);  
				List<JobMeta> defaultMetaList = metaHelper.getMasterList(JobMeta.class);
				for (JobMeta meta : defaultMetaList)
					meta.setV(meta.getProperty().getDefaultVal()); // set meta value to the default value found in the property attribute
				Map<String, String> parameters = MetaHelper.getKeyValueMap(software.getIName(), defaultMetaList); // set default parameters
				logger.debug("default parameter list: " + parameters);
				List<JobMeta> jobMeta = job.getJobMeta();
				if (jobMeta != null){
					Map<String, String> jobCustomParams = MetaHelper.getKeyValueMap(software.getIName(), jobMeta);
					logger.debug("job custom parameter list: " + jobCustomParams);
					parameters.putAll(jobCustomParams); // override with any parameters set in job meta
				}
				configuredSoftwareByType.put(softwareType, new SoftwareConfiguration(software, parameters));
			}
		} catch(Exception e){
			throw new JobContextInitializationException(e.getMessage(), e);
		}
	}
	
	
	public ResourceCategory getResourceCategory(){
		return job.getJobResourcecategory().get(0).getResourceCategory(); // should only be one
	}
	
	/**
	 * Get a configured software instance based on softwareType e.g. the ResourceType for an 'aligner'. Returns null if not found
	 * @param getResourceType
	 * @return a {@link SoftwareConfiguration} object or null if no new found
	 */
	public SoftwareConfiguration getConfiguredSoftware(ResourceType softwareType){
		if (softwareType == null){
			logger.error("software ResourceType is null");
			return null;
		}
		if (! configuredSoftwareByType.containsKey(softwareType)){
			logger.error("Configured software does not contain an entry for ResourceType=" + softwareType.getIName());
			return null;
		}
		return configuredSoftwareByType.get(softwareType);
	}

	
	/**
	 * Get a configured software instance for all software types
	 * @param getResourceType
	 * @return Map<ResourceType, SoftwareConfiguration>
	 */
	public Map<ResourceType, SoftwareConfiguration> getConfiguredSoftware(){
		return configuredSoftwareByType;
	}

	public void setJob(Integer jobId, JobService jobService) {
		this.job = jobService.getJobByJobId(jobId);
	}
	
	public void setJob(Job job) {
		this.job = job;
	}
	
	public Job getJob(){
		return this.job;
	}
	

	
	
	
}
