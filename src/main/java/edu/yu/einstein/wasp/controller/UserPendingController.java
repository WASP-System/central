package edu.yu.einstein.wasp.controller;

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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import edu.yu.einstein.wasp.controller.validator.MetaValidator;
import edu.yu.einstein.wasp.controller.validator.PasswordValidator;
import edu.yu.einstein.wasp.controller.validator.UserPendingMetaValidatorImpl;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.UserPendingMeta;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.UserPendingService;
import edu.yu.einstein.wasp.service.UserPendingMetaService;
import edu.yu.einstein.wasp.service.LabPendingService;
import edu.yu.einstein.wasp.service.LabPendingMetaService;

import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.PasswordService;

import edu.yu.einstein.wasp.model.MetaHelper;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


@Controller
@Transactional
@RequestMapping("/auth")
public class UserPendingController extends WaspController {

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
	private PasswordValidator passwordValidator;
	
	/**
	 *
	 */


	MetaHelper metaHelper = new MetaHelper("userPending", UserPending.class);

	@RequestMapping(value="/newuser", method=RequestMethod.GET)
	public String showNewPendingUserForm(ModelMap m) {
		metaHelper.setBundle(getBundle());

		UserPending userPending = new UserPending();

		userPending.setUserPendingMeta(metaHelper.getMasterList(UserPendingMeta.class)); 

		m.addAttribute(metaHelper.getParentArea(), userPending);
		prepareSelectListData(m);

		return "auth/newuser/form";

	}

	@RequestMapping(value="/newuser", method=RequestMethod.POST)
	public String createNewPendingUser (
			 @Valid UserPending userPendingForm, 
			 BindingResult result,
			 SessionStatus status, 
			 ModelMap m) {
		metaHelper.setBundle(getBundle());
		List<UserPendingMeta> userPendingMetaList = metaHelper.getFromRequest(request, UserPendingMeta.class);

		userPendingForm.setUserPendingMeta(userPendingMetaList);

		metaHelper.validate(new UserPendingMetaValidatorImpl(userService, labService), userPendingMetaList, result);
	
		// TODO USING STRING!
		passwordValidator.validate(result, userPendingForm.getPassword(), (String) request.getParameter("password2"), metaHelper.getParentArea(), "password");
		
		if (! result.hasFieldErrors("email")){
			Errors errors=new BindException(result.getTarget(), metaHelper.getParentArea());
			User user = userService.getUserByEmail(userPendingForm.getEmail());
			UserPending userPending = userPendingService.getUserPendingByEmail(userPendingForm.getEmail());
			if (user.getUserId() != 0 || userPending.getUserPendingId() != 0){
				errors.rejectValue("email", metaHelper.getParentArea()+".email_exists.error", metaHelper.getParentArea()+".email_exists.error (no message has been defined for this property)");
			}
			result.addAllErrors(errors);
		}
		
		if (result.hasErrors()) {
			prepareSelectListData(m);
			waspMessage("user.created.error");
			return "auth/newuser/form";
		}
		
		
		
		
		
		String piUserEmail = "";
		
		for (UserPendingMeta meta : userPendingMetaList) {
			if (meta.getK().equals(metaHelper.getArea() + ".primaryuseremail") ) {
				piUserEmail = meta.getV();
				break;
			}
		} 
		User primaryInvestigator = userService.getUserByEmail(piUserEmail);
		Lab lab = labService.getLabByPrimaryUserId(primaryInvestigator.getUserId());


		userPendingForm.setLabId(lab.getLabId());
		userPendingForm.setStatus("PENDING");
		

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
		metaHelper.setArea("piPending"); 
		metaHelper.setBundle(getBundle());

		UserPending userPending = new UserPending();

		userPending.setUserPendingMeta(metaHelper.getMasterList(UserPendingMeta.class));

		m.addAttribute(metaHelper.getParentArea(), userPending);
		prepareSelectListData(m);

		return "auth/newpi/form";

	}

	@RequestMapping(value="/newpi", method=RequestMethod.POST)
	public String createNewPendingPi (
			 @Valid UserPending userPendingForm, 
			 BindingResult result,
			 SessionStatus status, 
			 ModelMap m) {
		metaHelper.setBundle(getBundle());
		metaHelper.setArea("piPending");
		
		List<UserPendingMeta> userPendingMetaList = metaHelper.getFromRequest(request, UserPendingMeta.class);
		userPendingForm.setUserPendingMeta(userPendingMetaList);

		metaHelper.validate(userPendingMetaList, result);

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
		labPendingForm.setUserPendingId(userPendingDb.getUserPendingId());

		// TODO DEPARTMENT ID
		labPendingForm.setDepartmentId(1);
		for (UserPendingMeta meta : userPendingMetaList) {
			if (meta.getK().equals(metaHelper.getArea() + ".labName")) {
				labPendingForm.setName(meta.getV());
				continue;
			}
			if (meta.getK().equals(metaHelper.getArea() + ".departmentId")) {
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
