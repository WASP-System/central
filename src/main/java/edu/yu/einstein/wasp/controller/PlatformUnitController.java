package edu.yu.einstein.wasp.controller;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.support.SessionStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import org.springframework.security.access.prepost.*;

import edu.yu.einstein.wasp.controller.validator.MetaValidator;
import edu.yu.einstein.wasp.controller.validator.PasswordValidator;

import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.TypeSample;

import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.MetaUtil;

import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.SampleMetaService;
import edu.yu.einstein.wasp.service.TypeSampleService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.NullUserCache;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


@Controller
@Transactional
@RequestMapping("/facility/platformunit")
public class PlatformUnitController extends WaspController {

  private static final ResourceBundle BASE_BUNDLE=ResourceBundle.getBundle("messages", Locale.ENGLISH);

  @Autowired
  private SampleService sampleService;

  @Autowired
  private SampleMetaService sampleMetaService;

  @Autowired
  private TypeSampleService typeSampleService;

  @Autowired
  private BeanValidator validator;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }




  @RequestMapping(value="/create.do", method=RequestMethod.GET)
  @PreAuthorize("hasRole('ft')")
  public String showCreateForm(ModelMap m) {
    Sample sample = new Sample();

    final MetaAttribute.Area AREA = MetaAttribute.Area.platformunit;
    sample.setSampleMeta(MetaUtil.getMasterList(SampleMeta.class, AREA, getBundle()));

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

    return updatePlatformUnit(sampleForm, result, status, m); 
  }

  public String updatePlatformUnit(
    @Valid Sample sampleForm,
    BindingResult result,
    SessionStatus status,
    ModelMap m) {
    final MetaAttribute.Area AREA = MetaAttribute.Area.platformunit;
    final MetaAttribute.Area PARENTAREA = MetaAttribute.Area.sample;

    List<SampleMeta> sampleMetaList = MetaUtil.getMetaFromForm(request,
                                AREA, PARENTAREA, SampleMeta.class, getBundle()); 
    List<String> validateList = new ArrayList<String>();

    for (SampleMeta meta : sampleMetaList) {
      if (meta.getProperty() != null
          && meta.getProperty().getConstraint() != null) {
        validateList.add(meta.getK());
        validateList.add(meta.getProperty().getConstraint());
      }
    }
    MetaValidator validator = new MetaValidator(
        validateList.toArray(new String[] {}));

    validator.validate(sampleMetaList, result, PARENTAREA);

    if (result.hasErrors()) {
      // TODO REAL ERROR
      waspMessage("hello.error");

      sampleForm.setSampleMeta(sampleMetaList);
      m.put("sample", sampleForm); 

      return "facility/platformunit/detail_rw";
    }


    // TODO, drop constraint on labid.
    sampleForm.setSubmitterLabId(1);

    TypeSample typeSample = typeSampleService.getTypeSampleByIName("platformunit");
    sampleForm.setTypeSampleId(typeSample.getTypeSampleId());

    sampleForm.setReceiverUserId(sampleForm.getSubmitterUserId());
    sampleForm.setReceiveDts(new Date());
    sampleForm.setIsReceived(1);
    sampleForm.setIsActive(1);

    Sample sampleDb = sampleService.save(sampleForm);

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

  @RequestMapping(value="/modify/{sampleId}.do", method=RequestMethod.GET)
  @PreAuthorize("hasRole('ft')")
  public String updatePlatformUnit(
       @PathVariable("sampleId") Integer sampleId,  
       ModelMap m) {
    Sample sample = sampleService.getSampleBySampleId(sampleId);

    final MetaAttribute.Area AREA = MetaAttribute.Area.platformunit;

    sample.setSampleMeta(MetaUtil.syncWithMaster(sample.getSampleMeta(), AREA, SampleMeta.class));
    MetaUtil.setAttributesAndSort(sample.getSampleMeta(), AREA,getBundle());

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
    sampleDb.setName(sampleForm.getName()); 
     
    return updatePlatformUnit(sampleDb, result, status, m); 
  }
}
