/**
 *
 * TaskService.java 
 * @author echeng (table2type.pl)
 *  
 * the TaskService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.UserPending;

@Service
public interface TaskService extends WaspService {

	public int getLabManagerPendingTasks();

	public int getLabManagerPendingTasks(List<UserPending> newUsersPendingLmApprovalList, List<LabUser> existingUsersPendingLmApprovalList, List<Job> jobsPendingLmApprovalList);

	public int getDepartmentAdminPendingTasks();

	public int getDepartmentAdminPendingTasks(List<LabPending> labsPendingDaApprovalList, List<Job> jobsPendingDaApprovalList);

	public boolean isDepartmentAdminPendingTasks(List<Job> activeJobs);

	public boolean isLabManagerPendingTasks(List<Job> activeJobs);

	public boolean isFmPendingTasks(List<Job> activeJobs);
		
	
}
