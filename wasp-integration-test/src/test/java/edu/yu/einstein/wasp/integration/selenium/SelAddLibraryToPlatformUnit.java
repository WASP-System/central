package edu.yu.einstein.wasp.integration.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.util.SeleniumHelper;

public class SelAddLibraryToPlatformUnit extends SelBaseTest{
  
	@DataProvider(name = "DP1")
    public Object[][] createData1() throws Exception {
        Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
                "Test_001", "addLibraryToPlatformUnit");
        return(retObjArr);
    }
	@DataProvider(name = "DP2")
	public Object[][] createData2() throws Exception {
        Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
                "Test_001", "libraryQC");
        return(retObjArr);
    }
	
	@Test (groups = "integration-tests",  dataProvider = "DP1")
	public void createLibraryFromDNA(String sUserName, String sUserPass, String jobId) {
	  
	  SeleniumHelper.login(sUserName, sUserPass);
	  
	  driver.get("http://"+baseUrl+"/wasp/job/list.do");
	  driver.findElement(By.linkText(jobId)).click();
	  Assert.assertTrue(SeleniumHelper.verifyTextPresent("Samples And Libraries"), "Expected 'Samples and Libraries' page but was something else");

	  driver.findElement(By.xpath("//input[@value='Create Library']")).click();
	  Assert.assertTrue(SeleniumHelper.verifyTextPresent("New Library"), "Expected 'New Library' page but was something else");
	  
	  driver.findElement(By.xpath("//input[@id='name']")).sendKeys("libraryFromDna_01");
	  driver.findElement(By.xpath("//input[@id='concentration']")).sendKeys("12");
	  driver.findElement(By.xpath("//input[@id='volume']")).sendKeys("120");
	  
	  Select select = new Select(driver.findElement(By.id("buffer")));
	  select.selectByValue("Water");
	  select = new Select(driver.findElement(By.id("adaptorset")));
	  select.selectByVisibleText("TruSEQ INDEXED DNA");
	  select = new Select(driver.findElement(By.id("adaptor")));
	  select.selectByValue("2");
	  
	  driver.findElement(By.xpath("//input[@id='size']")).sendKeys("50");
	  driver.findElement(By.xpath("//input[@id='sizeSd']")).sendKeys("2");
	  driver.findElement(By.xpath("//input[@value='Save' and @name='submit']")).click();

	  
	}
	
	@Test (groups = "integration-tests",  dataProvider = "DP2")
	public void libraryQC(String sUserName, String sUserPass, String jobName) {
	  
	  SeleniumHelper.login(sUserName, sUserPass);
	  
	  //Library QC
      driver.findElement(By.linkText("Tasks")).click();
 	  driver.findElement(By.xpath("//a[contains(@href,'/wasp/task/libraryqc/list.do')]")).click();	  
 	  driver.findElement(By.xpath("//tbody//tr/td[text()[contains(., '"+jobName+"')]]/../td[6]/form/input[@type='radio' and @value='PASSED']")).click();
	  driver.findElement(By.xpath("//tbody//tr/td[text()[contains(., '"+jobName+"')]]/../td[6]/form/input[@type='submit' and @value='Submit']")).click();
	
	}
}
