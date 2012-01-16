
/**
 *
 * ProjectDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Project Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Project;


public interface ProjectDao extends WaspDao<Project> {

  public Project getProjectByProjectId (final int projectId);

  public Project getProjectByNameLabId (final String name, final int labId);


}

