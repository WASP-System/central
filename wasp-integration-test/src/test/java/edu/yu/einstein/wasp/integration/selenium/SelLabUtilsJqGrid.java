package edu.yu.einstein.wasp.integration.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.util.SeleniumHelper;

public class SelLabUtilsJqGrid extends SelBaseTest{
  

	  //TO DO:
	  @Parameters("environment")
	  @Test (groups = "integration-tests",  dataProvider = "DP1")
	  public void editLab(String environment, String sUserName, String sUserPass) {
		 String baseUrl = "localhost:8080";
	  	 if (environment.equals("production")) {
	  	  	baseUrl = "barcelona.einstein.yu.edu:8080";
	  	  }
		  WebElement element;
		  
		  SeleniumHelper.login(sUserName, sUserPass, driver);	
		  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/lab/list.do')]")).size() != 0, "Cannot locate 'Lab Utils' link.");
		  driver.findElement(By.xpath("//a[contains(@href,'/wasp/lab/list.do')]")).click();
		  Assert.assertEquals(driver.getCurrentUrl(), "http://"+baseUrl+"/wasp/lab/list.do");
		  
		  WebElement elMenuItem = driver.findElement(By.xpath("//td[contains(@title, 'Greally lab')]"));
		  Actions actions = new Actions(driver);
		  
	      actions.moveToElement(elMenuItem).doubleClick().perform();
	      
	      element = driver.findElement(By.xpath("//select[contains(@id, 'lab.internal_external_lab')]"));
	      element.sendKeys("Internal");
	      	      
	      element = driver.findElement(By.xpath("//input[contains(@id, 'lab.billing_contact')]"));
	      element.sendKeys("Abc");
	      
	      element = driver.findElement(By.xpath("//input[contains(@id, 'lab.billing_institution')]"));
	      element.sendKeys("Einstein");
	      
	      element = driver.findElement(By.xpath("//select[contains(@id, 'lab.billing_departmentId')]"));
	      element.sendKeys("Internal - Default Department");
	      	      
	      element = driver.findElement(By.xpath("//input[contains(@id, 'lab.billing_address')]"));
	      element.sendKeys("1200 Morris Park Avenue");
	      
	      
	      element = driver.findElement(By.xpath("//input[contains(@id, 'lab.billing_city')]"));
	      element.sendKeys("Bronx");
	      
	      element = driver.findElement(By.xpath("//select[contains(@id, 'lab.billing_state')]"));
	      element.sendKeys("New York");
	      
	      element = driver.findElement(By.xpath("//select[contains(@id, 'lab.billing_country')]"));
	      element.sendKeys("United States");
	      
	      
	      element = driver.findElement(By.xpath("//select[contains(@id, 'lab.billing_state')]"));
	      element.sendKeys("New York");
	      
	      element = driver.findElement(By.xpath("//input[contains(@id, 'lab.billing_zip')]"));
	      element.sendKeys("10001");
	      
	      element = driver.findElement(By.xpath("//input[contains(@id, 'lab.billing_phone')]"));
	      element.sendKeys("New York");
	      
	      WebElement elSubmitButton = driver.findElement(By.xpath("//a[contains(@id, 'sData')]"));	  
	      actions.moveToElement(elSubmitButton).click().perform();
	      
	      /*
	      WebElement elCancelButton = driver.findElement(By.xpath("//a[contains(@id, 'cData')]"));	  
	      actions.moveToElement(elCancelButton).click().perform();
	      */
	      
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
	  
	  @BeforeMethod
	  public void beforeMethod() {
	  }
	  
	  @AfterMethod
	  public void afterMethod() {
	  }
	  
	  /**
	  * 
	  * @return retObjArr
	  * @throws Exception
	  */
	 @DataProvider(name = "DP1")
	 public Object[][] createData1() throws Exception{
	     Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
	         "Test_001", "editLabJQuery");
	     return(retObjArr);
	}
  }

