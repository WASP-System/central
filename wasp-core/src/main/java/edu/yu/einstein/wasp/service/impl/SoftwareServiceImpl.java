package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.exception.JobContextInitializationException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.service.SoftwareService;
import edu.yu.einstein.wasp.software.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;

@Service
@Transactional("entityManager")
public class SoftwareServiceImpl extends WaspServiceImpl implements SoftwareService{
	
	@Autowired
	private SoftwareDao softwareDao;
	
	@Autowired 
	private JobSoftwareDao jobSoftwareDao;

	public SoftwareServiceImpl() {
		
	}
	
	@Override
	public Software getById(Integer id){
		return softwareDao.getById(id);
	}
	
	@Override
	public List<Software> getAll(){
		return softwareDao.findAll();
	}
	
	@Override
	public List<Software> getAllSoftwareForJob(Job job){
		List<Software> s = new ArrayList<>();
		Map<String, Integer> m = new HashMap<>();
		m.put("jobId", job.getId());
		List<JobSoftware> jsForJob = jobSoftwareDao.findByMap(m);
		if (jsForJob != null)
			for (JobSoftware js : jsForJob)
				s.add(js.getSoftware());
		return s;
	}
	
	@Override
	public JobSoftware saveJobSoftware(JobSoftware jobSoftware){
		return jobSoftwareDao.save(jobSoftware);
	}
	
	@Override
	public SoftwareConfiguration getDefaultSoftwareConfig(Software software) throws JobContextInitializationException{
		List<JobSoftware> jobSoftware = new ArrayList<>();
		JobSoftware js = new JobSoftware();
		Job job = new Job();
		js.setJob(job);
		js.setSoftware(software);
		jobSoftware.add(js);
		job.setJobSoftware(jobSoftware);
		WaspJobContext waspJobContext = new WaspJobContext(job);
		return waspJobContext.getConfiguredSoftware(software.getResourceType());
	}

	@Override
	public void removeJobSoftware(JobSoftware jobSoftware){
		jobSoftwareDao.remove(jobSoftware);
	}
}
