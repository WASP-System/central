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

public class SelNewRequest extends SelBaseTest {
	
	 @BeforeClass
	  public void beforeClass() {
		  
	  }
	  	  
	  /**
	   * 
	   * @param sUserName
	   * @param sUserPass
	   * @param sPIName
	   * @param sUrl
	   * @param successUrl
	   * @throws Exception
	   */
	  @Test (groups = "integration-tests",  dataProvider = "DP1")
	  public void requestAccessToLab(String sUserName, String sUserPass,String sPIName, String sUrl, String successUrl) throws Exception {   
			
		  SeleniumHelper.login(sUserName, sUserPass, driver);	 
		  driver.get("http://localhost:8080/wasp/dashboard.do");
		  
		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/lab/newrequest.do')]")).size() != 0, "Cannot locate 'REQUEST ACCESS TO LAB' link.");
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/newrequest.do')]")).click();
		  
		  Assert.assertEquals(driver.getCurrentUrl(),sUrl);
		  
		  Assert.assertTrue(driver.findElements(By.name("primaryUserLogin")).size() != 0, "Cannot locate 'Primary User Login' input text box");
		  driver.findElement(By.name("primaryUserLogin")).sendKeys(sPIName);
	  
		  Assert.assertTrue(driver.findElements(By.xpath("//input[@value='Request Access']")).size() != 0, "Cannot locate 'Request Access' submit button.");
		  driver.findElement(By.xpath("//input[@value='Request Access']")).click();
	      
		  Assert.assertEquals(driver.getCurrentUrl(), successUrl);
	  }
	  
	  /**
	   * 
	   * @param sUserName
	   * @param sUserPass
	   * @param sLab
	   * @param sPhone
	   * @param sAddress
	   * @param sPhone2
	   * @param sUrl
	   * @param successUrl
	   * @throws Exception
	   */
	  @Test (groups = "integration-tests",  dataProvider = "DP2")
	  public void createNewLabRequest(String sUserName, String sUserPass,String sLab, String sPhone, 
			  						  String sAddress, String sPhone2, String sUrl, String successUrl) throws Exception {   
			
		  SeleniumHelper.login(sUserName, sUserPass, driver);	 
		  driver.get("http://localhost:8080/wasp/dashboard.do");
		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/lab/newrequest.do')]")).size() != 0, "Cannot locate 'REQUEST ACCESS TO LAB' link.");
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/newrequest.do')]")).click();
		  Assert.assertEquals(driver.getCurrentUrl(),sUrl);
		  
		  Assert.assertTrue(driver.findElements(By.id("name")).size() != 0, "Cannot locate 'Lab Name' input text box");
		  driver.findElement(By.id("name")).sendKeys(sLab);
	  
		  Assert.assertTrue(driver.findElements(By.id("phone")).size() != 0, "Cannot locate 'Phone' input text box");
		  driver.findElement(By.id("phone")).sendKeys(sPhone);
		  
		  Assert.assertTrue(driver.findElements(By.id("billing_address")).size() != 0, "Cannot locate 'Address' input text box");
		  driver.findElement(By.id("billing_address")).sendKeys(sAddress);
		  
		  Assert.assertTrue(driver.findElements(By.id("billing_phone")).size() != 0, "Cannot locate 'Address' input text box");
		  driver.findElement(By.id("billing_phone")).sendKeys(sPhone2);
		  
		  Assert.assertTrue(driver.findElements(By.xpath("//input[@value='Submit']")).size() != 0, "Cannot locate 'Submit' button.");
		  driver.findElement(By.xpath("//input[@value='Submit']")).click();
		  
		  Assert.assertEquals(driver.getCurrentUrl(), successUrl);
	      
	  }
	  
	  /**
	   * 
	   * @param sUserName
	   * @param sUserPass
	   * @param sUserEmail
	   */
	  @Test (groups = "integration-tests",  dataProvider = "DP3")
	  public void approveAccessToLab(String sUserName, String sUserPass, String sUserEmail) {
		  
		  SeleniumHelper.login(sUserName, sUserPass, driver);
		  driver.findElement(By.xpath("//a[contains(.,'Pending User Approval')]")).click();
		  
	      Assert.assertNotNull(driver.findElement(By.linkText("APPROVE")), "'APPROVE' link does not exist");
		  driver.findElement(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'APPROVE')]")).click();
		  Assert.assertTrue(driver.findElements(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'APPROVE')]")).size() == 0, "Failed to approve request to access a lab");
	  
	  }
	  
	  /**
	   * 
	   * @param sUserName
	   * @param sUserPass
	   * @param sLab
	   */
	  @Test (groups = "integration-tests",  dataProvider = "DP4")
	  public void approveCreateNewLabRequest (String sUserName, String sUserPass, String sLab) {
		  
		  SeleniumHelper.login(sUserName, sUserPass, driver);	 
		  driver.get("http://localhost:8080/wasp/department/list.do");
		  
		  driver.findElement(By.xpath("//a[contains(@href, '/wasp/department/dapendingtasklist.do')]")).click();
		  Assert.assertTrue(SeleniumHelper.verifyTextPresent(sLab, driver),"Lab "+ sLab +" not found");
		  driver.findElement(By.xpath("//a[contains(.,'"+sLab+"')]")).click();
		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/lab/pending/approve/')]")).size() != 0, "Cannot locate APPROVE link");
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pending/approve/')]")).click();
		  //Assert.assertTrue(SeleniumHelper.verifyTextPresent("New lab application successfully approved", driver));
		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(.,'"+sLab+"')]")).size() == 0, "Failed to approve create new lab request");

	  }
	  
	  /**
	   * 	
	   * @param sUserName
	   * @param sUserPass
	   * @param sUserEmail
	   */
	  @Test (groups = "integration-tests",  dataProvider = "DP5")
	  public void rejecteAccessToLab(String sUserName, String sUserPass, String sUserEmail) {
		  
		  SeleniumHelper.login(sUserName, sUserPass, driver);
		  driver.findElement(By.xpath("//a[contains(.,'Pending User Approval')]")).click();
		  
	      Assert.assertNotNull(driver.findElement(By.linkText("REJECT")), "'REJECT' link does not exist");
		  driver.findElement(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'REJECT')]")).click();
		  Assert.assertTrue(driver.findElements(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'REJECT')]")).size() == 0, "Failed to reject request to access a lab");
		  
		  
	  }
	  
	  /**
	   * 
	   * @param sUserName
	   * @param sUserPass
	   * @param sLab
	   */
	  @Test (groups = "integration-tests",  dataProvider = "DP6")
	  public void rejectCreateNewLabRequest (String sUserName, String sUserPass, String sLab) {
		  
		  SeleniumHelper.login(sUserName, sUserPass, driver);	 
		  driver.get("http://localhost:8080/wasp/department/list.do");
		  
		  driver.findElement(By.xpath("//a[contains(@href, '/wasp/department/dapendingtasklist.do')]")).click();
		  Assert.assertTrue(SeleniumHelper.verifyTextPresent(sLab, driver),"Lab "+ sLab +" not found");
		  driver.findElement(By.xpath("//a[contains(.,'"+sLab+"')]")).click();
		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/lab/pending/reject/')]")).size() != 0, "Cannot locate Reject link");
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pending/reject/')]")).click();
		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(.,'"+sLab+"')]")).size() == 0, "Failed to reject create new lab request");
		  
		  
		  
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
	              "Test_001", "requestAccessToLab");
	      
	      return(retObjArr);
	  }
	  
	  @DataProvider(name = "DP2")
	  public Object[][] createData2() throws Exception{
	      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
	              "Test_001", "createNewLabRequest");
	      
	      return(retObjArr);
	  }
	  @DataProvider(name = "DP3")
	  public Object[][] createData3() throws Exception{
	      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
	              "Test_001", "approveAccessToLab");
	      
	      return(retObjArr);
	  }
	  @DataProvider(name = "DP4")
	  public Object[][] createData4() throws Exception{
	      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
	              "Test_001", "approveCreateNewLabRequest");
	      
	      return(retObjArr);
	  }
	  @DataProvider(name = "DP5")
	  public Object[][] createData5() throws Exception{
	      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
	              "Test_001", "rejecteAccessToLab");
	      
	      return(retObjArr);
	  }
	  @DataProvider(name = "DP6")
	  public Object[][] createData6() throws Exception{
	      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
	              "Test_001", "rejectCreateNewLabRequest");
	      
	      return(retObjArr);
	  }

}
