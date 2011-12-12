package edu.yu.einstein.wasp.integration.selenium;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.By;

import edu.yu.einstein.wasp.test.util.SeleniumHelper;

/**
 * 
 * @author nvolnova
 *
 */

public class SeleniumUserDetail extends SeleniumBaseTest {
	
	 @BeforeClass
	  public void beforeClass() {
		  
	  }
	  
	  /**
	   * 
	   * @return retObjArr
	   * @throws Exception
	   */
	  @DataProvider(name = "DP1")
	  public Object[][] createData1() throws Exception{
	      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
	              "Test1", "UserDetail");
	      
	      return(retObjArr);
	  }
	 	  
	  /**
	   * 
	   * @param sUserName
	   * @param sUserPass
	   * @param sLab
	   * @param sUserEmail
	   * @param sApprovedUrl
	   * @throws Exception
	   */
	  
	  @Test (groups = "integration-tests",  dataProvider = "DP1")
	  public void navigateMyProfile(String sUserName, String sUserPass) throws Exception {   
			
		  SeleniumHelper.login(sUserName, sUserPass, driver);	 
		  driver.get("http://localhost:8080/wasp/dashboard.do");
		  
		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/user/me_ro.do')]")).size() != 0, "Cannot locate 'My Profile' link.");
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/user/me_ro.do')]")).click();
		  
		  Assert.assertEquals(driver.getCurrentUrl(),"http://localhost:8080/wasp/user/me_ro.do");
			  
		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href, '/wasp/user/me_rw.do']")).size() != 0, "Cannot locate 'Edit' link.");
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/user/me_rw.do')]")).click();
	      
		  Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:8080/wasp/user/me_rw.do");
	  }

	  @AfterClass
	  public void afterClass() {
	  
	  }
}
