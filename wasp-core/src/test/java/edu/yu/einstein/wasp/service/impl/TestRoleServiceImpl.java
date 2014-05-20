package edu.yu.einstein.wasp.service.impl;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.dao.DepartmentUserDao;
import edu.yu.einstein.wasp.dao.impl.DepartmentUserDaoImpl;
import edu.yu.einstein.wasp.model.DepartmentUser;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userrole;


public class TestRoleServiceImpl {
	
	HibernateTemplate hibernateTemplateMock;
	DepartmentUserDao mockDepartmentUserDao;
	
	private RoleServiceImpl roleServiceImpl = new RoleServiceImpl();
	
  @Test  (groups = "unit-tests-service-objects")
  public void getUniqueSortedRoleList() {
	  
	  User user = new User();
	  Userrole userrole = new Userrole();
	  List<Userrole> userroles = new ArrayList<Userrole>();
	  List<LabUser> labUsers = new ArrayList<LabUser>();
	  LabUser labUser = new LabUser();
	  
	  Role role1 = new Role();
	  role1.setId(1);
	  role1.setName("Facility Manager");
	  userrole.setRole(role1);
	  userroles.add(userrole);
	  
	  Role role2 = new Role();
	  role2.setId(2);
	  role2.setName("PI");
	  labUser.setRole(role2);
	  labUsers.add(labUser);

	  user.setUserrole(userroles);
	  user.setLabUser(labUsers);
	  
	  Map<String, Integer> departmentuserQueryMap = new HashMap<String, Integer>();
	  departmentuserQueryMap.put("userId", null);
	  
	  List<DepartmentUser> departmentUsers = new ArrayList<DepartmentUser>();
	 	  
	  //what getUniqueSortedRoleList() expected to return
	  List<String> roles = new ArrayList<String>();
	  roles.add("Facility Manager");
	  roles.add("PI");
	  	  
	  roleServiceImpl.setDepartmentUserDao(mockDepartmentUserDao);	  
	  
	  expect(mockDepartmentUserDao.findByMap(departmentuserQueryMap)).andReturn(departmentUsers);
	  replay(mockDepartmentUserDao);
	  
	  Assert.assertEquals(roles, roleServiceImpl.getUniqueSortedRoleList(user));
	  
	  verify(mockDepartmentUserDao);
	  
  }
  
  @Test  (groups = "unit-tests-service-objects")
  public void getCompleteSortedRoleList() {
	 
	  //Set-up for test scenario 1 and 2
	  Userrole userrole = new Userrole();
	  List<Userrole> userroles = new ArrayList<Userrole>();
	  
	  User user = new User();
	  user.setLastName("Jones");
	  
	  Lab lab = new Lab();
	  lab.setUser(user);
	  
	  Role role1 = new Role();
	  role1.setId(1);
	  role1.setName("Facility Manager");
	  userrole.setRole(role1);
	  userroles.add(userrole);
	  
	  //Test scenario 1:  where roleName is not "lm" or "lu"
	  User testUser1 = new User();
	  LabUser labUser = new LabUser();
	  List<LabUser> labUsers = new ArrayList<LabUser>();
	  Role role2 = new Role();
	  role2.setId(2);
	  role2.setRoleName("pi");
	  role2.setName("Primary Investigator");
	  labUser.setRole(role2);
	  labUser.setLab(lab);
	  labUsers.add(labUser);
	  
	  //Test scenario 2: where roleName is "lm" or "lu"
	  LabUser labUser2 = new LabUser();
	  List<LabUser> labUsers2 = new ArrayList<LabUser>();
	  
	  User testUser2 = new User();
	  Role role3 = new Role();
	  role3.setId(3);
	  role3.setRoleName("lm");
	  role3.setName("Lab Manager");
	  labUser2.setRole(role3);
	  labUser2.setLab(lab);
	  labUsers2.add(labUser2);

	  testUser1.setUserrole(userroles);
	  testUser1.setLabUser(labUsers);

	  testUser2.setUserrole(userroles);
	  testUser2.setLabUser(labUsers2);

	  
	  
	  Map<String, Integer> departmentuserQueryMap = new HashMap<String, Integer>();
	  departmentuserQueryMap.put("userId", null);
	  
	  List<DepartmentUser> departmentUsers = new ArrayList<DepartmentUser>();
	 	  
	  //what getUniqueSortedRoleList() expected to return in test scenario 1
	  List<String> expectedListOfRoles1 = new ArrayList<String>();
	  expectedListOfRoles1.add("Facility Manager");
	  expectedListOfRoles1.add("Primary Investigator");
	  
	  //what getUniqueSortedRoleList() expected to return in test scenario 1
	  List<String> expectedListOfRoles2 = new ArrayList<String>();
	  expectedListOfRoles2.add("Facility Manager");
	  expectedListOfRoles2.add("Lab Manager (Jones Lab)");

	  	  
	  roleServiceImpl.setDepartmentUserDao(mockDepartmentUserDao);	  
	  
	  expect(mockDepartmentUserDao.findByMap(departmentuserQueryMap)).andReturn(departmentUsers);
	  expect(mockDepartmentUserDao.findByMap(departmentuserQueryMap)).andReturn(departmentUsers);

	  replay(mockDepartmentUserDao);
	  
	  Assert.assertEquals(expectedListOfRoles1, roleServiceImpl.getCompleteSortedRoleList(testUser1));
	  Assert.assertEquals(expectedListOfRoles2, roleServiceImpl.getCompleteSortedRoleList(testUser2));
	  
	  verify(mockDepartmentUserDao);
	  
  }
  
  @BeforeTest
  public void beforeTest() {
	  mockDepartmentUserDao = createMockBuilder(DepartmentUserDaoImpl.class).addMockedMethods(DepartmentUserDaoImpl.class.getMethods()).createMock();
	  hibernateTemplateMock = createMockBuilder(HibernateTemplate.class).addMockedMethods(HibernateTemplate.class.getMethods()).createMock();
	  	  
	  Assert.assertNotNull(mockDepartmentUserDao);
	  Assert.assertNotNull(hibernateTemplateMock);
  }
  
  @AfterMethod
  public void afterMethod() {
	  EasyMock.reset(mockDepartmentUserDao);
	  EasyMock.reset(hibernateTemplateMock);
	  
  }

}
