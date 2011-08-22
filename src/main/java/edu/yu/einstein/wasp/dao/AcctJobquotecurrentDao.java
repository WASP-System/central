
/**
 *
 * AcctJobquotecurrentDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctJobquotecurrentDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface AcctJobquotecurrentDao extends WaspDao<AcctJobquotecurrent> {

  public AcctJobquotecurrent getAcctJobquotecurrentByJobId (final int jobId);

}

