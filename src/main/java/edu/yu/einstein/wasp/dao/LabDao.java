
/**
 *
 * LabDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Lab Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface LabDao extends WaspDao<Lab> {

  public Lab getLabByLabId (final int labId);

  public Lab getLabByName (final String name);

  public Lab getLabByPrimaryUserId (final int primaryUserId);


}

