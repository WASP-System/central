
/**
 *
 * AcctGrantjobDraftDao.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctGrantjob Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.AcctGrantjobDraft;


public interface AcctGrantjobDraftDao extends WaspDao<AcctGrantjobDraft> {

  public AcctGrantjobDraft getAcctGrantjobByJobDraftId (final int jobDraftId);


}

