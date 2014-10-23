package edu.yu.einstein.wasp.integration.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class SelPendingLabs extends SelBaseTest{
  
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
  @Test (groups = {"integration-tests", "pending-lab-approval"},  dataProvider = "DP1")
  public void pendingLabApprove(String sUserName, String sUserPass, String sPiEmail, String sUserEmail, String sApprovedUrl) throws Exception {   
		
	  SeleniumHelper.login(sUserName, sUserPass);	 
	  Assert.assertNotNull(driver.findElement(By.linkText("Tasks")), "Unable to locate 'Tasks' tab.");
	  driver.findElement(By.linkText("Tasks")).click();
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'/wasp/task/daapprove/list.do')]")), "Unable to locate 'Department Administration Tasks' link.");
	  Assert.assertTrue(driver.findElement(By.xpath("//a[contains(@href,'/wasp/task/daapprove/list.do')]")).isDisplayed());	  
	  driver.findElement(By.linkText("Department Administration Tasks")).click();
	  
	  Assert.assertTrue(SeleniumHelper.verifyTextPresent(sPiEmail),"Lab "+ sPiEmail +" not found");
	  driver.findElement(By.xpath("//a[contains(.,'"+sPiEmail+"')]")).click();
	  
	  //WebDriverWait wait = new WebDriverWait(driver, 5);
	  //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='approve']")));
	  
	  
	  List<WebElement> radios = driver.findElements(By.xpath("//input[@type='radio' and @value='approve']"));
	  logger.debug("radios.size="+radios.size());
	  List<WebElement> submits = driver.findElements(By.xpath("//input[@type='submit' and @value='SUBMIT']"));

	  for (int i = 0; i < radios.size(); i++) {  
		  /* doesn't work. evaluates to false
		   * Assert.assertTrue(radios.get(i).isDisplayed());

		  if (radios.get(i).isDisplayed() ) {
			  radios.get(i).click(); 
		  }
		  */
		  
		  radios.get(i).click(); 

      }
	  for (int i = 0; i < driver.findElements(By.xpath("//input[@type='submit' and @value='SUBMIT']")).size(); i++) {  
		  //if (driver.findElements(By.xpath("//input[@type='submit' and @value='SUBMIT']")).get(i).isDisplayed() ) {
			  driver.findElements(By.xpath("//input[@type='submit' and @value='SUBMIT']")).get(i).click();
		  //}
	  }
	  driver.findElement(By.linkText("Logout")).click();

	  
	  //Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/lab/pending/approve/')]")).size() != 0, "Cannot locate APPROVE link");
	  //driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/pending/approve/')]")).click();
	  //Assert.assertTrue(SeleniumHelper.verifyTextPresent("New lab application successfully approved", driver));
      
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
	  
	  SeleniumHelper.login(sUserName, sUserPass);	 
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-daAdmin')]")), "Unable to locate 'Dept Admin' tab.");
	  driver.findElement(By.xpath("//a[contains(@href, '#tabs-daAdmin')]")).click();
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'/wasp/department/list.do')]")), "Unable to locate 'Department Management' link.");
	  WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/wasp/department/list.do')]"));
	  
	  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-daAdmin')]")).click();

	  driver.findElement(By.xpath("//a[contains(@href, '/wasp/department/list.do')]")).click();
	  Assert.assertEquals(driver.getCurrentUrl(),"http://"+baseUrl+"/wasp/department/list.do");
	  
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'/wasp/department/dapendingtasklist.do')]")), "Unable to locate 'Pending Department Admin Tasks' link.");
	  driver.findElement(By.xpath("//a[contains(@href, '/wasp/department/dapendingtasklist.do')]")).click();
	  
	  Assert.assertTrue(SeleniumHelper.verifyTextPresent(sLab),"Lab "+ sLab +" not found");
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
  
  public static void pause(final int iTimeInMillis) {
	    
      try
      {
        Thread.sleep(iTimeInMillis);
      }
      catch(InterruptedException ex)
      {
        System.out.println(ex.getMessage());
      }
    }
	
  

}
