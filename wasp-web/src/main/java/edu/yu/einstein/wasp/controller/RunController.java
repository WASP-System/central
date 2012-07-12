package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.RunCellDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeResourceCategoryDao;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunCell;
import edu.yu.einstein.wasp.model.RunCellFile;
import edu.yu.einstein.wasp.model.RunFile;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userrole;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;

@Controller
@Transactional
@RequestMapping("/run")
public class RunController extends WaspController {

	private RunDao	runDao;

	@Autowired
	public void setRunDao(RunDao runDao) {
		this.runDao = runDao;
	}

	public RunDao getRunDao() {
		return this.runDao;
	}

	@Autowired
	private RunMetaDao runMetaDao;

	private RunCellDao	runCellDao;

	@Autowired
	public void setResourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
	}

	public ResourceDao getResourceDao() {
		return this.resourceDao;
	}

	private ResourceDao	resourceDao;

	@Autowired
	public void setSampleDao(SampleDao sampleDao) {
		this.sampleDao = sampleDao;
	}

	public SampleDao getSampleDao() {
		return this.sampleDao;
	}

	private SampleDao	sampleDao;

	@Autowired
	public void setSampleSubtypeResourceCategoryDao(SampleSubtypeResourceCategoryDao sampleSubtypeResourceCategoryDao) {
		this.sampleSubtypeResourceCategoryDao = sampleSubtypeResourceCategoryDao;
	}

	public SampleSubtypeResourceCategoryDao getSampleSubtypeResourceCategoryDao() {
		return this.sampleSubtypeResourceCategoryDao;
	}

	private SampleSubtypeResourceCategoryDao	sampleSubtypeResourceCategoryDao;

	@Autowired
	public void setRunCellDao(RunCellDao runCellDao) {
		this.runCellDao = runCellDao;
	}

	public RunCellDao getRunCellDao() {
		return this.runCellDao;
	}

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp(RunMeta.class, request.getSession());
	}

	@Autowired
	private MessageService messageService;
	
	@RequestMapping("/list")
	public String list(ModelMap m) {
//		List<Run> runList = this.getRunDao().findAll();
//
//		m.addAttribute("run", runList);

		m.addAttribute("_metaList",	getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));

		prepareSelectListData(m);

		return "run/list";
	}

	@Override
	protected void prepareSelectListData(ModelMap m) {
		super.prepareSelectListData(m);

		m.addAttribute("machines", this.resourceDao.findAll());
		
		m.addAttribute("flowcells", this.sampleDao.findAllPlatformUnits());
		
		List <User> allUsers = this.userDao.getActiveUsers();
		Map <Integer, String> facUsers = new HashMap<Integer, String> ();
		for (User u : allUsers) {
			List<Userrole> urs = u.getUserrole();
			for (Userrole ur : urs) {
				String rn = ur.getRole().getRoleName();
				if ("fm".equals(rn) || "ft".equals(rn)) {
					facUsers.put(u.getUserId(), u.getNameFstLst());
					break;
				}
			}
		}
		m.addAttribute("techs", facUsers);
	}

	/*
	 * Returns compatible flowcells by given machine
	 * 
	 * @Author AJ Jing
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/samplesByResourceId", method=RequestMethod.GET)	
	public String samplesByResourceId(@RequestParam("resourceId") Integer resourceId, HttpServletResponse response) {

		Map <Integer, String> resultsMap = new LinkedHashMap<Integer, String>();
		
		if (resourceId.intValue() == -1) {
			
			for (Sample sample:sampleDao.findAllPlatformUnits()) {
				resultsMap.put(sample.getSampleId(), sample.getName());
			}
			
		} else {
		
			//first get the resourceCategoryId by resourceId
			Resource machine = this.resourceDao.getById(resourceId);
			//then get all the sampleSubtypeId by resourceCatgegoryId 
			Map queryMap = new HashMap();
			queryMap.put("resourcecategoryId", machine.getResourcecategoryId());
			List <SampleSubtypeResourceCategory> ssrcList = this.sampleSubtypeResourceCategoryDao.findByMap(queryMap);
			List <Integer> idList = new ArrayList<Integer> ();
			for (SampleSubtypeResourceCategory ssrc : ssrcList) {
				idList.add(ssrc.getSampleSubtypeId());
			}
			
			//last filter all platform units by the list of sampleSubtypeId
			for(Sample sample:sampleDao.findAllPlatformUnits()) {
				if (idList.contains(sample.getSampleSubtypeId()))
					resultsMap.put(sample.getSampleId(), sample.getName());
			}
		}

		try {
			return outputJSON(resultsMap, response); 	
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "+resultsMap,e);
		}
	}
	/*
	 * Returns compatible machines by given flowcell
	 * 
	 * @Author AJ Jing
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/resourcesBySampleId", method=RequestMethod.GET)	
	public String resourcesBySampleId(@RequestParam("sampleId") Integer sampleId, HttpServletResponse response) {

		Map <Integer, String> resultsMap = new LinkedHashMap<Integer, String>();
		
		if (sampleId.intValue() == -1) {
			
			for (Resource resource:resourceDao.findAll()) {
				resultsMap.put(resource.getResourceId(), resource.getName());
			}
			
		} else {
		
			//first get the sampleSubtypeId by sampleId
			Sample flowcell = sampleDao.getById(sampleId);
			//then get all the resourceCategoryId by resourceCatgegoryId 
			Map queryMap = new HashMap();
			queryMap.put("sampleSubtypeId", flowcell.getSampleSubtypeId());
			List <SampleSubtypeResourceCategory> ssrcList = this.sampleSubtypeResourceCategoryDao.findByMap(queryMap);
			List <Integer> idList = new ArrayList<Integer> ();
			for (SampleSubtypeResourceCategory ssrc : ssrcList) {
				idList.add(ssrc.getResourcecategoryId());
			}
			
			//last filter all platform units by the list of sampleSubtypeId
			for(Resource resource:resourceDao.findAll()) {
				if (idList.contains(resource.getResourcecategoryId()))
					resultsMap.put(resource.getResourceId(), resource.getName());
			}
		}

		try {
			return outputJSON(resultsMap, response); 	
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "+resultsMap,e);
		}
	}

	@RequestMapping(value="/listJSON", method=RequestMethod.GET)	
	public String getListJSON(HttpServletResponse response) {
		
		String search = request.getParameter("_search");
		String searchStr = request.getParameter("searchString");
	
		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");
		
		String resourceId = request.getParameter("resourceId") == null ? "" : request.getParameter("resourceId");
		String sampleId = request.getParameter("sampleId") == null ? "" : request.getParameter("sampleId");
		
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		List<Run> runList;
		
		if (!search.equals("true")	&& resourceId.isEmpty()	&& sampleId.isEmpty()) {
			if ("resourceName".equals(sidx))
				sidx = "resource.name";

			runList = sidx.isEmpty() ? this.runDao.findAll() : this.runDao.findAllOrderBy(sidx, sord);
		} else {
			  Map m = new HashMap();
			  
			  if (search.equals("true") && !searchStr.isEmpty())
				  m.put(request.getParameter("searchField"), request.getParameter("searchString"));
			  
			  if (!resourceId.isEmpty())
				  m.put("resourceId", Integer.parseInt(resourceId));
			  
			  if (!sampleId.isEmpty())
				  m.put("sampleId", Integer.parseInt(sampleId));
			  				  
			  runList = this.runDao.findByMap(m);
		}

		try {
			int pageIndex = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
			int rowNum = runList.size();										// total number of rows
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum;				// total number of pages
			
			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");
			jqgrid.put("page", pageIndex + "");
			 
			Map<String, String> runData=new HashMap<String, String>();
			runData.put("page", pageIndex + "");
			runData.put("selId",StringUtils.isEmpty(request.getParameter("selId"))?"":request.getParameter("selId"));
			jqgrid.put("rundata",runData);
			 
			List<Map> rows = new ArrayList<Map>();
			
			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			/* if the selId is set, change the page index to the one contains the selId */
			if(!StringUtils.isEmpty(request.getParameter("selId")))
			{
				int selId = Integer.parseInt(request.getParameter("selId"));
				int selIndex = runList.indexOf(runDao.findById(selId));
				frId = selIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}				

			List<Run> runPage = runList.subList(frId, toId);
			for (Run run:runPage) {
				Map cell = new HashMap();
				cell.put("id", run.getRunId());
				 
				List<RunMeta> runMeta = getMetaHelperWebapp().syncWithMaster(run.getRunMeta());
				
				User user = userDao.getById(run.getUserId());
				 					
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
							run.getName(),
							run.getResource().getName(),
							run.getResourceId().toString(),
							run.getSample().getName(),
							run.getSampleId().toString(),
							user.getFirstName() + " " + user.getLastName(),
							"", // placeholder for resourceId, refer to the list of columns on gridcolumns.jsp
							""  // placeholder for sampleId, refer to the list of columns on gridcolumns.jsp
				}));
				 
				for (RunMeta meta:runMeta) {
					cellList.add(meta.getV());
				}
				
				 
				cell.put("cell", cellList);
				 
				rows.add(cell);
			}

			 
			jqgrid.put("rows",rows);
			 
			return outputJSON(jqgrid, response); 	
			 
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + runList,e);
		}
	
	}

	@RequestMapping(value = "/detail_rw/updateJSON.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
	public String updateDetailJSON(@RequestParam("id") Integer runId, Run runForm, ModelMap m, HttpServletResponse response) {
		boolean adding = (runId == null || runId.intValue() == 0);

		List<RunMeta> runMetaList = getMetaHelperWebapp().getFromJsonForm(request, RunMeta.class);

		runForm.setRunMeta(runMetaList);
			
		if (adding) {
			// To add new run to DB
			//check if Resource Name already exists in db; if 'true', do not allow to proceed.
			Map queryMap = new HashMap();
			queryMap.put("name", runForm.getName());
			List<Run> runList = this.runDao.findByMap(queryMap);
			if(runList!=null && runList.size()>0) {
				try{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println(messageService.getMessage("run.run_exists.error"));
					return null;
				} catch (Throwable e) {
					throw new IllegalStateException("Cant output validation error "+messageService.getMessage("run.run_exists.error"),e);
				}
				
			}
			
			runForm.setUserId(Integer.parseInt(request.getParameter("start_esf_staff")));
			runForm.setLastUpdTs(new Date());
			runForm.setIsActive(1);
			
			Run runDb = this.runDao.save(runForm);
			runId = runDb.getRunId();
		} else {
			
			// editing run is not allowed
		}

		runMetaDao.updateByRunId(runId, runMetaList);

		try {
			response.getWriter().println(adding ? messageService.getMessage("run.created_success.label") 
					: messageService.getMessage("run.updated_success.label"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ", e);
		}
	}

	@RequestMapping(value = "/detail/{strId}", method = RequestMethod.GET)
	public String detail(@PathVariable("strId") String strId, ModelMap m) {
		String now = (new Date()).toString();

		Integer i;
		try {
			i = new Integer(strId);
		} catch (Exception e) {
			return "default";
		}

		Run run = this.getRunDao().getById(i.intValue());

		List<RunMeta> runMetaList = run.getRunMeta();
		runMetaList.size();

		List<RunCell> runCellList = run.getRunCell();
		runCellList.size();

		List<RunFile> runFileList = run.getRunFile();
		runFileList.size();

		m.addAttribute("now", now);
		m.addAttribute("run", run);
		m.addAttribute("runmeta", runMetaList);
		m.addAttribute("runcell", runCellList);
		m.addAttribute("runfile", runFileList);

		return "run/detail";
	}

	@RequestMapping(value = "/lane/detail/{strRunId}/{strId}", method = RequestMethod.GET)
	public String laneDetail(@PathVariable("strRunId") String strRunId, @PathVariable("strId") String strId, ModelMap m) {
		String now = (new Date()).toString();

		Integer i;
		try {
			i = new Integer(strId);
		} catch (Exception e) {
			return "default";
		}

		Integer runId;
		try {
			runId = new Integer(strRunId);
		} catch (Exception e) {
			return "default";
		}

		RunCell runCell = this.getRunCellDao().getById(i.intValue());

		//
		// TODO THROW EXCEPTION IF RUNID != RUNLANE.RUNID
		//

		List<RunCellFile> runCellFileList = runCell.getRunCellFile();
		runCellFileList.size();

		m.addAttribute("now", now);
		m.addAttribute("runcell", runCell);
		m.addAttribute("runcellfile", runCellFileList);

		return "run/lanedetail";
	}

}
