package edu.yu.einstein.wasp.integration.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.util.SeleniumHelper;

public class SelCreateNewMachine extends SelBaseTest{
	
	@DataProvider(name = "DP1")
    public Object[][] createData1() throws Exception{
        Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
                "Test_001", "createNewMachine");
        return(retObjArr);
    }
	
	@Test (groups = "integration-tests",  dataProvider = "DP1")
	public void createIlluminaHiSeq2000(String sUserName, String sUserPass) {
		SeleniumHelper.login(sUserName, sUserPass);
		driver.get("http://"+baseUrl+"/wasp/resource/list.do");
		 
		driver.findElement(By.xpath("//td[@title='Add new row']//span[@class='ui-icon ui-icon-plus']")).click();
		driver.findElement(By.xpath("//input[@id='name']")).sendKeys("IlluminaHiSeq2000_02");
		Select select = new Select(driver.findElement(By.id("resourceCategoryId")));
		select.selectByVisibleText("Illumina HiSeq 2000"); 
		
		WebElement date = driver.findElement(By.xpath("//tr[@id='tr_resource.commission_date']//input[@id='resource.commission_date']"));
	   	Actions action = new Actions(driver);
	   	action.click(date);
	   	action.perform();
	   	
	   	driver.findElement(By.xpath("//tr[@id='tr_resource.commission_date']//input[@id='resource.commission_date']")).sendKeys("2013/05/01");
	   	driver.findElement(By.xpath("//a[@id='sData']")).click();
    	Assert.assertTrue(SeleniumHelper.verifyTextPresent("IlluminaHiSeq2000_02"), "Error creating IlluminaHiSeq2000_02 resource: Expected 'List of Machines' but got something else.");

	
	}
	
	@Test (groups = "integration-tests",  dataProvider = "DP1")
	public void createIlluminaMiSeq(String sUserName, String sUserPass) {
		SeleniumHelper.login(sUserName, sUserPass);
		driver.get("http://"+baseUrl+"/wasp/resource/list.do");
		
		driver.findElement(By.xpath("//td[@title='Add new row']//span[@class='ui-icon ui-icon-plus']")).click();
		driver.findElement(By.xpath("//input[@id='name']")).sendKeys("IlluminaMiSeq_01");
		Select select = new Select(driver.findElement(By.id("resourceCategoryId")));
		select.selectByVisibleText("Illumina MiSeq"); 
		
		WebElement date = driver.findElement(By.xpath("//tr[@id='tr_resource.commission_date']//input[@id='resource.commission_date']"));
	   	Actions action = new Actions(driver);
	   	action.click(date);
	   	action.perform();
	   	
	   	driver.findElement(By.xpath("//tr[@id='tr_resource.commission_date']//input[@id='resource.commission_date']")).sendKeys("2013/05/01");
	   	driver.findElement(By.xpath("//a[@id='sData']")).click();
    	Assert.assertTrue(SeleniumHelper.verifyTextPresent("IlluminaMiSeq_01"), "Error creating IlluminaMiSeq_01 resource: Expected 'List of Machines' but got something else.");
		
   }
   
}
