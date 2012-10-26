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
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.dao.impl.UserDaoImpl;
import edu.yu.einstein.wasp.dao.impl.UserPendingDaoImpl;
import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;

@PrepareForTest(SecurityContext.class)
public class TestAuthenticationServiceImpl {
	
	UserDao mockUserDao;
	UserPendingDao mockUserPendingDao;
	AuthenticationServiceImpl authServiceImpl;
	
  @Test
  public void getAuthenticatedUser() {
	  User user = new User();
	  user.setLogin("jdoe");
	  user.setFirstName("Jane");
	  user.setLastName("Doe");

	  SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken("username", "password"));
	  
	  expect(mockUserDao.getUserByLogin("username")).andReturn(user);
	  replay(mockUserDao);
	  
	  Assert.assertEquals(authServiceImpl.getAuthenticatedUser(), user);
	  verify(mockUserDao);
  }
  
  @Test
  public void getRoles() {
	  User user = new User();
	  user.setLogin("userlogin");
	  
	  List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	  authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
	  
	  SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(user,"", authorities));
	  
	  String[] roles = new String[1];
	  roles[0] = "ROLE_USER";
	  Assert.assertEquals(authServiceImpl.getRoles(), roles);
  }
  
  
	
  /**
   * Test scenario when login name already chosen by someone with a different email address
   */
  @Test 
  public void isLoginAlreadyInUse1() {
	  
	  User user = new User();
	  Map loginQueryMap = new HashMap();
	  loginQueryMap.put("login", "jgreally");
	  
	  List<UserPending> userPendingList = new ArrayList<UserPending>();
	  UserPending userPending = new UserPending();
	  userPending.setStatus("WAIT_EMAIL");
	  userPending.setEmail("test@einstein.yu.edu");
		  
	  userPendingList.add(userPending);

	  expect(mockUserDao.getUserByLogin("jgreally")).andReturn(user);
	  expect(mockUserPendingDao.findByMap(loginQueryMap)).andReturn(userPendingList);
	  
	  authServiceImpl.setUserDao(mockUserDao);
	  authServiceImpl.setUserPendingDao(mockUserPendingDao);

	  replay(mockUserDao);
	  replay(mockUserPendingDao);
	  
	  try {
		Assert.assertTrue(authServiceImpl.isLoginAlreadyInUse("jgreally", "jgreally@einstein.yu.edu"));
		
	  } catch (LoginNameException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
	  
	  verify(mockUserDao);
	  verify(mockUserPendingDao);
	  
  }
  
  @Test
  public void hasRoleInRoleArray(){
	  
	  //Test case:1
	  String rolesToCompare [] = {"da","fm", "sa"};
	  String rolesBaseline [] = {"da","fm", "sa"};
	  //Test case:2
	  String rolesToCompare2 [] = {null, null, null};
	  String rolesBaseline2 [] = {"da","fm", "sa"};
	  //Test case:3
	  String rolesToCompare3 [] = {"da", "fm", "sa"};
	  String rolesBaseline3[] = {"asd","asd", "asd"};
	  
	  //Test case:4 throws NullPointerException
	  String rolesToCompare4 [] = {"da", "fm", "sa"};
	  String rolesBaseline4 [] = {null,"fm", "sa"};
	  
	  Assert.assertTrue(authServiceImpl.hasRoleInRoleArray(rolesToCompare, rolesBaseline));
	  Assert.assertFalse(authServiceImpl.hasRoleInRoleArray(rolesToCompare2, rolesBaseline2));
	  Assert.assertFalse(authServiceImpl.hasRoleInRoleArray(rolesToCompare3, rolesBaseline3));
	  
	  //Assert.assertFalse(authServiceImpl.hasRoleInRoleArray(rolesToCompare4, rolesBaseline4));



  }
  
  @Test
  public void hasRole(){
	  
	  List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	  authorities.add(new GrantedAuthorityImpl("da"));
	  
	  SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken("","", authorities));
	  
	  Assert.assertTrue(authServiceImpl.hasRole("da"));
	  


  }
  
  
  
  /**
   * Test scenario (by setting userId=1) when login name already exists.
   */
  @Test 
  public void isLoginAlreadyInUse2() {
	  
	  User user = new User();
	  user.setUserId(1);
	  
	  Map loginQueryMap = new HashMap();
	  loginQueryMap.put("login", "jgreally");

	  expect(mockUserDao.getUserByLogin("jgreally")).andReturn(user);
	  replay(mockUserDao);
	  
	  try {
		Assert.assertTrue(authServiceImpl.isLoginAlreadyInUse("jgreally", "jgreally@einstein.yu.edu"));
		
	  } catch (LoginNameException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
	  
	  verify(mockUserDao);
  }
  
  @BeforeTest
  public void beforeTest() {
	  
	  authServiceImpl = new AuthenticationServiceImpl();
	  mockUserDao = createMockBuilder(UserDaoImpl.class).addMockedMethods(UserDaoImpl.class.getMethods()).createMock();
	  mockUserPendingDao = createMockBuilder(UserPendingDaoImpl.class).addMockedMethods(UserPendingDaoImpl.class.getMethods()).createMock();
	  
	  Assert.assertNotNull(mockUserDao);
	  Assert.assertNotNull(mockUserPendingDao);
	  
	  authServiceImpl.setUserDao(mockUserDao);
	  authServiceImpl.setUserPendingDao(mockUserPendingDao);

  }
  
  @AfterTest
  public void afterTest() {
	  
	 
  }

  @BeforeMethod
  public void beforeMethod() {
  }

  @AfterMethod
  public void afterMethod() {
	  EasyMock.reset(mockUserDao);
	  EasyMock.reset(mockUserPendingDao);
	  
	  SecurityContextHolder.clearContext();
  }

  @BeforeClass
  public void beforeClass() {
  }

  @AfterClass
  public void afterClass() {
	  
  }

  

 

  @BeforeSuite
  public void beforeSuite() {
  }

  @AfterSuite
  public void afterSuite() {
  }

}
