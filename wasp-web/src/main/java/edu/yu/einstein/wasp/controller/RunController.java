package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunFile;
import edu.yu.einstein.wasp.model.RunLane;
import edu.yu.einstein.wasp.model.RunLanefile;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.RunLaneService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;

@Controller
@Transactional
@RequestMapping("/run")
public class RunController extends WaspController {

	private RunService	runService;

	@Autowired
	public void setRunService(RunService runService) {
		this.runService = runService;
	}

	public RunService getRunService() {
		return this.runService;
	}

	private RunLaneService	runLaneService;

	@Autowired
	public void setRunLaneService(RunLaneService runLaneService) {
		this.runLaneService = runLaneService;
	}

	public RunLaneService getRunLaneService() {
		return this.runLaneService;
	}

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("run", RunMeta.class, request.getSession());
	}

	@RequestMapping("/list")
	public String list(ModelMap m) {
//		List<Run> runList = this.getRunService().findAll();
//
//		m.addAttribute("run", runList);

		m.addAttribute("_metaList",	getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));

		prepareSelectListData(m);

		return "run/list";
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

			runList = sidx.isEmpty() ? this.runService.findAll() : this.runService.findAllOrderBy(sidx, sord);
		} else {
			  Map m = new HashMap();
			  
			  if (search.equals("true") && !searchStr.isEmpty())
				  m.put(request.getParameter("searchField"), request.getParameter("searchString"));
			  
			  if (!resourceId.isEmpty())
				  m.put("resourceId", Integer.parseInt(resourceId));
			  
			  if (!sampleId.isEmpty())
				  m.put("sampleId", Integer.parseInt(sampleId));
			  				  
			  runList = this.runService.findByMap(m);
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
				int selIndex = runList.indexOf(runService.findById(selId));
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
				
				User user = userService.getById(run.getUserId());
				 					
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
							run.getName(),
							run.getResource().getName(),
							run.getResourceId().toString(),
							run.getSample().getName(),
							run.getSampleId().toString(),
							user.getFirstName() + " " + user.getLastName()
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

	@RequestMapping(value = "/detail/{strId}", method = RequestMethod.GET)
	public String detail(@PathVariable("strId") String strId, ModelMap m) {
		String now = (new Date()).toString();

		Integer i;
		try {
			i = new Integer(strId);
		} catch (Exception e) {
			return "default";
		}

		Run run = this.getRunService().getById(i.intValue());

		List<RunMeta> runMetaList = run.getRunMeta();
		runMetaList.size();

		List<RunLane> runLaneList = run.getRunLane();
		runLaneList.size();

		List<RunFile> runFileList = run.getRunFile();
		runFileList.size();

		m.addAttribute("now", now);
		m.addAttribute("run", run);
		m.addAttribute("runmeta", runMetaList);
		m.addAttribute("runlane", runLaneList);
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

		RunLane runLane = this.getRunLaneService().getById(i.intValue());

		//
		// TODO THROW EXCEPTION IF RUNID != RUNLANE.RUNID
		//

		List<RunLanefile> runLaneFileList = runLane.getRunLanefile();
		runLaneFileList.size();

		m.addAttribute("now", now);
		m.addAttribute("runlane", runLane);
		m.addAttribute("runlanefile", runLaneFileList);

		return "run/lanedetail";
	}

}
