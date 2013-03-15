package edu.yu.einstein.wasp.integration.serviceactivators;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.Message;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.core.GrantedAuthority;

import edu.yu.einstein.wasp.MetaMessage;
//import edu.yu.einstein.wasp.controller.PlatformUnitController.SelectOptionsMeta;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.JobStatusMessageTemplate;

import edu.yu.einstein.wasp.security.WaspJdbcDaoImpl;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.UserService;

import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.User;
//import edu.yu.einstein.wasp.model.Role;

@Transactional("entityManager")//must have this or we get an exception in batch:  org.hibernate.LazyInitializationException: could not initialize proxy - no Session
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

	@Value("${wasp.email.jobabandoned.rolenames:js;pi;lm;fm;da;su;}")
	private String jobAbandonedRolenames;

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
				String jobIdAsString = job.getJobId().toString();
				String labIdAsString = job.getLab().getId().toString();
				String departmentIdAsString = job.getLab().getDepartment().getDepartmentId().toString();
				List<String> rolesForJobStart = convertDelimitedListToArrayList(this.jobStartRolenames, ";");
				for(User user : userService.getUserDao().findAll()){
					List<GrantedAuthority> grantedAuthorityList = waspJdbcDaoImpl.getUserWaspAuthorities(user.getLogin());
					Set<String> grantedAuthoritySet = new HashSet<String>();
					for(GrantedAuthority ga : grantedAuthorityList){
						grantedAuthoritySet.add(ga.getAuthority());
					}					
					//note that it really is not necessary to go through all users since the jobsubmitter, the pi, the correct lab managers, the facilty manager, and superuser can quickly be gotten directly, but.... 
					if(rolesForJobStart.contains("su") && (grantedAuthoritySet.contains("su") || grantedAuthoritySet.contains("su-*"))){
						emailService.sendJobStarted(job, job.getUser(), "emails/inform_submitter_job_started");//TODO maybe change this email
					}
					else if(rolesForJobStart.contains("fm") && (grantedAuthoritySet.contains("fm") || grantedAuthoritySet.contains("fm-*"))){//facility manager
						emailService.sendJobStarted(job, user, "emails/inform_facility_manager_job_started");
					}
					else if(rolesForJobStart.contains("da") && grantedAuthoritySet.contains("da-" + departmentIdAsString)){//dept admin
						emailService.sendJobStarted(job, user, "emails/inform_da_job_started");
					}					
					else if(rolesForJobStart.contains("pi") && grantedAuthoritySet.contains("pi-" + labIdAsString) && grantedAuthoritySet.contains("js-" + jobIdAsString)){//the pi of the lab is also the job submitter
						emailService.sendJobStarted(job, user, "emails/inform_submitter_who_is_pi_job_started");
					}
					else if(rolesForJobStart.contains("pi") && grantedAuthoritySet.contains("pi-" + labIdAsString) && !grantedAuthoritySet.contains("js-" + jobIdAsString)){
						emailService.sendJobStarted(job, user, "emails/inform_pi_or_lab_manager_job_started");
					}
					else if(rolesForJobStart.contains("lm") && grantedAuthoritySet.contains("lm-" + labIdAsString)){
						emailService.sendJobStarted(job, user, "emails/inform_pi_or_lab_manager_job_started");
					}
					else if(rolesForJobStart.contains("js") && grantedAuthoritySet.contains("js-" + jobIdAsString)){//job submitter (but not also pi)
						emailService.sendJobStarted(job, user, "emails/inform_submitter_job_started");
					}					
				}//end for(User user
			}//end if (job != null		
		}//end if (jobStatusMessageTemplate.getStatus().equals(WaspStatus.STARTED) && job
		else if (   jobStatusMessageTemplate.getStatus().equals(WaspStatus.ABANDONED) 
				&& 
				(   jobStatusMessageTemplate.getTask().equals(WaspJobTask.PI_APPROVE)
					||
					jobStatusMessageTemplate.getTask().equals(WaspJobTask.FM_APPROVE)
					||
					jobStatusMessageTemplate.getTask().equals(WaspJobTask.DA_APPROVE)   ) 	){			
			Job job = jobService.getJobByJobId(jobStatusMessageTemplate.getJobId());
			if(job != null && job.getId() != null){
				logger.debug("ROB ---- at A in js for rejected by an admin or pi for cause");
				String jobIdAsString = job.getJobId().toString();
				String labIdAsString = job.getLab().getId().toString();
				String departmentIdAsString = job.getLab().getDepartment().getDepartmentId().toString();
				List<String> rolesForJobAbandoned = convertDelimitedListToArrayList(this.jobAbandonedRolenames, ";");
				logger.debug("ROB ---- at B in js for rejected by an admin or pi for cause");
				String whoAbandonedJob = "";
				String reasonForAbandoned = "";
				String jobApproveCode = "";
				if(jobStatusMessageTemplate.getTask().equals(WaspJobTask.PI_APPROVE)){
					logger.debug("ROB ---- at C in js for rejected by an admin or pi for cause");
					whoAbandonedJob += "Principal Investigator or designated Lab Manager";
					jobApproveCode = "piApprove";
				}
				else if(jobStatusMessageTemplate.getTask().equals(WaspJobTask.FM_APPROVE)){
					logger.debug("ROB ---- at D in js for rejected by an admin or pi for cause");
					whoAbandonedJob += "Sequencing Facility Manager";
					jobApproveCode = "fmApprove";
				}
				else if(jobStatusMessageTemplate.getTask().equals(WaspJobTask.DA_APPROVE)){
					logger.debug("ROB ---- at E in js for rejected by an admin or pi for cause");
					whoAbandonedJob += "Accounts Manager";
					jobApproveCode = "daApprove";
				}
				logger.debug("ROB ---- at F in js for rejected by an admin or pi for cause");
				List<MetaMessage> metaMessageList = jobService.getJobApprovalComments(jobApproveCode, job.getId());
				if(metaMessageList.size()>0){
					logger.debug("ROB ---- at G in js for rejected by an admin or pi for cause");
					//MetaMessage mm = metaMessageList.get(metaMessageList.size()-1);
					//reasonForAbandoned += mm.getValue();//unfortunately, this comes up empty, since same transaction or too fast, so use jobStatusMessageTemplate
					String comment = jobStatusMessageTemplate.getComment();
					User userWhoCreatedComment = jobStatusMessageTemplate.getUserCreatingMessage();
					reasonForAbandoned = comment + " (" + userWhoCreatedComment.getNameFstLst() + ")";
				}
				
				
				for(User user : userService.getUserDao().findAll()){
					logger.debug("ROB ---- at H in js for rejected by an admin or pi for cause");
					List<GrantedAuthority> grantedAuthorityList = waspJdbcDaoImpl.getUserWaspAuthorities(user.getLogin());
					Set<String> grantedAuthoritySet = new HashSet<String>();
					for(GrantedAuthority ga : grantedAuthorityList){
						grantedAuthoritySet.add(ga.getAuthority());
					}					
					//note that it really is not necessary to go through all users since the jobsubmitter, the pi, the correct lab managers, the facilty manager, and superuser can quickly be gotten directly, but.... 
					if(rolesForJobAbandoned.contains("su") && (grantedAuthoritySet.contains("su") || grantedAuthoritySet.contains("su-*"))){
						logger.debug("ROB ---- at I in js for rejected by an admin or pi for cause");
						emailService.sendJobAbandoned(job, job.getUser(), "emails/inform_job_abandoned", whoAbandonedJob, reasonForAbandoned);//TODO maybe change this email
					}
					else if(rolesForJobAbandoned.contains("fm") && (grantedAuthoritySet.contains("fm") || grantedAuthoritySet.contains("fm-*"))){//facility manager
						logger.debug("ROB ---- at J in js for rejected by an admin or pi for cause");
						emailService.sendJobAbandoned(job, user, "emails/inform_job_abandoned", whoAbandonedJob, reasonForAbandoned);
					}
					else if(rolesForJobAbandoned.contains("da") && grantedAuthoritySet.contains("da-" + departmentIdAsString)){//dept admin
						logger.debug("ROB ---- at K in js for rejected by an admin or pi for cause");
						emailService.sendJobAbandoned(job, user, "emails/inform_job_abandoned", whoAbandonedJob, reasonForAbandoned);
					}					
					else if(rolesForJobAbandoned.contains("pi") && grantedAuthoritySet.contains("pi-" + labIdAsString)){
						logger.debug("ROB ---- at K in js for rejected by an admin or pi for cause");
						emailService.sendJobAbandoned(job, user, "emails/inform_job_abandoned", whoAbandonedJob, reasonForAbandoned);
					}
					else if(rolesForJobAbandoned.contains("lm") && grantedAuthoritySet.contains("lm-" + labIdAsString)){
						logger.debug("ROB ---- at M in js for rejected by an admin or pi for cause");
						emailService.sendJobAbandoned(job, user, "emails/inform_job_abandoned", whoAbandonedJob, reasonForAbandoned);
					}
					else if(rolesForJobAbandoned.contains("js") && grantedAuthoritySet.contains("js-" + jobIdAsString)){//job submitter (but not also pi)
						logger.debug("ROB ---- at N in js for rejected by an admin or pi for cause");
						emailService.sendJobAbandoned(job, user, "emails/inform_job_abandoned", whoAbandonedJob, reasonForAbandoned);
					}	
					logger.debug("ROB ---- at O in js for rejected by an admin or pi for cause");
				}//end for(User user
				logger.debug("ROB ---- at P in js for rejected by an admin or pi for cause");
				
			}
		}
				
	}//end of method isSuccessfulRun(Message
}