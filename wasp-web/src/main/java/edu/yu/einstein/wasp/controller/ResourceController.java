package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.model.Barcode;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceBarcode;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceLane;
import edu.yu.einstein.wasp.model.ResourceMeta;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.TypeResource;
import edu.yu.einstein.wasp.service.BarcodeService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.ResourceBarcodeService;
import edu.yu.einstein.wasp.service.ResourceCategoryService;
import edu.yu.einstein.wasp.service.ResourceMetaService;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.TypeResourceService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;

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
	
	@Autowired
	private ResourceCategoryService resourceCategoryService;
	
	@Autowired
	private ResourceBarcodeService resourceBarcodeService;
	
	@Autowired
	private BarcodeService barcodeService;
	
	@Autowired
	private MessageService messageService;

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("resource", ResourceMeta.class,
				request.getSession());
	}

	@Override
	protected void prepareSelectListData(ModelMap m) {
		super.prepareSelectListData(m);

		List <TypeResource> typeResourceList = new ArrayList <TypeResource> (typeResourceService.findAll());
		
		//When adding a new record in Resources JqGrid, it displays  all Type Resources that are NOT "aligner" or "peakcaller"
		for (Iterator<TypeResource> it = typeResourceList.iterator(); it.hasNext();) {
			TypeResource tr = it.next();
			if (tr.getIName().equals("aligner") || tr.getIName().equals("peakcaller")) {
				it.remove();
			}
		}
		m.addAttribute("typeResources", typeResourceList);
		
		//List <ResourceCategory> resourceCategoryList = new ArrayList <ResourceCategory> (resourceCategoryService.findAll());
//		for (ResourceCategory rc : resourceCategoryList) {
//			if (rc.getIsActive().intValue() == 0 && !rc.getName().contains("_INACTIVE")) {
//				rc.setName(rc.getName() + "_INACTIVE");
//			}
//		}
		m.addAttribute("categoryResources", resourceCategoryService.getActiveResourceCategories());

	}

	@RequestMapping("/list")
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String list(ModelMap m) {
		
		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));
		
		prepareSelectListData(m);

		return "resource/list";
	}

	@RequestMapping(value = "/listJSON", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String getListJSON(HttpServletResponse response) {
		// result
		Map<String, Object> jqgrid = new HashMap<String, Object>();

		List<Resource> resourceList;
		
		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");

		if (request.getParameter("_search") == null
				|| StringUtils.isEmpty(request.getParameter("searchString"))) {
			resourceList = sidx.isEmpty() ? this.resourceService.findAll() : this.resourceService.findAllOrderBy(sidx, sord);

			//resourceList = resourceService.findAll();
		} else {
			Map<String, String> m = new HashMap<String, String>();

			m.put(request.getParameter("searchField"),
					request.getParameter("searchString"));

			resourceList = resourceService.findByMap(m);

			if ("ne".equals(request.getParameter("searchOper"))) {
				//List<Resource> allResources = new ArrayList<Resource>(resourceService.findAll());
				List<Resource> allResources = new ArrayList<Resource>(sidx.isEmpty() ? this.resourceService.findAll() : this.resourceService.findAllOrderBy(sidx, sord));

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
			
			Map<Integer, Integer> allResourceBarcode = new TreeMap<Integer, Integer>();
			for (ResourceBarcode resourceBarcode : (List<ResourceBarcode>) this.resourceBarcodeService.findAll()) {
				if (resourceBarcode != null) {
					
					allResourceBarcode.put(resourceBarcode.getResourceId(), resourceBarcode.getBarcodeId());
				}
			}
			
			Map<Integer, String> allBarcode = new TreeMap<Integer, String>();
			for (Barcode barcode : (List<Barcode>) this.barcodeService.findAll()) {
				if (barcode != null) {
					
					allBarcode.put(barcode.getBarcodeId(), barcode.getBarcode());
				}
			}
			
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
			
			
			/***** Begin Sort by Resource name *****/
			class ResourceNameComparator implements Comparator<Resource> {
				@Override
				public int compare(Resource arg0, Resource arg1) {
					return arg0.getName().compareToIgnoreCase(arg1.getName());
				}
			}

			if (sidx.equals("name")) {
				Collections.sort(resourceList, new ResourceNameComparator());
				if (sord.equals("desc"))
					Collections.reverse(resourceList);
			}
			/***** End Sort by Resource name *****/

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

				List<ResourceMeta> resourceMeta = getMetaHelperWebapp()
						.syncWithMaster(resource.getResourceMeta());

				List<String> cellList = new ArrayList<String>(
						Arrays.asList(new String[] {
								resource.getName(),
								resource.getResourceCategory().getName(),
								"",
								resource.getTypeResource().getName(), 
								resource.getIsActive().intValue() == 1 ? "yes" : "no", //}));
								allResourceBarcode.get(resource.getResourceId())==null? "" : allBarcode.get(allResourceBarcode.get(resource.getResourceId()))}));

				for (ResourceMeta meta : resourceMeta) {
					cellList.add(meta.getV());
				}

				cell.put("cell", cellList);

				rows.add(cell);
			}

			jqgrid.put("rows", rows);

			return outputJSON(jqgrid, response);

		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + resourceList, e);
		}

	}
	
	/**
	 * Creates/Updates user
	 * 
	 * @Author Sasha Levchuk 
	 */	
	@RequestMapping(value = "/detail_rw/updateJSON.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or User.login == principal.name")
	public String updateDetailJSON(@RequestParam("id") Integer resourceId,
			Resource resourceForm, ModelMap m, HttpServletResponse response) {

		List<ResourceMeta> resourceMetaList = getMetaHelperWebapp().getFromJsonForm(request, ResourceMeta.class);

		resourceForm.setResourceMeta(resourceMetaList);
			
		if (resourceId == null || resourceId.intValue() == 0) {
			
			//check if Resource Name already exists in db; if 'true', do not allow to proceed.
			if(this.resourceService.getResourceByName(resourceForm.getName()).getName() != null) {
				try{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println(messageService.getMessage("resource.resource_exists.error"));
					return null;
				} catch (Throwable e) {
					throw new IllegalStateException("Cant output validation error "+messageService.getMessage("resource.resource_exists.error"),e);
				}
				
			}
			
			//check if barcode already exists in Db; if 'true', do not allow to proceed.
			if(this.barcodeService.getBarcodeByBarcode(request.getParameter("barcode")).getBarcode() != null && 
					this.barcodeService.getBarcodeByBarcode(request.getParameter("barcode")).getBarcode().length() != 0) {
				try{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println(messageService.getMessage("resource.barcode_exists.error"));
					return null;
				} catch (Throwable e) {
					throw new IllegalStateException("Cant output validation error "+messageService.getMessage("resource.barcode_exists.error"),e);
				}
				
			}
			
			ResourceCategory resourceCategory = this.resourceCategoryService.getResourceCategoryByResourceCategoryId(new Integer(request.getParameter("resourceCategoryId")));
			Integer resourceCategoryId =resourceCategory.getResourceCategoryId();
			Integer typeResourceId = resourceCategory.getTypeResourceId();

			resourceForm.setResourcecategoryId(resourceCategoryId);
			resourceForm.setTypeResourceId(typeResourceId);
			
			resourceForm.setIName(resourceForm.getName());
			resourceForm.setLastUpdTs(new Date());
			
			if(request.getParameter("resource.decommission_date") == null || request.getParameter("resource.decommission_date").length() == 0){
				resourceForm.setIsActive(1);
			}
			else {
				resourceForm.setIsActive(0);
			}
			
			ResourceBarcode resourceBarcode = new ResourceBarcode();
			Barcode barcode = new Barcode();
			
			barcode.setBarcode(request.getParameter("barcode")==null? "" : request.getParameter("barcode"));
			resourceBarcode.setBarcode(barcode);
			
			Barcode barcodeDB = this.barcodeService.save(barcode);
			resourceBarcode.setBarcodeId(barcodeDB.getBarcodeId());

			Resource resourceDb = this.resourceService.save(resourceForm);
			resourceId = resourceDb.getResourceId();
			
			resourceBarcode.setResourceId(resourceId);
			this.resourceBarcodeService.save(resourceBarcode);
			
		} else {
			
			Resource resourceDb = this.resourceService.getById(resourceId);
			ResourceBarcode resourceBarcodeDB = this.resourceBarcodeService.getResourceBarcodeByResourceId(resourceId);
			
			if(request.getParameter("resource.decommission_date") == null || request.getParameter("resource.decommission_date").length() == 0){
				resourceDb.setIsActive(1);
			}
			else {
				resourceDb.setIsActive(0);
			}
			
			resourceDb.setName(resourceForm.getName());
			//resourceDb.setResourcecategoryId(Integer.parseInt(request.getParameter("resourceCategoryId")));
			//resourceDb.setTypeResourceId(resourceForm.getTypeResourceId());
			resourceDb.setIName(resourceForm.getName());

			if (resourceBarcodeDB == null || resourceBarcodeDB.getBarcode() == null) {
				ResourceBarcode resourceBarcode = new ResourceBarcode();
				Barcode barcode = new Barcode();
				
				barcode.setBarcode(request.getParameter("barcode")==null? "" : request.getParameter("barcode"));
				barcode.setIsActive(1);
				resourceBarcode.setBarcode(barcode);
				
				Barcode barcodeDB = this.barcodeService.save(barcode);
				resourceBarcode.setBarcodeId(barcodeDB.getBarcodeId());
			
				resourceBarcode.setResourceId(resourceId);
				this.resourceBarcodeService.save(resourceBarcode);
			} else {
				resourceBarcodeDB.getBarcode().setBarcode(request.getParameter("barcode")==null? "" : request.getParameter("barcode"));
				this.resourceBarcodeService.merge(resourceBarcodeDB);
			}
			
			this.resourceService.merge(resourceDb);
		}

		resourceMetaService.updateByResourceId(resourceId, resourceMetaList);

		try {
			response.getWriter().println(messageService.getMessage("resource.updated_success.label"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ", e);
		}
	}


	@RequestMapping(value = "/subgridJSON.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
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
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String detailRW(@PathVariable("resourceId") Integer resourceId,
			ModelMap m) {
		return detail(resourceId, m, true);
	}

	@RequestMapping(value = "/detail_ro/{resourceId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String detailRO(@PathVariable("resourceId") Integer resourceId,
			ModelMap m) {
		return detail(resourceId, m, false);
	}

	private String detail(Integer resourceId, ModelMap m, boolean isRW) {
		Resource resource = resourceService.getById(resourceId);
		resource.setResourceMeta(getMetaHelperWebapp().syncWithMaster(
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
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String updateResource(
			@PathVariable("resourceId") Integer resourceId,
			@Valid Resource resourceForm, BindingResult result,
			SessionStatus status, ModelMap m) {
		return validateAndUpdateResource(resourceId.intValue(), resourceForm,
				result, status, m);
	}

	@RequestMapping(value = "/create.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String showCreateForm(ModelMap m) {

		Resource resource = new Resource();

		resource.setResourceMeta(getMetaHelperWebapp().getMasterList(
				ResourceMeta.class));

		m.put("resource", resource);

		prepareSelectListData(m);

		return "resource/create";
	}

	@RequestMapping(value = "/create.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
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

		List<ResourceMeta> resourceMetaList = getMetaHelperWebapp().getFromRequest(
				request, ResourceMeta.class);

		getMetaHelperWebapp().validate(resourceMetaList, result);

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

	/*	for (ResourceMeta meta : resourceMetaList) {
			if (meta.getK().contains("assay_platform")) {
				resourceForm.setPlatform(meta.getV());
				break;
			}
		} */

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
