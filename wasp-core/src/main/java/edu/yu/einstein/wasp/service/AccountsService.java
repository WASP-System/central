/**
 *
 * AccountsService.java 
 * @author RDubin (09-18-13)
 *  
 * the AccountsService interface
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import edu.yu.einstein.wasp.model.AcctGrant;
import edu.yu.einstein.wasp.model.AcctGrantjob;
import edu.yu.einstein.wasp.model.AcctGrantjobDraft;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.Lab;

public interface AccountsService extends WaspService{
	
	public AcctGrant saveGrant(AcctGrant grant);
	
	public AcctGrant getAcctGrantById(Integer id);
	
	public List<AcctGrant> getGrantsForLab(Lab lab);
	
	public AcctGrant getGrantForJob(Job job);
	
	public AcctGrantjob saveJobGrant(Job job, AcctGrant grant);
	
	public AcctGrantjob getJobGrant(Job job, AcctGrant grant);
	
	public AcctGrant getGrantForJobDraft(JobDraft jobDraft);
	
	public AcctGrantjobDraft saveJobDraftGrant(JobDraft jobDraft, AcctGrant grant);
	
	public AcctGrantjobDraft getJobDraftGrant(JobDraft jobDraft, AcctGrant grant);

	/**
	 * return list of non-expired grants
	 * @param lab
	 * @return
	 */
	public List<AcctGrant> getNonExpiredGrantsForLab(Lab lab);

	public void recordQuoteEmailedToPI(AcctQuote acctQuote);
	public boolean isQuoteEmailedToPI(AcctQuote acctQuote);
}
