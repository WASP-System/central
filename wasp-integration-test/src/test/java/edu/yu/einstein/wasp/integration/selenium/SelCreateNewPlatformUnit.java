package edu.yu.einstein.wasp.integration.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.util.SeleniumHelper;

public class SelCreateNewPlatformUnit extends SelBaseTest{
	
	@DataProvider(name = "DP1")
    public Object[][] createData1() throws Exception{
        Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
                "Test_001", "createIlluminaFlowCellMiSeqV1");
        return(retObjArr);
    }
	
	@Test (groups = "integration-tests",  dataProvider = "DP1")
	public void createIlluminaFlowCellMiSeqV1(String sUserName, String sUserPass) {
		SeleniumHelper.login(sUserName, sUserPass);
		
		/* The code below passes but doesn't click on sub menu
		 Actions actions = new Actions(driver);
		 WebElement menuHoverLink = driver.findElement(By.linkText("Facility"));
		 actions.moveToElement(menuHoverLink);

		 WebElement subLink = driver.findElement(By.xpath("//div[@id='menu']//ul[@class='main_menu']//a[text()[contains(., 'Platform Units')]]"));
		 actions.moveToElement(subLink);
		 WebElement subLinkNewPlatUnit = driver.findElement(By.xpath("//div[@id='menu']//ul[@class='main_menu']//a[text()[contains(., 'New Platform Unit')]]"));
		 actions.moveToElement(subLinkNewPlatUnit);
		 actions.click();
		 actions.build();
		 actions.perform();
		*/
		driver.get("http://"+baseUrl+"/wasp/facility/platformunit/createUpdatePlatformUnit.do?sampleSubtypeId=0&sampleId=0");
		 
		Select select = new Select(driver.findElement(By.name("sampleSubtypeId")));
		select.selectByVisibleText("Illumina Flow Cell MiSeq V1");
    	driver.findElement(By.id("barcode")).sendKeys("illuminaFlowCellMiSeqV1_00001");
    	select = new Select(driver.findElement(By.name("numberOfCellsRequested")));
    	select.selectByVisibleText("1");
    	
    	select = new Select(driver.findElement(By.id("readType")));
    	select.selectByValue("single");

    	select = new Select(driver.findElement(By.id("readLength")));
    	select.selectByValue("25");
    	
    	driver.findElement(By.id("comment")).sendKeys("test");
    	driver.findElement(By.xpath("//input[@type='button' and @value='Submit']")).click();
    	Assert.assertTrue(SeleniumHelper.verifyTextPresent("Platform Unit List"), "Error creating Illumina Flow Cell MiSeq V1 Platform Unit: Expected 'Platform Unit List' but got something else.");
   }
	
	@Test (groups = "integration-tests",  dataProvider = "DP1")
	public void createIlluminaFlowCellVersion3(String sUserName, String sUserPass) {
		SeleniumHelper.login(sUserName, sUserPass);
		driver.get("http://"+baseUrl+"/wasp/facility/platformunit/createUpdatePlatformUnit.do?sampleSubtypeId=0&sampleId=0");
		 
		Select select = new Select(driver.findElement(By.name("sampleSubtypeId")));
		select.selectByVisibleText("Illumina Flow Cell Version 3");
    	driver.findElement(By.id("barcode")).sendKeys("illuminaFlowcellV3_00001");
    	select = new Select(driver.findElement(By.name("numberOfCellsRequested")));
    	select.selectByVisibleText("1");
    	
    	select = new Select(driver.findElement(By.id("readType")));
    	select.selectByValue("single");

    	select = new Select(driver.findElement(By.id("readLength")));
    	select.selectByValue("50");
    	
    	driver.findElement(By.id("comment")).sendKeys("test");
    	driver.findElement(By.xpath("//input[@type='button' and @value='Submit']")).click();
    	Assert.assertTrue(SeleniumHelper.verifyTextPresent("Platform Unit List"), "Error creating illuminaFlowcellV3 Platform Unit: Expected 'Platform Unit List' but got something else.");
   }
}
