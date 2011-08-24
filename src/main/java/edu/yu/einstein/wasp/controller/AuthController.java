package edu.yu.einstein.wasp.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
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

import org.springframework.security.access.prepost.*;

import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.Userpasswordauth;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.UserPendingMeta;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabPendingMeta;
import edu.yu.einstein.wasp.model.UserMeta;

import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.UserpasswordauthService;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.UserPendingService;
import edu.yu.einstein.wasp.service.UserPendingMetaService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.LabPendingService;
import edu.yu.einstein.wasp.service.LabPendingMetaService;
import edu.yu.einstein.wasp.service.PasswordEncoderService;

import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaAttribute.Country;
import edu.yu.einstein.wasp.model.MetaAttribute.State;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.MetaUtil;

import edu.yu.einstein.wasp.taglib.MessageTag;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


@Controller
@Transactional
@RequestMapping("/auth")
public class AuthController extends WaspController {

  private static final ResourceBundle BASE_BUNDLE=ResourceBundle.getBundle("messages", Locale.ENGLISH);

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserpasswordauthService userpasswordauthService;

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
  private PasswordEncoderService passwordEncoderService;

  @Autowired
  private BeanValidator validator;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }

  private String _createAuthcode(){
	  final int CODE_LENGTH = 20;
	  String authcode = new String();
	  Random random = new Random();
	  for (int i=0; i < CODE_LENGTH; i++){
		  int ascii = 0;
		  switch(random.nextInt(3)){
		  	case 0:
		  		ascii = 48 + random.nextInt(10); // 0-9
		  		break;
		  	case 1:
		  		ascii = 65 + random.nextInt(26); // A-Z 
		  		break;
		  	case 2:
		  		ascii = 97 + random.nextInt(26); // a-z
		  		break;	
		  }
		  authcode = authcode.concat(String.valueOf( (char)ascii ));  
	  }
	  return authcode;
  }
  
  @RequestMapping(value="/forgotpassword", method=RequestMethod.GET)
  public String showForgotPasswordForm(ModelMap m) {
    return "auth/forgotpassword/form";
  }

  @RequestMapping(value="/forgotpassword", method=RequestMethod.POST)
  public String forgotPassword(@RequestParam("j_username") String username, ModelMap m) {

	  User user=userService.getUserByLogin(username);
	  
	  if (user==null || user.getUserId()==0)  {
		  MessageTag.addMessage(request.getSession(), "auth.forgotpassword.error.username");
		  return "auth/forgotpassword/form";
	  }

	String authcode = this._createAuthcode();
	logger.debug("ANDY: Setting authcode: ".concat(authcode));
	Userpasswordauth existingUserpasswordauth = userpasswordauthService.getUserpasswordauthByUserId(user.getUserId());
	Userpasswordauth userpasswordauth;
	if (existingUserpasswordauth.getUserId() != 0) {
		logger.debug("ANDY: modifying existing Userpasswordauth");
		userpasswordauth = existingUserpasswordauth; // old entry exists for current user so update 
	}
	else{
		logger.debug("ANDY: creating new Userpasswordauth");
		userpasswordauth = new Userpasswordauth(); // no existing entry so create a new one
		userpasswordauth.setUserId(user.getUserId());
	}
	userpasswordauth.setAuthcode(authcode);
	userpasswordauthService.merge(userpasswordauth);
	emailService.sendForgotPassword(user, authcode);
	  
    return "auth/forgotpassword/email";
  }

  @RequestMapping(value="/recoverpassword", method=RequestMethod.GET)
  public String showRecoverPasswordForm(
        @RequestParam(required = false, value="authcode") String authCode, 
        ModelMap m) {
    if (authCode == null || "".equals(authCode)) {
      return "auth/recoverpassword/authcodeform";
    }

    m.put("authcode", authCode);
    return "auth/recoverpassword/form";
  }
  
  @RequestMapping(value="/recoverpassword", method=RequestMethod.POST)
  public String resetPassword(
        @RequestParam("j_username") String username, 
        @RequestParam("authcode") String authCode, 
        @RequestParam("password1") String password1, 
        @RequestParam("password2") String password2, 
        ModelMap m) {
    if (authCode == null || "".equals(authCode)) {
      return "auth/recoverpassword/authcodeform";
    }

// TODO real error
    if (username.equals("")) {
       MessageTag.addMessage(request.getSession(), "hello.error");
       m.put("authcode", authCode);
       return "auth/recoverpassword/form";
    }
    if (password1.equals("") || password2.equals("") || ! password1.equals(password2)) {
       MessageTag.addMessage(request.getSession(), "hello.error");
       m.put("authcode", authCode);
       return "auth/recoverpassword/form";
    }

    Userpasswordauth userpasswordauth   = userpasswordauthService.getUserpasswordauthByAuthcode(authCode);
    User user = userService.getUserByLogin(username);

    if (userpasswordauth.getUserId() == 0) {
       MessageTag.addMessage(request.getSession(), "auth.recoverpassword.error.badauthcode");
       m.put("authcode", authCode);
       return "auth/recoverpassword/form";
    }

    // cannot find user
    if (user.getUserId() == 0) {
       m.put("authcode", authCode);

       MessageTag.addMessage(request.getSession(), "hello.error");
       return "auth/recoverpassword/form";
    }

    if (user.getUserId() != userpasswordauth.getUserId()) {
      m.put("authcode", authCode);

       MessageTag.addMessage(request.getSession(), "auth.recoverpassword.error.wronguser");
       return "auth/recoverpassword/form";
    }

    user.setPassword( passwordEncoderService.encodePassword(password1) ); 
    userService.merge(user);

    // removes auth code from future use
    userpasswordauthService.remove(userpasswordauth);

    m.put("user", user); 

    return "auth/recoverpassword/ok";
  }




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

    Errors errors = new BindException(result.getTarget(), AREA.name());

    //if (userPendingForm.getPassword() == null || userPendingForm.getPassword().isEmpty()) {
    //  errors.rejectValue("password", "userPending.password.error");
    //}

    result.addAllErrors(errors);

    List<String> validateList = new ArrayList<String>();
    //validateList.add("password");
    //validateList.add(MetaValidator.Constraint.NotEmpty.name());

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

    String primaryUserEmail = "";
    for (UserPendingMeta meta : userPendingMetaList) {
      if (meta.getK().equals(AREA.name() + ".primaryuseremail")) {
        primaryUserEmail = meta.getV();
        break;
      }
    } 

    // TODO add  user not found add lab not found
    User primaryUser = userService.getUserByEmail(primaryUserEmail);
    Lab lab = labService.getLabByPrimaryUserId(primaryUser.getUserId());

    userPendingForm.setLabId(lab.getLabId());
    userPendingForm.setStatus("PENDING");

    if (result.hasErrors()) {
      prepareSelectListData(m);
      MessageTag.addMessage(request.getSession(), "user.created.error");

      return "auth/newuser/form";
    }

    userPendingForm.setPassword( passwordEncoderService.encodePassword(userPendingForm.getPassword()) ); 


    UserPending userPendingDb = userPendingService.save(userPendingForm);

    for (UserPendingMeta upm : userPendingMetaList) {
      upm.setUserpendingId(userPendingDb.getUserPendingId());
    }
    userPendingMetaService.updateByUserpendingId(userPendingDb.getUserPendingId(), userPendingMetaList);


    status.setComplete();

    // TODO email PI/LM that a new user is pending

    MessageTag.addMessage(request.getSession(), "hello.error");
    return "auth/newuser/ok";
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
      MessageTag.addMessage(request.getSession(), "user.created.error");

      return "auth/newpi/form";
    }

    userPendingForm.setStatus("PENDING");

    userPendingForm.setPassword( passwordEncoderService.encodePassword(userPendingForm.getPassword()) ); 

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

    MessageTag.addMessage(request.getSession(), "hello.error");
    return "redirect:/auth/newpi/ok.do";
  }

  // *********************************************************

  @RequestMapping("/reauth")
  public String reauth(ModelMap m) {

    SecurityContext securityContext= SecurityContextHolder.getContext();
    Authentication currentUser = securityContext.getAuthentication();
    UserDetails currentUserDetails = (UserDetails) currentUser.getPrincipal();

    UserDetails u = userDetailsService.loadUserByUsername(currentUserDetails.getUsername());

    UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword());

    SecurityContextHolder.getContext().setAuthentication(newToken);

    return "redirect:/dashboard.do";
  }

}
