package edu.yu.einstein.wasp.service;

import java.util.List;

import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.WUser;

public interface FilterService {

	/**
	 * Identifies Jobs covered by a viewer that is a DA (dept admin)
	 * @param List<Job> complete list (or subset) of jobs
	 * @return List<Job> list of jobs from the incoming list that are covered by this DA (jobs to retain)
	 */
	public List<Job> filterJobListForDA(List<Job> jobList);

	/**
	 * Identifies Labs covered by a viewer that is a DA (dept admin)
	 * @param List<Lab> complete list (or subset) of labs
	 * @return List<Lab> list of labs from the incoming list that are covered by this DA (labs to retain)
	 */
	public List<Lab> filterLabListForDA(List<Lab> labList);
	
	/**
	 * Identifies Departments covered by a viewer that is a DA (dept admin)
	 * @param List<Lab> complete list (or subset) of Departments
	 * @return List<Lab> list of departments from the incoming list that are covered by this DA (departments to retain)
	 */
	public List<Department> filterDepartmentListForDA(List<Department> departmentList);

	/**
	 * Identifies Users covered by a viewer that is a DA (dept admin)
	 * @param List<WUser> complete list (or subset) of Users
	 * @return List<WUser> list of users from the incoming list that are covered by this DA (departments to retain)
	 */
	public List<WUser> filterUserListForDA(List<WUser> userList);
}
