
/**
 *
 * ProjectServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ProjectService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.ProjectService;
import edu.yu.einstein.wasp.dao.ProjectDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Project;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectServiceImpl extends WaspServiceImpl<Project> implements ProjectService {

	/**
	 * projectDao;
	 *
	 */
	private ProjectDao projectDao;

	/**
	 * setProjectDao(ProjectDao projectDao)
	 *
	 * @param projectDao
	 *
	 */
	@Autowired
	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
		this.setWaspDao(projectDao);
	}

	/**
	 * getProjectDao();
	 *
	 * @return projectDao
	 *
	 */
	public ProjectDao getProjectDao() {
		return this.projectDao;
	}


  public Project getProjectByProjectId (final int projectId) {
    return this.getProjectDao().getProjectByProjectId(projectId);
  }

  public Project getProjectByNameLabId (final String name, final int labId) {
    return this.getProjectDao().getProjectByNameLabId(name, labId);
  }

}

