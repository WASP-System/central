package edu.yu.einstein.wasp.integration.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.util.SeleniumHelper;

/**
 * 
 * @author nvolnova
 *
 */

public class SelUserDetail extends SelBaseTest {
	
	 @BeforeClass
	  public void beforeClass() {
		  
	  }
	 	  
	  /**
	   * 
	   * @param sUserName
	   * @param sUserPass
	   * @param sSuccessUrl
	   * @param sOldPass
	   * @param sNewPass
	   * @param sSuccessUrl2
	   * @throws Exception
	   */
	  
	  @Test (groups = "integration-tests",  dataProvider = "DP1")
	  public void myProfile(String sUserName, String sUserPass, String sSuccessUrl, String sOldPass, String sNewPass, String sSuccessUrl2) throws Exception {   
			
		  SeleniumHelper.login(sUserName, sUserPass);	 
		  driver.get("http://"+baseUrl+"/wasp/dashboard.do");
		  
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-home')]")), "Unable to locate 'My Account' tab.");
		  driver.findElement(By.xpath("//a[contains(@href,'#tabs-home')]")).click();

		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/user/me_ro.do')]")).size() != 0, "Cannot locate 'My Profile' link.");

		  WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/wasp/user/me_ro.do')]"));
		  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-home')]")).click();
		  
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/user/me_ro.do')]")).click();
		  
		  Assert.assertEquals(driver.getCurrentUrl(),"http://"+baseUrl+"/wasp/user/me_ro.do");
		  Assert.assertTrue(driver.findElements(By.linkText("Edit")).size() != 0, "Cannot locate 'Edit' link.");
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/user/me_rw.do')]")).click();
		  Assert.assertEquals(driver.getCurrentUrl(), "http://"+baseUrl+"/wasp/user/me_rw.do");
		  
		  Assert.assertEquals(driver.findElement(By.id("login")).getAttribute("value"), "super");
		  Assert.assertFalse(driver.findElement(By.id("login")).getAttribute("value").isEmpty(), "Login field is empty");
		  Assert.assertFalse(driver.findElement(By.id("firstName")).getAttribute("value").isEmpty(), "First Name field is empty");
		  Assert.assertFalse(driver.findElement(By.id("lastName")).getAttribute("value").isEmpty(), "Last Name field is empty");
		  Assert.assertFalse(driver.findElement(By.id("email")).getAttribute("value").isEmpty(), "Email field is empty");
		  Assert.assertFalse(driver.findElement(By.name("locale")).getAttribute("value").isEmpty(), "Locale field is empty");
		  driver.findElement(By.id("title")).sendKeys("Prof");
		  Assert.assertFalse(driver.findElement(By.id("institution")).getAttribute("value").isEmpty(), "Institution field is empty");
		
		  driver.findElement(By.id("departmentId")).sendKeys("Internal - Default Department");
		  Assert.assertFalse(driver.findElement(By.id("building_room")).getAttribute("value").isEmpty(), "Building room field is empty");
		  Assert.assertFalse(driver.findElement(By.id("address")).getAttribute("value").isEmpty(), "Address field is empty");
		  Assert.assertFalse(driver.findElement(By.id("city")).getAttribute("value").isEmpty(), "City field is empty");
		  driver.findElement(By.id("state")).sendKeys("New York");
		  driver.findElement(By.id("country")).sendKeys("United States");
		  Assert.assertFalse(driver.findElement(By.id("zip")).getAttribute("value").isEmpty(), "Zip field is empty");
		  driver.findElement(By.id("phone")).sendKeys("1234567890");
		
		  Assert.assertTrue(driver.findElements(By.xpath("//input[@value='Save Changes']")).size() != 0, "Cannot locate 'Save Changes' button");
		  driver.findElement(By.xpath("//input[@value='Save Changes']")).click();
		  Assert.assertEquals(driver.getCurrentUrl(), sSuccessUrl);
		  
		  
		  Assert.assertTrue(driver.findElements(By.linkText("Edit")).size() != 0, "Cannot locate 'Edit' link.");
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/user/me_rw.do')]")).click();
		  Assert.assertEquals(driver.getCurrentUrl(), "http://"+baseUrl+"/wasp/user/me_rw.do");
		  Assert.assertTrue(driver.findElements(By.xpath("//input[@value='Cancel']")).size() != 0, "Cannot locate 'Cancel' button");
		  driver.findElement(By.xpath("//input[@value='Cancel']")).click();
		  Assert.assertEquals(driver.getCurrentUrl(), sSuccessUrl);
		  
		  
	  }
	  
	   /**
	   * 
	   * @param sUserName
	   * @param sUserPass
	   * @param sSuccessUrl
	   * @param sOldPass
	   * @param sNewPass
	   * @param sSuccessUrl2
	   * @throws Exception
	   */
	  @Test (groups="integration-tests", dataProvider = "DP1")
	  public void changePassword(String sUserName, String sUserPass, String sSuccessUrl, String sOldPass, String sNewPass, String sSuccessUrl2) throws Exception {   
		  SeleniumHelper.login(sUserName, sUserPass);	 
		  driver.get("http://"+baseUrl+"/wasp/dashboard.do");
		  
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-home')]")), "Unable to locate 'My Account' tab.");
		  driver.findElement(By.xpath("//a[contains(@href,'#tabs-home')]")).click();

		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/user/me_ro.do')]")).size() != 0, "Cannot locate 'My Profile' link.");

		  WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/wasp/user/me_ro.do')]"));
		  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-home')]")).click();
		  
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/user/me_ro.do')]")).click();
		  
		  Assert.assertTrue(driver.findElements(By.linkText("Change Password")).size() != 0, "Cannot locate 'Change Password' link.");
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/user/mypassword.do')]")).click();
		  Assert.assertEquals(driver.getCurrentUrl(), sSuccessUrl2);
		  
		  driver.findElement(By.name("oldpassword")).sendKeys(sOldPass);
		  driver.findElement(By.name("newpassword1")).sendKeys(sNewPass);
		  driver.findElement(By.name("newpassword2")).sendKeys(sNewPass);
		  driver.findElement(By.xpath("//input[@value='Submit']")).click();
		  Assert.assertEquals(driver.getCurrentUrl(), "http://"+baseUrl+"/wasp/dashboard.do");
		  
	  }
	  
	  @AfterClass
	  public void afterClass() {
	  
	  }
	  
	  /**
	   * 
	   * @return retObjArr
	   * @throws Exception
	   */
	  @DataProvider(name = "DP1")
	  public Object[][] createData1() throws Exception{
	      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
	              "Test_001", "userDetail");
	      
	      return(retObjArr);
	  }
}
