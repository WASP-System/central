package edu.yu.einstein.wasp.integration.selenium;

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
		driver.findElement(By.name("name")).sendKeys("Test Job 015");
		
		Select select = new Select(driver.findElement(By.name("labId")));
		select.selectByVisibleText("natalia's lab");
		
    	Assert.assertTrue(driver.findElements(By.name("workflowId")).size() != 0, "Cannot locate 'Assay Workflow' radio button");
    	driver.findElement(By.name("workflowId")).click();
		
    	driver.findElement(By.xpath("//input[@type='submit']")).click();
    	
    	Select selectResource = new Select(driver.findElement(By.name("changeResource")));
    	selectResource.selectByVisibleText("Illumina HiSeq 2000");
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Save Changes']")).click();
    	
    	//Create DNA
    	driver.findElement(By.linkText("+ ChIP-seq DNA")).click();
    	driver.findElement(By.id("name")).sendKeys("Library 001");    	
    	Select selectSpecies = new Select(driver.findElement(By.id("species")));
    	selectSpecies.selectByValue("Human [Homo sapiens - GRCh37]");
    	driver.findElement(By.id("concentration")).sendKeys("100");
    	driver.findElement(By.id("volume")).sendKeys("100");
    	Select selectBuffer = new Select(driver.findElement(By.id("buffer")));
    	selectBuffer.selectByValue("Water");    	
    	driver.findElement(By.id("A260_280")).sendKeys("200");
    	driver.findElement(By.id("A260_230")).sendKeys("300");
    	driver.findElement(By.id("fragmentSize")).sendKeys("150");
    	driver.findElement(By.id("fragmentSizeSD")).sendKeys("5");
    	driver.findElement(By.id("antibody")).sendKeys("10");
    	driver.findElement(By.xpath("//input[@type='submit' and @value='Save Changes']")).click();

    	//Create LIBRARY
    	driver.findElement(By.linkText("+ ChIP-seq Library")).click();
    	driver.findElement(By.id("name")).sendKeys("Sample 001");  	
    	Select selectSpeciesLib = new Select(driver.findElement(By.id("species")));
    	selectSpeciesLib.selectByValue("Human [Homo sapiens - GRCh37]");
    	driver.findElement(By.id("fragmentSize")).sendKeys("150");
    	driver.findElement(By.id("fragmentSizeSD")).sendKeys("5");
    	driver.findElement(By.id("antibody")).sendKeys("10");	
    	driver.findElement(By.id("concentration")).sendKeys("100");
    	driver.findElement(By.id("volume")).sendKeys("100");  	
    	Select selectBufferLib = new Select(driver.findElement(By.id("buffer")));
    	selectBufferLib.selectByValue("Water");    	
    	Select selectAdaptorSet = new Select(driver.findElement(By.id("adaptorset")));
    	selectAdaptorSet.selectByValue("1");  	
    	Select selectAdaptor = new Select(driver.findElement(By.id("adaptor")));
    	selectAdaptor.selectByValue("1");  	
    	driver.findElement(By.id("size")).sendKeys("100");
    	driver.findElement(By.id("sizeSd")).sendKeys("10");

    	driver.findElement(By.xpath("//input[@type='submit' and @value='Save Changes']")).click();

    	driver.findElement(By.xpath("//input[@type='submit' and @value='Next']")).click();
    	
        driver.findElement(By.name("sdc_45_1")).click();


    	Assert.assertTrue(driver.findElement(By.xpath("//table[contains(@id='cells')//input[@type='checkbox' ]")).isDisplayed(), "checkbox not displayed");
    	
    	int checkboxCount = driver.findElements(By.xpath("//checkbox")).size();
    	logger.debug("checkboxCount="+checkboxCount);
        for (int i = 0; i < checkboxCount; i++) {  
        	driver.findElement(By.xpath("//tr[" + (checkboxCount) + "]//checkbox")).click();  
        }  

		
		/*
        if(SeleniumHelper.verifyTextPresent("Logout", driver)) {
        	driver.findElement(By.linkText("Logout")).click();
        }
        else {
        	driver.get("http://localhost:8080/wasp/auth/login.do");
        }
        */
    }
    
    @Override
	@AfterClass
    public void tearDown(){
        //driver.close();
        
    } 
   

}
