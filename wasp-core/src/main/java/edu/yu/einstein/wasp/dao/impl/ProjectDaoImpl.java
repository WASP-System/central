
/**
 *
 * ProjectDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Project Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Project;


@Transactional
@Repository
public class ProjectDaoImpl extends WaspDaoImpl<Project> implements edu.yu.einstein.wasp.dao.ProjectDao {

	/**
	 * ProjectDaoImpl() Constructor
	 *
	 *
	 */
	public ProjectDaoImpl() {
		super();
		this.entityClass = Project.class;
	}


	/**
	 * getProjectByProjectId(final int projectId)
	 *
	 * @param final int projectId
	 *
	 * @return project
	 */

	@Override
	@Transactional
	public Project getProjectByProjectId (final int projectId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", projectId);

		List<Project> results = this.findByMap(m);

		if (results.size() == 0) {
			Project rt = new Project();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getProjectByNameLabId(final String name, final int labId)
	 *
	 * @param final String name, final int labId
	 *
	 * @return project
	 */

	@Override
	@Transactional
	public Project getProjectByNameLabId (final String name, final int labId) {
    		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", name);
		m.put("labId", Integer.toString(labId));

		List<Project> results = this.findByMap(m);

		if (results.size() == 0) {
			Project rt = new Project();
			return rt;
		}
		return results.get(0);
	}



}

