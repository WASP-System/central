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

public class SelDepartmentMgmt extends SelBaseTest{
  

  @Parameters("environment")
  @Test(dataProvider = "DP1")
  public void createNewDept(String environment, String sUserName, String sUserPass, String sDeptName, String sAdminName) {
	  SeleniumHelper.login(sUserName, sUserPass, driver);	 
	  String baseUrl = "localhost:8080";
	  if (environment.equals("production")) {
  		baseUrl = "barcelona.einstein.yu.edu:8080";
  	  }	
	  driver.get("http://"+baseUrl+"/wasp/dashboard.do"); 
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'#tabs-daAdmin')]")), "Unable to locate 'Dept Admin' tab.");
	  driver.findElement(By.xpath("//a[contains(@href, '#tabs-daAdmin')]")).click();
	  Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'/wasp/department/list.do')]")), "Unable to locate 'Department Management' link.");
	  WebElement element = driver.findElement(By.xpath("//a[contains(@href,'/wasp/department/list.do')]"));
	  
	  if (!element.isDisplayed())  driver.findElement(By.xpath("//a[contains(@href, '#tabs-daAdmin')]")).click();

	  driver.findElement(By.xpath("//a[contains(@href, '/wasp/department/list.do')]")).click();
	  Assert.assertEquals(driver.getCurrentUrl(),"http://"+baseUrl+"wasp/department/list.do");
	  
	  
	  Assert.assertTrue(driver.findElements(By.name("departmentName")).size() != 0, "Cannot locate 'Department Name' input text field link.");
	  Assert.assertTrue(driver.findElements(By.name("adminName")).size() != 0, "Cannot locate 'Admin Name' input text field link.");

	  WebElement elDeptName = driver.findElement(By.name("departmentName"));
	  WebElement elAdminName = driver.findElement(By.name("adminName"));
	  elDeptName.clear();
	  elAdminName.clear();
	  elDeptName.sendKeys(sDeptName);
	  elAdminName.sendKeys(sAdminName);
	  
	  WebElement elMenuItem = driver.findElement(By.xpath("/html/body/ul/li/a"));
      Actions actions = new Actions(driver);
      actions.moveToElement(elMenuItem).click().perform();

	  driver.findElement(By.xpath("//input[@value='Submit']")).click();
	  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(., '"+sDeptName+"')]")).size() != 0);
	  
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
                "Test_001", "createNewDept");
        return(retObjArr);
    }
  }

