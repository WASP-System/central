package edu.yu.einstein.wasp.integration.selenium;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import edu.yu.einstein.wasp.test.util.SeleniumHelper;
/**
 * 
 * @author nvolnova
 *
 */
public class SeleniumPendingUserApproval extends SeleniumBaseTest{
  
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
              "Test1", "pendingUserApprove");
      
      return(retObjArr);
  }
  
  @DataProvider(name = "DP2")
  public Object[][] createData2() throws Exception{
      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
              "Test1", "pendingUserReject");
      
      return(retObjArr);
  }
  
  @Test (groups = "integration-tests",  dataProvider = "DP1")
  public void pendingUserApprove(String sUserName, String sUserPass, String sExpectedUrl, String sUserEmail, String sApprovedUrl) throws Exception {   
		
	  SeleniumHelper.loginAsDA(sUserName, sUserPass, driver);	 
	  driver.get("http://localhost:8080/wasp/lab/pendinguser/list/2.do");
      Assert.assertEquals(driver.getCurrentUrl(),sExpectedUrl);
      Assert.assertTrue(SeleniumHelper.verifyTextPresent(sUserEmail, driver),"User with "+ sUserEmail+" email not found");
      Assert.assertNotNull(driver.findElement(By.linkText("APPROVE")), "'APPROVE' link does not exist");
	  driver.findElement(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'APPROVE')]")).click();

      //TO DO: currently 'approve' link throws an error.  Once fixed, verify that it navigates to the right page
      
      
  }
  
  @Test  (groups = "integration-tests",  dataProvider = "DP2")
  public void pendingUserReject(String sUserName, String sUserPass, String sExpectedUrl, String sUserEmail, String sRejectedUrl) throws Exception { 
	  SeleniumHelper.loginAsDA(sUserName, sUserPass, driver);	 
	  driver.get("http://localhost:8080/wasp/lab/pendinguser/list/2.do");
	  //TO DO:
      Assert.assertTrue(SeleniumHelper.verifyTextPresent(sUserEmail, driver),"User with "+ sUserEmail+" email not found");

	  Assert.assertNotNull(driver.findElement(By.linkText("REJECT")), "'REJECT' link not found");
  
	  driver.findElement(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'REJECT')]")).click();
      Assert.assertEquals(driver.getCurrentUrl(),sRejectedUrl);
  }
  
  
  @AfterClass
  public void afterClass() {
  }

}
