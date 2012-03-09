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
public class SelPendingLabs extends SelBaseTest{
  
  @BeforeClass
  public void beforeClass() {
	  
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
  public void pendingLabApprove(String sUserName, String sUserPass, String sLab, String sUserEmail, String sApprovedUrl) throws Exception {   
		
	  SeleniumHelper.login(sUserName, sUserPass, driver);	 
	  driver.get("http://localhost:8080/wasp/department/list.do");
	  
	  driver.findElement(By.xpath("//a[contains(@href, '/wasp/department/dapendingtasklist.do')]")).click();
	  Assert.assertTrue(SeleniumHelper.verifyTextPresent(sLab, driver),"Lab "+ sLab +" not found");
	  driver.findElement(By.xpath("//a[contains(.,'"+sLab+"')]")).click();
	  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/lab/pending/approve/')]")).size() != 0, "Cannot locate APPROVE link");
	  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pending/approve/')]")).click();
	  Assert.assertTrue(SeleniumHelper.verifyTextPresent("New lab application sucessfully approved", driver));
      
      
  }
  
  
  /**
   *  	
   * @param sUserName
   * @param sUserPass
   * @param sLab
   * @param sUserEmail
   * @param sRejectedUrl
   * @throws Exception
   */
  @Test  (groups = "integration-tests",  dataProvider = "DP2")
  public void pendingLabReject(String sUserName, String sUserPass, String sLab, String sUserEmail, String sRejectedUrl) throws Exception {
	  
	  SeleniumHelper.login(sUserName, sUserPass, driver);	 
	  driver.get("http://localhost:8080/wasp/department/list.do");
	  SeleniumHelper.verifyTextPresent("Internal - Default Department", driver);
	  
	  driver.findElement(By.xpath("//a[contains(.,'Internal - Default Department')]")).click();
	  Assert.assertTrue(SeleniumHelper.verifyTextPresent(sLab, driver),"Lab "+ sLab +" not found");
	  driver.findElement(By.xpath("//a[contains(.,'"+sLab+"')]")).click();
	  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/lab/pending/reject/')]")).size() != 0, "Cannot locate REJECT link");
	  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pending/reject/')]")).click();
	  
  }

  
  @AfterClass
  public void afterClass() {
  }
  
  /**
   * 
   * @return
   * @throws Exception
   */
  @DataProvider(name = "DP1")
  public Object[][] createData1() throws Exception{
      Object[][] retObjArr = SeleniumHelper.getTableArray("WaspTestData.xls",
              "Test_001", "pendingLabApprove");
      Assert.assertNotNull(retObjArr, "object is null");
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
              "Test_001", "pendingLabReject");
      
      return(retObjArr);
  }
  

}
