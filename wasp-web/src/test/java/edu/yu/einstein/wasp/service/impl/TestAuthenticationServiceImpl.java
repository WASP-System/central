package edu.yu.einstein.wasp.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
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

public class TestAuthenticationServiceImpl {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	WebAuthenticationServiceImpl authServiceImpl;
	
  
  
  @Test
  public void testHasPermissionNormal1(){
	  try {
		Assert.assertFalse(authServiceImpl.hasPermission("hasRole('su') or hasRole('fm') or hasRole('jv-3')"));
	} catch (IOException e) {
		Assert.fail("Caught IOException", e);
	}
  }
  
  @Test
  public void testHasPermissionNormal2(){
	  Map<String, Integer> parameterMap = new HashMap<String, Integer>();
	  parameterMap.put("jobId", 4);
	  parameterMap.put("sampleId", 22);
	  try {
		Assert.assertFalse(authServiceImpl.hasPermission("hasRole('su') or hasRole('fm') or hasRole('jv-#jobId') or hasRole('s-#sampleId')", parameterMap));
	} catch (IOException e) {
		Assert.fail("Caught IOException", e);
	}
  }
  
  @Test
  public void testHasPermissionFail1(){
	  Map<String, Integer> parameterMap = new HashMap<String, Integer>();
	  parameterMap.put("jobId", 4);
	  parameterMap.put("sampleId", 22);
	  String exception = "";
	  try {
		Assert.assertFalse(authServiceImpl.hasPermission("hasRole('su') or hasRole('fm-#unexpected') or hasRole('jv-#jobId') or hasRole('s-#sampleId')", parameterMap));
	  } catch (IOException e) {
		logger.debug("Caught IOException as expected: " + e.getMessage());
		exception = e.getMessage();
	  }
	  Assert.assertEquals(exception, "not all placeholders in permission string have been resolved from parameter map");
  }
  
  @BeforeTest
  public void beforeTest() {
	  
	  authServiceImpl = new WebAuthenticationServiceImpl();

  }
  
  @AfterTest
  public void afterTest() {
	  
	 
  }

  @BeforeMethod
  public void beforeMethod() {
  }

  @AfterMethod
  public void afterMethod() {
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
