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

import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.TaskMappingDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.TaskMapping;
import edu.yu.einstein.wasp.model.UserPending;

@Service
public interface TaskService extends WaspService {


	public TaskMappingDao getTaskMappingDao(); 


	public void setTaskMappingDao(TaskMappingDao taskMappingDao);
// >>>>> TODO: REMOVE
	public List<State> getJobCreatedStates();

	public List<State> getQuoteJobStates();

	public List<State> getJob2QuoteStates();

	public List<State> getPiApprovedStates();

	public List<State> getDaApprovedStates();

	public List<State> getSampleReceivedStates();
	
	public List<State> getSampleNotYetReceivedStates();
// <<<<<<<
	/**
	 * Obtains a list of task-mappings, with the stateCount attribute set to the number of taskMappings, for 
	 * named batch steps in the preset state which have at least one match in the Batch database. Only task-mappings that the current 
	 * user has permissions to see are returned.
	 * @return
	 */
	public List<TaskMapping> getMappedTasksForCurrentUser();

	public List<State> filterStatesByStatusAndPermission(List<State> states, String status, String permsission);

	public int getLabManagerPendingTasks();

	public int getLabManagerPendingTasks(List<UserPending> newUsersPendingLmApprovalList, List<LabUser> existingUsersPendingLmApprovalList, List<Job> jobsPendingLmApprovalList);

	public int getDepartmentAdminPendingTasks();

	public int getDepartmentAdminPendingTasks(List<LabPending> labsPendingDaApprovalList, List<Job> jobsPendingDaApprovalList);
}
