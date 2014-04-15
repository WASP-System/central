package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import edu.yu.einstein.wasp.dao.BarcodeDao;
import edu.yu.einstein.wasp.dao.ResourceBarcodeDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceMetaDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Barcode;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceBarcode;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceMeta;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;

@Controller
@Transactional
@RequestMapping("/resource")
public class ResourceController extends WaspController {

	@Autowired
	private ResourceService resourceService; 
	
	@Autowired
	private SampleService sampleService;

	@Autowired
	private ResourceMetaDao resourceMetaDao;

	@Autowired
	private ResourceTypeDao resourceTypeDao;
	
	@Autowired
	private ResourceCategoryDao resourceCategoryDao;
	
	@Autowired
	private ResourceBarcodeDao resourceBarcodeDao;
	
	@Autowired
	private BarcodeDao barcodeDao;
	
	@Autowired
	private MessageServiceWebapp messageService;

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp(ResourceMeta.class, request.getSession());
	}

	@Override
	protected void prepareSelectListData(ModelMap m) {
		super.prepareSelectListData(m);

		List <ResourceType> resourceTypeList = new ArrayList <ResourceType> (resourceTypeDao.findAll());
		
		//When adding a new record in Resources JqGrid, it displays  all Type Resources that are NOT "aligner" or "peakcaller"
		for (Iterator<ResourceType> it = resourceTypeList.iterator(); it.hasNext();) {
			ResourceType tr = it.next();
			if (!tr.getIName().equals("mps")) {
				it.remove();
			}
		}
		m.addAttribute("resourceTypes", resourceTypeList);
		
		//List <ResourceCategory> resourceCategoryList = new ArrayList <ResourceCategory> (resourceCategoryDao.findAll());
//		for (ResourceCategory rc : resourceCategoryList) {
//			if (rc.getIsActive().intValue() == 0 && !rc.getName().contains("_INACTIVE")) {
//				rc.setName(rc.getName() + "_INACTIVE");
//			}
//		}
		m.addAttribute("categoryResources", resourceCategoryDao.getActiveResourceCategories());
		
	}

	@RequestMapping("/list")
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String list(ModelMap m) {
		
		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));
		
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
			resourceList = sidx.isEmpty() ? resourceService.getResourceDao().findAll() : resourceService.getResourceDao().findAllOrderBy(sidx, sord);

			//resourceList = resourceService.getResourceDao().findAll();
		} else {
			Map<String, String> m = new HashMap<String, String>();

			m.put(request.getParameter("searchField"),
					request.getParameter("searchString"));

			resourceList = resourceService.getResourceDao().findByMap(m);

			if ("ne".equals(request.getParameter("searchOper"))) {
				//List<Resource> allResources = new ArrayList<Resource>(resourceService.getResourceDao().findAll());
				List<Resource> allResources = new ArrayList<Resource>(sidx.isEmpty() ? resourceService.getResourceDao().findAll() : resourceService.getResourceDao().findAllOrderBy(sidx, sord));

				for (Iterator<Resource> it = resourceList.iterator(); it
						.hasNext();) {
					Resource excludeResource = it.next();
					allResources.remove(excludeResource);
				}
				resourceList = allResources;
			}
		}

		try {
			
			Map<Integer, Integer> allResourceBarcode = new TreeMap<Integer, Integer>();
			for (ResourceBarcode resourceBarcode : this.resourceBarcodeDao.findAll()) {
				if (resourceBarcode != null) {
					
					allResourceBarcode.put(resourceBarcode.getResourceId(), resourceBarcode.getBarcodeId());
				}
			}
			
			Map<Integer, String> allBarcode = new TreeMap<Integer, String>();
			for (Barcode barcode : this.barcodeDao.findAll()) {
				if (barcode != null) {
					
					allBarcode.put(barcode.getId(), barcode.getBarcode());
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

			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			/* if the selId is set, change the page index to the one contains the selId */
			if(!StringUtils.isEmpty(request.getParameter("selId")))
			{
				int selId = Integer.parseInt(request.getParameter("selId"));
				int selIndex = resourceList.indexOf(resourceService.getResourceDao().findById(selId));
				frId = selIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}				

			List<Resource> resourcePage = resourceList.subList(frId, toId);
			for (Resource resource : resourcePage) {
				Map<String, Object> cell = new HashMap<String, Object>();
				cell.put("id", resource.getId());

				List<ResourceMeta> resourceMeta = getMetaHelperWebapp()
						.syncWithMaster(resource.getResourceMeta());

				List<String> cellList = new ArrayList<String>(
						Arrays.asList(new String[] {
								resource.getName(),
								resource.getResourceCategory().getName(),
								"",
								resource.getResourceType().getName(), 
								resource.getIsActive().intValue() == 1 ? "yes" : "no", //}));
								allResourceBarcode.get(resource.getId())==null? "" : allBarcode.get(allResourceBarcode.get(resource.getId()))}));

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
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String updateDetailJSON(@RequestParam("id") Integer resourceId,
			Resource resourceForm, ModelMap m, HttpServletResponse response) {

		List<ResourceMeta> resourceMetaList = getMetaHelperWebapp().getFromJsonForm(request, ResourceMeta.class);

		resourceForm.setResourceMeta(resourceMetaList);
		
		for (ResourceMeta rm : resourceMetaList) {
			logger.debug("Got resourceMeta: " + rm.getK() + ":" + rm.getV());
		}
		
		if (resourceId == null || resourceId.intValue() == 0) {
			
			//check if Resource Name already exists in db; if 'true', do not allow to proceed.
			if(resourceService.getResourceDao().getResourceByName(resourceForm.getName()).getName() != null) {
				logger.debug("obtained null or 0 resource from form and found not null resource by name");
				try{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println(messageService.getMessage("resource.resource_exists.error"));
					return null;
				} catch (Throwable e) {
					throw new IllegalStateException("Cant output validation error "+messageService.getMessage("resource.resource_exists.error"),e);
				}
				
			}
			
			//check if barcode already exists in Db; if 'true', do not allow to proceed.
			if(this.barcodeDao.getBarcodeByBarcode(request.getParameter("barcode")).getBarcode() != null && 
					this.barcodeDao.getBarcodeByBarcode(request.getParameter("barcode")).getBarcode().length() != 0) {
				try{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println(messageService.getMessage("resource.barcode_exists.error"));
					return null;
				} catch (Throwable e) {
					throw new IllegalStateException("Cant output validation error "+messageService.getMessage("resource.barcode_exists.error"),e);
				}
				
			}
			
			ResourceCategory resourceCategory = this.resourceCategoryDao.findById(new Integer(request.getParameter("resourceCategoryId")));

			resourceForm.setResourceCategory(resourceCategory);
			resourceForm.setResourceType(resourceCategory.getResourceType());
			
			resourceForm.setIName(resourceForm.getName());
			
			if(request.getParameter("resource.decommission_date") == null || request.getParameter("resource.decommission_date").length() == 0){
				resourceForm.setIsActive(1);
			}
			else {
				resourceForm.setIsActive(0);
			}
			
			ResourceBarcode resourceBarcode = new ResourceBarcode();
			Barcode barcode = new Barcode();
			
			barcode.setBarcode(request.getParameter("barcode")==null? "" : request.getParameter("barcode"));
			Barcode barcodeDB = this.barcodeDao.save(barcode);
			
			logger.debug("barcode id: " + barcodeDB.getId());
			
			resourceBarcode.setBarcode(barcodeDB);
			
			logger.debug("resource form id: " + resourceForm.getId());

			Resource resourceDb = resourceService.getResourceDao().save(resourceForm);
			
			resourceBarcode.setResource(resourceDb);
			this.resourceBarcodeDao.save(resourceBarcode);
			
			resourceId = resourceDb.getId();
			
		} else {
			
			Resource resourceDb = resourceService.getResourceDao().getById(resourceId);
			ResourceBarcode resourceBarcodeDB = this.resourceBarcodeDao.getResourceBarcodeByResourceId(resourceId);
			
			if(request.getParameter("resource.decommission_date") == null || request.getParameter("resource.decommission_date").length() == 0){
				resourceDb.setIsActive(1);
			}
			else {
				resourceDb.setIsActive(0);
			}
			
			resourceDb.setName(resourceForm.getName());
			//resourceDb.setResourcecategoryId(Integer.parseInt(request.getParameter("resourceCategoryId")));
			//resourceDb.setResourceTypeId(resourceForm.getResourceTypeId());
			resourceDb.setIName(resourceForm.getName());

			if (resourceBarcodeDB == null || resourceBarcodeDB.getBarcode() == null) {
				ResourceBarcode resourceBarcode = new ResourceBarcode();
				Barcode barcode = new Barcode();
				
				barcode.setBarcode(request.getParameter("barcode")==null? "" : request.getParameter("barcode"));
				barcode.setIsActive(1);
				resourceBarcode.setBarcode(barcode);
				
				Barcode barcodeDB = this.barcodeDao.save(barcode);
				resourceBarcode.setBarcodeId(barcodeDB.getId());
			
				resourceBarcode.setResourceId(resourceId);
				this.resourceBarcodeDao.save(resourceBarcode);
			} else {
				resourceBarcodeDB.getBarcode().setBarcode(request.getParameter("barcode")==null? "" : request.getParameter("barcode"));
				this.resourceBarcodeDao.merge(resourceBarcodeDB);
			}
			
			resourceService.getResourceDao().merge(resourceDb);
		}
		try {
			try{
				logger.debug("setting resource meta for " + resourceId);
				resourceMetaDao.setMeta(resourceMetaList, resourceId);
				response.getWriter().println(messageService.getMessage("resource.updated_success.label"));
				return null;
			} catch (MetadataException e){
				logger.warn(e.getLocalizedMessage());
				response.getWriter().println(messageService.getMessage("resource.updated_meta.error"));
				return null;
			}
			
		} catch (Throwable e1) {
			logger.warn(e1.getLocalizedMessage());
			throw new IllegalStateException("Cant output success message ", e1);
		}
	}


	@RequestMapping(value = "/subgridJSON.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String subgridJSON(@RequestParam("id") Integer resourceId,
			ModelMap m, HttpServletResponse response) {

		Map<String, Object> jqgrid = new HashMap<String, Object>();

		Resource resourceDb = resourceService.getResourceDao().getById(resourceId);

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

		try {

			jqgrid.put("page", "1");
			jqgrid.put("records", max + "");
			jqgrid.put("total", max + "");

			String text;

			int j = 0;
			for (Run run : runs) {
				text = run.getId() == null ? "No Runs"
						: "<a href=" + getServletPath() + "/" + sampleService.getPlatformunitViewLink(run.getPlatformUnit()) + ">"
								+ run.getName() + "</a>";
				mtrx[j] = text;

				j++;

			}

			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

			for (j = 0; j < max; j++) {

				Map <String, Object>cell = new HashMap<String, Object>();
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


}
