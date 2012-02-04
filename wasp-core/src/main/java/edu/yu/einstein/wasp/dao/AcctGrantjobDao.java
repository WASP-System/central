
/**
 *
 * AcctGrantjobDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantjob Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.AcctGrantjob;


public interface AcctGrantjobDao extends WaspDao<AcctGrantjob> {

  public AcctGrantjob getAcctGrantjobByJobId (final int jobId);


}

