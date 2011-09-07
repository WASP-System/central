
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.Project;

@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Transactional
	public Project getProjectByProjectId (final int projectId) {
    		HashMap m = new HashMap();
		m.put("projectId", projectId);

		List<Project> results = (List<Project>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Project rt = new Project();
			return rt;
		}
		return (Project) results.get(0);
	}



	/**
	 * getProjectByNameLabId(final String name, final int labId)
	 *
	 * @param final String name, final int labId
	 *
	 * @return project
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public Project getProjectByNameLabId (final String name, final int labId) {
    		HashMap m = new HashMap();
		m.put("name", name);
		m.put("labId", labId);

		List<Project> results = (List<Project>) this.findByMap((Map) m);

		if (results.size() == 0) {
			Project rt = new Project();
			return rt;
		}
		return (Project) results.get(0);
	}



}

