package edu.yu.einstein.wasp.integration.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.util.SeleniumHelper;

public class SelSubmitNewJob extends SelBaseTest{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SelSubmitNewJob.class);

	/**
     * 
     * @return retObjArr
     * @throws Exception
     */
    @DataProvider(name = "DP1")
    public Object[][] createData1() throws Exception{
        Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
                "loadTestNewJobs", "submitNewJob");
        return retObjArr;
    }
    /**
     * 
     * @param sUserName
     * @param sUserPass
     */
  	@Test (groups = {"integration-tests", "submit-new-job"}, dataProvider = "DP1")
	public void submitNewJob(String sUserName, String sUserPass, String sJobNum, String sDnaName, String sLibName, String labName, String sExpectedUrl, String strategy) throws Exception {   
    	
  		SeleniumHelper.login(sUserName, sUserPass);
  		
    	/* Does not work. Possible firefox webdriver bug.
    	Assert.assertNotNull(driver.findElement(By.linkText("Jobs")), "Unable to locate 'Jobs' menu link.");
    	
    	Actions actions = new Actions(driver);
    	WebElement menuHoverLink = driver.findElement(By.linkText("Jobs"));
    	actions.moveToElement(menuHoverLink).build().perform();
    	WebElement subLink = driver.findElement(By.linkText("Submit A New Job"));
    	actions.moveToElement(subLink);
    	actions.click();
    	actions.perform();
    	*/
    	driver.get("http://"+baseUrl+"/wasp/jobsubmit/create.do");
    	Assert.assertTrue(driver.findElements(By.name("name")).size() != 0, "Cannot locate 'Job Name' input text box");
		driver.findElement(By.name("name")).sendKeys(sJobNum);
		
		Select select = new Select(driver.findElement(By.name("labId")));
		select.selectByVisibleText(labName);
		
		select = new Select(driver.findElement(By.id("strategy")));
		select.selectByVisibleText(strategy);
		select = new Select(driver.findElement(By.id("workflowId")));
    	select.selectByValue("1");
    	driver.findElement(By.xpath("//input[@type='submit']")).click();
    	
    	select = new Select(driver.findElement(By.name("changeResource")));
    	select.selectByVisibleText("Illumina HiSeq 2000");
    	select = new Select(driver.findElement(By.id("readLength")));
    	select.selectByValue("50");
    	select = new Select(driver.findElement(By.id("readType")));
    	select.selectByValue("single");
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Continue']")).click();
    	
    	//Create DNA
    	driver.findElement(By.linkText("New Sample")).click();
    	driver.findElement(By.id("name")).sendKeys(sDnaName);    	
    	select = new Select(driver.findElement(By.id("organism")));
    	select.selectByValue("9606");
    	driver.findElement(By.id("concentration")).sendKeys("100");
    	driver.findElement(By.id("volume")).sendKeys("100");
    	select = new Select(driver.findElement(By.id("buffer")));
    	select.selectByValue("Water");    	
    	driver.findElement(By.id("A260_280")).sendKeys("200");
    	driver.findElement(By.id("A260_230")).sendKeys("300");
    	driver.findElement(By.id("fragmentSize")).sendKeys("150");
    	driver.findElement(By.id("fragmentSizeSD")).sendKeys("5");
    	driver.findElement(By.id("antibody")).sendKeys("10");
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Save']")).click();

    	//Create LIBRARY
    	driver.findElement(By.linkText("New Library")).click();
    	driver.findElement(By.id("name")).sendKeys(sLibName);  	
    	select = new Select(driver.findElement(By.id("organism")));
    	select.selectByValue("10090");
    	driver.findElement(By.id("fragmentSize")).sendKeys("150");
    	driver.findElement(By.id("fragmentSizeSD")).sendKeys("5");
    	driver.findElement(By.id("antibody")).sendKeys("10");	
    	driver.findElement(By.id("concentration")).sendKeys("100");
    	driver.findElement(By.id("volume")).sendKeys("100");  	
    	select = new Select(driver.findElement(By.id("buffer")));
    	select.selectByValue("Water");    	
    	select = new Select(driver.findElement(By.id("adaptorset")));
    	select.selectByVisibleText("TruSEQ INDEXED DNA");  	
    	select = new Select(driver.findElement(By.id("adaptor")));
    	select.selectByVisibleText("TruSeq Adapter, Index 1 (ATCACG)");  	
    	driver.findElement(By.id("size")).sendKeys("100");
    	driver.findElement(By.id("sizeSd")).sendKeys("10");

    	driver.findElement(By.xpath("//input[@type='submit' and @value='Save']")).click();
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Continue']")).click();
    	
    	Assert.assertTrue(SeleniumHelper.verifyTextPresent("Select Genome"), "Cannot find 'Create A Job -- Select Genome' page");
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Continue']")).click();

    	
 
    	Assert.assertTrue(driver.findElement(By.xpath("//form[@name='jobDraft']//table[@id='cells']//td[@style='display: table-cell;']//input[@type='checkbox']")).isDisplayed());
    	
    	int checkboxCount = driver.findElements(By.xpath("//form[@name='jobDraft']//table[@id='cells']//td[@style='display: table-cell;']//input[@type='checkbox']")).size();
    	List<WebElement> checkboxes = driver.findElements(By.xpath("//form[@name='jobDraft']//table[@id='cells']//td[@style='display: table-cell;']//input[@type='checkbox']"));
        for (int i = 0; i < checkboxCount; i++) {  
        	checkboxes.get(i).click();  
        }  
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Continue']")).click();
    	
    	List<WebElement> checkboxesTestVsControl = driver.findElements(By.xpath("//input[@type='checkbox']"));
    	checkboxCount = driver.findElements(By.xpath("//input[@type='checkbox']")).size();
    	for (int i = 0; i < checkboxCount; i++) {  
    		checkboxesTestVsControl.get(i).click();  
        } 
        driver.findElement(By.xpath("//input[@type='submit' and @value='Continue']")).click();

    	select = new Select(driver.findElement(By.name("changeResource")));
    	select.selectByIndex(1); 
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Continue']")).click();
    	
    	select = new Select(driver.findElement(By.name("changeResource")));
    	select.selectByIndex(1); 
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Continue']")).click();
    	
    	Assert.assertTrue(SeleniumHelper.verifyTextPresent("Add A Comment"), "Cannot find 'Add A Comment' page");
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Continue']")).click();
    	
    	Assert.assertTrue(SeleniumHelper.verifyTextPresent("Verify New Job"), "Cannot find 'Verify New Job' page");
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Submit Job']")).click();
    	
    	Assert.assertTrue(SeleniumHelper.verifyTextPresent("Job Successfully Submitted"), "Job submission did not go through. Check that wasp-daemon application is running.");
    	
    }
  	
  	public static void pause(final int iTimeInMillis) {
    
      try
      {
        Thread.sleep(iTimeInMillis);
      }
      catch(InterruptedException ex)
      {
        LOGGER.debug(ex.getMessage());
      }
    }
    
    @Override
	@AfterClass
    public void tearDown(){
        //driver.close();
        
    } 
   

}
