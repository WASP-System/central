package edu.yu.einstein.wasp.controller;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.dao.AcctJobquotecurrentDao;
import edu.yu.einstein.wasp.dao.AcctQuoteDao;
import edu.yu.einstein.wasp.dao.AcctQuoteMetaDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;
import edu.yu.einstein.wasp.model.AcctJobquotecurrent;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.AcctQuoteMeta;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FilterService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.StringHelper;

@Controller
@Transactional
@RequestMapping("/job2quote")
public class Job2QuoteController extends WaspController {

	@Autowired
	private AcctQuoteDao	acctQuoteDao;
	@Autowired
	private AcctQuoteMetaDao	acctQuoteMetaDao;
	@Autowired
	private AcctJobquotecurrentDao acctJobquotecurrentDao;
	@Autowired
	private JobService jobService;
	
	@Autowired
	private LabDao			labDao;

	@Autowired
	private FilterService	filterService;

	@Autowired
	private MessageService	messageService;
	@Autowired
	private AuthenticationService	authenticationService;

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("acctQuote", AcctQuoteMeta.class, request.getSession());
	}

	@RequestMapping("/list")
	public String list(ModelMap m) {

		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));

		prepareSelectListData(m);

		return "job2quote/list";
	}

	@RequestMapping(value = "/listJSON", method = RequestMethod.GET)
	public String getListJSON(HttpServletResponse response) {

		// result
		Map<String, Object> jqgrid = new HashMap<String, Object>();

		String search = request.getParameter("_search");
		String searchStr = request.getParameter("searchString");
		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");
		System.out.println("sidx = " + sidx);System.out.println("sord = " + sord);System.out.println("search = " + search);

		String userId = request.getParameter("userId");
		String showall = request.getParameter("showall");
		
		String jobIdAsString = request.getParameter("jobId")==null?null:request.getParameter("jobId").trim();//if not passed, jobIdAsString will be null
		String submitterNameAndLogin = request.getParameter("submitter")==null?null:request.getParameter("submitter").trim();//if not passed, will be null
		String piNameAndLogin = request.getParameter("lab")==null?null:request.getParameter("lab").trim();//if not passed, will be null
		System.out.println("jobIdAsString = " + jobIdAsString);System.out.println("submitterNameAndLogin = " + submitterNameAndLogin);System.out.println("piNameAndLogin = " + piNameAndLogin);

		//DEAL WITH PARAMETERS
		//deal with jobId
		Integer jobId = null;
		if(jobIdAsString != null){//something was passed
			jobId = StringHelper.convertStringToInteger(jobIdAsString);//returns null is unable to convert
			if(jobId == null){//perhaps the passed value was abc, which is not a valid jobId
				jobId = new Integer(0);//fake it so that result set will be empty; this way, the search will be performed with jobId = 0 and will come up with an empty result set
			}
		}		
				
		//nothing to do to deal with jobname
				
		//deal with submitter from grid and userId from URL (note that submitterNameAndLogin and userIdFromURL can both be null, but if either is not null, only one should be not null)
		User submitter = null;
		//from grid
		if(submitterNameAndLogin != null){//something was passed; expecting firstname lastname (login)
			String submitterLogin = StringHelper.getLoginFromFormattedNameAndLogin(submitterNameAndLogin.trim());//if fails, returns empty string
			if(submitterLogin.isEmpty()){//most likely incorrect format !!!!for later, if some passed in amy can always do search for users with first or last name of amy, but would need to be done by searching every job
				submitter = new User();
				submitter.setUserId(new Integer(0));//fake it; perform search below and no user will appear in the result set
			}
			else{
				submitter = userDao.getUserByLogin(submitterLogin);
				if(submitter.getUserId()==null){//if not found in database, submitter is NOT null and getUserId()=null
					submitter.setUserId(new Integer(0));//fake it; perform search below and no user will appear in the result set
				}
			}
		}
				
		//deal with PI (lab)
		User pi = null;
		Lab piLab = null;//this is what's tested below
		if(piNameAndLogin != null){//something was passed; expecting firstname lastname (login)
			String piLogin = StringHelper.getLoginFromFormattedNameAndLogin(piNameAndLogin.trim());//if fails, returns empty string
			if(piLogin.isEmpty()){//likely incorrect format
				piLab = new Lab();
				piLab.setLabId(new Integer(0));//fake it; result set will come up empty
			}
			else{
				pi = userDao.getUserByLogin(piLogin);//if User not found, pi object is NOT null and pi.getUnserId()=null
				if(pi.getUserId()==null){
					piLab = new Lab();
					piLab.setLabId(new Integer(0));//fake it; result set will come up empty
				}
				else{
					piLab = labDao.getLabByPrimaryUserId(pi.getUserId().intValue());//if the Lab not found, piLab object is NOT null and piLab.getLabId()=null
					if(piLab.getLabId()==null){
						piLab.setLabId(new Integer(0));//fake it; result set will come up empty
					}
				}
			}
		}
		
		List<Job> jobList = new ArrayList();
		List<Job> job2quoteList = new ArrayList();
		
		Map m = new HashMap();
		if(jobId != null){
			m.put("jobId", jobId.intValue());
		}
		if(submitter != null){
			m.put("UserId", submitter.getUserId().intValue());
		}
		if(piLab != null){
			m.put("labId", piLab.getLabId().intValue());
		}
		
		List<String> orderByColumnNames = new ArrayList<String>();
		orderByColumnNames.add("jobId");
		
		//if Map m has no entries, SQL will find ALL jobs
		jobList = this.jobService.getJobDao().findByMapDistinctOrderBy(m, null, orderByColumnNames, "desc");//default order is by jobId/desc		
		
		//perform ONLY if the viewer is A DA but is NOT any other type of facility member
		if(authenticationService.isOnlyDepartmentAdministrator()){//remove jobs not in the DA's department
			List<Job> jobsToKeep = filterService.filterJobListForDA(jobList);
			jobList.retainAll(jobsToKeep);
		}
		
		if(sidx != null && !sidx.isEmpty() && sord != null && !sord.isEmpty() ){
			
			//resultset within jobList is currently sorted by jobId/desc.
			if(sidx.equals("jobId") && sord.equals("asc")){
				Collections.sort(jobList, new JobIdComparator());
			}
			
			else if(sidx.equals("submitter")){
				Collections.sort(jobList, new SubmitterLastNameFirstNameComparator());
				if(sord.equals("desc")){
					Collections.reverse(jobList);
				}
			}
			else if(sidx.equals("lab")){
				Collections.sort(jobList, new PILastNameFirstNameComparator());	
				if(sord.equals("desc")){
					Collections.reverse(jobList);
				}
			}
			else if(sidx.equals("amount")){
				Collections.sort(jobList, new QuoteAmountComparator());	
				if(sord.equals("desc")){
					Collections.reverse(jobList);
				}
			}						
		}
		job2quoteList.addAll(jobList);
		
	
		try {
			// index of page
			int pageIndex = Integer.parseInt(request.getParameter("page")); 
			// number of rows in one page
			int pageRowNum = Integer.parseInt(request.getParameter("rows")); 
			// total number of rows
			int rowNum = job2quoteList.size(); 
			// total number of pages
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum; 

			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");
			jqgrid.put("page", pageIndex + "");

			Map<String, String> userData = new HashMap<String, String>();
			userData.put("page", pageIndex + "");
			userData.put("selId", StringUtils.isEmpty(request.getParameter("selId")) ? "" : request.getParameter("selId"));
			jqgrid.put("userdata", userData);

			List<Map> rows = new ArrayList<Map>();

			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			/*
			 * if the selId is set, change the page index to the one contains
			 * the selId
			 */
			if (!StringUtils.isEmpty(request.getParameter("selId"))) {
				int selId = Integer.parseInt(request.getParameter("selId"));
				int selIndex = job2quoteList.indexOf(jobService.getJobDao().findById(selId));
				frId = selIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}

			List<Job> page = job2quoteList.subList(frId, toId);
			for (Job item : page) {
				Map cell = new HashMap();
				cell.put("id", item.getJobId());

				User user = userDao.getById(item.getUserId());
				List<AcctJobquotecurrent> ajqcList = item.getAcctJobquotecurrent();
				float amount = ajqcList.isEmpty() ? 0 : ajqcList.get(0).getAcctQuote().getAmount();

				List<AcctQuoteMeta> itemMetaList = ajqcList.isEmpty() ? new ArrayList() : 
					getMetaHelperWebapp().syncWithMaster(ajqcList.get(0).getAcctQuote().getAcctQuoteMeta());
				
				Format formatter = new SimpleDateFormat("MM/dd/yyyy");
				
				List<String> cellList = new ArrayList<String>(
					Arrays.asList(new String[] { 
						"J"+item.getJobId().intValue() + " (<a href=/wasp/sampleDnaToLibrary/listJobSamples/"+item.getJobId()+".do>details</a>)",
						item.getName(),
						String.format("%.2f", amount),
						user.getNameFstLst(), 
						item.getLab().getUser().getNameFstLst(),
						formatter.format(item.getCreatets())//item.getLastUpdTs().toString() 
					}));

				for (AcctQuoteMeta meta : itemMetaList) {
					cellList.add(meta.getV());
				}

				cell.put("cell", cellList);

				rows.add(cell);
			}

			jqgrid.put("rows", rows);

			return outputJSON(jqgrid, response);

		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + job2quoteList, e);
		}
	}

	/**
	 * Creates/Updates job quote
	 * 
	 * @Author AJ Jing 
	 */	
	@RequestMapping(value = "/detail_rw/updateJSON.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('da-*') or hasRole('sa') or hasRole('ga')")
	public String updateDetailJSON(@RequestParam("id") Integer jobId, AcctQuote quoteForm, ModelMap m, HttpServletResponse response) {

		List<AcctQuoteMeta> metaList = getMetaHelperWebapp().getFromJsonForm(request, AcctQuoteMeta.class);
		quoteForm.setJobId(jobId);
		quoteForm.setAcctQuoteMeta(metaList);
		
		AcctQuote acctQuoteDb = this.acctQuoteDao.save(quoteForm);
		Integer quoteId = acctQuoteDb.getQuoteId();
		this.acctQuoteMetaDao.updateByQuoteId(quoteId, metaList);
		
		AcctJobquotecurrent acctJobquotecurrent = this.acctJobquotecurrentDao.getAcctJobquotecurrentByJobId(jobId);
		acctJobquotecurrent.setQuoteId(quoteId);
		// if jobid is null, create a new record in database table 
		if(acctJobquotecurrent.getJobId() == null) {
			acctJobquotecurrent.setJobId(jobId);
			acctJobquotecurrentDao.persist(acctJobquotecurrent);
		}
		try{
			jobService.updateJobQuoteStatus(jobService.getJobDao().getJobByJobId(jobId), WaspStatus.CREATED);
		} catch (WaspMessageBuildingException e){
			logger.error(e.getMessage());
			try {
				response.getWriter().println(this.messageService.getMessage("wasp.integration_message_send.error"));
				return null;
			} catch (Throwable t) {
				throw new IllegalStateException("Cant output message sending failure message", t);
			}
		}
		
		
		try {
			response.getWriter().println(this.messageService.getMessage("acctQuote.created_success.label"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message", e);
		}
	
	    
	}
	
	class JobIdComparator implements Comparator<Job> {
		@Override
		public int compare(Job arg0, Job arg1) {
			return arg0.getJobId().intValue() >= arg1.getJobId().intValue()?1:0;
		}
	}
	class SubmitterLastNameFirstNameComparator implements Comparator<Job> {
		@Override
		public int compare(Job arg0, Job arg1) {
			return arg0.getUser().getLastName().concat(arg0.getUser().getFirstName()).compareToIgnoreCase(arg1.getUser().getLastName().concat(arg1.getUser().getFirstName()));
		}
	}
	class PILastNameFirstNameComparator implements Comparator<Job> {
		@Override
		public int compare(Job arg0, Job arg1) {
			return arg0.getLab().getUser().getLastName().concat(arg0.getLab().getUser().getFirstName()).compareToIgnoreCase(arg1.getLab().getUser().getLastName().concat(arg1.getLab().getUser().getFirstName()));
		}
	}
	class QuoteAmountComparator implements Comparator<Job> {
		@Override
		public int compare(Job arg0, Job arg1) {
			
			List<AcctJobquotecurrent> ajqcList0 = arg0.getAcctJobquotecurrent();
			float amount0 = ajqcList0.isEmpty() ? 0 : ajqcList0.get(0).getAcctQuote().getAmount();
			List<AcctJobquotecurrent> ajqcList1 = arg1.getAcctJobquotecurrent();
			float amount1 = ajqcList1.isEmpty() ? 0 : ajqcList1.get(0).getAcctQuote().getAmount();
			return amount0 >= amount1 ? 1:0;
		}
	}
}
