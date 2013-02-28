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
		  
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-home')]")), "Unable to locate 'My Account' tab.");
		  driver.findElement(By.xpath("//a[contains(@href,'#tabs-home')]")).click();

		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/lab/newrequest.do')]")).size() != 0, "Cannot locate 'Request Access to a Lab' link.");

		  WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/newrequest.do')]"));
		  //Check if 'My Account' tab is open, if not, click on it to make the newrequest.do link visible
		  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-home')]")).click();

		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/newrequest.do')]")).click();
		  Assert.assertEquals(driver.getCurrentUrl(),sUrl);
		  
		  Assert.assertTrue(driver.findElements(By.name("primaryUserLogin")).size() != 0, "Cannot locate 'Primary WUser Login' input text box");
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
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-home')]")), "Unable to locate 'My Account' tab.");
		  driver.findElement(By.xpath("//a[contains(@href,'#tabs-home')]")).click();

		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/lab/newrequest.do')]")).size() != 0, "Cannot locate 'Request Access to a Lab' link.");

		  WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/newrequest.do')]"));
		  //Check if 'My Account' tab is open, if not, click on it to make the newrequest.do link visible
		  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-home')]")).click();

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
		/*  
	      Assert.assertNotNull(driver.findElement(By.linkText("APPROVE")), "'APPROVE' link does not exist");
		  driver.findElement(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'APPROVE')]")).click();
		  Assert.assertTrue(driver.findElements(By.xpath("//p[contains(.,'"+sUserEmail+"')]/a[contains(.,'APPROVE')]")).size() == 0, "Failed to approve request to access a lab");
	    
	    */
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")), "Unable to locate 'Lab Utils' tab.");
		  driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")).click();

	      WebElement element = driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]"));
		  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-labUtils')]")).click();
		  
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pendinglmapproval/')]")).click();
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(.,'"+sUserEmail+"')]")), "Could not locate user with '"+sUserEmail+"' email.");
		  driver.findElement(By.xpath("//a[contains(.,'"+sUserEmail+"')]")).click();
		  
		  Assert.assertNotNull(driver.findElement(By.xpath("//div[contains(@id, 'accordion')]//a[contains(.,'"+sUserEmail+"')]/../../div/div/a[contains(., 'APPROVE')]")), "");
		  driver.findElement(By.xpath("//div[contains(@id, 'accordion')]//a[contains(.,'"+sUserEmail+"')]/../../div/div/a[contains(., 'APPROVE')]")).click();
		  
		  Assert.assertTrue(driver.findElements(By.xpath("//div[contains(@id, 'accordion')]//a[contains(.,'"+sUserEmail+"')]/../../div/div/a[contains(., 'APPROVE')]")).size() == 0, "Failed to APPROVE a new user");

	  
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
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-daAdmin')]")), "Unable to locate 'Dept Admin' tab.");
		  driver.findElement(By.xpath("//a[contains(@href, '#tabs-daAdmin')]")).click();
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'/wasp/department/list.do')]")), "Unable to locate 'Department Management' link.");
		  WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/wasp/department/list.do')]"));
		  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-daAdmin')]")).click();

		  driver.findElement(By.xpath("//a[contains(@href, '/wasp/department/list.do')]")).click();
		  Assert.assertEquals(driver.getCurrentUrl(),"http://localhost:8080/wasp/department/list.do");
		  
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'/wasp/department/dapendingtasklist.do')]")), "Unable to locate 'Pending Department Admin Tasks' link.");
		  driver.findElement(By.xpath("//a[contains(@href, '/wasp/department/dapendingtasklist.do')]")).click();
		  
		  Assert.assertTrue(SeleniumHelper.verifyTextPresent(sLab, driver),"Lab "+ sLab +" not found");
		  driver.findElement(By.xpath("//a[contains(.,'"+sLab+"')]")).click();

		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/lab/pending/approve/')]")).size() != 0, "Cannot locate APPROVE link");
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pending/approve/')]")).click();

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
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")), "Unable to locate 'Lab Utils' tab.");
		  driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]")).click();

	      WebElement element = driver.findElement(By.xpath("//a[contains(@href,'#tabs-labUtils')]"));
		  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-labUtils')]")).click();
		  
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pendinglmapproval/')]")).click();
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(.,'"+sUserEmail+"')]")), "Could not locate user with '"+sUserEmail+"' email.");
		  driver.findElement(By.xpath("//a[contains(.,'"+sUserEmail+"')]")).click();
		  
		  Assert.assertNotNull(driver.findElement(By.xpath("//div[contains(@id, 'accordion')]//a[contains(.,'"+sUserEmail+"')]/../../div/div/a[contains(., 'REJECT')]")), "");
		  driver.findElement(By.xpath("//div[contains(@id, 'accordion')]//a[contains(.,'"+sUserEmail+"')]/../../div/div/a[contains(., 'REJECT')]")).click();
		  
		  Assert.assertTrue(driver.findElements(By.xpath("//div[contains(@id, 'accordion')]//a[contains(.,'"+sUserEmail+"')]/../../div/div/a[contains(., 'REJECT')]")).size() == 0, "Failed to reject a new user");
		  
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
		  
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-daAdmin')]")), "Unable to locate 'Dept Admin' tab.");
		  driver.findElement(By.xpath("//a[contains(@href, '#tabs-daAdmin')]")).click();
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'/wasp/department/list.do')]")), "Unable to locate 'Department Management' link.");
		  WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/wasp/department/list.do')]"));
		  
		  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-daAdmin')]")).click();

		  driver.findElement(By.xpath("//a[contains(@href, '/wasp/department/list.do')]")).click();
		  Assert.assertEquals(driver.getCurrentUrl(),"http://localhost:8080/wasp/department/list.do");
		  
		  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'/wasp/department/dapendingtasklist.do')]")), "Unable to locate 'Pending Department Admin Tasks' link.");
		  driver.findElement(By.xpath("//a[contains(@href, '/wasp/department/dapendingtasklist.do')]")).click();
		  
		  Assert.assertTrue(SeleniumHelper.verifyTextPresent(sLab, driver),"Lab "+ sLab +" not found");
		  driver.findElement(By.xpath("//a[contains(.,'"+sLab+"')]")).click();
		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/lab/pending/reject/')]")).size() != 0, "Cannot locate REJECT link");
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
