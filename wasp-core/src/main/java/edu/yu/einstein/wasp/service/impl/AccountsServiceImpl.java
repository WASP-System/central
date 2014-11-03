
/**
 *
 * AccountsServiceImpl.java 
 * @author RDubin
 *  
 * the AccountsServiceImpl object
 * note: moved pdf functionality to PDFService 10-17-14
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.yu.einstein.wasp.dao.AcctGrantDao;
import edu.yu.einstein.wasp.dao.AcctGrantjobDao;
import edu.yu.einstein.wasp.dao.AcctGrantjobDraftDao;
import edu.yu.einstein.wasp.model.AcctGrant;
import edu.yu.einstein.wasp.model.AcctGrantjob;
import edu.yu.einstein.wasp.model.AcctGrantjobDraft;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.service.AccountsService;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.SampleService;

@Service
@Transactional("entityManager")
public class AccountsServiceImpl extends WaspServiceImpl implements AccountsService{

	@Autowired
	private JobService jobService;
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private AcctGrantDao acctGrantDao;
	
	@Autowired
	private AcctGrantjobDao acctGrantjobDao;
	
	@Autowired
	private AcctGrantjobDraftDao acctGrantjobDraftDao;
	
	@Autowired
	private AdaptorService adaptorService;

	@Autowired
	private MessageService messageService;
	
	@Value("${wasp.customimage.logo}")
	private String relativeLogoUrl;

	@Override
	public AcctGrant saveGrant(AcctGrant grant) {
		return acctGrantDao.save(grant);
	}

	@Override
	public List<AcctGrant> getGrantsForLab(Lab lab) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("labId", lab.getId());
		List<AcctGrant> grants = acctGrantDao.findByMap(m);
		if (grants != null)
			return grants;
		return new ArrayList<AcctGrant>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AcctGrant> getNonExpiredGrantsForLab(Lab lab) {
		ArrayList<AcctGrant> currentGrants = new ArrayList<AcctGrant>();
		for (AcctGrant grant : getGrantsForLab(lab)){
			if (grant.getExpirationdt().before(new Date()))
				grant.setIsActive(0);
			else
				currentGrants.add(grant);
		}
		return currentGrants;
	}

	@Override
	public AcctGrant getGrantForJob(Job job) {
		List<AcctGrantjob> agjs = acctGrantjobDao.getAcctGrantjobByJobId(job.getId());
		if (agjs.isEmpty())
			return null;
		AcctGrantjob agj = agjs.get(0);
		if (agj == null)
			return null;
		return agj.getAcctGrant();
	}

	@Override
	public AcctGrantjob saveJobGrant(Job job, AcctGrant grant) {
		AcctGrantjob acj = new AcctGrantjob();
		List<AcctGrantjob> acjs = acctGrantjobDao.getAcctGrantjobByJobId(job.getId());
		if (!acjs.isEmpty())
			acj = acjs.get(0);
		acj.setGrantId(grant.getId());
		acj.setJobId(job.getId());
		return acctGrantjobDao.save(acj);
	}

	@Override
	public AcctGrant getAcctGrantById(Integer id) {
		return acctGrantDao.getById(id);
	}

	@Override
	public AcctGrantjob getJobGrant(Job job, AcctGrant grant) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", job.getId()); // jobid is primary key in this table (why ????) hence just use id
		m.put("grantId", grant.getId());
		List<AcctGrantjob> jobgrants = acctGrantjobDao.findByMap(m);
		if (jobgrants != null && !jobgrants.isEmpty())
			return jobgrants.get(0);
		return null;
	}

	@Override
	public AcctGrant getGrantForJobDraft(JobDraft jobDraft) {
		List<AcctGrantjobDraft> agjds = acctGrantjobDraftDao.getAcctGrantjobDraftByJobDraftId(jobDraft.getId());
		if (agjds.isEmpty())
			return null;
		AcctGrantjobDraft agjd = agjds.get(0);
		if (agjd == null)
			return null;
		return agjd.getAcctGrant();
	}

	@Override
	public AcctGrantjobDraft saveJobDraftGrant(JobDraft jobDraft, AcctGrant grant) {
		AcctGrantjobDraft acjd = new AcctGrantjobDraft();
		List<AcctGrantjobDraft> acjds = acctGrantjobDraftDao.getAcctGrantjobDraftByJobDraftId(jobDraft.getId());
		if (!acjds.isEmpty())
			acjd = acjds.get(0);
		acjd.setGrantId(grant.getId());
		acjd.setJobDraftId(jobDraft.getId());
		return acctGrantjobDraftDao.save(acjd);
	}

	@Override
	public AcctGrantjobDraft getJobDraftGrant(JobDraft jobDraft, AcctGrant grant) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", jobDraft.getId()); // jobDraft is primary key in this table (why ????) hence just use id
		m.put("grantId", grant.getId());
		List<AcctGrantjobDraft> jobDraftgrants = acctGrantjobDraftDao.findByMap(m);
		if (jobDraftgrants != null && !jobDraftgrants.isEmpty())
			return jobDraftgrants.get(0);
		return null;
	}

}
