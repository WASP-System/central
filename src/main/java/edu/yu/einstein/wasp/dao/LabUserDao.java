
/**
 *
 * LabUserDao.java 
 * @author echeng (table2type.pl)
 *  
 * the LabUser Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface LabUserDao extends WaspDao<LabUser> {

  public LabUser getLabUserByLabUserId (final int labUserId);

  public LabUser getLabUserByLabIdUserId (final int labId, final int UserId);


}

