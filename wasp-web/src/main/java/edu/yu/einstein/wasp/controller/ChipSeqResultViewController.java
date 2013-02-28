package edu.yu.einstein.wasp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.dao.AcctJobquotecurrentDao;
import edu.yu.einstein.wasp.dao.AcctQuoteDao;
import edu.yu.einstein.wasp.dao.AcctQuoteMetaDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.file.DummyFileUrlResolver;
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.model.AcctJobquotecurrent;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.AcctQuoteMeta;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.WUser;
import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FilterService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.StringHelper;

@Controller
@Transactional
@RequestMapping("/jobresults/chipSeq")
public class ChipSeqResultViewController extends WaspController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JobService	jobService;

	@Autowired
	private FilterService	filterService;

	@Autowired
	private AuthenticationService	authenticationService;

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private DummyFileUrlResolver fileUrlResolver;

	//get locale-specific message
	private String getMessage(String key, String defaultMessage) {
		String r=getMessage(key);
		
		if (defaultMessage!=null && r!=null && r.equals(key)) return defaultMessage; 
		
		return r;
	}
	
	private String getMessage(String key) {
		HttpSession session = this.request.getSession();
		
		Locale locale = (Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		
		return DBResourceBundle.MESSAGE_SOURCE.getMessage(key, null, locale);
	}

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("job", JobMeta.class, request.getSession());
	}

	@RequestMapping(value = "/getJobDetailsJSON", method = RequestMethod.GET)
	public String getDetailsJSON(HttpServletResponse response){
		List<Job> jobsToBeQuoted = jobService.getJobsAwaitingQuote();
		try {
			return "";// outputJSON(getQuoteListJGrid(jobsToBeQuoted), response);
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON ", e);
		}	
	}

	@RequestMapping(value="/treeview")
	public String treeView(ModelMap m) {
		
		int jobId = Integer.parseInt(this.request.getParameter("jobId"));
		Job job = this.jobService.getJobDao().getById(jobId);
		
		m.addAttribute("jobId", jobId);
		m.addAttribute("workflow", job.getWorkflow().getIName());
		
		return "jobresults/treeview"; 	
	}
	

	@RequestMapping(value="/getJSTreeJson", method = RequestMethod.GET)
	public @ResponseBody String getJobSampleTreeJson(@RequestParam("jobId") Integer jobId, HttpServletResponse response) {
		Map <String, Object> jsTree = new HashMap<String, Object>();
		
		Job job = this.jobService.getJobDao().getById(jobId);
		if(job==null || job.getJobId()==null){
			  waspErrorMessage("listJobSamples.jobNotFound.label");
			  return null;
		}
		
		jsTree.put("name", job.getName());
		jsTree.put("myid", jobId);
		jsTree.put("type", "job");
		
		List<Map> children = new ArrayList<Map>();

		List<JobSample> jobSampleList = job.getJobSample();
		for (JobSample js : jobSampleList) {
			Map sample = new HashMap();
			sample.put("name", js.getSample().getName());
			sample.put("myid", js.getSampleId());
			sample.put("type", "sample");
			children.add(sample);
		}
		jsTree.put("children",children);
		
		try {
			return outputJSON(jsTree, response); 	
		} 
		catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + jsTree, e);
		}	
	}

	@RequestMapping(value="/getDetailsJson", method = RequestMethod.GET)
	public @ResponseBody String getDetailsJson(@RequestParam("id") Integer id, @RequestParam("type") String type, HttpServletResponse response) {
		Map <String, Object> jsTree = new HashMap<String, Object>();
		LinkedHashMap<String, Object> jsDetails = new LinkedHashMap<String, Object>();
		
		if(type.equalsIgnoreCase("job")) {
			Integer jobId = id;
			Job job = this.jobService.getJobDao().getById(jobId);
			if(job==null || job.getJobId()==null){
				  waspErrorMessage("listJobSamples.jobNotFound.label");
				  return null;
			}
			
			jsDetails.put(getMessage("job.name.label"), job.getName());
			jsDetails.putAll(jobService.getExtraJobDetails(job));
		
			List<JobMeta> metaList = job.getJobMeta();
			Map <String, Map<String, String>> metaListMap = new HashMap();
			for (JobMeta mt : metaList) {
				String key = mt.getK();
				//logger.debug(Arrays.deepToString(metaNameSplit));
				
				try {
					String msg = getMessage("job."+key+".label");
					jsDetails.put(msg, mt.getV());
				} 
				catch (NoSuchMessageException e) {
					String[] metaKeySplit = key.split("\\.");
					//logger.debug(Arrays.deepToString(metaNameSplit));
					if(metaKeySplit.length == 1) {
						jsDetails.put(key, mt.getV());
					} else if (metaKeySplit.length == 2) {
						Map <String, String> subKeyMap = metaListMap.get(metaKeySplit[0]);
						if(subKeyMap == null) {
							subKeyMap = new HashMap();
							metaListMap.put(metaKeySplit[0], subKeyMap);
						}
						subKeyMap.put(metaKeySplit[1], mt.getV());
					}
				}
			}
			jsDetails.putAll(metaListMap);
			
		} else if(type.equalsIgnoreCase("sample")) {
			Integer sampleId = id;
			Sample sample = this.sampleService.getSampleById(sampleId);
			if(sample==null || sample.getSampleId()==null){
				  waspErrorMessage("sampleDetail.sampleNotFound.error");
				  return null;
			}
			
			jsDetails.put(getMessage("sample.name.label"), sample.getName());
		
			List<SampleMeta> metaList = sample.getSampleMeta();
			Map <String, Map<String, String>> metaListMap = new HashMap();
			for (SampleMeta mt : metaList) {
				String key = mt.getK();
				//logger.debug(Arrays.deepToString(metaNameSplit));

				try {
					String msg = getMessage("sample."+key+".label");
					jsDetails.put(msg, mt.getV());
				} 
				catch (NoSuchMessageException e) {
					String[] metaKeySplit = key.split("\\.");
					//logger.debug(Arrays.deepToString(metaNameSplit));
					if(metaKeySplit.length == 1) {
						jsDetails.put(key, mt.getV());
					} else if (metaKeySplit.length == 2) {
						Map <String, String> subKeyMap = metaListMap.get(metaKeySplit[0]);
						if(subKeyMap == null) {
							subKeyMap = new HashMap();
							metaListMap.put(metaKeySplit[0], subKeyMap);
						}
						subKeyMap.put(metaKeySplit[1], mt.getV());
					}
				}
			}
			jsDetails.putAll(metaListMap);
		}
			
		try {
			//return outputJSON(jsTree, response);
			return outputJSON(jsDetails, response);
		} 
		catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + jsTree, e);
		}	
	}
}
