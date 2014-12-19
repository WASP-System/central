package edu.yu.einstein.wasp.controller;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.dao.AcctQuoteDao;
import edu.yu.einstein.wasp.dao.AcctQuoteMetaDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.model.AcctGrant;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.AcctQuoteMeta;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.quote.MPSQuote;
import edu.yu.einstein.wasp.service.AccountsService;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FilterService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.StringHelper;
import edu.yu.einstein.wasp.web.Tooltip;

@Controller
@Transactional
@RequestMapping("/job2quote")
public class Job2QuoteController extends WaspController {

	@Autowired
	private AcctQuoteDao	acctQuoteDao;
	@Autowired
	private AcctQuoteMetaDao	acctQuoteMetaDao;
	@Autowired
	private JobService jobService;
	
	@Autowired
	private LabDao			labDao;

	@Autowired
	private FilterService	filterService;

	@Autowired
	private MessageServiceWebapp	messageService;
	
	@Autowired
	private AuthenticationService	authenticationService;
	
	@Autowired
	private AccountsService accountsService;

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("acctQuote", AcctQuoteMeta.class, request.getSession());
	}

	@RequestMapping("/list")
	public String list(ModelMap m) {

		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));

		prepareSelectListData(m);

		return "job2quote/list";
	}
	
	@RequestMapping("/list_all")
	public String listAll(ModelMap m) {

		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));

		prepareSelectListData(m);

		return "job2quote/list_all";
	}
	
	/**
	 * If a job list is provided, only jobs within the list are considered, otherwise if null, all jobs are considered
	 * @param restrictedJobList
	 * @return
	 */
	private Map<String, Object> getQuoteListJGrid(List<Job> restrictedJobList){
		List<Job> job2quoteList = new ArrayList<Job>();
		
		Map<String, Object> jqgrid = new HashMap<String, Object>();

		String search = request.getParameter("_search");
		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");
		logger.debug("sidx = " + sidx);logger.debug("sord = " + sord);logger.debug("search = " + search);

		String jobIdAsString = request.getParameter("jobId")==null?null:request.getParameter("jobId").trim();//if not passed, jobIdAsString will be null
		String submitterNameAndLogin = request.getParameter("submitter")==null?null:request.getParameter("submitter").trim();//if not passed, will be null
		String piNameAndLogin = request.getParameter("lab")==null?null:request.getParameter("lab").trim();//if not passed, will be null
		String submittedOnDateAsString = request.getParameter("submitted_on")==null?null:request.getParameter("submitted_on").trim();//if not passed, will be null
		logger.debug("jobIdAsString = " + jobIdAsString);logger.debug("submitterNameAndLogin = " + submitterNameAndLogin);
		logger.debug("piNameAndLogin = " + piNameAndLogin);logger.debug("submittedOnDateAsString = " + submittedOnDateAsString);

		//DEAL WITH PARAMETERS
		//deal with jobId
		Integer jobId = null;
		if(jobIdAsString != null){//something was passed
			jobId = StringHelper.convertStringToInteger(jobIdAsString);//returns null is unable to convert
			if(jobId == null){//perhaps the passed value was abc, which is not a valid jobId
				jobId = new Integer(0);//fake it so that result set will be empty; this way, the search will be performed with jobId = 0 and will come up with an empty result set
			}
		}		
				
		//deal with submitter from grid and UserId from URL (note that submitterNameAndLogin and userIdFromURL can both be null, but if either is not null, only one should be not null)
		User submitter = null;
		//from grid
		if(submitterNameAndLogin != null){//something was passed; expecting firstname lastname (login)
			String submitterLogin = StringHelper.getLoginFromFormattedNameAndLogin(submitterNameAndLogin.trim());//if fails, returns empty string
			if(submitterLogin.isEmpty()){//most likely incorrect format !!!!for later, if some passed in amy can always do search for users with first or last name of amy, but would need to be done by searching every job
				submitter = new User();
				submitter.setId(new Integer(0));//fake it; perform search below and no user will appear in the result set
			}
			else{
				submitter = userDao.getUserByLogin(submitterLogin);
				if(submitter.getId()==null){//if not found in database, submitter is NOT null and getUserId()=null
					submitter.setId(new Integer(0));//fake it; perform search below and no user will appear in the result set
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
				piLab.setId(new Integer(0));//fake it; result set will come up empty
			}
			else{
				pi = userDao.getUserByLogin(piLogin);//if User not found, pi object is NOT null and pi.getUnserId()=null
				if(pi.getId()==null){
					piLab = new Lab();
					piLab.setId(new Integer(0));//fake it; result set will come up empty
				}
				else{
					piLab = labDao.getLabByPrimaryUserId(pi.getId().intValue());//if the Lab not found, piLab object is NOT null and piLab.getLabId()=null
					if(piLab.getId()==null){
						piLab.setId(new Integer(0));//fake it; result set will come up empty
					}
				}
			}
		}
		
		//deal with submittedOnDateAsString
		Date submittedOnAsDate = null;
		if(submittedOnDateAsString != null){
			DateFormat formatter;
		
			formatter = new SimpleDateFormat("yyyy/MM/dd");//this is the format that the date is coming in from the Grid
			try{				
				submittedOnAsDate = (Date)formatter.parse(submittedOnDateAsString); 
			}
			catch(Exception e){ 
				submittedOnAsDate = new Date(0);//fake it; parameter of 0 sets date to 01/01/1970 which is NOT in this database. So result set will be empty
			}	
		}
		
		
		
		Map<String, Integer> m = new HashMap<String, Integer>();
		if(jobId != null){
			m.put("id", jobId.intValue());
		}
		if(submitter != null){
			m.put("userId", submitter.getId().intValue());
		}
		if(piLab != null){
			m.put("labId", piLab.getId().intValue());
		}
		
		Map<String, Date> dateMap = new HashMap<String, Date>();
		if(submittedOnAsDate != null){
			dateMap.put("createts", submittedOnAsDate);
		}
		
		List<String> orderByColumnAndDirection = new ArrayList<String>();		
		if(sidx!=null && !"".equals(sidx)){//sord is apparently never null; default is desc
			if(sidx.equals("jobId")){
				orderByColumnAndDirection.add("id " + sord);
			}
			else if(sidx.equals("name")){//job.name
				orderByColumnAndDirection.add("name " + sord);
			}
			else if(sidx.equals("submitter")){
				orderByColumnAndDirection.add("user.lastName " + sord); orderByColumnAndDirection.add("user.firstName " + sord);
			}
			else if(sidx.equals("lab")){
				orderByColumnAndDirection.add("lab.user.lastName " + sord); orderByColumnAndDirection.add("lab.user.firstName " + sord);
			}
			else if(sidx.equals("submitted_on")){
				orderByColumnAndDirection.add("createts " + sord); 
			}
		}
		else if(sidx==null || "".equals(sidx)){
			orderByColumnAndDirection.add("id desc");
		}
		
		List<Job> workingJobList = this.jobService.getJobDao().findByMapsIncludesDatesDistinctOrderBy(m, dateMap, null, orderByColumnAndDirection);
		/*for (Job job : workingJobList)
			logger.debug("working job list jobId=" + job.getId() + " UUID =" + job.getUUID() + ", Hashcode=" + job.hashCode());
		for (Job job : restrictedJobList)
			logger.debug("restricted job list jobId=" + job.getId() + " UUID =" + job.getUUID() + ", Hashcode=" + job.hashCode());
		*/
		if (restrictedJobList != null)
			workingJobList.retainAll(restrictedJobList);
		
		for (Job job : workingJobList)
			logger.debug("working job list jobId=" + job.getId());
		//perform ONLY if the viewer is A DA but is NOT any other type of facility member
		if(authenticationService.isOnlyDepartmentAdministrator()){//remove jobs not in the DA's department
			List<Job> jobsToKeep = filterService.filterJobListForDA(workingJobList);
			workingJobList.retainAll(jobsToKeep);
		}

		//orderby amount is special; must be done by comparator
		if(sidx != null && !sidx.isEmpty() && sord != null && !sord.isEmpty() ){
			if(sidx.equals("amount")){
				Collections.sort(workingJobList, new QuoteAmountComparator());	
				if(sord.equals("desc")){
					Collections.reverse(workingJobList);					
				}
			}						
		}

		job2quoteList.addAll(workingJobList);
		
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

		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

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
			Map<String, Object> cell = new HashMap<String, Object>();
			cell.put("id", item.getId()); //job id

			User user = userDao.getById(item.getUserId());
			AcctQuote currentQuote = item.getCurrentQuote();
			////float amount = ajqcList.isEmpty() ? 0 : ajqcList.get(0).getAcctQuote().getAmount();
			String quoteAsString;// = ajqcList.isEmpty() ? "?.??" : String.format("%.2f", ajqcList.get(0).getAcctQuote().getAmount());
			String quoteId = null;
			if(currentQuote == null || currentQuote.getId() == null){
				quoteAsString = "Quote Not Yet Generated";
			}
			else{
				quoteId = currentQuote.getId().toString();
				try{
					  Float price = new Float(currentQuote.getAmount());
					  ////////quoteAsString = String.format("%.2f", price);
					  quoteAsString = Currency.getInstance(Locale.getDefault()).getSymbol()+String.format("%.2f", price);
				}
				catch(Exception e){
					  logger.warn("JobController: jobList : " + e);
					  quoteAsString = this.messageService.getMessage("acctQuote.problemContactAdmin.label");
					  if(quoteAsString==null || quoteAsString.isEmpty()){
						  quoteAsString = "Problem - Contact Admin";
					  }
				}					
			}

			Format formatterForDisplay = new SimpleDateFormat("yyyy/MM/dd");
			
			//String noteAboutNeedingQuote =   ((currentQuote == null || currentQuote.getId() == null) && !jobService.isJobActive(item)) ? "[Job Terminated]":"";
			String currentStatus = jobService.getDetailedJobStatusString(item);
			String jobStatusComment = jobService.getJobStatusComment(item);//this is really ONLY a comment if the job was rejected by fm, pi, or da
			String grantCode = "N/A";
			AcctGrant grant = accountsService.getGrantForJob(item);
			if (grant != null)
				grantCode = accountsService.getGrantForJob(item).getCode();
			if (jobStatusComment != null)
				currentStatus += Tooltip.getCommentHtmlString(jobStatusComment, getServletPath());
			
			List<String> cellList = new ArrayList<String>(
				Arrays.asList(new String[] {
					"<a href=" + getServletPath() + "/job/"+item.getId()+"/homepage.do#ui-tabs-2>J"+item.getId().intValue()+"</a>",
					currentStatus,
					item.getName(),
					//String.format("%.2f", amount),
					quoteAsString,
					grantCode,
					user.getNameFstLst(), 
					item.getLab().getUser().getNameFstLst(),
					formatterForDisplay.format(item.getCreated()), //item.getLastUpdTs().toString() 
					quoteId//this is not needed, I don't think, since we're going to create new quotes
				}));

			cell.put("cell", cellList);

			rows.add(cell);
		}

		jqgrid.put("rows", rows);
		return jqgrid;
	}

	@RequestMapping(value = "/listAllJSON", method = RequestMethod.GET)
	public String getListAllJSON(HttpServletResponse response){
		try {
			return outputJSON(getQuoteListJGrid(null), response);
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON ", e);
		}	
	}
	
	@RequestMapping(value = "/listJSON", method = RequestMethod.GET)
	public String getListJSON(HttpServletResponse response){
		List<Job> jobsToBeQuoted = jobService.getJobsAwaitingQuote();
		try {
			return outputJSON(getQuoteListJGrid(jobsToBeQuoted), response);
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON ", e);
		}	
	}

	@Transactional
	@RequestMapping(value = "/subgridJSON.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft') ")
	public String subgridJSON(@RequestParam("id") Integer jobId,ModelMap m, HttpServletResponse response) {//added 12-18-14; dubin
				
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		Job job = jobService.getJobByJobId(jobId);
		MPSQuote mostRecentMpsQuote = new MPSQuote();
		AcctQuote mostRecentAcctQuote = job.getCurrentQuote();
		if(mostRecentAcctQuote!=null && mostRecentAcctQuote.getId()!=null){
			for(AcctQuoteMeta acm : mostRecentAcctQuote.getAcctQuoteMeta()){
				if(acm.getK().toLowerCase().contains("json")){
					try{							
						JSONObject jsonObject = new JSONObject(acm.getV());	
						mostRecentMpsQuote =  MPSQuote.getMPSQuoteFromJSONObject(jsonObject, MPSQuote.class);						
					}catch(Exception e){
						logger.debug("unable to access mspQuote via stored json; in quote subgrid");
					}
				}
			}			
		}
		Integer initialSequenceFacilityTotalCost = mostRecentMpsQuote.getTotalLibraryConstructionCost() +
				mostRecentMpsQuote.getTotalSequenceRunCost() + 
				mostRecentMpsQuote.getTotalAdditionalCost();
		Integer discountedSequenceFacilityTotalCost = initialSequenceFacilityTotalCost - mostRecentMpsQuote.getTotalDiscountCost();
		String localCurrencySymbol = Currency.getInstance(Locale.getDefault()).getSymbol();
		try {
				List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			
				Map<String, Object> cell = new HashMap<String, Object>();
				cell.put("id", job.getId());
				List<String> cellList = null;
				if(mostRecentMpsQuote.getJobId()==null || mostRecentMpsQuote.getJobId()==0)	 {
					cellList = new ArrayList<String>(
							Arrays.asList(
								new String[] {
									"",
									"",
									"",
									"",
									"",
									"",
									"",
									""
								}
							)
						);
				}
				else{
					cellList = new ArrayList<String>(
						Arrays.asList(
							new String[] {
								localCurrencySymbol+mostRecentMpsQuote.getTotalLibraryConstructionCost().toString(),
								localCurrencySymbol+mostRecentMpsQuote.getTotalSequenceRunCost().toString(),
								localCurrencySymbol+mostRecentMpsQuote.getTotalAdditionalCost().toString(), 
								localCurrencySymbol+initialSequenceFacilityTotalCost.toString(),
								"("+localCurrencySymbol+mostRecentMpsQuote.getTotalDiscountCost().toString()+")",
								localCurrencySymbol+discountedSequenceFacilityTotalCost.toString(),
								localCurrencySymbol+mostRecentMpsQuote.getTotalComputationalCost().toString(),
								localCurrencySymbol+mostRecentMpsQuote.getTotalFinalCost().toString()
							}
						)
					);
				}	 
				cell.put("cell", cellList);
				rows.add(cell);			
			 
				jqgrid.put("rows",rows);
			 
				return outputJSON(jqgrid, response); 	
			
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't marshall to JSON for jog2quote subgrid for jobId " + job.getId(), e);
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

		String message = null;
		try{
			jobService.addNewQuote(jobId, quoteForm, getMetaHelperWebapp().getFromJsonForm(request, AcctQuoteMeta.class));
			message = this.messageService.getMessage("acctQuote.created_success.label");
		}
		catch(Exception e){
			logger.warn(e.getMessage());
			message = this.messageService.getMessage("acctQuote.update_failed.label");
		}
		
		try{
			response.getWriter().println(message);
		}catch(Exception e){}
		return null;//why bother with a return value??
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
			
			AcctQuote quote0 = arg0.getCurrentQuote();
			float amount0 = (quote0 == null || quote0.getId()==null) ? 0f : quote0.getAmount();
			AcctQuote quote1 = arg1.getCurrentQuote();
			float amount1 =  (quote1 == null || quote1.getId()==null) ? 0f : quote1.getAmount();
			return amount0<amount1 ? -1: (amount0>amount1 ? 1 : 0);
		}
	}
}
