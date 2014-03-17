package edu.yu.einstein.wasp.integration.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.util.SeleniumHelper;

public class SelApproveNewJob extends SelBaseTest{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SelApproveNewJob.class);

	/**
     * 
     * @return retObjArr
     * @throws Exception
     */
    @DataProvider(name = "DP1")
    public Object[][] createData1() throws Exception{
        Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
                "Test_001", "approveNewJob");
        return retObjArr;
    }
    /**
     * 
     * @param sUserName
     * @param sUserPass
     */
  	@Test (groups = {"integration-tests"}, dataProvider = "DP1")
	public void approveNewJob(String sUserName, String sUserPass, String jobId, String id, String jobName, String formName) throws Exception {   
  		
  		SeleniumHelper.login(sUserName, sUserPass);	     	
    	Assert.assertNotNull(driver.findElement(By.linkText("Tasks")), "Unable to locate 'Tasks' tab.");
    	
    	
    	//QUOTE JOB
    	driver.findElement(By.linkText("Tasks")).click();   
   	    Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'/wasp/job2quote/list.do')]")), "Unable to locate 'Job Quote Tasks' link.");
   	    Assert.assertTrue(driver.findElement(By.xpath("//a[contains(@href,'/wasp/job2quote/list.do')]")).isDisplayed());	  
		driver.findElement(By.linkText("Job Quote Tasks")).click();
   	    WebElement quote = driver.findElement(By.xpath("//tbody/tr[@id='"+id+"']"));
	   	Actions action = new Actions(driver);
	   	action.doubleClick(quote);
	   	action.perform();
	   	
	   	driver.findElement(By.xpath("//input[@id='acctQuote.library_cost']")).clear();
	   	driver.findElement(By.xpath("//input[@id='acctQuote.library_cost']")).sendKeys("500");
	   	driver.findElement(By.xpath("//input[@id='acctQuote.sample_cost']")).clear();
	   	driver.findElement(By.xpath("//input[@id='acctQuote.sample_cost']")).sendKeys("400");
	   	driver.findElement(By.xpath("//input[@id='acctQuote.cell_cost']")).clear();
	   	driver.findElement(By.xpath("//input[@id='acctQuote.cell_cost']")).sendKeys("300");
	   	driver.findElement(By.xpath("//a[@id='sData']")).click();
	   	
    	//DEPARTMENT ADMIN APPROVAL
   	    driver.findElement(By.linkText("Tasks")).click();
  	    driver.findElement(By.linkText("Department Administration Tasks")).click();
  	    driver.findElement(By.xpath("//a[contains(.,'"+jobId+"')]")).click();
	  	  
  	    //radios = driver.findElements(By.xpath("//input[@type='radio' and @value='APPROVED']"));		
  	    driver.findElement(By.xpath("//form[@id='"+formName+"']/input[@type='radio' and @value='APPROVED']")).click();

	   	pause(5000);

		  for (int i = 0; i < driver.findElements(By.xpath("//input[@type='submit' and @value='SUBMIT']")).size(); i++) {  
			  if (driver.findElements(By.xpath("//input[@type='submit' and @value='SUBMIT']")).get(i).isDisplayed() ) {
				  driver.findElements(By.xpath("//input[@type='submit' and @value='SUBMIT']")).get(i).click();
			  }
		  }
		  
		  
		  //FACILITY MANAGER APPROVAL
		  driver.findElement(By.linkText("Tasks")).click();
  	      driver.findElement(By.linkText("Facility Manager Tasks")).click();
  	      driver.findElement(By.xpath("//a[contains(.,'"+jobId+"')]")).click();
	  	  
  	      //radios = driver.findElements(By.xpath("//input[@type='radio' and @value='APPROVED']"));
		  /*
  	      for (int i = 0; i < radios.size(); i++) {  
				  radios.get(i).click(); 
			  
	      }
	      */
    	    driver.findElement(By.xpath("//form[@id='"+formName+"']/input[@type='radio' and @value='APPROVED']")).click();

		  for (int i = 0; i < driver.findElements(By.xpath("//input[@type='submit' and @value='SUBMIT']")).size(); i++) {  
			  if (driver.findElements(By.xpath("//input[@type='submit' and @value='SUBMIT']")).get(i).isDisplayed() ) {
				  driver.findElements(By.xpath("//input[@type='submit' and @value='SUBMIT']")).get(i).click();
			  }
		  }

		  //LAB MANAGER APPROVAL
		  driver.findElement(By.linkText("Tasks")).click();
  	      driver.findElement(By.linkText("Lab Management Tasks")).click();
  	      driver.findElement(By.xpath("//a[contains(.,'"+jobId+"')]")).click();
      	
  	       

  	     List<WebElement> radios = driver.findElements(By.xpath("//input[@type='radio' and @value='APPROVED']"));	
		  /*
  	      for (int i = 0; i < radios.size(); i++) {  
				  radios.get(i).click(); 
			  
	      }
	      */
  	      driver.findElement(By.xpath("//form[@id='"+formName+"']/input[@type='radio' and @value='APPROVED']")).click();

		  for (int i = 0; i < driver.findElements(By.xpath("//input[@type='submit' and @value='SUBMIT']")).size(); i++) {  
			  if (driver.findElements(By.xpath("//input[@type='submit' and @value='SUBMIT']")).get(i).isDisplayed() ) {
				  driver.findElements(By.xpath("//input[@type='submit' and @value='SUBMIT']")).get(i).click();
			  }
		  }
  	   
    	  //SAMPLE RECEIVER MANAGER
		  driver.findElement(By.linkText("Tasks")).click();
	      driver.findElement(By.linkText("Sample Receiver Manager")).click();
      
	      driver.findElement(By.xpath("//tr/td[6]/span/a[contains(@onclick,'theForm"+id+"')]")).click();
	      driver.findElement(By.xpath("//tr/td[6]/span/a[contains(@onclick,'theForm"+id+"')]/../../input[@type='submit' and @value='Submit']")).click();
	      
	      //Library QC
	      driver.findElement(By.linkText("Tasks")).click();
     	  driver.findElement(By.xpath("//a[contains(@href,'/wasp/task/libraryqc/list.do')]")).click();	  
     	  driver.findElement(By.xpath("//tbody//tr/td[text()[contains(., '"+jobName+"')]]/../td[6]/form/input[@type='radio' and @value='PASSED']")).click();
		  driver.findElement(By.xpath("//tbody//tr/td[text()[contains(., '"+jobName+"')]]/../td[6]/form/input[@type='submit' and @value='Submit']")).click();
		  
		  //Sample QC
		  driver.findElement(By.linkText("Tasks")).click();
     	  driver.findElement(By.xpath("//a[contains(@href,'/wasp/task/sampleqc/list.do')]")).click();	  
     	  driver.findElement(By.xpath("//tbody//tr/td[text()[contains(., '"+jobName+"')]]/../td[6]/form/input[@type='radio' and @value='PASSED']")).click();
		  driver.findElement(By.xpath("//tbody//tr/td[text()[contains(., '"+jobName+"')]]/../td[6]/form/input[@type='submit' and @value='Submit']")).click();
	      
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

