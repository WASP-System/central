package edu.yu.einstein.wasp.controller;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.support.SessionStatus;
import org.springframework.validation.BindingResult;

import org.springframework.security.access.prepost.*;

import edu.yu.einstein.wasp.model.MetaHelper;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.TypeSample;

import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.SampleMetaService;
import edu.yu.einstein.wasp.service.TypeSampleService;

import edu.yu.einstein.wasp.taglib.JQFieldTag;

import org.springframework.transaction.annotation.Transactional;

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
  
  MetaHelper metaHelper = new MetaHelper("platformunit", "sample", SampleMeta.class);

  @RequestMapping(value="/list.do", method=RequestMethod.GET)
  @PreAuthorize("hasRole('ft')")
  public String showList(ModelMap m) {

    /*
    final MetaAttribute.Area AREA = MetaAttribute.Area.platformunit;


    m.addAttribute("_metaList", MetaUtil.getMasterList(SampleMeta.class, AREA, getBundle()));
    m.addAttribute(JQFieldTag.AREA_ATTR, "platformunit");

    // prepareSelectListData(m);
    */

    return "facility/platformunit/list";
  }

  @RequestMapping(value="/create.do", method=RequestMethod.GET)
  @PreAuthorize("hasRole('ft')")
  public String showCreateForm(ModelMap m) {

    Sample sample = new Sample();

    sample.setSampleMeta(metaHelper.getMasterList(SampleMeta.class));

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

    User me = getAuthenticatedUser();
    sampleForm.setSubmitterUserId(me.getUserId());

    // TODO, drop constraint on labid.
    sampleForm.setSubmitterLabId(1);

    TypeSample typeSample = typeSampleService.getTypeSampleByIName("platformunit");
    sampleForm.setTypeSampleId(typeSample.getTypeSampleId());

    sampleForm.setReceiverUserId(sampleForm.getSubmitterUserId());
    sampleForm.setReceiveDts(new Date());
    sampleForm.setIsReceived(1);
    sampleForm.setIsActive(1);

    return updatePlatformUnit(sampleForm, result, status, m);
  }

  @RequestMapping(value="/modify/{sampleId}.do", method=RequestMethod.GET)
  @PreAuthorize("hasRole('ft')")
  public String updatePlatformUnit(
       @PathVariable("sampleId") Integer sampleId,
       ModelMap m) {
    Sample sample = sampleService.getSampleBySampleId(sampleId);

    sample.setSampleMeta(metaHelper.syncWithMaster(sample.getSampleMeta()));

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

    Sample sampleDb =  sampleService.getSampleBySampleId(sampleId);

    // TODO do compares that i am the same sample as sampleform, and not new

    // fetches the updates
    // sampleDb.setName(sampleForm.getName());

    sampleForm.setSampleId(sampleId);

    sampleForm.setSubmitterUserId(sampleDb.getSubmitterUserId());
    sampleForm.setSubmitterLabId(sampleDb.getSubmitterLabId());
    sampleForm.setTypeSampleId(sampleDb.getTypeSampleId());

    sampleForm.setReceiverUserId(sampleDb.getReceiverUserId());
    sampleForm.setReceiveDts(sampleDb.getReceiveDts());
    sampleForm.setIsReceived(sampleDb.getIsReceived());
    sampleForm.setIsActive(sampleDb.getIsActive());

    return updatePlatformUnit(sampleForm, result, status, m);
  }

  /* ****************************** */

  public String updatePlatformUnit(
    Sample sampleForm,
    BindingResult result,
    SessionStatus status,
    ModelMap m) {

    List<SampleMeta> sampleMetaList = metaHelper.getFromRequest(request, SampleMeta.class);

    metaHelper.validate(sampleMetaList, result);

    if (result.hasErrors()) {
      // TODO REAL ERROR
      waspMessage("hello.error");

      sampleForm.setSampleMeta(sampleMetaList);
      m.put("sample", sampleForm);

      return "facility/platformunit/detail_rw";
    }


    Sample sampleDb;
    if (sampleForm.getSampleId() == 0) {
      sampleDb = sampleService.save(sampleForm);
    } else {
      sampleDb = sampleService.merge(sampleForm);
    }

    sampleMetaService.updateBySampleId(sampleDb.getSampleId(), sampleMetaList);

    /// TODO depending on how many *.resourceId is set, create sample lanes
    /// query resourceServices.getLaneCount(resourceId)

    /// TODO depending on how many *.lanecount is set, create sample lanes
    // int xxx = somefunction(getLaneCount());
    // for (int i = 0; i < xxxx; i++) {
    //   Sample lane = new Sample()
    // }


    //  return "facility/platformunit/ok";
    return "redirect:/facility/platformunit/ok";
  }

}
