package edu.yu.einstein.wasp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.transaction.annotation.*;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.ResourceMetaService;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.TypeResourceService;
import edu.yu.einstein.wasp.model.*;

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

	protected void prepareSelectListData(ModelMap m) {
		m.addAttribute("typeResources", typeResourceService.findAll());
	}

	@RequestMapping("/list")
	public String list(ModelMap m) {
		List<Resource> resourceList = resourceService.findAll();

		m.addAttribute("resource", resourceList);

		return "resource/list";
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
		return validateAndUpdateResource(resourceId.intValue(), resourceForm, result,
				status, m);
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
//		resourceForm.setLastUpdTs(new Date());
//		resourceForm.setLastUpdUser(getAuthenticatedUser().getUserId());

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

		waspMessage(newResource ? "resource.created_success.label" : "resource.updated_success.label");

		return "redirect:/resource/detail_ro/" + resourceId + ".do";
	}

}
