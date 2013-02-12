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

import edu.yu.einstein.wasp.test.util.SeleniumHelper;

public class SelSubmitNewJob extends SelBaseTest{
	
	private static final Logger logger = LoggerFactory.getLogger(SelSubmitNewJob.class);

	/**
     * 
     * @return retObjArr
     * @throws Exception
     */
    @DataProvider(name = "DP1")
    public Object[][] createData1() throws Exception{
        Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
                "Test_001", "submitNewJob");
        return(retObjArr);
    }
    /**
     * 
     * @param sUserName
     * @param sUserPass
     */
  	@Test (groups = "integration-tests",  dataProvider = "DP1")
	public void submitNewJob(String sUserName, String sUserPass, String sExpectedUrl) throws Exception {   
  		
  	    driver.get("http://localhost:8080/wasp/auth/login.do");
	   	WebElement userName = driver.findElement(By.name("j_username"));
    	WebElement userPassword = driver.findElement(By.name("j_password"));
    	userName.clear();
    	userPassword.clear();
    	userName.sendKeys(sUserName);
    	userPassword.sendKeys(sUserPass);
    	
		Assert.assertNotNull(driver.findElement(By.xpath("//input[@type='submit']")), "'Submit' button does not exist");
		driver.findElement(By.xpath("//input[@type='submit']")).click();
    	Assert.assertEquals(driver.getCurrentUrl(), sExpectedUrl);

    	WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/wasp/jobsubmit/create.do')]"));
		  //Check if 'My Account' tab is open, if not, click on it to make the newrequest.do link visible
    	if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-jobUtils')]")).click();
    	
    	Assert.assertTrue(element.isDisplayed(), "Cannot locate 'Submit A Job' link");
    	driver.findElement(By.xpath("//a[contains(@href, '/wasp/jobsubmit/create.do')]")).click();
    	
    	Assert.assertTrue(driver.findElements(By.name("name")).size() != 0, "Cannot locate 'Job Name' input text box");
		driver.findElement(By.name("name")).sendKeys("Test Job 038");
		
		Select select = new Select(driver.findElement(By.name("labId")));
		select.selectByVisibleText("natalia's lab");
		
    	Assert.assertTrue(driver.findElements(By.name("workflowId")).size() != 0, "Cannot locate 'Assay Workflow' radio button");
    	driver.findElement(By.name("workflowId")).click();
		
    	driver.findElement(By.xpath("//input[@type='submit']")).click();
    	
    	select = new Select(driver.findElement(By.name("changeResource")));
    	select.selectByVisibleText("Illumina HiSeq 2000");
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Save Changes']")).click();
    	
    	//Create DNA
    	driver.findElement(By.linkText("+ ChIP-seq DNA")).click();
    	driver.findElement(By.id("name")).sendKeys("Library 001");    	
    	select = new Select(driver.findElement(By.id("species")));
    	select.selectByValue("Human [Homo sapiens - GRCh37]");
    	driver.findElement(By.id("concentration")).sendKeys("100");
    	driver.findElement(By.id("volume")).sendKeys("100");
    	select = new Select(driver.findElement(By.id("buffer")));
    	select.selectByValue("Water");    	
    	driver.findElement(By.id("A260_280")).sendKeys("200");
    	driver.findElement(By.id("A260_230")).sendKeys("300");
    	driver.findElement(By.id("fragmentSize")).sendKeys("150");
    	driver.findElement(By.id("fragmentSizeSD")).sendKeys("5");
    	driver.findElement(By.id("antibody")).sendKeys("10");
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Save Changes']")).click();

    	//Create LIBRARY
    	driver.findElement(By.linkText("+ ChIP-seq Library")).click();
    	driver.findElement(By.id("name")).sendKeys("Sample 001");  	
    	select = new Select(driver.findElement(By.id("species")));
    	select.selectByValue("Human [Homo sapiens - GRCh37]");
    	driver.findElement(By.id("fragmentSize")).sendKeys("150");
    	driver.findElement(By.id("fragmentSizeSD")).sendKeys("5");
    	driver.findElement(By.id("antibody")).sendKeys("10");	
    	driver.findElement(By.id("concentration")).sendKeys("100");
    	driver.findElement(By.id("volume")).sendKeys("100");  	
    	select = new Select(driver.findElement(By.id("buffer")));
    	select.selectByValue("Water");    	
    	select = new Select(driver.findElement(By.id("adaptorset")));
    	select.selectByValue("1");  	
    	select = new Select(driver.findElement(By.id("adaptor")));
    	select.selectByValue("1");  	
    	driver.findElement(By.id("size")).sendKeys("100");
    	driver.findElement(By.id("sizeSd")).sendKeys("10");

    	driver.findElement(By.xpath("//input[@type='submit' and @value='Save Changes']")).click();
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Next']")).click();
 
    	Assert.assertTrue(driver.findElement(By.xpath("//form[@name='jobDraft']//table[@id='cells']//td[@style='display: table-cell;']//input[@type='checkbox']")).isDisplayed());
    	
    	int checkboxCount = driver.findElements(By.xpath("//form[@name='jobDraft']//table[@id='cells']//td[@style='display: table-cell;']//input[@type='checkbox']")).size();
    	List<WebElement> checkboxes = driver.findElements(By.xpath("//form[@name='jobDraft']//table[@id='cells']//td[@style='display: table-cell;']//input[@type='checkbox']"));
    	logger.debug("checkboxCount="+checkboxCount);
        for (int i = 0; i < checkboxCount; i++) {  
        	checkboxes.get(i).click();  
        }  
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Save Changes']")).click();
    	
    	List<WebElement> checkboxesTestVsControl = driver.findElements(By.xpath("//input[@type='checkbox']"));
    	checkboxCount = driver.findElements(By.xpath("//input[@type='checkbox']")).size();
    	for (int i = 0; i < checkboxCount; i++) {  
    		checkboxesTestVsControl.get(i).click();  
        } 
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Save Changes']")).click();

    	select = new Select(driver.findElement(By.name("changeResource")));
    	select.selectByIndex(1); 
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Save Changes']")).click();
    	
    	select = new Select(driver.findElement(By.name("changeResource")));
    	select.selectByIndex(1); 
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Save Changes']")).click();
    	
    	Assert.assertTrue(SeleniumHelper.verifyTextPresent("Add A Comment", driver), "Cannot find 'Add A Comment' page");
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Next']")).click();
    	
    	Assert.assertTrue(SeleniumHelper.verifyTextPresent("Verify New Job", driver), "Cannot find 'Verify New Job' page");
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Submit Job']")).click();
    	
    	Assert.assertTrue(SeleniumHelper.verifyTextPresent("Job Successfully Submitted", driver), "Job submission did not go through. Check that wasp-daemon application is running.");
    	
    }
    
    @Override
	@AfterClass
    public void tearDown(){
        //driver.close();
        
    } 
   

}
