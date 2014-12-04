
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

import java.util.List;

import edu.yu.einstein.wasp.model.AcctGrantjobDraft;


public interface AcctGrantjobDraftDao extends WaspDao<AcctGrantjobDraft> {

  public AcctGrantjobDraft getAcctGrantjobDraftById (final int id);
  
  public List<AcctGrantjobDraft> getAcctGrantjobDraftByJobDraftId (final int jobDraftid);
  
  public List<AcctGrantjobDraft> getAcctGrantjobDraftByAcctGrantId (final int acctGrantId);
  
  public AcctGrantjobDraft getAcctGrantjobDraftByJobDraftIdAcctGrantId (final int jobDraftid, final int acctGrantId);


}

