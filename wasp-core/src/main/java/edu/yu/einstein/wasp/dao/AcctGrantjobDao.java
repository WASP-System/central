
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

import java.util.List;

import edu.yu.einstein.wasp.model.AcctGrantjob;


public interface AcctGrantjobDao extends WaspDao<AcctGrantjob> {

  public AcctGrantjob getAcctGrantjobById (final int id);
  
  public List<AcctGrantjob> getAcctGrantjobByJobId (final int jobid);
  
  public List<AcctGrantjob> getAcctGrantjobByAcctGrantId (final int acctGrantId);
  
  public AcctGrantjob getAcctGrantjobByJobIdAcctGrantId (final int jobid, final int acctGrantId);


}

