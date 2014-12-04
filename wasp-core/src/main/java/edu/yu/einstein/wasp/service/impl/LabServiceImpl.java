
/**
 *
 * LabService.java 
 * @author RDubin
 *  
 * the LabService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.ConfirmEmailAuthDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.LabPendingDao;
import edu.yu.einstein.wasp.dao.LabUserDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.UserroleDao;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.service.LabService;

@Service
@Transactional("entityManager")
public class LabServiceImpl extends WaspServiceImpl implements LabService {

	@Autowired
	private ConfirmEmailAuthDao confirmEmailAuthDao;
	@Autowired
	private UserroleDao userroleDao;
	@Autowired
	private LabDao labDao;
	@Autowired
	private LabPendingDao labPendingDao;
	@Autowired
	private LabUserDao labUserDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;


  

	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Lab getLabByPI(User pi){
		  if(pi==null || pi.getUserId()==null){
			  return new Lab();
		  }
		  else{return labDao.getLabByPrimaryUserId(pi.getUserId().intValue());}
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public boolean isUserLabMember(Lab lab, User user){
		  LabUser labUser = labUserDao.getLabUserByLabIdUserId(lab.getLabId(), user.getUserId());
		  if(labUser == null || labUser.getLabUserId() == null){
			  return false;
		  }
		  return true;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Role getUserRoleInLab(Lab lab, User user){
		  LabUser labUser = labUserDao.getLabUserByLabIdUserId(lab.getLabId(), user.getUserId());
		  if(labUser == null || labUser.getLabUserId() == null){
			  return new Role();
		  }
		  return labUser.getRole();
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public LabUser addExistingUserToLabAsLabMemberPending(Lab lab, User user){
			
		  LabUser labUser = new LabUser();
		  Role role = roleDao.getRoleByRoleName("lp");
		  labUser.setLabId(lab.getLabId());
		  labUser.setUserId(user.getUserId());
		  labUser.setRoleId(role.getRoleId());
		  LabUser labUserDB = labUserDao.save(labUser);
		  
		  return labUserDB;

	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public boolean isExistingUserPIPending(User user){
		  if(user.getUserId()==null){
			  return false;
		  }
		  Map queryMap = new HashMap();
		  queryMap.put("primaryUserId", user.getUserId());
		  queryMap.put("status", "PENDING");
		  List<LabPending> labPendingList = labPendingDao.findByMap(queryMap);
		  if(labPendingList.size()>0){
			  return true;
		  }
		  return false;
	  }
	  
	  @Override
	  public List<LabUser> getAllLabUsers(){
		  return labUserDao.findAll();
	  }
	  
	  @Override
	  public String getInstitutionOfLabPI(Lab lab){
		  String institution = "";
		  User pi = lab.getUser();//labPI
		  for(UserMeta um : pi.getUserMeta()){
			  if(um.getK().toLowerCase().endsWith("institution")){
				  institution = um.getV();
				  break;
			  }
		  }		  
		  return institution;
	  }
	  
	  @Override
	  public Lab getLabByLabId(Integer labId){
		  Lab lab = labDao.getLabByLabId(labId);
		  return lab;
	  }
}

