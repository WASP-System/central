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
  
  @Test (groups = "integration-tests",  dataProvider = "DP1")
  public void pendingUserApprove(String sUserName, String sUserPass, String sExpectedUrl, String sUserEmail, String sApprovedUrl) throws Exception {   
		
	  SeleniumHelper.login(sUserName, sUserPass, driver);
	  
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")), "Unable to locate 'Lab Utils' tab.");
	  driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")).click();

	  WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pendinglmapproval/')]"));
	  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-labUtils')]")).click();

	  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pendinglmapproval/')]")).click();
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(.,'"+sUserEmail+"')]")), "");
	  driver.findElement(By.xpath("//a[contains(.,'"+sUserEmail+"')]")).click();
	  
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(.,'"+sUserEmail+"')]/div/div/")), "");
	  Assert.assertNotNull(driver.findElement(By.xpath("//h4/a[contains(.,'"+sUserEmail+"')]/div/div/a[contains(text(),'APPROVE')]")));
	  driver.findElement(By.xpath("///a[contains(text(),'APPROVE')]")).click();
	  //Assert.assertTrue(driver.findElements(By.xpath("//div[contains(.,'"+sUserEmail+"')]/a[contains(.,'APPROVE')]")).size() == 0, "Failed to approve a new user");
      
  }
  
  @Test  (groups = "integration-tests",  dataProvider = "DP2")
  public void pendingUserReject(String sUserName, String sUserPass, String sExpectedUrl, String sUserEmail, String sRejectedUrl) throws Exception { 
	  SeleniumHelper.login(sUserName, sUserPass, driver);	 
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")), "Unable to locate 'Lab Utils' tab.");
	  driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")).click();

	  Assert.assertNotNull(driver.findElement(By.xpath("//div[contains(.,'"+sUserEmail+"')]/a[contains(.,'REJECT')]")));
     
      WebElement element = driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]"));
	  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-labUtils')]")).click();

  	  driver.findElement(By.xpath("//div[contains(.,'"+sUserName+"')]/a[contains(.,'REJECT')]")).click();
	  Assert.assertTrue(driver.findElements(By.xpath("//div[contains(.,'"+sUserEmail+"')]/a[contains(.,'REJECT')]")).size() == 0, "Failed to reject a new user");

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
  
  @DataProvider(name = "DP2")
  public Object[][] createData2() throws Exception{
      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
              "Test_001", "pendingUserReject");
      
      return(retObjArr);
  }

}
