package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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

import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.UserroleDao;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userrole;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.util.StringHelper;

/**
 * Class for handling requests for adding and removing system roles to existing users
 * @author asmclellan
 *
 */
@Controller
@Transactional
@RequestMapping("/sysrole")
public class SystemRoleController extends WaspController {


	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserroleDao userroleDao;

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
	@PreAuthorize("hasRole('su') or hasRole('fm')")
	public String listSystemUser(ModelMap m) {

		Map<String, String> roleQueryMap = new HashMap<String, String>();
		roleQueryMap.put("domain", "system");
		List<Role> systemRoleList = roleDao.findByMap(roleQueryMap);

		SortedMap<String, ArrayList<Userrole>> userRoleMap = new TreeMap<String, ArrayList<Userrole>>();
		for (Userrole ur : userroleDao.findAll()){
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
	@PreAuthorize("hasRole('su')")
	public String addSystemUser(
			@RequestParam("userHook") String userHook,
			@RequestParam("roleName") String roleName,
			ModelMap m) {

		if (userHook == null || userHook.isEmpty()){
			waspErrorMessage("sysrole.noUserSpecified.error");
			return "redirect:/sysrole/list.do";
		}
		String userLogin = StringHelper.getLoginFromFormattedNameAndLogin(userHook.trim());

		User user = userDao.getUserByLogin(userLogin);
		if (user.getId() == null){
			waspErrorMessage("sysrole.userNonexistant.error");
			return "redirect:/sysrole/list.do";
		}
		if (roleName == null || roleName.isEmpty()){
			waspErrorMessage("sysrole.noRoleSpecified.error");
			return "redirect:/sysrole/list.do";
		}
		Role role= roleDao.getRoleByRoleName(roleName);
		if (role.getId() == null || !role.getDomain().equals("system")){
			waspErrorMessage("sysrole.invalidRoleSpecified.error");
			return "redirect:/sysrole/list.do";
		}
		Map<String, Integer> userroleQueryMap = new HashMap<String, Integer>();
		userroleQueryMap.put("userId", user.getId());
		userroleQueryMap.put("roleId", role.getId());
		if (!userroleDao.findByMap(userroleQueryMap).isEmpty()){
			waspErrorMessage("sysrole.userRoleExists.error");
			return "redirect:/sysrole/list.do";
		}
		Userrole userrole = new Userrole();
		userrole.setUserId(user.getId());
		userrole.setRoleId(role.getId());
		userroleDao.save(userrole);


		// if i am the user,  reauth
		User me = authenticationService.getAuthenticatedUser();
		if (me.getId().intValue() == user.getId().intValue()) {
			doReauth();
		}

		waspMessage("sysrole.success.label");

		return "redirect:/sysrole/list.do";
	}

	/**
	 * Removes specified system role from specified user.
	 * @param UserId
	 * @param roleName (e.g. da, fm etc)
	 * @param m model 
	 * @return view
	 */
	@RequestMapping(value="/remove/{UserId}/{roleName}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su')")
	public String removeSystemUser(
			@PathVariable("UserId") Integer userId,
			@PathVariable("roleName") String roleName,
			ModelMap m) {

		if (userId == null){
			waspErrorMessage("sysrole.noUserSpecified.error");
			return "redirect:/sysrole/list.do";
		}
		if (userDao.getUserByUserId(userId).getUserId() == null){
			waspErrorMessage("sysrole.userNonexistant.error");
			return "redirect:/sysrole/list.do";
		}
		if (roleName == null || roleName.isEmpty()){
			waspErrorMessage("sysrole.noRoleSpecified.error");
			return "redirect:/sysrole/list.do";
		}
		Role role= roleDao.getRoleByRoleName(roleName);
		if (role.getRoleId() == null || !role.getDomain().equals("system")){
			waspErrorMessage("sysrole.invalidRoleSpecified.error");
			return "redirect:/sysrole/list.do";
		}
		// ensure we do not remove the only userrole entry for the chosen role
		// must have at least one user granted each system role.
		Map<String, Integer> roleIdQuery = new HashMap<String, Integer>();
		roleIdQuery.put("roleId", role.getRoleId());
		if (userroleDao.findByMap(roleIdQuery).size() == 1){
			waspErrorMessage("sysrole.onlyUserWithRole.error");
			return "redirect:/sysrole/list.do";
		}
		Userrole userrole = userroleDao.getUserroleByUserIdRoleId(userId, role.getRoleId());
		
		if (userrole.getUserroleId() == null){
			waspErrorMessage("sysrole.wrongUserRoleCombination.error");
			return "redirect:/sysrole/list.do";
		}
		userroleDao.remove(userrole);
		waspMessage("sysrole.success.label");
		// if i am the user,  reauth
		User me = authenticationService.getAuthenticatedUser();
		if (me.getUserId().intValue() == userId.intValue()) {
			doReauth();
			if (roleName.equals("su")){
				return "redirect:/dashboard.do";
			}
		}
		return "redirect:/sysrole/list.do";
	}

}
