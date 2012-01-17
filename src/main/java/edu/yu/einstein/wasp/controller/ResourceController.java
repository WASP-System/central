package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.controller.validator.MetaHelper;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceLane;
import edu.yu.einstein.wasp.model.ResourceMeta;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.service.ResourceMetaService;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.TypeResourceService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;

@Controller
@Transactional
@RequestMapping("/resource")
public class ResourceController extends WaspController {

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private ResourceMetaService resourceMetaService;

	@Autowired
	private TypeResourceService typeResourceService;

	private final MetaHelper getMetaHelper() {
		return new MetaHelper("resource", ResourceMeta.class,
				request.getSession());
	}

	@Override
	protected void prepareSelectListData(ModelMap m) {
		super.prepareSelectListData(m);

		m.addAttribute("typeResources", typeResourceService.findAll());
	}

	@RequestMapping("/list")
	@PreAuthorize("hasRole('god') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String list(ModelMap m) {
		m.addAttribute("_metaList",
				getMetaHelper().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelper().getArea());

		prepareSelectListData(m);

		// List<Resource> resourceList = resourceService.findAll();
		//
		// m.addAttribute("resource", resourceList);

		return "resource/list";
	}

	@RequestMapping(value = "/listJSON", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String getListJSON(HttpServletResponse response) {
		// result
		Map<String, Object> jqgrid = new HashMap<String, Object>();

		List<Resource> resourceList;

		if (request.getParameter("_search") == null
				|| StringUtils.isEmpty(request.getParameter("searchString"))) {
			resourceList = resourceService.findAll();
		} else {
			Map<String, String> m = new HashMap<String, String>();

			m.put(request.getParameter("searchField"),
					request.getParameter("searchString"));

			resourceList = resourceService.findByMap(m);

			if ("ne".equals(request.getParameter("searchOper"))) {
				List<Resource> allResources = new ArrayList<Resource>(
						resourceService.findAll());
				for (Iterator<Resource> it = resourceList.iterator(); it
						.hasNext();) {
					Resource excludeResource = it.next();
					allResources.remove(excludeResource);
				}
				resourceList = allResources;
			}
		}

		ObjectMapper mapper = new ObjectMapper();

		try {
			int pageIndex = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
			int rowNum = resourceList.size();										// total number of rows
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum;				// total number of pages
			
			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");
			jqgrid.put("page", pageIndex + "");

			Map<String, String> resourceData = new HashMap<String, String>();
			resourceData.put("page", pageIndex + "");
			resourceData.put("selId",
					StringUtils.isEmpty(request.getParameter("selId")) ? ""
							: request.getParameter("selId"));
			jqgrid.put("resourcedata", resourceData);

			List<Map> rows = new ArrayList<Map>();

			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			/* if the selId is set, change the page index to the one contains the selId */
			if(!StringUtils.isEmpty(request.getParameter("selId")))
			{
				int selId = Integer.parseInt(request.getParameter("selId"));
				int selIndex = resourceList.indexOf(resourceService.findById(selId));
				frId = selIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}				

			List<Resource> resourcePage = resourceList.subList(frId, toId);
			for (Resource resource : resourcePage) {
				Map cell = new HashMap();
				cell.put("id", resource.getResourceId());

				List<ResourceMeta> resourceMeta = getMetaHelper()
						.syncWithMaster(resource.getResourceMeta());

				List<String> cellList = new ArrayList<String>(
						Arrays.asList(new String[] {
								new Integer(resource.getResourceId())
										.toString(), 
								resource.getName(),
								resource.getPlatform(),
								resource.getTypeResource().getName(),
								resource.getIsActive().intValue() == 1 ? "yes" : "no" }));

				for (ResourceMeta meta : resourceMeta) {
					cellList.add(meta.getV());
				}

				cell.put("cell", cellList);

				rows.add(cell);
			}

			jqgrid.put("rows", rows);

			return outputJSON(jqgrid, response);

		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "
					+ resourceList, e);
		}

	}

	@RequestMapping(value = "/subgridJSON.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String subgridJSON(@RequestParam("id") Integer resourceId,
			ModelMap m, HttpServletResponse response) {

		Map<String, Object> jqgrid = new HashMap<String, Object>();

		Resource resourceDb = resourceService.getById(resourceId);

		List<Run> runs = resourceDb.getRun();

		// get max lenth of the previous 4 lists
		int max = runs.size();

		// if (max == 0) {
		// Lab lab = new Lab();
		//
		// puLabs.add(lab);
		//
		// LabUser lu = new LabUser();
		// lu.setLab(lab);
		// uLabs.add(lu);
		//
		// uJobs.add(new Job());
		//
		// uSamples.add(new Sample());
		//
		// max = 1;
		// }

		String[] mtrx = new String[max];

		ObjectMapper mapper = new ObjectMapper();

		try {

			jqgrid.put("page", "1");
			jqgrid.put("records", max + "");
			jqgrid.put("total", max + "");

			String text;

			int i = 0;
			int j = 0;
			for (Run run : runs) {

				text = run.getRunId() == null ? "No Runs"
						: "<a href=/wasp/run/detail/" + run.getRunId() + ".do>"
								+ run.getName() + "</a>";
				mtrx[j] = text;

				j++;

			}

			List<Map> rows = new ArrayList<Map>();

			for (j = 0; j < max; j++) {

				Map cell = new HashMap();
				rows.add(cell);

				cell.put("id", j + "");
				List<String> cellList = Arrays.asList(mtrx[j]);
				cell.put("cell", cellList);
			}

			jqgrid.put("rows", rows);

			return outputJSON(jqgrid, response);

		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + runs, e);
		}

	}

	@RequestMapping(value = "/detail_rw/{resourceId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String detailRW(@PathVariable("resourceId") Integer resourceId,
			ModelMap m) {
		return detail(resourceId, m, true);
	}

	@RequestMapping(value = "/detail_ro/{resourceId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String detailRO(@PathVariable("resourceId") Integer resourceId,
			ModelMap m) {
		return detail(resourceId, m, false);
	}

	private String detail(Integer resourceId, ModelMap m, boolean isRW) {
		Resource resource = resourceService.getById(resourceId);

		resource.setResourceMeta(getMetaHelper().syncWithMaster(
				resource.getResourceMeta()));

		// List<ResourceMeta> resourceMetaList = resource.getResourceMeta();
		// resourceMetaList.size();

		List<ResourceLane> resourceLaneList = resource.getResourceLane();
		resourceLaneList.size();

		List<Run> runList = resource.getRun();
		runList.size();

		// List<ResourceUser> resourceUserList = resource.getResourceUser();
		// resourceUserList.size();

		// m.addAttribute("now", now);
		m.addAttribute("resource", resource);
		// m.addAttribute("resourcemeta", resourceMetaList);
		m.addAttribute("resourcelane", resourceLaneList);
		m.addAttribute("run", runList);
		// m.addAttribute("resourceuser", resourceUserList);

		prepareSelectListData(m);

		return isRW ? "resource/detail_rw" : "resource/detail_ro";
	}

	@RequestMapping(value = "/detail_rw/{resourceId}.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('god') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String updateResource(
			@PathVariable("resourceId") Integer resourceId,
			@Valid Resource resourceForm, BindingResult result,
			SessionStatus status, ModelMap m) {
		return validateAndUpdateResource(resourceId.intValue(), resourceForm,
				result, status, m);
	}

	@RequestMapping(value = "/create.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String showCreateForm(ModelMap m) {

		Resource resource = new Resource();

		resource.setResourceMeta(getMetaHelper().getMasterList(
				ResourceMeta.class));

		m.put("resource", resource);

		prepareSelectListData(m);

		return "resource/create";
	}

	@RequestMapping(value = "/create.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('god') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String createResource(@Valid Resource resourceForm,
			BindingResult result, SessionStatus status, ModelMap m) {
		return validateAndUpdateResource(-1, resourceForm, result, status, m);
	}

	public String validateAndUpdateResource(int resourceId,
			Resource resourceForm, BindingResult result, SessionStatus status,
			ModelMap m) {

		boolean newResource = true;
		if (resourceId > 0) {
			newResource = false;
		}

		List<ResourceMeta> resourceMetaList = getMetaHelper().getFromRequest(
				request, ResourceMeta.class);

		getMetaHelper().validate(resourceMetaList, result);

		if (result.hasErrors()) {
			resourceForm.setResourceMeta(resourceMetaList);
			m.put("resource", resourceForm);
			prepareSelectListData(m);

			if (newResource) {
				waspMessage("resource.created.error");
				return "resource/create";
			} else {
				waspMessage("resource.updated.error");
				return "resource/detail_rw";
			}
		}

		for (ResourceMeta meta : resourceMetaList) {
			if (meta.getK().contains("assay_platform")) {
				resourceForm.setPlatform(meta.getV());
				break;
			}
		}

		resourceForm.setResourceMeta(resourceMetaList);
		// resourceForm.setLastUpdTs(new Date());
		// resourceForm.setLastUpdUser(getAuthenticatedUser().getUserId());

		Resource resourceDb;
		if (newResource) {
			resourceDb = resourceService.save(resourceForm);
		} else {
			resourceForm.setResourceId(resourceId);
			resourceDb = resourceService.merge(resourceForm);
		}

		resourceId = resourceDb.getResourceId();
		resourceMetaService.updateByResourceId(resourceId,
				resourceForm.getResourceMeta());

		status.setComplete();

		waspMessage(newResource ? "resource.created_success.label"
				: "resource.updated_success.label");

		return "redirect:/resource/detail_ro/" + resourceId + ".do";
	}

}
