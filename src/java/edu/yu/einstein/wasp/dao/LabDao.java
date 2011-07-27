
/**
 *
 * LabDao.java 
 * @author echeng (table2type.pl)
 *  
 * the LabDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface LabDao extends WaspDao<Lab> {

  public Lab getLabByLabId (final int labId);

  public Lab getLabByName (final String name);

}

