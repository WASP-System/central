package edu.yu.einstein.wasp.service.impl;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.Assert;

import org.easymock.EasyMock;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.dao.ConfirmEmailAuthDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.impl.ConfirmEmailAuthDaoImpl;
import edu.yu.einstein.wasp.dao.impl.UserDaoImpl;
import edu.yu.einstein.wasp.model.ConfirmEmailAuth;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;

public class TestUserServiceImpl {
	
  private ConfirmEmailAuthDao mockConfirmEmailAuthDao;
  private UserDao mockUserDao;
  UserServiceImpl userServiceImpl = new UserServiceImpl();
 
  @Test
  public void getUniqueLoginName() {
	  
	  User userNew = new User();
	  userNew.setUserId(123);
	  userNew.setFirstName("John");
	  userNew.setLastName("Greally");
	  
	  
	  User userExists = new User();
	  userExists.setUserId(123);
	  userExists.setFirstName("Jane");
	  userExists.setLastName("Doe");
	  
	  User user_apostrophe = new User();
	  user_apostrophe.setUserId(123);
	  user_apostrophe.setFirstName("Jane");
	  user_apostrophe.setLastName("O'Brien");
	  
	  User user_hyphen = new User();
	  user_hyphen.setUserId(123);
	  user_hyphen.setFirstName("Jane");
	  user_hyphen.setLastName("Some-Lastname");
	  
	  User user_apostrophe_middle = new User();
	  user_apostrophe_middle.setUserId(123);
	  user_apostrophe_middle.setFirstName("Jane");
	  user_apostrophe_middle.setLastName("Some'Lastname");
	  
	  User user_apostrophe_beg = new User();
	  user_apostrophe_beg.setUserId(123);
	  user_apostrophe_beg.setFirstName("Jane");
	  user_apostrophe_beg.setLastName("'SomeLastname");
	  
	  User userBlank = new User();//use this to break out of the while loop
	  
	  userServiceImpl.setUserDao(mockUserDao);
	  
	  expect(mockUserDao.getUserByLogin("jdoe")).andReturn(userExists);
	  expect(mockUserDao.getUserByLogin("jdoe1")).andReturn(userBlank);
	  expect(mockUserDao.getUserByLogin("jgreally")).andReturn(userBlank);
	  expect(mockUserDao.getUserByLogin("jobrien")).andReturn(userBlank);
	  expect(mockUserDao.getUserByLogin("jsomelastname")).andReturn(userBlank);
	  expect(mockUserDao.getUserByLogin("jsomelastname")).andReturn(userBlank);
	  expect(mockUserDao.getUserByLogin("jsomelastname")).andReturn(userBlank);


	  replay(mockUserDao);
	  Assert.assertEquals("jdoe1", userServiceImpl.getUniqueLoginName(userExists));
	  Assert.assertEquals("jgreally", userServiceImpl.getUniqueLoginName(userNew));
	  Assert.assertEquals("jobrien", userServiceImpl.getUniqueLoginName(user_apostrophe));
	  Assert.assertEquals("jsomelastname", userServiceImpl.getUniqueLoginName(user_apostrophe_middle));
	  Assert.assertEquals("jsomelastname", userServiceImpl.getUniqueLoginName(user_hyphen));
	  Assert.assertEquals("jsomelastname", userServiceImpl.getUniqueLoginName(user_apostrophe_beg));

	  verify(mockUserDao);
	  
  }
  
  @Test
  public void getNewAuthcodeForUser() {
	  User user = new User();
	  user.setUserId(123);
	  
	  userServiceImpl.setconfirmEmailAuthDao(mockConfirmEmailAuthDao);
	  ConfirmEmailAuth confirmEmailAuth = new ConfirmEmailAuth();
	  
	  expect(mockConfirmEmailAuthDao.getConfirmEmailAuthByUserId(123)).andReturn(confirmEmailAuth);
      expect(mockConfirmEmailAuthDao.save(confirmEmailAuth)).andReturn(confirmEmailAuth);

	  replay(mockConfirmEmailAuthDao);
	  Assert.assertNotNull(userServiceImpl.getNewAuthcodeForUser(user));
	  verify(mockConfirmEmailAuthDao);
	  
  }
  
  @Test
  public void getNewAuthcodeForUserPending() {
	  UserPending userPending = new UserPending();
	  userPending.setUserPendingId(123);
	  
	  userServiceImpl.setconfirmEmailAuthDao(mockConfirmEmailAuthDao);
	  ConfirmEmailAuth confirmEmailAuth = new ConfirmEmailAuth();
	  
	  expect(mockConfirmEmailAuthDao.getConfirmEmailAuthByUserpendingId(123)).andReturn(confirmEmailAuth);
      expect(mockConfirmEmailAuthDao.save(confirmEmailAuth)).andReturn(confirmEmailAuth);

	  replay(mockConfirmEmailAuthDao);
	  Assert.assertNotNull(userServiceImpl.getNewAuthcodeForUserPending(userPending));
	  verify(mockConfirmEmailAuthDao);
	  
  }
  @BeforeMethod
  public void beforeMethod() {
  }

  @AfterMethod
  public void afterMethod() {
	  EasyMock.reset(mockConfirmEmailAuthDao);
	  EasyMock.reset(mockUserDao);
  }

  @BeforeClass
  public void beforeClass() {
  }

  @AfterClass
  public void afterClass() {
  }

  @BeforeTest
  public void beforeTest() {
	  mockConfirmEmailAuthDao = createMockBuilder(ConfirmEmailAuthDaoImpl.class).addMockedMethods(ConfirmEmailAuthDaoImpl.class.getMethods()).createMock();
	  mockUserDao = createMockBuilder(UserDaoImpl.class).addMockedMethods(UserDaoImpl.class.getMethods()).createMock();

	  Assert.assertNotNull(mockConfirmEmailAuthDao);
  }

  @AfterTest
  public void afterTest() {
  }

}
