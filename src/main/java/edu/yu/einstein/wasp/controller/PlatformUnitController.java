package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.model.MetaHelper;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.TypeSample;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.SampleMetaService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.TypeSampleService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;

@Controller
@Transactional
@RequestMapping("/facility/platformunit")
public class PlatformUnitController extends WaspController {

	@Autowired
	private SampleService sampleService;

	@Autowired
	private SampleMetaService sampleMetaService;

	@Autowired
	private TypeSampleService typeSampleService;
	
	private final MetaHelper getMetaHelper() {
		return new MetaHelper("platformunit",  "sample",SampleMeta.class, request.getSession());
	}

	@RequestMapping(value="/list.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String showListShell(ModelMap m) {
		m.addAttribute("_metaList", getMetaHelper().getMasterList(SampleMeta.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, "platformunit");

		return "facility/platformunit/list";
	}

	@RequestMapping(value="/listJson.do", method=RequestMethod.GET)
	public @ResponseBody String getListJson() {

		Map <String, Object> jqgrid = new HashMap<String, Object>();

		List<Sample> sampleList;

		Map sampleListBaseQueryMap = new HashMap();
		sampleListBaseQueryMap.put("typeSampleId", 5);

		if (request.getParameter("_search")==null || request.getParameter("_search").equals("false") || StringUtils.isEmpty(request.getParameter("searchString"))) {
			sampleList = sampleService.findByMap(sampleListBaseQueryMap);

		} else {

			sampleListBaseQueryMap.put(request.getParameter("searchField"), request.getParameter("searchString"));
	
			sampleList = this.sampleService.findByMap(sampleListBaseQueryMap);


			if ("ne".equals(request.getParameter("searchOper"))) {
				Map allSampleListBaseQueryMap = new HashMap();
				allSampleListBaseQueryMap.put("typeSampleId", 5);

				List<Sample> allSampleList = sampleService.findByMap(allSampleListBaseQueryMap);
				for (Sample excludeSample : allSampleList) {
					allSampleList.remove(excludeSample);
				}
				sampleList=allSampleList;
			}

		}

		ObjectMapper mapper = new ObjectMapper();

		jqgrid.put("page","1");
		jqgrid.put("records", sampleList.size()+"");
		jqgrid.put("total", sampleList.size()+"");

			Map<String, String> sampleData=new HashMap<String, String>();

			sampleData.put("page","1");
			sampleData.put("selId",StringUtils.isEmpty(request.getParameter("selId"))?"":request.getParameter("selId"));
			jqgrid.put("sampledata",sampleData);
			

			List<Map> rows = new ArrayList<Map>();

			for (Sample sample: sampleList) {
				Map cell = new HashMap();
				cell.put("id", sample.getSampleId());

				List<SampleMeta> sampleMetaList=getMetaHelper().syncWithMaster(sample.getSampleMeta());
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
					sample.getName(),
				}));

				for(SampleMeta meta:sampleMetaList) {
					cellList.add(meta.getV());
				}

				cell.put("cell", cellList);

				rows.add(cell);
			}

			jqgrid.put("rows",rows);

		try {
			String json=mapper.writeValueAsString(jqgrid);
			return json;


		} catch (Exception e) {
			throw new IllegalStateException("Can't marshall to JSON "+sampleList,e);

		}
	}

	@RequestMapping(value="/updateJson.do", method=RequestMethod.POST)
	public String updateJson(
			@RequestParam("id") Integer sampleId,
			@Valid Sample sampleForm, 
			ModelMap m, 
			HttpServletResponse response) {

		List<SampleMeta> sampleMetaList = getMetaHelper().getFromJsonForm(request, SampleMeta.class);
		sampleForm.setSampleMeta(sampleMetaList);
		sampleForm.setSampleId(sampleId);

		preparePlatformUnit(sampleForm);
		updatePlatformUnit(sampleForm);

		try {
			response.getWriter().println(getMessage("hello.error"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ",e);
		}

	}




	@RequestMapping(value="/create.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String showCreateForm(ModelMap m) {

		Sample sample = new Sample();

		sample.setSampleMeta(getMetaHelper().getMasterList(SampleMeta.class));

		m.put("sample", sample);

		return "facility/platformunit/detail_rw";
	}

	@RequestMapping(value="/create.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ft')")
	public String createPlatformUnit(
		@Valid Sample sampleForm,
		BindingResult result,
		SessionStatus status,
		ModelMap m) {

		preparePlatformUnit(sampleForm);

		return validateAndUpdatePlatformUnit(sampleForm, result, status, m);
	}

	@RequestMapping(value="/view/{sampleId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String viewPlatformUnit(
			 @PathVariable("sampleId") Integer sampleId,
			 ModelMap m) {
		Sample sample = sampleService.getSampleBySampleId(sampleId);

		sample.setSampleMeta(getMetaHelper().syncWithMaster(sample.getSampleMeta()));

		m.put("sample", sample);

		return "facility/platformunit/detail_ro";
	}

	@RequestMapping(value="/modify/{sampleId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String updatePlatformUnitForm(
			 @PathVariable("sampleId") Integer sampleId,
			 ModelMap m) {
		Sample sample = sampleService.getSampleBySampleId(sampleId);

		sample.setSampleMeta(getMetaHelper().syncWithMaster(sample.getSampleMeta()));

		m.put("sample", sample);

		return "facility/platformunit/detail_rw";
	}

	@RequestMapping(value="/modify/{sampleId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ft')")
	public String modifyPlatformUnit(
		@PathVariable("sampleId") Integer sampleId,
		@Valid Sample sampleForm,
		BindingResult result,
		SessionStatus status,
		ModelMap m) {

		preparePlatformUnit(sampleForm);

		return validateAndUpdatePlatformUnit(sampleForm, result, status, m);
	}

	/* ****************************** */

	public Sample preparePlatformUnit(Sample sampleForm) {
		if (sampleForm.getSampleId() == 0) {
			User me = getAuthenticatedUser();
			sampleForm.setSubmitterUserId(me.getUserId());

			TypeSample typeSample = typeSampleService.getTypeSampleByIName("platformunit");
			sampleForm.setTypeSampleId(typeSample.getTypeSampleId());

			sampleForm.setSubmitterLabId(1);
	
			sampleForm.setReceiverUserId(sampleForm.getSubmitterUserId());
			sampleForm.setReceiveDts(new Date());
			sampleForm.setIsReceived(1);
			sampleForm.setIsActive(1);
		} else {
			Sample sampleDb =	sampleService.getSampleBySampleId(sampleForm.getSampleId());

			// TODO do compares that i am the same sample as sampleform, and not new
	
			// fetches the updates
			// sampleDb.setName(sampleForm.getName());
	
	
			sampleForm.setSubmitterUserId(sampleDb.getSubmitterUserId());
			sampleForm.setSubmitterLabId(sampleDb.getSubmitterLabId());
			sampleForm.setTypeSampleId(sampleDb.getTypeSampleId());
	
			sampleForm.setReceiverUserId(sampleDb.getReceiverUserId());
			sampleForm.setReceiveDts(sampleDb.getReceiveDts());
			sampleForm.setIsReceived(sampleDb.getIsReceived());
			sampleForm.setIsActive(sampleDb.getIsActive());
		}
		return sampleForm;
	}

	public String validateAndUpdatePlatformUnit(
		Sample sampleForm,
		BindingResult result,
		SessionStatus status,
		ModelMap m) {

		List<SampleMeta> sampleMetaList = getMetaHelper().getFromRequest(request, SampleMeta.class);

		getMetaHelper().validate(sampleMetaList, result);

		if (result.hasErrors()) {
			// TODO REAL ERROR
			waspMessage("hello.error");

			sampleForm.setSampleMeta(sampleMetaList);
			m.put("sample", sampleForm);

			return "facility/platformunit/detail_rw";
		}

		sampleForm.setSampleMeta(sampleMetaList);

		String returnString = updatePlatformUnit(sampleForm);


		return returnString;
	}


	// TODO move to service?
	public String updatePlatformUnit( Sample sampleForm ) {
	
		Sample sampleDb;
		if (sampleForm.getSampleId() == 0) {
			sampleDb = sampleService.save(sampleForm);
		} else {
			sampleDb = sampleService.merge(sampleForm);
		}

		sampleMetaService.updateBySampleId(sampleDb.getSampleId(), sampleForm.getSampleMeta());

		/// TODO depending on how many *.resourceId is set, create sample lanes
		/// query resourceServices.getLaneCount(resourceId)

		/// TODO depending on how many *.lanecount is set, create sample lanes
		// int xxx = somefunction(getLaneCount());
		// for (int i = 0; i < xxxx; i++) {
		//	 Sample lane = new Sample()
		// }


		//	return "facility/platformunit/ok";
		return "redirect:/facility/platformunit/ok";
	}

}
