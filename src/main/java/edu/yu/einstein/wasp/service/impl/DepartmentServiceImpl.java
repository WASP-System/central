
/**
 *
 * DepartmentServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the DepartmentService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.DepartmentDao;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.LabPendingService;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.TaskService;

@Service
public class DepartmentServiceImpl extends WaspServiceImpl<Department> implements DepartmentService {

	/**
	 * departmentDao;
	 *
	 */
	private DepartmentDao departmentDao;

	/**
	 * setDepartmentDao(DepartmentDao departmentDao)
	 *
	 * @param departmentDao
	 *
	 */
	@Override
	@Autowired
	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
		this.setWaspDao(departmentDao);
	}

	/**
	 * getDepartmentDao();
	 *
	 * @return departmentDao
	 *
	 */
	@Override
	public DepartmentDao getDepartmentDao() {
		return this.departmentDao;
	}

	 @Autowired
	  private LabPendingService labPendingService;
	 @Autowired
	  private StateService stateService;
	 @Autowired
	  private AuthenticationService authenticationService;
	 @Autowired
	  private TaskService taskService;

  @Override
public Department getDepartmentByDepartmentId (final int departmentId) {
    return this.getDepartmentDao().getDepartmentByDepartmentId(departmentId);
  }

  @Override
public Department getDepartmentByName (final String name) {
    return this.getDepartmentDao().getDepartmentByName(name);
  }

  @Override
public int getDepartmentAdminPendingTasks(){
	  List<LabPending> labsPendingDaApprovalList = new ArrayList<LabPending>();
	  List<Job> jobsPendingDaApprovalList = new ArrayList<Job>();
	  return getDepartmentAdminPendingTasks(labsPendingDaApprovalList, jobsPendingDaApprovalList);
  }
  @Override
public int getDepartmentAdminPendingTasks(List<LabPending> labsPendingDaApprovalList, List<Job> jobsPendingDaApprovalList){

	    Map themap = new HashMap();
	    Task task = taskService.getTaskByIName("DA Approval");

	    if(task.getTaskId()==null){//unexpectedly not found
	    	//TODO: throw exception
	    }
	    
	    if(authenticationService.isGod() || authenticationService.hasRole("ga")){
	    	
	    	themap.put("status", "PENDING"); 
			labsPendingDaApprovalList.addAll(labPendingService.findByMap(themap));//returns a list
	    	themap.clear();
			themap.put("taskId", task.getTaskId());
			themap.put("status", "CREATED");
			List<State> stateList = stateService.findByMap(themap);
			for(State state : stateList){//stateList was created and filled above
				List<Statejob> statejobList = state.getStatejob(); //this should be one:one
				for(Statejob stateJob : statejobList){
					jobsPendingDaApprovalList.add(stateJob.getJob());
				}
			}	    	
	    }
	    else{
	    	//get list of departmentId values for this authenticated user
	    	List<Integer> departmentIdList = new ArrayList<Integer>();
	    	for (String role: authenticationService.getRoles()) {	
	    		String[] splitRole = role.split("-");
				if (splitRole.length != 2) { continue; }
				if (splitRole[1].equals("*")) { continue; }
				if("da".equals(splitRole[0])){//department administrator
					try{
						departmentIdList.add(Integer.parseInt(splitRole[1]));
					}catch (Exception e)	{
						continue;
					}
				}
	    	}
	    	if(departmentIdList.size() > 0){
	    		
	    		themap.clear();
    			themap.put("taskId", task.getTaskId());
    			themap.put("status", "CREATED");
    			List<State> stateList = stateService.findByMap(themap);//may need to be iterated over a few times
    			
	    		for(int departmentId: departmentIdList){
	    			themap.clear();
	    			themap.put("departmentId", departmentId); //this is the new line
	    			themap.put("status", "PENDING"); 
	    			labsPendingDaApprovalList.addAll(labPendingService.findByMap(themap)); 
	    			
	    			for( int i = 0; i < stateList.size(); i++){
	    				List<Statejob> statejobList = stateList.get(i).getStatejob();
	    				for(Statejob stateJob : statejobList){
	    					if(stateJob.getJob().getLab().getDepartmentId().intValue() == departmentId){
	    						jobsPendingDaApprovalList.add(stateJob.getJob());
	    					}
	    				}
	    			}	    			
	    		}
	    	}
	    }
		return labsPendingDaApprovalList.size() + jobsPendingDaApprovalList.size();//total number of tasksPendingDaApproval
  }
  
  @Override
public List<Department> getDepartmentsByName (final String name) {
	  Department d=getDepartmentByName (name);
	  
	  List<Department> result = new ArrayList<Department>();
	  
	  if (d==null) return result;
	  
	  result.add(d);
	  
	  return result;
  }
}

