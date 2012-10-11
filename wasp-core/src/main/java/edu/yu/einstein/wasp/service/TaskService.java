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

import edu.yu.einstein.wasp.dao.TaskMappingDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.TaskMapping;
import edu.yu.einstein.wasp.model.UserPending;

@Service
public interface TaskService extends WaspService {


	public TaskMappingDao getTaskMappingDao(); 


	public void setTaskMappingDao(TaskMappingDao taskMappingDao);

	public int getLabManagerPendingTasks();

	public int getLabManagerPendingTasks(List<UserPending> newUsersPendingLmApprovalList, List<LabUser> existingUsersPendingLmApprovalList, List<Job> jobsPendingLmApprovalList);

	public int getDepartmentAdminPendingTasks();

	public int getDepartmentAdminPendingTasks(List<LabPending> labsPendingDaApprovalList, List<Job> jobsPendingDaApprovalList);
	
		
	/**
	 * Obtains a list of task-mappings, with the stateCount attribute set to the number of taskMappings, for 
	 * named batch steps in the preset state which have at least one match in the Batch database. Only task-mappings that the current 
	 * user has permissions to see are returned.
	 * @return
	 */
	public List<TaskMapping> getMappedTasksForCurrentUser();
	
	/**
	 * Returns true if provided job is awaiting PI approval, otherwise returns false
	 * @param job
	 * @return
	 */
	public Boolean isJobAwaitingPiApproval(Job job);

	
	/**
	 * Returns true if provided job is awaiting administrator approval, otherwise returns false
	 * @param job
	 * @return
	 */
	public Boolean isJobAwaitingDaApproval(Job job);
	
	/**
	 * Returns true if provided sample is received, otherwise returns false
	 * @param sample
	 * @return
	 */
	public Boolean isSampleReceived(Sample sample);
	
	/**
	 * Returns true if provided sample is received, otherwise returns false
	 * @param sample
	 * @return
	 */
	public Boolean isJobAwaitingLibraryCreation(Sample sample);
	
	/**
	 * Returns true if provided library sample is in a state of awaiting placement on a platform unit, otherwise returns false
	 * @param sample
	 * @return
	 */
	public Boolean isLibraryAwaitingPlatformUnitPlacement(Sample sample);
	
	/**
	 * Returns true if provided platform unit sample is in a state of awaiting placement on a sequencing run, otherwise returns false
	 * @param sample
	 * @return
	 */
	public Boolean isPlatformUnitAwaitingSequenceRunPlacement(Sample sample);
}
