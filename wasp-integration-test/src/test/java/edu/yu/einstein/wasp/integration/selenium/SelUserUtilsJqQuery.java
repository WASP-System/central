package edu.yu.einstein.wasp.integration.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.util.SeleniumHelper;


public class SelUserUtilsJqQuery extends SelBaseTest {
 	  
	  //TO DO:
	  @Test (groups = "integration-tests",  dataProvider = "DP1")
	  public void f(String sUserName, String sUserPass) {

		  SeleniumHelper.login(sUserName, sUserPass, driver);	
		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/user/list.do')]")).size() != 0, "Cannot locate 'User Utils' link.");
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/user/list.do')]")).click();
		  Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:8080/wasp/user/list.do");
		  
		  WebElement elMenuItem = driver.findElement(By.xpath("//td[contains(@title, 'jgreally')]"));
		  Actions actions = new Actions(driver);
	      actions.moveToElement(elMenuItem).doubleClick().perform();
	      
	      //WebElement elCancelButton = driver.findElement(By.xpath("//a[contains(@id, 'cData')]"));	
	      	      
	      //actions.moveToElement(elCancelButton).click().perform();
	      
	      /*
		  //Add New Row
		  WebElement elAddRow = driver.findElement(By.xpath("//td[contains(@id, 'add_grid_id')]"));
		  actions.moveToElement(elAddRow).click().perform();
		  
		  
		  //Edit
		  WebElement elEditRow = driver.findElement(By.xpath("//td[contains(@id, 'edit_grid_id')]"));
		  actions.moveToElement(elEditRow).click().perform();
		  
		  //View Selected Row
		  WebElement elViewRow = driver.findElement(By.xpath("//td[contains(@id, 'view_grid_id')]"));
		  actions.moveToElement(elViewRow).click().perform();
		  
		  //Delete Selected Row
		  WebElement elDelRow = driver.findElement(By.xpath("//td[contains(@id, 'del_grid_id')]"));
		  actions.moveToElement(elDelRow).click().perform();
		  
		  //Find Records
		  WebElement elFindRec = driver.findElement(By.xpath("//td[contains(@id, 'search_grid_id')]"));
		  actions.moveToElement(elFindRec).click().perform();
		  
		  */
		  
	  }
	  
	   @Override
	@AfterClass
	   public void tearDown(){
		   //driver.close();
		     
	   } 
	  
	  /**
	   * 
	   * @return retObjArr
	   * @throws Exception
	   */
		@DataProvider(name = "DP1")
		public Object[][] createData1() throws Exception{
		    Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
		        "Test_001", "addNewUserJQuery");
		    return(retObjArr);
		}
	  
}
