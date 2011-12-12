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

public class SeleniumNewRequest extends SeleniumBaseTest {
	
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
	              "Test1", "requestAccessToLab");
	      
	      return(retObjArr);
	  }
	  
	  @DataProvider(name = "DP2")
	  public Object[][] createData2() throws Exception{
	      Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
	              "Test1", "createNewLabRequest");
	      
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
	  
	  @Test (groups = "integration-tests",  dataProvider = "DP2")
	  public void createNewLabRequest(String sUserName, String sUserPass,String sLab, String sPhone, String sAddress, String sPhone2, String sUrl, String successUrl) throws Exception {   
			
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

	  @AfterClass
	  public void afterClass() {
	  
	  }
}
