package edu.yu.einstein.wasp.service;

import java.util.List;

import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.Software;

public interface SoftwareService extends WaspService {
	
	public Software getById(Integer id);
	
	public List<Software> getAll();
	
	public List<Software> getAllSoftwareForJob(Job job);
	
	public JobSoftware saveJobSoftware(JobSoftware jobSoftware);

}