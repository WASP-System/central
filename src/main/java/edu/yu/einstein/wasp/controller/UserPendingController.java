package edu.yu.einstein.wasp.controller;

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
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.UserPendingMeta;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabPendingMeta;

import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.UserPendingService;
import edu.yu.einstein.wasp.service.UserPendingMetaService;
import edu.yu.einstein.wasp.service.LabPendingService;
import edu.yu.einstein.wasp.service.LabPendingMetaService;

import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.PasswordService;

import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaAttribute.Country;
import edu.yu.einstein.wasp.model.MetaAttribute.State;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.MetaUtil;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.NullUserCache;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


@Controller
@Transactional
@RequestMapping("/auth")
public class UserPendingController extends WaspController {

  private static final ResourceBundle BASE_BUNDLE=ResourceBundle.getBundle("messages", Locale.ENGLISH);

  @Autowired
  private UserPendingService userPendingService;

  @Autowired
  private UserPendingMetaService userPendingMetaService;

  @Autowired
  private LabService labService;

  @Autowired
  private LabPendingService labPendingService;

  @Autowired
  private LabPendingMetaService labPendingMetaService;

  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private PasswordService passwordService;

  @Autowired
  private BeanValidator validator;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }

  /**
   *
   */

  @RequestMapping(value="/newuser", method=RequestMethod.GET)
  public String showNewPendingUserForm(ModelMap m) {
    final MetaAttribute.Area AREA = MetaAttribute.Area.userPending;
    UserPending userPending = new UserPending();

    userPending.setUserPendingMeta(MetaUtil.getMasterList(UserPendingMeta.class, AREA, getBundle()));

    m.addAttribute(AREA.name(), userPending);
    prepareSelectListData(m);

    return "auth/newuser/form";

  }

  @RequestMapping(value="/newuser", method=RequestMethod.POST)
  public String createNewPendingUser (
       @Valid UserPending userPendingForm, 
       BindingResult result,
       SessionStatus status, 
       ModelMap m) {
    final MetaAttribute.Area AREA = MetaAttribute.Area.userPending;
    List<UserPendingMeta> userPendingMetaList = MetaUtil.getMetaFromForm(request,
                                AREA, UserPendingMeta.class, getBundle());

    userPendingForm.setUserPendingMeta(userPendingMetaList);
    
    List<String> validateList = new ArrayList<String>();

    for (UserPendingMeta meta : userPendingMetaList) {
      if (meta.getProperty() != null
          && meta.getProperty().getConstraint() != null) {
        validateList.add(meta.getK());
        validateList.add(meta.getProperty().getConstraint());
      }
    }
    MetaValidator validator = new MetaValidator(
        validateList.toArray(new String[] {}));

    validator.validate(userPendingMetaList, result, AREA);
    Errors errors = new BindException(result.getTarget(), AREA.name());
    
    // validate password
    PasswordValidator passwordValidator = new PasswordValidator();
    passwordValidator.validate(result, userPendingForm.getPassword(), (String) request.getParameter("password2"), AREA, "password");
    
    String primaryUserEmail = "";
    for (UserPendingMeta meta : userPendingMetaList) {
      if (meta.getK().equals(AREA.name() + ".primaryuseremail")) {
        primaryUserEmail = meta.getV();
        break;
      }
    } 
    User primaryInvestigator = userService.getUserByEmail(primaryUserEmail);
    result.addAllErrors(errors);
    
    

    // TODO add  user not found add lab not found
    
    Lab lab = labService.getLabByPrimaryUserId(primaryInvestigator.getUserId());

    userPendingForm.setLabId(lab.getLabId());
    userPendingForm.setStatus("PENDING");

    if (result.hasErrors()) {
      prepareSelectListData(m);
      waspMessage("user.created.error");

      return "auth/newuser/form";
    }

    userPendingForm.setPassword( passwordService.encodePassword(userPendingForm.getPassword()) ); 


    UserPending userPendingDb = userPendingService.save(userPendingForm);

    for (UserPendingMeta upm : userPendingMetaList) {
      upm.setUserpendingId(userPendingDb.getUserPendingId());
      userPendingMetaService.save(upm);
    }

    status.setComplete();

    // TODO email PI/LM that a new user is pending

    waspMessage("hello.error");
    return "redirect:/auth/newuser/ok.do";
  }

  @RequestMapping(value="/newpi", method=RequestMethod.GET)
  public String showNewPendingPiForm(ModelMap m) {
    final MetaAttribute.Area AREA = MetaAttribute.Area.piPending;
    final MetaAttribute.Area PARENTAREA = MetaAttribute.Area.userPending;

    UserPending userPending = new UserPending();

    userPending.setUserPendingMeta(MetaUtil.getMasterList(UserPendingMeta.class, AREA, getBundle()));

    m.addAttribute(PARENTAREA.name(), userPending);
    prepareSelectListData(m);

    return "auth/newpi/form";

  }

  @RequestMapping(value="/newpi", method=RequestMethod.POST)
  public String createNewPendingPi (
       @Valid UserPending userPendingForm, 
       BindingResult result,
       SessionStatus status, 
       ModelMap m) {
    final MetaAttribute.Area AREA = MetaAttribute.Area.piPending;
    final MetaAttribute.Area PARENTAREA = MetaAttribute.Area.userPending;


    List<UserPendingMeta> userPendingMetaList = MetaUtil.getMetaFromForm(request,
                                AREA, PARENTAREA, UserPendingMeta.class, getBundle());

    userPendingForm.setUserPendingMeta(userPendingMetaList);

    Errors errors = new BindException(result.getTarget(), "userPending");

    //if (userPendingForm.getPassword() == null || userPendingForm.getPassword().isEmpty()) {
    //  errors.rejectValue("password", "userPending.password.error");
    //}

    result.addAllErrors(errors);

    List<String> validateList = new ArrayList<String>();
    //validateList.add("password");
    //validateList.add(MetaValidator.Constraint.NotEmpty.name());

    // ADD VALIDATION 
    //   - uniq name 
    //   - departmentid

    for (UserPendingMeta meta : userPendingMetaList) {
      if (meta.getProperty() != null
          && meta.getProperty().getConstraint() != null) {
        validateList.add(meta.getK());
        validateList.add(meta.getProperty().getConstraint());
      }
    }

    MetaValidator validator = new MetaValidator(
        validateList.toArray(new String[] {}));

    validator.validate(userPendingMetaList, result, PARENTAREA);

    if (result.hasErrors()) {
      prepareSelectListData(m);
      waspMessage("user.created.error");

      return "auth/newpi/form";
    }

    userPendingForm.setStatus("PENDING");

    userPendingForm.setPassword( passwordService.encodePassword(userPendingForm.getPassword()) ); 

    UserPending userPendingDb = userPendingService.save(userPendingForm);

    for (UserPendingMeta upm : userPendingMetaList) {
      upm.setUserpendingId(userPendingDb.getUserPendingId());
    }
    userPendingMetaService.updateByUserpendingId(userPendingDb.getUserPendingId(), userPendingMetaList);

    // insert into labpending table
    LabPending labPendingForm = new LabPending();
    labPendingForm.setStatus("PENDING");
    labPendingForm.setUserpendingId(userPendingDb.getUserPendingId());
    labPendingForm.setDepartmentId(1);
    for (UserPendingMeta meta : userPendingMetaList) {
      if (meta.getK().equals(AREA.name() + ".labName")) {
        labPendingForm.setName(meta.getV());
        continue;
      }
      if (meta.getK().equals(AREA.name() + ".departmentId")) {
        try{
          labPendingForm.setDepartmentId(Integer.parseInt(meta.getV()));
        } catch (Exception e) {
        }
        continue;
      }
    } 
    LabPending labPendingDb = labPendingService.save(labPendingForm);

    status.setComplete();

    // TODO email DA that a new pi is pending

    waspMessage("hello.error");
    return "redirect:/auth/newpi/ok.do";
  }


}
