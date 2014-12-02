package edu.yu.einstein.wasp.integration.endpoints;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.JobStatusMessageTemplate;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.security.WaspJdbcDaoImpl;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.UserService;

@Transactional("entityManager")//must have this or we get an exception in batch:  org.hibernate.LazyInitializationException: could not initialize proxy - no Session
public class JobEmailServiceActivator {
	
	private static int TIMEOUT = 60000; //ms
	
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
	
	@Value("${email.sending.enabled:true}")
	private Boolean isSendingEmailEnabled;
	
	@Value("${wasp.mode.isDemo:false}")
	private Boolean isDemoMode;

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
	public void handleJobStatusMessage(Message<WaspStatus> jobStatusMessage) {
		if (!isSendingEmailEnabled){
			logger.info("Email sending is disabled so not going to send email");
			return;
		}
		if (isDemoMode){
			// TODO: fix us so can access demo email 
			logger.info("In Demo mode so not going to send email (email to send to exists only in web session)");
			return;
		}
		if (!JobStatusMessageTemplate.isMessageOfCorrectType(jobStatusMessage)){
			logger.warn("Message is not of the correct type (a Job message). Check service activator and input channel are correct");
			return;
		}
		
		JobStatusMessageTemplate jobStatusMessageTemplate = new JobStatusMessageTemplate(jobStatusMessage);

		Job job = getJob(jobStatusMessageTemplate.getJobId());
		if (job == null || job.getId() == null){
			logger.error("Unable to obtain Job object for id=" + jobStatusMessageTemplate.getJobId()+ " within timeout period. No email will be sent in response to message: " + jobStatusMessage.toString());
			return;
		}
	
		if (jobStatusMessageTemplate.getStatus().equals(WaspStatus.STARTED) && jobStatusMessageTemplate.getTask().equals(WaspTask.NOTIFY_STATUS)){			
			String jobIdAsString = job.getId().toString();
			String labIdAsString = job.getLab().getId().toString();
			String departmentIdAsString = job.getLab().getDepartment().getId().toString();
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
			
			String jobIdAsString = job.getId().toString();
			String labIdAsString = job.getLab().getId().toString();
			String departmentIdAsString = job.getLab().getDepartment().getId().toString();
			List<String> rolesForJobAbandoned = convertDelimitedListToArrayList(this.jobAbandonedRolenames, ";");
			List<String> rolesForJobAccepted = convertDelimitedListToArrayList(this.jobAcceptedRolenames, ";");
			String whoAbandonedJob = "";
			String reasonForAbandoned = "";
			if(jobStatusMessageTemplate.getTask().equals(WaspJobTask.PI_APPROVE))
				whoAbandonedJob += "Principal Investigator or designated Lab Manager";
			else if(jobStatusMessageTemplate.getTask().equals(WaspJobTask.FM_APPROVE))
				whoAbandonedJob += "Sequencing Facility Manager";
			else if(jobStatusMessageTemplate.getTask().equals(WaspJobTask.DA_APPROVE))
				whoAbandonedJob += "Accounts Manager";
			String comment = jobStatusMessageTemplate.getComment();
			if(comment == null){comment = "";}
			User userWhoCreatedComment = jobStatusMessageTemplate.getUserCreatingMessage();
			if(userWhoCreatedComment!=null){
				reasonForAbandoned = comment + " (" + userWhoCreatedComment.getNameFstLst() + ")";
			}else{
				reasonForAbandoned = comment;
			}
			
			
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
		}//end of else if (jobStatusMessageTemplate.getStatus().equals(WaspStatus.ABANDONED) and jobApprovals
		else if (jobStatusMessageTemplate.getStatus().equals(WaspStatus.COMPLETED) && jobStatusMessageTemplate.getTask().equals(WaspTask.NOTIFY_STATUS)){			
			if(job != null && job.getId() != null){
				String jobIdAsString = job.getId().toString();
				String labIdAsString = job.getLab().getId().toString();
				String departmentIdAsString = job.getLab().getDepartment().getId().toString();
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
		
		
	}//end of method handleJobStatusMessage()
	
	private Job getJob(int jobId){
		// transactional issue: May get here before transaction for persisting job has finished so may need to wait
		Job job = jobService.getJobByJobId(jobId);
		int timeElapsed = 0;
		int sleepTime = 200; // ms
		while ((job == null || job.getId() == null) && timeElapsed <= TIMEOUT){
			try {
				logger.debug("Unable to obtain Job object for id="+ jobId + ". Going to try again in " + sleepTime + " ms");
				Thread.sleep(sleepTime);
			} catch (InterruptedException e1) {
				logger.info("interrupted: " + e1.toString());
				Thread.currentThread().interrupt();
			}
			timeElapsed += sleepTime;
			sleepTime *= 2;
			if (sleepTime + timeElapsed > TIMEOUT)
				sleepTime = TIMEOUT - timeElapsed;
			job = jobService.getJobByJobId(jobId);
		}
		return job;
	}
	
}