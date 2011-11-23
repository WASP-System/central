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
public class SeleniumPendingLabs extends SeleniumBaseTest{
  
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
              "Test1", "pendingLabApprove");
      
      return(retObjArr);
  }
  
  @DataProvider(name = "DP2")
  public Object[][] createData2() throws Exception{
      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
              "Test1", "pendingLabReject");
      
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
  public void pendingLabApprove(String sUserName, String sUserPass, String sLab, String sUserEmail, String sApprovedUrl) throws Exception {   
		
	  SeleniumHelper.loginAsDA(sUserName, sUserPass, driver);	 
	  driver.get("http://localhost:8080/wasp/department/list.do");
	  SeleniumHelper.verifyTextPresent("Internal - Default Department", driver);
	  
	  driver.findElement(By.xpath("//a[contains(.,'Internal - Default Department')]")).click();
	  driver.findElement(By.xpath("//a[contains(.,'"+sLab+"')]")).click();
	  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pending/approve/')]")).click();
      
      
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
	  
	  SeleniumHelper.loginAsDA(sUserName, sUserPass, driver);	 
	  driver.get("http://localhost:8080/wasp/department/list.do");
	  SeleniumHelper.verifyTextPresent("Internal - Default Department", driver);
	  
	  driver.findElement(By.xpath("//a[contains(.,'Internal - Default Department')]")).click();
	  driver.findElement(By.xpath("//a[contains(.,'"+sLab+"')]")).click();
	  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pending/reject/')]")).click();
  }

  
  @AfterClass
  public void afterClass() {
  }

}
