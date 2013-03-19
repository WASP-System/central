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

	@Value("${wasp.email.jobaccepted.rolenames:js;pi;lm;}")
	private String jobAcceptedRolenames;

	@Value("${wasp.email.jobcompleted.rolenames:js;pi;lm;fm;da;su;}")
	private String jobCompletedRolenames;

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
				
				List<String> newRolesForJobStart = new ArrayList<String>();
				for(String str : rolesForJobStart){
					if(!str.equalsIgnoreCase("su") && !str.equalsIgnoreCase("fm") && 
						!str.equalsIgnoreCase("da") && !str.equalsIgnoreCase("pi") &&
						!str.equalsIgnoreCase("lm") && !str.equalsIgnoreCase("js") ){
						newRolesForJobStart.add(str);
					}						
				}
				
				for(User user : userService.getUserDao().findAll()){
					List<GrantedAuthority> grantedAuthorityList = waspJdbcDaoImpl.getUserWaspAuthorities(user.getLogin());
					Set<String> grantedAuthoritySet = new HashSet<String>();
					for(GrantedAuthority ga : grantedAuthorityList){
						grantedAuthoritySet.add(ga.getAuthority());
					}					
					//note that it really is not necessary to go through all users since the jobsubmitter, the pi, the correct lab managers, the facilty manager, and superuser can quickly be gotten directly, but.... 
					if(rolesForJobStart.contains("su") && (grantedAuthoritySet.contains("su") || grantedAuthoritySet.contains("su-*"))){
						emailService.sendJobStarted(job, user, "emails/inform_facility_manager_job_started");//TODO maybe change this email
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
					else if(newRolesForJobStart.size()>0){
						for(String str : newRolesForJobStart){
							if(grantedAuthoritySet.contains(str) || grantedAuthoritySet.contains(str + "-*")){
								emailService.sendJobStarted(job, user, "emails/inform_generic_job_started");
								break;
							}
						}
					}
				}//end for(User user
			}//end if (job != null		
		}//end if (jobStatusMessageTemplate.getStatus().equals(WaspStatus.STARTED) && job
		else if (    (jobStatusMessageTemplate.getStatus().equals(WaspStatus.ABANDONED)  
					&& 
					(
						jobStatusMessageTemplate.getTask().equals(WaspJobTask.PI_APPROVE)//at least one rejected job
						||
						jobStatusMessageTemplate.getTask().equals(WaspJobTask.FM_APPROVE)
						||
						jobStatusMessageTemplate.getTask().equals(WaspJobTask.DA_APPROVE)
					))
					
					|| 
					
					( jobStatusMessageTemplate.getStatus().equals(WaspStatus.ACCEPTED) && jobStatusMessageTemplate.getTask().equals(WaspTask.NOTIFY_STATUS) )//all three accepted the job
					
				){			
			Job job = jobService.getJobByJobId(jobStatusMessageTemplate.getJobId());
			if(job != null && job.getId() != null){
				String jobIdAsString = job.getJobId().toString();
				String labIdAsString = job.getLab().getId().toString();
				String departmentIdAsString = job.getLab().getDepartment().getDepartmentId().toString();
				List<String> rolesForJobAbandoned = convertDelimitedListToArrayList(this.jobAbandonedRolenames, ";");
				List<String> rolesForJobAccepted = convertDelimitedListToArrayList(this.jobAcceptedRolenames, ";");
				String whoAbandonedJob = "";
				String reasonForAbandoned = "";
				String jobApproveCode = "";
				if(jobStatusMessageTemplate.getTask().equals(WaspJobTask.PI_APPROVE)){
					whoAbandonedJob += "Principal Investigator or designated Lab Manager";
					jobApproveCode = "piApprove";
				}
				else if(jobStatusMessageTemplate.getTask().equals(WaspJobTask.FM_APPROVE)){
					whoAbandonedJob += "Sequencing Facility Manager";
					jobApproveCode = "fmApprove";
				}
				else if(jobStatusMessageTemplate.getTask().equals(WaspJobTask.DA_APPROVE)){
					whoAbandonedJob += "Accounts Manager";
					jobApproveCode = "daApprove";
				}
				String comment = jobStatusMessageTemplate.getComment();
				User userWhoCreatedComment = jobStatusMessageTemplate.getUserCreatingMessage();
				reasonForAbandoned = comment + " (" + userWhoCreatedComment.getNameFstLst() + ")";
				/* never worked this way
				List<MetaMessage> metaMessageList = jobService.getJobApprovalComments(jobApproveCode, job.getId());
				if(metaMessageList.size()>0){
					logger.debug("ROB ---- at G in js for rejected by an admin or pi for cause");
					//MetaMessage mm = metaMessageList.get(metaMessageList.size()-1);
					//reasonForAbandoned += mm.getValue();//unfortunately, this comes up empty, since same transaction or too fast, so use jobStatusMessageTemplate
				}
				*/
				
				List<String> newRolesForJobAbandoned = new ArrayList<String>();
				for(String str : rolesForJobAbandoned){
					if(!str.equalsIgnoreCase("su") && !str.equalsIgnoreCase("fm") && 
						!str.equalsIgnoreCase("da") && !str.equalsIgnoreCase("pi") &&
						!str.equalsIgnoreCase("lm") && !str.equalsIgnoreCase("js") ){
						newRolesForJobAbandoned.add(str);
					}						
				}

				List<String> newRolesForJobAccepted = new ArrayList<String>();
				for(String str : rolesForJobAccepted){
					if(!str.equalsIgnoreCase("pi") && !str.equalsIgnoreCase("lm") && !str.equalsIgnoreCase("js") ){
						newRolesForJobAccepted.add(str);
					}						
				}

				
				for(User user : userService.getUserDao().findAll()){
					List<GrantedAuthority> grantedAuthorityList = waspJdbcDaoImpl.getUserWaspAuthorities(user.getLogin());
					Set<String> grantedAuthoritySet = new HashSet<String>();
					for(GrantedAuthority ga : grantedAuthorityList){
						grantedAuthoritySet.add(ga.getAuthority());
					}
					if(jobStatusMessageTemplate.getStatus().equals(WaspStatus.ACCEPTED)){
						if(rolesForJobAccepted.contains("pi") && grantedAuthoritySet.contains("pi-" + labIdAsString)){
							emailService.sendJobAccepted(job, user, "emails/inform_job_accepted");
						}
						else if(rolesForJobAccepted.contains("lm") && grantedAuthoritySet.contains("lm-" + labIdAsString)){
							emailService.sendJobAccepted(job, user, "emails/inform_job_accepted");
						}
						else if(rolesForJobAccepted.contains("js") && grantedAuthoritySet.contains("js-" + jobIdAsString)){//job submitter (but not also pi)
							emailService.sendJobAccepted(job, user, "emails/inform_job_accepted");
						}
						else if(newRolesForJobAccepted.size()>0){
							for(String str : newRolesForJobAccepted){
								if(grantedAuthoritySet.contains(str) || grantedAuthoritySet.contains(str + "-*")){
									emailService.sendJobAccepted(job, user, "emails/inform_job_accepted");
									break;
								}
							}
						}
					}
					else if(jobStatusMessageTemplate.getStatus().equals(WaspStatus.ABANDONED)){
					//note that it really is not necessary to go through all users since the jobsubmitter, the pi, the correct lab managers, the facilty manager, and superuser can quickly be gotten directly, but.... 
						if(rolesForJobAbandoned.contains("su") && (grantedAuthoritySet.contains("su") || grantedAuthoritySet.contains("su-*"))){
							emailService.sendJobAbandoned(job, user, "emails/inform_job_abandoned", whoAbandonedJob, reasonForAbandoned);//TODO maybe change this email
						}
						else if(rolesForJobAbandoned.contains("fm") && (grantedAuthoritySet.contains("fm") || grantedAuthoritySet.contains("fm-*"))){//facility manager
							emailService.sendJobAbandoned(job, user, "emails/inform_job_abandoned", whoAbandonedJob, reasonForAbandoned);
						}
						else if(rolesForJobAbandoned.contains("da") && grantedAuthoritySet.contains("da-" + departmentIdAsString)){//dept admin
							emailService.sendJobAbandoned(job, user, "emails/inform_job_abandoned", whoAbandonedJob, reasonForAbandoned);
						}					
						else if(rolesForJobAbandoned.contains("pi") && grantedAuthoritySet.contains("pi-" + labIdAsString)){
							emailService.sendJobAbandoned(job, user, "emails/inform_job_abandoned", whoAbandonedJob, reasonForAbandoned);
						}
						else if(rolesForJobAbandoned.contains("lm") && grantedAuthoritySet.contains("lm-" + labIdAsString)){
							emailService.sendJobAbandoned(job, user, "emails/inform_job_abandoned", whoAbandonedJob, reasonForAbandoned);
						}
						else if(rolesForJobAbandoned.contains("js") && grantedAuthoritySet.contains("js-" + jobIdAsString)){//job submitter (but not also pi)
							emailService.sendJobAbandoned(job, user, "emails/inform_job_abandoned", whoAbandonedJob, reasonForAbandoned);
						}
						else if(newRolesForJobAbandoned.size()>0){
							for(String str : newRolesForJobAbandoned){
								if(grantedAuthoritySet.contains(str) || grantedAuthoritySet.contains(str + "-*")){
									emailService.sendJobAccepted(job, user, "emails/inform_job_abandoned");
									break;
								}
							}
						}
					}
				}//end of for(User user				
			}//end of if(job!=null
		}//end of else if (jobStatusMessageTemplate.getStatus().equals(WaspStatus.ABANDONED) and jobApprovals
		else if (jobStatusMessageTemplate.getStatus().equals(WaspStatus.COMPLETED) && jobStatusMessageTemplate.getTask().equals(WaspTask.NOTIFY_STATUS)){			
			Job job = jobService.getJobByJobId(jobStatusMessageTemplate.getJobId());
			if(job != null && job.getJobId() != null){
				String jobIdAsString = job.getJobId().toString();
				String labIdAsString = job.getLab().getId().toString();
				String departmentIdAsString = job.getLab().getDepartment().getDepartmentId().toString();
				List<String> rolesForJobCompleted = convertDelimitedListToArrayList(this.jobCompletedRolenames, ";");
				
				List<String> newRolesForJobCompleted = new ArrayList<String>();
				for(String str : rolesForJobCompleted){
					if(!str.equalsIgnoreCase("su") && !str.equalsIgnoreCase("fm") && 
						!str.equalsIgnoreCase("da") && !str.equalsIgnoreCase("pi") &&
						!str.equalsIgnoreCase("lm") && !str.equalsIgnoreCase("js") ){
						newRolesForJobCompleted.add(str);
					}						
				}

				for(User user : userService.getUserDao().findAll()){
					List<GrantedAuthority> grantedAuthorityList = waspJdbcDaoImpl.getUserWaspAuthorities(user.getLogin());
					Set<String> grantedAuthoritySet = new HashSet<String>();
					for(GrantedAuthority ga : grantedAuthorityList){
						grantedAuthoritySet.add(ga.getAuthority());
					}					
					//note that it really is not necessary to go through all users since the jobsubmitter, the pi, the correct lab managers, the facilty manager, and superuser can quickly be gotten directly, but.... 
					if(rolesForJobCompleted.contains("su") && (grantedAuthoritySet.contains("su") || grantedAuthoritySet.contains("su-*"))){
						emailService.sendJobCompleted(job, user, "emails/inform_job_completed");//TODO maybe change this email
					}
					else if(rolesForJobCompleted.contains("fm") && (grantedAuthoritySet.contains("fm") || grantedAuthoritySet.contains("fm-*"))){//facility manager
						emailService.sendJobCompleted(job, user, "emails/inform_job_completed");
					}
					else if(rolesForJobCompleted.contains("da") && grantedAuthoritySet.contains("da-" + departmentIdAsString)){//dept admin
						emailService.sendJobCompleted(job, user, "emails/inform_job_completed");
					}					
					else if(rolesForJobCompleted.contains("pi") && grantedAuthoritySet.contains("pi-" + labIdAsString)){//the pi of the lab 
						emailService.sendJobCompleted(job, user, "emails/inform_job_completed");
					}
					else if(rolesForJobCompleted.contains("lm") && grantedAuthoritySet.contains("lm-" + labIdAsString)){
						emailService.sendJobCompleted(job, user, "emails/inform_job_completed");
					}
					else if(rolesForJobCompleted.contains("js") && grantedAuthoritySet.contains("js-" + jobIdAsString)){//job submitter (but not also pi)
						emailService.sendJobCompleted(job, user, "emails/inform_job_completed");
					}
					else if(newRolesForJobCompleted.size()>0){
						for(String str : newRolesForJobCompleted){
							if(grantedAuthoritySet.contains(str) || grantedAuthoritySet.contains(str + "-*")){
								emailService.sendJobAccepted(job, user, "emails/inform_job_completed");
								break;
							}
						}
					}				
				}//end for(User user
			}//end if (job != null		
		}//end if (jobStatusMessageTemplate.getStatus().equals(WaspStatus.COMPLETED) && job
		
		
	}//end of method isSuccessfulRun(Message
}