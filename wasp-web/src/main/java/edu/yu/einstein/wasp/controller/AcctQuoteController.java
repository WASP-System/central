package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.dao.AcctQuoteDao;
import edu.yu.einstein.wasp.dao.JobCellDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.AcctQuoteMeta;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;

@Controller
@Transactional
@RequestMapping("/acctquote")
public class AcctQuoteController extends WaspController {
	
	private AcctQuoteDao acctQuoteDao;

	@Autowired
	public void setacctQuoteDao(AcctQuoteDao aqDao) {
		this.acctQuoteDao = aqDao;
	}

	public AcctQuoteDao getacctQuoteDao() {
		return this.acctQuoteDao;
	}
	
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
	private TaskDao		taskDao;
	@Autowired
	private StateDao	stateDao;
	@Autowired
	private WorkflowresourcecategoryDao workflowresourcecategoryDao;
	@Autowired
	private JobCellDao jobCellDao;

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
			jobQuoteList = sidx.isEmpty() ? this.acctQuoteDao.findAll() : this.acctQuoteDao.findAllOrderBy(sidx, sord);
		} else {
			Map m = new HashMap();

			if (search.equals("true") && !searchStr.isEmpty())
				m.put(request.getParameter("searchField"), request.getParameter("searchString"));

			if (!StringUtils.isEmpty(userId))
				m.put("UserId", Integer.parseInt(userId));

//			if (!labId.isEmpty())
//				m.put("labId", Integer.parseInt(labId));

			jobQuoteList = this.acctQuoteDao.findByMap(m);
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
				int selIndex = jobQuoteList.indexOf(jobDao.findById(selId));
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
				
				User user = userDao.getById(quote.getJob().getUserId());
				 					
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
