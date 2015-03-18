package edu.yu.einstein.wasp.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailPreparationException;
import org.springframework.messaging.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.itextpdf.text.DocumentException;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.Strategy.StrategyType;
import edu.yu.einstein.wasp.controller.util.JsonHelperWebapp;
import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.controller.util.SampleAndSampleDraftMetaHelper;
import edu.yu.einstein.wasp.controller.util.SampleWrapperWebapp;
import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.dao.AdaptorsetResourceCategoryDao;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.QuoteException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleMultiplexException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.AcctGrant;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.AcctQuoteMeta;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowSoftware;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;
import edu.yu.einstein.wasp.quote.AdditionalCost;
import edu.yu.einstein.wasp.quote.Comment;
import edu.yu.einstein.wasp.quote.Discount;
import edu.yu.einstein.wasp.quote.LibraryCost;
import edu.yu.einstein.wasp.quote.MPSQuote;
import edu.yu.einstein.wasp.quote.SequencingCost;
import edu.yu.einstein.wasp.service.AccountsService;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.FilterService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.PDFService;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.SoftwareService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.WebAuthenticationService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.SampleWrapper;
import edu.yu.einstein.wasp.util.StringHelper;
import edu.yu.einstein.wasp.web.Tooltip;

@Controller
@RequestMapping("/reports")
public class ReportController extends WaspController {

	private JobDao	jobDao;

	@Autowired
	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	public JobDao getJobDao() {
		return this.jobDao;
	}

	private JobUserDao	jobUserDao;

	@Autowired
	public void setJobUserDao(JobUserDao jobUserDao) {
		this.jobUserDao = jobUserDao;
	}

	public JobUserDao getJobUserDao() {
		return this.jobUserDao;
	}

	private RoleDao	roleDao;

	@Autowired
	public void setJobUserDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public RoleDao getRoleUserDao() {
		return this.roleDao;
	}

	@Autowired
	private JobMetaDao	jobMetaDao;
	@Autowired
	private LabDao	labDao;
	@Autowired
	private WorkflowresourcecategoryDao workflowresourcecategoryDao;
	@Autowired
	private JobCellSelectionDao jobCellSelectionDao;
	@Autowired
	private AdaptorsetDao adaptorsetDao;
	@Autowired
	private AdaptorsetResourceCategoryDao adaptorsetResourceCategoryDao;
	@Autowired
	private SampleService sampleService;
	@Autowired
	private FilterService filterService;
	@Autowired
	private FileService fileService;
	@Autowired
	private GenomeService genomeService;
	@Autowired
	private JobService jobService;
	@Autowired
	private UserService UserService;
	@Autowired
	private WebAuthenticationService webAuthenticationService;
	@Autowired
	private AdaptorService adaptorService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private RunService runService;
	@Autowired
	private MessageServiceWebapp messageService;
	@Autowired
	private AccountsService accountsService;
	@Autowired
	private PDFService pdfService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private UserService userService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private SoftwareService softwareService;
	
	@Value("${wasp.analysis.perLibraryFee:0}")
	private Float perLibraryAnalysisFee;
	
	// list of baserolenames (da-department admin, lu- labuser ...)
	// see role table
	// higher level roles such as 'lm' or 'js' are used on the view
	public static enum DashboardEntityRolename {
		da, lu, jv, jd, su, ga
	};
	
	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp(JobMeta.class, request.getSession());
	}

	@RequestMapping(value="/feesCharged", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('da-*') or hasRole('ga') or hasRole('fm')")
	  public String jobFeesCharged(
			  ModelMap m)  {    	
		return "reports/feesCharged";
	}

	@RequestMapping(value="/feesCharged", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('da-*') or hasRole('ga') or hasRole('fm')")
	  public String jobFeesChargedPOST(
			  @RequestParam(value="reportStartDateAsString") String reportStartDateAsString,
			  @RequestParam(value="reportEndDateAsString") String reportEndDateAsString,
			  ModelMap m)  {
		
		//reportStartDateAsString and reportEndDateAsString format coming from web is like: "2015/03/16"
		//reportStartDateAsString and reportEndDateAsString may be empty; this will cause display of all completed jobs
		//finally, since user may type in the date after using datepicker, it could be oddly formed, and throw an exception 
		DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");//this is the format jquery's datepicker has been configured to send from the web
  		Date reportStartDate = null;//yyyy/MM/dd
  		try{
  			if(reportStartDateAsString.isEmpty()){
  				reportStartDate = (Date) dateFormatter.parse("1970/01/01");//a very long time ago, way before any entries in the wasp database
  				reportStartDateAsString = "Earliest Entry In Database";
  			}
  			else{
  				reportStartDate = (Date) dateFormatter.parse(reportStartDateAsString);
  			}
  		}
  		catch(Exception e){ 
  			logger.debug("unable to parse reportStartDateAsString in ReportController.jobFeesChargedPOST");
  			waspErrorMessage("reports.feesCharged_ReportStartDateFormat.error");
  			return "reports/feesCharged";
  		}
  		
  		Date reportEndDate = null;//yyyy/MM/dd
  		try{
  			if(reportEndDateAsString.isEmpty()){
  				reportEndDate = new Date();//the current date
  				reportEndDateAsString = dateFormatter.format(reportEndDate);
  			}
  			else{
  				reportEndDate = (Date) dateFormatter.parse(reportEndDateAsString);
  			}
  		}
  		catch(Exception e){ 
  			logger.debug("unable to parse reportEndDateAsString in ReportController.jobFeesChargedPOST");
  			waspErrorMessage("reports.feesCharged_ReportEndDateFormat.error"); 
  			return "reports/feesCharged";
  		}
  		
  		//new Date() is current date (now)
  		if(reportStartDate.compareTo(new Date()) > 0 || reportEndDate.compareTo(new Date()) > 0){//not valid; neither reportStartDate nor reportEndDate may be after current (today's) date
  			logger.debug("neither reportStartDate nor reportEndDate may be greater than the current date in ReportController.jobFeesChargedPOST");
  			waspErrorMessage("reports.feesCharged_neitherReportStartOrEndDateMayBeAfterCurrentDate.error"); 
  			return "reports/feesCharged";
  		}
  		
  		if(reportStartDate.compareTo(reportEndDate) > 0){//not valid
  			logger.debug("reportStartDate cannot be greater than reportEndDate in ReportController.jobFeesChargedPOST");
  			waspErrorMessage("reports.feesCharged_ReportStartAndEndDateConflict.error"); 
  			return "reports/feesCharged";
  		}
  		
  		populateFeesChargedPage(m, reportStartDate, reportEndDate);  		
		m.addAttribute("reportStartDateAsString", reportStartDateAsString);
		m.addAttribute("reportEndDateAsString", reportEndDateAsString);

		return "reports/feesCharged";
	}

	
  	private void populateFeesChargedPage(ModelMap m, Date reportStartDate, Date reportEndDate){
  		
  		Format formatter = new SimpleDateFormat("yyyy/MM/dd");  
  		List<Job> jobs = new ArrayList<Job>();
  		List<Job> jobsNotYetCompleted = new ArrayList<Job>();
  		List<Job> jobsNotYetCompletedBecauseWithdrawn = new ArrayList<Job>();
  		List<Job> jobsMarkedAsCompletedButNoJobCompletionDateRecorded = new ArrayList<Job>();
  		List<Job> jobsMarkedAsCompletedButOutsideOfReportDates = new ArrayList<Job>();
  		List<Job> jobsMarkedAsCompletedButQuoteNotFound = new ArrayList<Job>();
  		List<Job> jobsMarkedAsCompletedButQuoteMetaDateNotFoundOrNotAccessible = new ArrayList<Job>();
  		Map<Job, MPSQuote> jobMPSQuoteMap = new HashMap<Job, MPSQuote>();
  		Map<Job, List<Integer>> jobMPSQuoteAsIntegerListMap = new HashMap<Job, List<Integer>>();
  		
  		Map<Job, String> jobStartDateAsStringMap = new HashMap<Job, String>();
  		Map<Job, String> jobCompletionDateAsStringMap = new HashMap<Job, String>();
  		
  		Map<Job, User> jobPIMap = new HashMap<Job, User>(); 		
  		
 		//String queryString = "SELECT j FROM " + Job.class.getName() + " j  WHERE j.created >= '" + reportStartDateAsString + "'";
  		//String queryString = "SELECT j FROM " + Job.class.getName() + " j, " +  Lab.class.getName() + " l, " + User.class.getName() + " u WHERE j.labId = l.id  AND l.primaryUserId = u.id AND j.created >= '" + reportStartDateAsString + "' ORDER BY u.lastName, u.firstName, j.id";
  		//String queryString = "SELECT j FROM " + Job.class.getName() + " j, " +  Lab.class.getName() + " l, " + User.class.getName() + " u WHERE j.labId = l.id  AND l.primaryUserId = u.id  ORDER BY u.lastName, u.firstName, j.id";
  		String queryString = "SELECT j FROM " + Job.class.getName() + " j, " +  Lab.class.getName() + " l, " + User.class.getName() + " uPI, " + User.class.getName() + " uSubmitter WHERE j.labId = l.id  AND l.primaryUserId = uPI.id  AND j.userId = uSubmitter.id ORDER BY uPI.lastName, uPI.firstName, j.labId, uSubmitter.lastName, uSubmitter.firstName, j.id";
  		List<Job> sqlJobList = jobService.getJobDao().findBySqlString(queryString);
  		logger.debug("queryString = " + queryString);
  		logger.debug("size of sqlJobList = " + sqlJobList.size());
  		
  		//for(Job job : jobService.getJobDao().findAll()){
  		for(Job job : sqlJobList){//all jobs, currently ordered by PI name (lab)
  			Date jobCompletionDate = null;
  			if(!jobService.isFinishedSuccessfully(job)){
  				jobsNotYetCompleted.add(job);
  			}
  			else if(jobService.isFinishedSuccessfully(job)){
  				jobCompletionDate = jobService.getJobCompletionDate(job);
  				
  				if(jobCompletionDate==null){//very, very, very unlikely
  					logger.debug("jobCompletionDate unexpectedly found to be null for jobId J" + job.getId().toString());
  					jobsMarkedAsCompletedButNoJobCompletionDateRecorded.add(job);
  					continue;
  				}
  				
  				//zero out the time (hr, min, sec, ms) for jobCompletionDate; taken from http://stackoverflow.com/questions/17821601/set-time-to-000000
  				//this is needed since reportStartDate and reportEndDate are set to zero for hr,min,sec,ms (note: reportEndDate might also be NOW, which has current time for hr,min,sec,ms  which will also be fine).
  				final GregorianCalendar gc = new GregorianCalendar();
  				gc.setTime( jobCompletionDate );
  			    gc.set( Calendar.HOUR_OF_DAY, 0 );
  			    gc.set( Calendar.MINUTE, 0 );
  			    gc.set( Calendar.SECOND, 0 );
  			    gc.set( Calendar.MILLISECOND, 0 );
  			    jobCompletionDate = gc.getTime();  
  			    
  				if(jobCompletionDate.compareTo(reportStartDate) < 0){//EXCLUDE
  					logger.debug("jobCompletionDate is before reportStartDate requested in this report for jobID J" + job.getId().toString());
  					jobsMarkedAsCompletedButOutsideOfReportDates.add(job);
  					continue;
  				}
  				else if(jobCompletionDate.compareTo(reportEndDate) > 0){//EXCLUDE
  					logger.debug("jobCompletionDate is after reportEndDate requested in this report for jobID J" + job.getId().toString());
  					jobsMarkedAsCompletedButOutsideOfReportDates.add(job);
  					continue;
  				}
  				 
  				//these are used for display on web  				
  				String jobStartDateAsString = formatter.format(job.getCreated());
  				jobStartDateAsStringMap.put(job, jobStartDateAsString);
  				String jobCompletionDateAsString = formatter.format(jobCompletionDate);
  				jobCompletionDateAsStringMap.put(job, jobCompletionDateAsString);
  				logger.debug("jobId = " + job.getId().toString() + " started : completed  = " + jobStartDateAsString + " : " + jobCompletionDateAsString);
  				
	  			AcctQuote mostRecentAcctQuote = job.getCurrentQuote();
	  			
	  			if(mostRecentAcctQuote==null || mostRecentAcctQuote.getId()==null){
	  				jobsMarkedAsCompletedButQuoteNotFound.add(job);
	  			}
	  			else if(mostRecentAcctQuote!=null && mostRecentAcctQuote.getId()!=null){
	  				
	  				boolean acctQuoteJsonMetaDataFound = false;
	  				for(AcctQuoteMeta acm : mostRecentAcctQuote.getAcctQuoteMeta()){	  					
	  					if(acm.getK().toLowerCase().contains("json")){
	  						acctQuoteJsonMetaDataFound = true;
	  						try{							
	  							JSONObject jsonObject = new JSONObject(acm.getV());	
	  							MPSQuote mostRecentMpsQuote =  MPSQuote.getMPSQuoteFromJSONObject(jsonObject, MPSQuote.class);
	  							jobMPSQuoteMap.put(job, mostRecentMpsQuote);
	  				 			jobs.add(job);
	  				  			jobPIMap.put(job, userService.getUserDao().findById(job.getLab().getPrimaryUserId().intValue()));
	  				  			
	  				  			List<Integer> mostRecentMpsQuoteAsIntegerList = new ArrayList<Integer>();
	  				  			//TOTAL CHARGE APPEARING ON QUOTE (ESF lab costs - esf discount + analysis fee)
	  				  			mostRecentMpsQuoteAsIntegerList.add(mostRecentMpsQuote.getTotalFinalCost());
	  				  			//TOTAL ESF LAB CHARGES (ESF lab costs)
	  				  			mostRecentMpsQuoteAsIntegerList.add(mostRecentMpsQuote.getTotalLibraryConstructionCost() + mostRecentMpsQuote.getTotalSequenceRunCost() +  mostRecentMpsQuote.getTotalAdditionalCost());
	  				  			//Discount on TOTAL ESF LAB CHARGES (ESF discount)
	  				  			mostRecentMpsQuoteAsIntegerList.add(mostRecentMpsQuote.getTotalDiscountCost());
	  				  			//DISCOUNTED TOTAL ESF LAB CHARGES (ESF lab costs - ESF discount)
	  				  			mostRecentMpsQuoteAsIntegerList.add(mostRecentMpsQuote.getTotalLibraryConstructionCost() + mostRecentMpsQuote.getTotalSequenceRunCost() +  mostRecentMpsQuote.getTotalAdditionalCost() - mostRecentMpsQuote.getTotalDiscountCost());
	  				  			//ANALYSIS CHARGE
	  				  			mostRecentMpsQuoteAsIntegerList.add(mostRecentMpsQuote.getTotalComputationalCost());
	  				  			//Map<Job, Integer> 
	  				  			jobMPSQuoteAsIntegerListMap.put(job, mostRecentMpsQuoteAsIntegerList);	  				  			
	  						}catch(Exception e){
	  							logger.debug("Job J" + job.getId().toString() + ": unable to access mspQuote via stored json; could be an uploaded file or no file exists");
	  							jobsMarkedAsCompletedButQuoteMetaDateNotFoundOrNotAccessible.add(job);
	  							//some acctQuotes may have uploaded a custom file (although rather unlikely), so they will not have any json string in the meta!
	  							//so just catch the exception and move on.
	  						}
	  					}
	  				}
	  				if(acctQuoteJsonMetaDataFound == false){
	  					jobsMarkedAsCompletedButQuoteMetaDateNotFoundOrNotAccessible.add(job);
	  				}
	  				
	  			}
	  			
  			}
  		}
  		
  		m.addAttribute("jobs", jobs);
  		
  		
  		jobService.sortJobsByJobId(jobsNotYetCompleted);
  		Map<Job,String> jobNotYetCompletedJobStatusAsStringMap = new HashMap<Job,String>();
  		Map<Job,String> jobNotYetCompletedJobSubmittedDateAsStringMap = new HashMap<Job,String>();
  		
  		Map<Job,String> jobNotYetCompletedDiscountedFacilityCostAsStringAsStringMap = new HashMap<Job,String>();
  		Map<Job,String> jobNotYetCompletedAnalysisCostAsStringAsStringMap = new HashMap<Job,String>();
  		int grandTotalForJobsNotYetCompletedDiscountedFacilityCost = 0;
  		int grandTotalForJobsNotYetCompletedAnalysisCost = 0;
  		
  		for(Job jobNotYetCompleted : jobsNotYetCompleted){
  			String currentStatus = jobService.getDetailedJobStatusString(jobNotYetCompleted);
  			if(currentStatus.toLowerCase().contains("withdrawn")){
  				jobsNotYetCompletedBecauseWithdrawn.add(jobNotYetCompleted);
  				continue;
  			}
  			jobNotYetCompletedJobStatusAsStringMap.put(jobNotYetCompleted, currentStatus);
  			String jobSubmittedDateAsString = formatter.format(jobNotYetCompleted.getCreated());
  			jobNotYetCompletedJobSubmittedDateAsStringMap.put(jobNotYetCompleted, jobSubmittedDateAsString);
  			AcctQuote mostRecentAcctQuote = jobNotYetCompleted.getCurrentQuote();
  			if(mostRecentAcctQuote==null || mostRecentAcctQuote.getId()==null){
  				jobNotYetCompletedDiscountedFacilityCostAsStringAsStringMap.put(jobNotYetCompleted,"no quote");
  				jobNotYetCompletedAnalysisCostAsStringAsStringMap.put(jobNotYetCompleted,"no quote");
  			}
  			else if(mostRecentAcctQuote!=null && mostRecentAcctQuote.getId()!=null){
  				
  				for(AcctQuoteMeta acm : mostRecentAcctQuote.getAcctQuoteMeta()){
  					if(acm.getK().toLowerCase().contains("json")){
  						try{							
  							JSONObject jsonObject = new JSONObject(acm.getV());	
  							MPSQuote mostRecentMpsQuote =  MPSQuote.getMPSQuoteFromJSONObject(jsonObject, MPSQuote.class);
  							
  				  			
  				  			/////////List<Integer> mostRecentMpsQuoteAsIntegerList = new ArrayList<Integer>();
  				  			//TOTAL CHARGE APPEARING ON QUOTE (ESF lab costs - esf discount + analysis fee)
  				  			/////////mostRecentMpsQuoteAsIntegerList.add(mostRecentMpsQuote.getTotalFinalCost());
  				  			//TOTAL ESF LAB CHARGES (ESF lab costs)
  				  			/////////mostRecentMpsQuoteAsIntegerList.add(mostRecentMpsQuote.getTotalLibraryConstructionCost() + mostRecentMpsQuote.getTotalSequenceRunCost() +  mostRecentMpsQuote.getTotalAdditionalCost());
  				  			//Discount on TOTAL ESF LAB CHARGES (ESF discount)
  				  			/////////mostRecentMpsQuoteAsIntegerList.add(mostRecentMpsQuote.getTotalDiscountCost());
  				  			//DISCOUNTED TOTAL ESF LAB CHARGES (ESF lab costs - ESF discount)
  				  			/////////mostRecentMpsQuoteAsIntegerList.add(mostRecentMpsQuote.getTotalLibraryConstructionCost() + mostRecentMpsQuote.getTotalSequenceRunCost() +  mostRecentMpsQuote.getTotalAdditionalCost() - mostRecentMpsQuote.getTotalDiscountCost());
  				  			//ANALYSIS CHARGE
  				  			/////////mostRecentMpsQuoteAsIntegerList.add(mostRecentMpsQuote.getTotalComputationalCost());
  				  			//Map<Job, Integer> 
  				  			
  				  			Integer discountedFacilityCost = new Integer(mostRecentMpsQuote.getTotalLibraryConstructionCost() + mostRecentMpsQuote.getTotalSequenceRunCost() +  mostRecentMpsQuote.getTotalAdditionalCost() - mostRecentMpsQuote.getTotalDiscountCost());
  				  			grandTotalForJobsNotYetCompletedDiscountedFacilityCost += discountedFacilityCost.intValue();
  				  			Integer analysisCost = new Integer(mostRecentMpsQuote.getTotalComputationalCost());
  				  			grandTotalForJobsNotYetCompletedAnalysisCost += analysisCost.intValue();
  				  			jobNotYetCompletedDiscountedFacilityCostAsStringAsStringMap.put(jobNotYetCompleted, discountedFacilityCost.toString());
  				  			jobNotYetCompletedAnalysisCostAsStringAsStringMap.put(jobNotYetCompleted, analysisCost.toString());
  				  							  			
  						}catch(Exception e){
  							logger.debug("Job J" + jobNotYetCompleted.getId().toString() + ": this job not completed: unable to access mspQuote via stored json; could be an uploaded file or no file exists");
  							//some acctQuotes may have uploaded a custom file (although rather unlikely), so they will not have any json string in the meta!
  							jobNotYetCompletedDiscountedFacilityCostAsStringAsStringMap.put(jobNotYetCompleted,"no quote metadata");
  			  				jobNotYetCompletedAnalysisCostAsStringAsStringMap.put(jobNotYetCompleted,"no quote metadata");
  						}
  					}
  				}
  				
  			}
  		} 
  		
  		jobsNotYetCompleted.removeAll(jobsNotYetCompletedBecauseWithdrawn);
  		
  		List<Job> totalJobsInDatabase = new ArrayList<Job>();
  		totalJobsInDatabase.addAll(jobService.getJobDao().findAll());
  		m.addAttribute("totalJobsInDatabase", totalJobsInDatabase);
  		m.addAttribute("jobsNotYetCompletedBecauseWithdrawn", jobsNotYetCompletedBecauseWithdrawn);
  		
  		m.addAttribute("jobsNotYetCompleted", jobsNotYetCompleted);
  		m.addAttribute("jobNotYetCompletedJobStatusAsStringMap", jobNotYetCompletedJobStatusAsStringMap);
  		m.addAttribute("jobNotYetCompletedJobSubmittedDateAsStringMap", jobNotYetCompletedJobSubmittedDateAsStringMap);
  		m.addAttribute("jobNotYetCompletedDiscountedFacilityCostAsStringAsStringMap", jobNotYetCompletedDiscountedFacilityCostAsStringAsStringMap);
  		m.addAttribute("jobNotYetCompletedAnalysisCostAsStringAsStringMap", jobNotYetCompletedAnalysisCostAsStringAsStringMap);
  		m.addAttribute("grandTotalForJobsNotYetCompletedDiscountedFacilityCostAsString", Integer.valueOf(grandTotalForJobsNotYetCompletedDiscountedFacilityCost).toString());
  		m.addAttribute("grandTotalForJobsNotYetCompletedAnalysisCostAsString", Integer.valueOf(grandTotalForJobsNotYetCompletedAnalysisCost).toString());
  		
  		m.addAttribute("jobsMarkedAsCompletedButQuoteMetaDateNotFoundOrNotAccessible", jobsMarkedAsCompletedButQuoteMetaDateNotFoundOrNotAccessible);
  		
  		m.addAttribute("jobsMarkedAsCompletedButNoJobCompletionDateRecorded", jobsMarkedAsCompletedButNoJobCompletionDateRecorded);
  		m.addAttribute("jobsMarkedAsCompletedButOutsideOfReportDates", jobsMarkedAsCompletedButOutsideOfReportDates);
  		m.addAttribute("jobsMarkedAsCompletedButQuoteNotFound", jobsMarkedAsCompletedButQuoteNotFound);  		
  		
  		m.addAttribute("jobStartDateAsStringMap", jobStartDateAsStringMap);  		
  		m.addAttribute("jobCompletionDateAsStringMap", jobCompletionDateAsStringMap);
		logger.debug("dubin : number of completed jobs = " + jobs.size());
		m.addAttribute("jobPIMap", jobPIMap);
		m.addAttribute("jobMPSQuoteMap", jobMPSQuoteMap);
		m.addAttribute("jobMPSQuoteAsIntegerListMap", jobMPSQuoteAsIntegerListMap);
	 	m.addAttribute("localCurrencyIcon", Currency.getInstance(Locale.getDefault()).getSymbol()); 
	 	
	 	//first, get all the labs	 	
	 	List<Lab> labList = new ArrayList<Lab>();
	 	Map<Lab,List<Job>> labJobListMap = new HashMap<Lab,List<Job>>();	 	
	 	for(Job job : jobs){//first get the labs	 		
	 		if(!labList.contains(job.getLab())){
	 			Lab lab = job.getLab();
	 			labList.add(lab);
	 			labJobListMap.put(lab, new ArrayList<Job>());	 			
	 		}
	 	}
	 	//next associate the labs with a list of jobs
	 	for(Job job : jobs){
	 		labJobListMap.get(job.getLab()).add(job);
	 	}
	 	m.addAttribute("labList", labList);
	 	m.addAttribute("labJobListMap", labJobListMap);
	 	
	 	Map<Lab, List<Integer>> labGrandTotalsAsIntegerListMap = new HashMap<Lab, List<Integer>>();	 	
	 	int [] intArrayForTheReport  = new int [5];	
	 	
	 	for(Lab lab : labList){	
	 		int [] intArrayForALab  = new int [5];	
	 		for(Job job : labJobListMap.get(lab)){
	 			List<Integer> mpsQuoteAsIntegerListForSingleCompletedJob = jobMPSQuoteAsIntegerListMap.get(job);
	 			intArrayForALab[0] += mpsQuoteAsIntegerListForSingleCompletedJob.get(0).intValue();	 			
	 			intArrayForALab[1] += mpsQuoteAsIntegerListForSingleCompletedJob.get(1).intValue();	 		
	 			intArrayForALab[2] += mpsQuoteAsIntegerListForSingleCompletedJob.get(2).intValue();	 		
	 			intArrayForALab[3] += mpsQuoteAsIntegerListForSingleCompletedJob.get(3).intValue();	 			
	 			intArrayForALab[4] += mpsQuoteAsIntegerListForSingleCompletedJob.get(4).intValue();	 		
	 		}
	 		
	  		List<Integer> integerListForLab = new ArrayList<Integer>();
	 		integerListForLab.add(Integer.valueOf(intArrayForALab[0]));
	 		integerListForLab.add(Integer.valueOf(intArrayForALab[1]));
	 		integerListForLab.add(Integer.valueOf(intArrayForALab[2]));
	 		integerListForLab.add(Integer.valueOf(intArrayForALab[3]));
	 		integerListForLab.add(Integer.valueOf(intArrayForALab[4]));
	 		labGrandTotalsAsIntegerListMap.put(lab, integerListForLab);
	 		
	 		intArrayForTheReport[0] += intArrayForALab[0];
	 		intArrayForTheReport[1] += intArrayForALab[1];
	 		intArrayForTheReport[2] += intArrayForALab[2];
	 		intArrayForTheReport[3] += intArrayForALab[3];
	 		intArrayForTheReport[4] += intArrayForALab[4];
	 	}
	 	
	 	List<Integer> reportGrandTotalsAsIntegerList = new ArrayList<Integer>();
	 	reportGrandTotalsAsIntegerList.add(Integer.valueOf(intArrayForTheReport[0]));
	 	reportGrandTotalsAsIntegerList.add(Integer.valueOf(intArrayForTheReport[1]));
	 	reportGrandTotalsAsIntegerList.add(Integer.valueOf(intArrayForTheReport[2]));
	 	reportGrandTotalsAsIntegerList.add(Integer.valueOf(intArrayForTheReport[3]));
	 	reportGrandTotalsAsIntegerList.add(Integer.valueOf(intArrayForTheReport[4]));
	 	
	 	m.addAttribute("labGrandTotalsAsIntegerListMap", labGrandTotalsAsIntegerListMap);
	 	m.addAttribute("reportGrandTotalsAsIntegerList", reportGrandTotalsAsIntegerList);
 
	 	/* very old stuff
    	Job job = jobService.getJobByJobId(jobId);
		//need this (viewerIsFacilityStaff) since might be coming from callable (and security context lost)
		if(webAuthenticationService.hasRole("su") || webAuthenticationService.hasRole("fm") || webAuthenticationService.hasRole("ft") || webAuthenticationService.hasRole("da-*")){
			m.addAttribute("viewerIsFacilityStaff", true);
		}
		else{
			m.addAttribute("viewerIsFacilityStaff", false);
		}
		
		m.addAttribute("job", job);
		
		AcctQuote mostRecentAcctQuote = job.getCurrentQuote();
		if(mostRecentAcctQuote!=null && mostRecentAcctQuote.getId()!=null){
			m.addAttribute("mostRecentQuotedAmount", mostRecentAcctQuote.getAmount());
			m.addAttribute("mostRecentQuoteId", mostRecentAcctQuote.getId());
			
			for(AcctQuoteMeta acm : mostRecentAcctQuote.getAcctQuoteMeta()){
				if(acm.getK().toLowerCase().contains("json")){
					try{							
						JSONObject jsonObject = new JSONObject(acm.getV());	
						MPSQuote mostRecentMpsQuote =  MPSQuote.getMPSQuoteFromJSONObject(jsonObject, MPSQuote.class);
						m.addAttribute("mostRecentMpsQuote", mostRecentMpsQuote);
					}catch(Exception e){
						logger.debug("unable to access mspQuote via stored json; could be an uploaded file");
						//some acctQuotes uploaded a file, so they will not have any json string in the meta!
						//so just catch the exception and move on.
					}
				}
			}
			
		}		
 		m.addAttribute("localCurrencyIcon", Currency.getInstance(Locale.getDefault()).getSymbol()); 		

		Set<AcctQuote> acctQuoteSet = job.getAcctQuote();
		List<AcctQuote>  acctQuoteList = new ArrayList<AcctQuote>(acctQuoteSet);
		class AcctQuoteCreatedComparator implements Comparator<AcctQuote> {
			@Override
			public int compare(AcctQuote arg0, AcctQuote arg1) {
				return arg1.getCreated().compareTo(arg0.getCreated());
			}
		}
		Collections.sort(acctQuoteList, new AcctQuoteCreatedComparator());//most recent is now first, least recent is last

		List<FileGroup> fileGroups = new ArrayList<FileGroup>();
		Map<FileGroup, List<FileHandle>> fileGroupFileHandlesMap = new HashMap<FileGroup, List<FileHandle>>();
		List<FileHandle> fileHandlesThatCanBeViewedList = new ArrayList<FileHandle>();
		Map<AcctQuote, FileGroup> acctQuoteFileGroupMap= new HashMap<AcctQuote, FileGroup>();
		List<AcctQuote>acctQuotesWithJsonEntry = new ArrayList<AcctQuote>();
		Map<AcctQuote, String> acctQuoteEmailSentToPIMap= new HashMap<AcctQuote, String>();
		
		for(AcctQuote acctQuote : acctQuoteList){//most recent is first, least recent is last
			if(acctQuoteWithEmailJustSent!=null && acctQuoteWithEmailJustSent.getId().intValue() == acctQuote.getId().intValue()){//a hack due to transaction issues; I give up, never seeing that the email is sent for the acctQuoteMeta just updated in emailQuoteToPI
				acctQuoteEmailSentToPIMap.put(acctQuote, "yes");
			}
			else if(accountsService.isQuoteEmailedToPI(acctQuote)){
				acctQuoteEmailSentToPIMap.put(acctQuote, "yes");
			}else{
				acctQuoteEmailSentToPIMap.put(acctQuote, "no");
			}
			List<AcctQuoteMeta> acctQuoteMetaList = acctQuote.getAcctQuoteMeta();
			logger.debug("size of acctQuoteMetaList = " + acctQuoteMetaList.size());
			for(AcctQuoteMeta acctQuoteMeta : acctQuoteMetaList){
				if(acctQuoteMeta.getK().toLowerCase().contains("filegroupid")){
					try{
						FileGroup fileGroup = fileService.getFileGroupById(Integer.parseInt(acctQuoteMeta.getV()));
						if(fileGroup.getId()!=null){
							acctQuoteFileGroupMap.put(acctQuote, fileGroup);
							fileGroups.add(fileGroup);
							List<FileHandle> fileHandles = new ArrayList<FileHandle>();
							for(FileHandle fh : fileGroup.getFileHandles()){//there is really only one fileHandle for this filegroup
								logger.debug("filename: " + fh.getFileName() + " size of acctQuoteMetaList: " + acctQuoteMetaList.size());
								fileHandles.add(fh);
								String mimeType = fileService.getMimeType(fh.getFileName());
								if(!mimeType.isEmpty()){
									fileHandlesThatCanBeViewedList.add(fh);
								}
							}
							fileGroupFileHandlesMap.put(fileGroup, fileHandles);
						}
						
					}catch(Exception e){logger.warn("FileGroup unexpectedly not found");}
				}
				if(acctQuoteMeta.getK().toLowerCase().contains("json")){
					acctQuotesWithJsonEntry.add(acctQuote);
				}
				if(acctQuoteMeta.getK().toLowerCase().contains("quoteemailedtopi")){
					acctQuotesWithJsonEntry.add(acctQuote);
				}
			}
		}		
		m.addAttribute("fileGroups", fileGroups);
		m.addAttribute("fileGroupFileHandlesMap", fileGroupFileHandlesMap);
		m.addAttribute("fileHandlesThatCanBeViewedList", fileHandlesThatCanBeViewedList);
		m.addAttribute("acctQuoteList", acctQuoteList);
		m.addAttribute("acctQuoteFileGroupMap", acctQuoteFileGroupMap);
		m.addAttribute("acctQuotesWithJsonEntry", acctQuotesWithJsonEntry);	
		m.addAttribute("acctQuoteEmailSentToPIMap", acctQuoteEmailSentToPIMap);
		*/		
	}
}
