
/**
 *
 * AcctGrantDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrant Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.AcctGrant;


public interface AcctGrantDao extends WaspDao<AcctGrant> {

  public AcctGrant getAcctGrantByGrantId (final int grantId);


}

