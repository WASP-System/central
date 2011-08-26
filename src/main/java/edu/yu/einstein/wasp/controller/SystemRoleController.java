package edu.yu.einstein.wasp.controller;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.security.core.Authentication;

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
import edu.yu.einstein.wasp.model.Userrole;
import edu.yu.einstein.wasp.model.Role;

import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.UserroleService;

import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.PasswordService;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


@Controller
@Transactional
@RequestMapping("/sysrole")
public class SystemRoleController extends WaspController {

	private static final ResourceBundle BASE_BUNDLE=ResourceBundle.getBundle("messages", Locale.ENGLISH);

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserroleService userroleService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private BeanValidator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}


	@RequestMapping(value="/list", method=RequestMethod.GET)
  @PreAuthorize("hasRole('god')")
	public String listSystemUser(ModelMap m) {
		List<Userrole> userroleList = userroleService.findAll();

		List<User> userList = userService.findAll();

		Map roleQueryMap = new HashMap();
		roleQueryMap.put("domain", "system");
		List<Role> roleList = roleService.findByMap(roleQueryMap);

		m.addAttribute("userrole", userroleList);
		m.addAttribute("user", userList);
		m.addAttribute("role", roleList);

		return "sysrole/list";
	}

	@RequestMapping(value="/add", method=RequestMethod.POST)
	@PreAuthorize("hasRole('god')")
	public String addSystemUser(
			@RequestParam("userId") Integer userId, 
			@RequestParam("roleName") String roleName, 
			ModelMap m) {

		// TODO CHECK REAL USER
		// TODO CHECK REAL ROLENAME
		// TODO CHECK HAS NO ACCESS

		Role role= roleService.getRoleByRoleName(roleName);

		Userrole userrole = new Userrole();
		userrole.setUserId(userId);
		userrole.setRoleId(role.getRoleId());
		userroleService.save(userrole);

		// if i am the user,  reauth
		User me = getAuthenticatedUser();
		if (me.getUserId() == userId) {
			doReauth();
		}

		waspMessage("hello.error");

		return "redirect:/sysrole/list.do";
	}

	@RequestMapping(value="/remove/{userId}/{roleName}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('god')")
	public String removeSystemUser(
			@PathVariable("userId") Integer userId, 
			@PathVariable("roleName") String roleName, 
			ModelMap m) {

		// TODO CHECK REAL USER
		// TODO CHECK REAL ROLENAME
		// TODO CHECK HAS ACCESS

		Role role = roleService.getRoleByRoleName(roleName);

		Userrole userrole = userroleService.getUserroleByUserIdRoleId(userId, role.getRoleId());
		userroleService.remove(userrole);

		// if i am the user,  reauth
		User me = getAuthenticatedUser();
		if (me.getUserId() == userId) {
			doReauth();
		}

		waspMessage("hello.error");

		return "redirect:/sysrole/list.do";
	}

}
