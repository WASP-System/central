package edu.yu.einstein.wasp.integration.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.test.util.SeleniumHelper;
/**
 * 
 * @author nvolnova
 *
 */
public class SelPendingUserApproval extends SelBaseTest{
  
  @BeforeClass
  public void beforeClass() {
	  
  }
  /**
   * 
   * @param sUserName
   * @param sUserPass
   * @param sExpectedUrl
   * @param sUserEmail
   * @param sApprovedUrl
   * @throws Exception
   */
  @Test (groups = "integration-tests",  dataProvider = "DP1")
  public void pendingUserApprove(String sUserName, String sUserPass, String sExpectedUrl, String sUserEmail, String sApprovedUrl) throws Exception {   
		
	  SeleniumHelper.login(sUserName, sUserPass, driver);
	  
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")), "Unable to locate 'Lab Utils' tab.");
	  driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")).click();

	  WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pendinglmapproval/')]"));
	  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-labUtils')]")).click();

	  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pendinglmapproval/')]")).click();
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(.,'"+sUserEmail+"')]")), "Could not locate user with '"+sUserEmail+"' email.");
	  driver.findElement(By.xpath("//a[contains(.,'"+sUserEmail+"')]")).click();
	  
	  Assert.assertNotNull(driver.findElement(By.xpath("//div[contains(@id, 'accordion')]//a[contains(.,'"+sUserEmail+"')]/../../div/div/a[contains(., 'APPROVE')]")), "");
	  driver.findElement(By.xpath("//div[contains(@id, 'accordion')]//a[contains(.,'"+sUserEmail+"')]/../../div/div/a[contains(., 'APPROVE')]")).click();
	  Assert.assertTrue(driver.findElements(By.xpath("//div[contains(@id, 'accordion')]//a[contains(.,'"+sUserEmail+"')]/../../div/div/a[contains(., 'APPROVE')]")).size() == 0, "Failed to approve a new user");
      
  }
  
  /**
   * 
   * @param sUserName
   * @param sUserPass
   * @param sExpectedUrl
   * @param sUserEmail
   * @param sRejectedUrl
   * @throws Exception
   */
  @Test  (groups = "integration-tests",  dataProvider = "DP2")
  public void pendingUserReject(String sUserName, String sUserPass, String sExpectedUrl, String sUserEmail, String sRejectedUrl) throws Exception { 
	  SeleniumHelper.login(sUserName, sUserPass, driver);	 
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")), "Unable to locate 'Lab Utils' tab.");
	  driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")).click();

      WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pendinglmapproval/')]"));
	  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-labUtils')]")).click();
	  
	  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pendinglmapproval/')]")).click();
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(.,'"+sUserEmail+"')]")), "Could not locate user with '"+sUserEmail+"' email.");
	  driver.findElement(By.xpath("//a[contains(.,'"+sUserEmail+"')]")).click();
	  
	  Assert.assertNotNull(driver.findElement(By.xpath("//div[contains(@id, 'accordion')]//a[contains(.,'"+sUserEmail+"')]/../../div/div/a[contains(., 'REJECT')]")), "");
	  driver.findElement(By.xpath("//div[contains(@id, 'accordion')]//a[contains(.,'"+sUserEmail+"')]/../../div/div/a[contains(., 'REJECT')]")).click();
	  
	  Assert.assertTrue(driver.findElements(By.xpath("//div[contains(@id, 'accordion')]//a[contains(.,'"+sUserEmail+"')]/../../div/div/a[contains(., 'REJECT')]")).size() == 0, "Failed to reject a new user");

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
              "Test_001", "pendingUserApprove");
      
      return(retObjArr);
  }
  
  /**
   * 
   * @return
   * @throws Exception
   */
  @DataProvider(name = "DP2")
  public Object[][] createData2() throws Exception{
      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
              "Test_001", "pendingUserReject");
      
      return(retObjArr);
  }

}
