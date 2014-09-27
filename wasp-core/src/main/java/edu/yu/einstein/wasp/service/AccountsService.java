/**
 *
 * AccountsService.java 
 * @author RDubin (09-18-13)
 *  
 * the LabService interface
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.io.OutputStream;
import java.util.List;

import com.itextpdf.text.DocumentException;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.AcctGrant;
import edu.yu.einstein.wasp.model.AcctGrantjob;
import edu.yu.einstein.wasp.model.AcctGrantjobDraft;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.quote.MPSQuote;

public interface AccountsService extends WaspService{

	public void buildQuoteAsPDF(MPSQuote mpsQuote, Job job, OutputStream outputStream)throws DocumentException, MetadataException;	
	
	public AcctGrant saveGrant(AcctGrant grant);
	
	public AcctGrant getAcctGrantById(Integer id);
	
	public List<AcctGrant> getGrantsForLab(Lab lab);
	
	public AcctGrant getGrantForJob(Job job);
	
	public AcctGrantjob saveJobGrant(Job job, AcctGrant grant);
	
	public AcctGrantjob getJobGrant(Job job, AcctGrant grant);
	
	public AcctGrant getGrantForJobDraft(JobDraft jobDraft);
	
	public AcctGrantjobDraft saveJobDraftGrant(JobDraft jobDraft, AcctGrant grant);
	
	public AcctGrantjobDraft getJobDraftGrant(JobDraft jobDraft, AcctGrant grant);

}
