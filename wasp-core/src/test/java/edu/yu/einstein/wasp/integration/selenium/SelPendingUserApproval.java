package edu.yu.einstein.wasp.integration.selenium;

import org.openqa.selenium.By;
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
	  //driver.findElement(By.xpath("//a[contains(.,'#tabs-labUtils')]")).click();
	  
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")), "Unable to locate 'Lab Utils' tab.");
	  driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")).click();

      Assert.assertNotNull(driver.findElement(By.linkText("APPROVE")), "'APPROVE' link does not exist");
	  driver.findElement(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'APPROVE')]")).click();
	  Assert.assertTrue(driver.findElements(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'APPROVE')]")).size() == 0, "Failed to approve a new user");
      
  }
  
  @Test  (groups = "integration-tests",  dataProvider = "DP2")
  public void pendingUserReject(String sUserName, String sUserPass, String sExpectedUrl, String sUserEmail, String sRejectedUrl) throws Exception { 
	  SeleniumHelper.login(sUserName, sUserPass, driver);	 
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")), "Unable to locate 'Lab Utils' tab.");
	  driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")).click();

      Assert.assertNotNull(driver.findElement(By.linkText("REJECT")), "'REJECT' link does not exist");

  	  driver.findElement(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'REJECT')]")).click();
	  Assert.assertTrue(driver.findElements(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'REJECT')]")).size() == 0, "Failed to reject a new user");

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
