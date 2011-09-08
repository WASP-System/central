
/**
 *
 * ConfirmEmailAuthDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ConfirmEmailAuth Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface ConfirmEmailAuthDao extends WaspDao<ConfirmEmailAuth> {

  public ConfirmEmailAuth getConfirmEmailAuthByUserpendingId (final int userpendingId);

  public ConfirmEmailAuth getConfirmEmailAuthByAuthcode (final String authcode);


}

