package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userrole;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.UserroleService;
import edu.yu.einstein.wasp.util.StringHelper;

/**
 * Class for handling requests for adding and removing system roles to existing users
 * @author andymac
 *
 */
@Controller
@Transactional
@RequestMapping("/sysrole")
public class SystemRoleController extends WaspController {


	@Autowired
	private RoleService roleService;

	@Autowired
	private UserroleService userroleService;

	@Autowired
	private BeanValidator validator;
	  
	@Autowired
	private AuthenticationService authenticationService;

	@Override
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	/**
	 * Add a systems role to an existing user 'get' request to generate view
	 * @param m model
	 * @return view 
	 */
	@RequestMapping(value="/list", method=RequestMethod.GET)
	@PreAuthorize("hasRole('god')")
	public String listSystemUser(ModelMap m) {

		Map roleQueryMap = new HashMap();
		roleQueryMap.put("domain", "system");
		List<Role> systemRoleList = roleService.findByMap(roleQueryMap);

		SortedMap userRoleMap = new TreeMap();
		for (Userrole ur : (List<Userrole>) userroleService.findAll()){
			if (systemRoleList.contains(ur.getRole())){
				// current userrole user has a systems role
				String nameKey = ur.getUser().getLastName() + ", " + ur.getUser().getFirstName() + " (" + ur.getUser().getLogin() + ")";
				if (!userRoleMap.containsKey(nameKey)){
					userRoleMap.put(nameKey, new ArrayList<Userrole>());
				}
				((List<Userrole>)userRoleMap.get(nameKey)).add(ur);
			}
		}

		m.addAttribute("userRoleMap", userRoleMap);
		m.addAttribute("role", systemRoleList);

		return "sysrole/list";
	}
	/**
	 * Add a systems role to an existing user 'post' request from view
	 * @param userHook is at a minimum a login name but may be of the form 'lastName, firstname (loginName)' in which case loginName is parsed out 
	 * @param roleName
	 * @param m model
	 * @return view
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	@PreAuthorize("hasRole('god')")
	public String addSystemUser(
			@RequestParam("userHook") String userHook,
			@RequestParam("roleName") String roleName,
			ModelMap m) {

		if (userHook == null || userHook.isEmpty()){
			waspMessage("sysrole.noUserSpecified.error");
			return "redirect:/sysrole/list.do";
		}
		String userLogin = StringHelper.getLoginFromFormattedNameAndLogin(userHook.trim());

		User user = userService.getUserByLogin(userLogin);
		if (user.getUserId() == null){
			waspMessage("sysrole.userNonexistant.error");
			return "redirect:/sysrole/list.do";
		}
		if (roleName == null || roleName.isEmpty()){
			waspMessage("sysrole.noRoleSpecified.error");
			return "redirect:/sysrole/list.do";
		}
		Role role= roleService.getRoleByRoleName(roleName);
		if (role.getRoleId() == null || !role.getDomain().equals("system")){
			waspMessage("sysrole.invalidRoleSpecified.error");
			return "redirect:/sysrole/list.do";
		}
		Map userroleQueryMap = new HashMap();
		userroleQueryMap.put("UserId", user.getUserId());
		userroleQueryMap.put("roleId", role.getRoleId());
		if (!userroleService.findByMap(userroleQueryMap).isEmpty()){
			waspMessage("sysrole.userRoleExists.error");
			return "redirect:/sysrole/list.do";
		}
		Userrole userrole = new Userrole();
		userrole.setUserId(user.getUserId());
		userrole.setRoleId(role.getRoleId());
		userroleService.save(userrole);


		// if i am the user,  reauth
		User me = authenticationService.getAuthenticatedUser();
		if (me.getUserId().intValue() == user.getUserId().intValue()) {
			doReauth();
		}

		waspMessage("sysrole.success.label");

		return "redirect:/sysrole/list.do";
	}

	/**
	 * Removes specified system role from specified user.
	 * @param userId
	 * @param roleName (e.g. da, fm etc)
	 * @param m model 
	 * @return view
	 */
	@RequestMapping(value="/remove/{userId}/{roleName}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('god')")
	public String removeSystemUser(
			@PathVariable("userId") Integer userId,
			@PathVariable("roleName") String roleName,
			ModelMap m) {

		if (userId == null){
			waspMessage("sysrole.noUserSpecified.error");
			return "redirect:/sysrole/list.do";
		}
		if (userService.getUserByUserId(userId).getUserId() == null){
			waspMessage("sysrole.userNonexistant.error");
			return "redirect:/sysrole/list.do";
		}
		if (roleName == null || roleName.isEmpty()){
			waspMessage("sysrole.noRoleSpecified.error");
			return "redirect:/sysrole/list.do";
		}
		Role role= roleService.getRoleByRoleName(roleName);
		if (role.getRoleId() == null || !role.getDomain().equals("system")){
			waspMessage("sysrole.invalidRoleSpecified.error");
			return "redirect:/sysrole/list.do";
		}
		// ensure we do not remove the only userrole entry for the chosen role
		// must have at least one user granted each system role.
		Map roleIdQuery = new HashMap();
		roleIdQuery.put("roleId", role.getRoleId());
		if (userroleService.findByMap(roleIdQuery).size() == 1){
			waspMessage("sysrole.onlyUserWithRole.error");
			return "redirect:/sysrole/list.do";
		}
		Userrole userrole = userroleService.getUserroleByUserIdRoleId(userId, role.getRoleId());
		
		if (userrole.getUserroleId() == null){
			waspMessage("sysrole.wrongUserRoleCombination.error");
			return "redirect:/sysrole/list.do";
		}
		userroleService.remove(userrole);
		waspMessage("sysrole.success.label");
		// if i am the user,  reauth
		User me = authenticationService.getAuthenticatedUser();
		if (me.getUserId().intValue() == userId.intValue()) {
			doReauth();
			if (roleName.equals("god")){
				return "redirect:/dashboard.do";
			}
		}
		return "redirect:/sysrole/list.do";
	}

}
