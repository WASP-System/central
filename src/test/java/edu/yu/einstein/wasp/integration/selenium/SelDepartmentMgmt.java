package edu.yu.einstein.wasp.integration.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import edu.yu.einstein.wasp.test.util.SeleniumHelper;

public class SelDepartmentMgmt extends SelBaseTest{
  
	
  @Test(dataProvider = "DP1")
  public void createNewDept(String sUseName, String sUserPass, String sDeptName, String sAdminName) {
	  SeleniumHelper.login(sUseName, sUserPass, driver);	 
	  driver.get("http://localhost:8080/wasp/dashboard.do");
	  
	  Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/department/list.do')]")).size() != 0, "Cannot locate 'Department Management' link.");
	  driver.findElement(By.xpath("//a[contains(@href,'/wasp/department/list.do')]")).click();
	  Assert.assertEquals(driver.getCurrentUrl(),"http://localhost:8080/wasp/department/list.do");
	  Assert.assertTrue(driver.findElements(By.name("departmentName")).size() != 0, "Cannot locate 'Department Name' input text field link.");
	  Assert.assertTrue(driver.findElements(By.name("adminName")).size() != 0, "Cannot locate 'Admin Name' input text field link.");

	  WebElement elDeptName = driver.findElement(By.name("departmentName"));
	  WebElement elAdminName = driver.findElement(By.name("adminName"));
	  elDeptName.clear();
	  elAdminName.clear();
	  elDeptName.sendKeys(sDeptName);
	  elAdminName.sendKeys(sAdminName);
      //WebElement menu = driver.findElement(By.xpath("//html/body/ul/li/a[@id='ui-active-menuitem']"));
      WebElement elMenuItem = driver.findElement(By.xpath("//html/body/ul/li/a[@class='ui-corner-all']"));
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
                "Test1", "deptMgmt");
        return(retObjArr);
    }
  }

