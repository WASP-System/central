package edu.yu.einstein.wasp.integration.serviceactivators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.Message;
import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.core.GrantedAuthority;

//import edu.yu.einstein.wasp.controller.PlatformUnitController.SelectOptionsMeta;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.JobStatusMessageTemplate;

import edu.yu.einstein.wasp.security.WaspJdbcDaoImpl;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.UserService;

import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.User;
//import edu.yu.einstein.wasp.model.Role;

public class JobEmailServiceActivator {
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private JobService jobService;

	@Autowired
	private UserService userService;

	@Autowired
	private WaspJdbcDaoImpl waspJdbcDaoImpl;
	
	@Value("${wasp.email.jobstart.rolenames:js;pi;lm;fm;da;su;}")
	private String jobStartRolenames;

	private static final Logger logger = LoggerFactory.getLogger(JobEmailServiceActivator.class);
	
	private List<String> convertDelimitedListToArrayList(String delimitedList, String delimiter){
		List<String> list = new ArrayList<String>();
		String[] tokens = delimitedList.split(delimiter);
		for(String token : tokens){
			list.add(token);							
		}
		return list;
	}
	
	@ServiceActivator
	public void isSuccessfulRun(Message<WaspStatus> jobStatusMessage) {
		if (!JobStatusMessageTemplate.isMessageOfCorrectType(jobStatusMessage)){
			logger.warn("Message is not of the correct type (a Job message). Check service activator and input channel are correct");
		}
		JobStatusMessageTemplate jobStatusMessageTemplate = new JobStatusMessageTemplate(jobStatusMessage);
		if (jobStatusMessageTemplate.getStatus().equals(WaspStatus.STARTED) && jobStatusMessageTemplate.getTask().equals(WaspTask.NOTIFY_STATUS)){			
			Job job = jobService.getJobByJobId(jobStatusMessageTemplate.getJobId());
			if(job != null && job.getJobId() != null){

				//emailService.sendFacilityManagerJobStartedConfirmRequest(job);

				String jobIdAsString = job.getJobId().toString();
				String departmentIdAsString = job.getLab().getDepartment().getDepartmentId().toString();
				
				logger.debug("ROB jobIdAsString: " + jobIdAsString);
				logger.debug("ROB departmentIdAsString: " + departmentIdAsString);
				
				
				List<String> rolesForJobStart = convertDelimitedListToArrayList(this.jobStartRolenames, ";");
				
				logger.debug("rolesForJobStart as list items");
				for(String s : rolesForJobStart){
					logger.debug("ROB ---"+s);
				}
				
				
				for(User user : userService.getUserDao().findAll()){
					
					
					
					logger.debug("ROB User: " + user.getLogin());
					
					
					
					
					List<GrantedAuthority> grantedAuthorityList = waspJdbcDaoImpl.getUserWaspAuthorities(user.getLogin());
					
					logger.debug("ROB ----Granted authority: " + grantedAuthorityList.toString());
					for(GrantedAuthority ga : grantedAuthorityList){
						logger.debug("ROB ------"+ga.getAuthority());
					}
					
					Set<String> grantedAuthoritySet = new HashSet<String>();
					for(GrantedAuthority ga : grantedAuthorityList){
						grantedAuthoritySet.add(ga.getAuthority());
					}
					
					
					if(rolesForJobStart.contains("su") && (grantedAuthoritySet.contains("su") || grantedAuthoritySet.contains("su-*"))){
						logger.debug("ROB ----in su");
						emailService.sendJobStarted(job, user, "emails/inform_submitter_job_started");//TODO maybe change this email
					}
					else if(rolesForJobStart.contains("fm") && (grantedAuthoritySet.contains("fm") || grantedAuthoritySet.contains("fm-*"))){//facility manager
						logger.debug("ROB ----in fm");
						emailService.sendJobStarted(job, user, "emails/inform_facility_manager_job_started");
					}
					else if(rolesForJobStart.contains("da") && grantedAuthoritySet.contains("da-" + departmentIdAsString)){//dept admin
						logger.debug("ROB ----in da");
						emailService.sendJobStarted(job, user, "emails/inform_da_job_started");
					}					
					else if(rolesForJobStart.contains("pi") && grantedAuthoritySet.contains("pi-" + jobIdAsString) && grantedAuthoritySet.contains("js-" + jobIdAsString)){
						logger.debug("ROB ----in pi with submitter who is pi");
						emailService.sendJobStarted(job, user, "emails/inform_submitter_who_is_pi_job_started");
					}
					else if(rolesForJobStart.contains("pi") && grantedAuthoritySet.contains("pi-" + jobIdAsString) && !grantedAuthoritySet.contains("js-" + jobIdAsString)){
						logger.debug("ROB ----in pi with submitter NOT pi");
						emailService.sendJobStarted(job, user, "emails/inform_pi_or_lab_manager_job_started");
					}
					else if(rolesForJobStart.contains("lm") && grantedAuthoritySet.contains("lm-" + jobIdAsString)){
						logger.debug("ROB ----in lm");
						emailService.sendJobStarted(job, user, "emails/inform_pi_or_lab_manager_job_started");
					}
					else if(rolesForJobStart.contains("js") && grantedAuthoritySet.contains("js-" + jobIdAsString)){//job submitter (but not also pi)
						logger.debug("ROB ----in js");
						emailService.sendJobStarted(job, user, "emails/inform_submitter_job_started");
					}
					
				}
			}
			
			/*	old code, replaced by above		
			if(job != null && job.getJobId() != null){
				if(job.getUserId().intValue() != job.getLab().getPrimaryUserId().intValue()){//submitter is not the lab PI
					emailService.sendSubmitterJobStarted(job);
					emailService.sendPIJobStartedConfirmRequest(job);
				}
				else{
					emailService.sendSubmitterWhoIsAlsoThePIJobStartedConfirmRequest(job);
				}				
				emailService.sendLabManagerJobStartedConfirmRequest(job);//the designated lab manager in the submitter's lab
				emailService.sendDAJobStartedConfirmRequest(job);
				emailService.sendFacilityManagerJobStartedConfirmRequest(job);//the shared facility
			}
			*/
		}		
	}

}