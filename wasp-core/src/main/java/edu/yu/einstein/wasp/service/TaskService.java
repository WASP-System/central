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
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.UserPending;

@Service
public interface TaskService extends WaspService {

	/**
	 * setTaskDao(TaskDao taskDao)
	 * 
	 * @param taskDao
	 * 
	 */
	public void setTaskDao(TaskDao taskDao);

	/**
	 * getTaskDao();
	 * 
	 * @return taskDao
	 * 
	 */
	public TaskDao getTaskDao();

	public List<State> getJobCreatedStates();

	public List<State> getQuoteJobStates();

	public List<State> getJob2QuoteStates();

	public List<State> getPiApprovedStates();

	public List<State> getDaApprovedStates();

	public List<State> getSampleReceivedStates();
	public List<State> getSampleNotYetReceivedStates();

	public List<State> getStatesByTaskMappingRule(Task task, String status);

	public List<State> filterStatesByStatusAndPermission(List<State> states, String status, String permsission);
/* these three out 5/15/12
	public int getLabManagerPendingTasks(int labId);

	public int getLabManagerPendingTasks(int labId, List<UserPending> newUsersPendingLmApprovalList, List<LabUser> existingUsersPendingLmApprovalList, List<Job> jobsPendingLmApprovalList);

	public int getLabManagerPendingTasks2(List<UserPending> newUsersPendingLmApprovalList, List<LabUser> existingUsersPendingLmApprovalList, List<Job> jobsPendingLmApprovalList);
*/
	public int getLabManagerPendingTasks();

	public int getLabManagerPendingTasks(List<UserPending> newUsersPendingLmApprovalList, List<LabUser> existingUsersPendingLmApprovalList, List<Job> jobsPendingLmApprovalList);

	public int getDepartmentAdminPendingTasks();

	public int getDepartmentAdminPendingTasks(List<LabPending> labsPendingDaApprovalList, List<Job> jobsPendingDaApprovalList);
}
