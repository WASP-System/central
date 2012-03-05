package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.AcctQuoteMeta;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobCell;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleCell;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;
import edu.yu.einstein.wasp.service.AcctQuoteService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.JobCellService;
import edu.yu.einstein.wasp.service.JobUserService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.WorkflowresourcecategoryService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.StringHelper;

@Controller
@Transactional
@RequestMapping("/acctquote")
public class AcctQuoteController extends WaspController {
	
	private AcctQuoteService acctQuoteService;

	@Autowired
	public void setacctQuoteService(AcctQuoteService aqService) {
		this.acctQuoteService = aqService;
	}

	public AcctQuoteService getacctQuoteService() {
		return this.acctQuoteService;
	}
	
	private JobService	jobService;

	@Autowired
	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}

	public JobService getJobService() {
		return this.jobService;
	}

	private JobUserService	jobUserService;

	@Autowired
	public void setJobUserService(JobUserService jobUserService) {
		this.jobUserService = jobUserService;
	}

	public JobUserService getJobUserService() {
		return this.jobUserService;
	}

	private RoleService	roleService;

	@Autowired
	public void setJobUserService(RoleService roleService) {
		this.roleService = roleService;
	}

	public RoleService getRoleUserService() {
		return this.roleService;
	}

	@Autowired
	private TaskService		taskService;
	@Autowired
	private StateService	stateService;
	@Autowired
	private WorkflowresourcecategoryService workflowresourcecategoryService;
	@Autowired
	private JobCellService jobCellService;

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("acctQuote", AcctQuoteMeta.class, request.getSession());
	}

	@RequestMapping("/list")
	public String list(ModelMap m) {

		m.addAttribute("_metaList",	getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));

		prepareSelectListData(m);

		return "acctquote/list";
	}

	@RequestMapping(value="/listJSON", method=RequestMethod.GET)	
	public String getListJSON(HttpServletResponse response) {
		
		String search = request.getParameter("_search");
		String searchStr = request.getParameter("searchString");
	
		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");
		
		String userId = request.getParameter("userId");
		//String labId = request.getParameter("labId");
		
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		//List<Job> jobList;
		List<AcctQuote> jobQuoteList;
		
		if (!search.equals("true")	&& StringUtils.isEmpty(userId)) {
			jobQuoteList = sidx.isEmpty() ? this.acctQuoteService.findAll() : this.acctQuoteService.findAllOrderBy(sidx, sord);
		} else {
			Map m = new HashMap();

			if (search.equals("true") && !searchStr.isEmpty())
				m.put(request.getParameter("searchField"), request.getParameter("searchString"));

			if (!StringUtils.isEmpty(userId))
				m.put("UserId", Integer.parseInt(userId));

//			if (!labId.isEmpty())
//				m.put("labId", Integer.parseInt(labId));

			jobQuoteList = this.acctQuoteService.findByMap(m);
		}

		try {
			int pageIndex = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
			int rowNum = jobQuoteList.size();										// total number of rows
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum;				// total number of pages
			
			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");
			jqgrid.put("page", pageIndex + "");
			 
			Map<String, String> userData=new HashMap<String, String>();
			userData.put("page", pageIndex + "");
			userData.put("selId",StringUtils.isEmpty(request.getParameter("selId"))?"":request.getParameter("selId"));
			jqgrid.put("userdata",userData);
			 
			List<Map> rows = new ArrayList<Map>();
			
			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			/* if the selId is set, change the page index to the one contains the selId */
			if(!StringUtils.isEmpty(request.getParameter("selId")))
			{
				int selId = Integer.parseInt(request.getParameter("selId"));
				int selIndex = jobQuoteList.indexOf(jobService.findById(selId));
				frId = selIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}				

			List<AcctQuote> quotePage = jobQuoteList.subList(frId, toId);
			for (AcctQuote quote:quotePage) {
				Map cell = new HashMap();
				cell.put("id", quote.getQuoteId());
				 
				List<AcctQuoteMeta> quoteMeta = getMetaHelperWebapp().syncWithMaster(quote.getAcctQuoteMeta());
				
				User user = userService.getById(quote.getJob().getUserId());
				 					
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
							quote.getJob().getName(),
							Float.toString(quote.getAmount()),
							user.getNameFstLst(),
							quote.getJob().getLab().getName(),
							quote.getJob().getLastUpdTs().toString()
				}));
				 
				for (AcctQuoteMeta meta:quoteMeta) {
					cellList.add(meta.getV());
				}
				
				 
				cell.put("cell", cellList);
				 
				rows.add(cell);
			}

			 
			jqgrid.put("rows",rows);
			 
			return outputJSON(jqgrid, response); 	
			 
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + jobQuoteList,e);
		}
	
	}


}
