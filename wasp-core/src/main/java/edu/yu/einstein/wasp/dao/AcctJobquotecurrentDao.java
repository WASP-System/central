
/**
 *
 * AcctJobquotecurrentDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctJobquotecurrent Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.AcctJobquotecurrent;


public interface AcctJobquotecurrentDao extends WaspDao<AcctJobquotecurrent> {

  public AcctJobquotecurrent getAcctJobquotecurrentByJobId (final int jobId);


}

