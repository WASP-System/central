
/**
 *
 * ProjectDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ProjectDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface ProjectDao extends WaspDao<Project> {

  public Project getProjectByProjectId (final int projectId);

  public Project getProjectByNameLabId (final String name, final int labId);

}

