package edu.yu.einstein.wasp.integration.selenium;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import edu.yu.einstein.wasp.test.util.SeleniumHelper;

/**
 * 
 * @author nvolnova
 *
 */

public class SeleniumNewPI extends SeleniumBaseTest {
		
	/**
	 * Simulates Wasp login by different users using WaspTestData.xls
	 *
	 * 
	 */
	
	/**
	 * @BeforeClass has a groups() attribute as well, and it’s respected when you run group test suites. 
	 * If you want it to run before all methods, you need to use the alwaysRun = true:
	 * @throws Exception
	 */
	@BeforeClass (alwaysRun = true)
    public void setUp() throws Exception {
		
	}

    
    /**
     * 
     * @return retObjArr
     * @throws Exception
     */
    @DataProvider(name = "DP1")
    public Object[][] createData1() throws Exception{
        Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
                "Test1", "addNewPI");
        return(retObjArr);
    }
     
    /**
     * 
     * @param sUrl
     * @param sFName
     * @param sLName
     * @param sEmail
     * @param pwd
     * @param locale
     * @param sLab
     * @param title
     * @param sInst
     * @param sDept
     * @param building_room
     * @param address
     * @param sCity
     * @param sState
     * @param sCountry
     * @param sZip
     * @param sPhone
     * @param sFax
     * @throws Exception
     */
  	@Test (groups = "integration-tests", dataProvider = "DP1")
	public void navigateNewPIForm(String sUrl, String sFName, String sLName, String sEmail, String pwd, String locale, String sLab, String title, String sInst, String sDept, String building_room, String address, String sCity, String sState, String sCountry, String sZip, String sPhone, String sFax) throws Exception {  
  		
  		driver.get("http://localhost:8080/wasp/auth/login.do");
		Assert.assertNotNull(driver.findElement(By.xpath("//a[contains(@href,'/wasp/auth/newpi/institute.do')]")), "New PI link does not exist");
		driver.findElement(By.xpath("//a[contains(@href,'/wasp/auth/newpi/institute.do')]")).click();
		Assert.assertEquals(driver.getCurrentUrl(), sUrl);
		
		//Select Institute
		driver.findElement(By.name("instituteSelect")).sendKeys(sInst);
 		//TO DO:  check for read-only attribute of 'other' input field.
		
		//Submit
		Assert.assertNotNull(driver.findElement(By.xpath("//input[@type='submit']")));
		driver.findElement(By.xpath("//input[@type='submit']")).click();
		
		//Fill out New PI Form
		Assert.assertEquals(driver.getCurrentUrl(), sUrl);
		
		driver.findElement(By.id("firstName")).sendKeys(sFName);
		driver.findElement(By.id("lastName")).sendKeys(sLName);
		driver.findElement(By.id("email")).sendKeys(sEmail);
		driver.findElement(By.id("password")).sendKeys(pwd);
		driver.findElement(By.name("password2")).sendKeys(pwd);
		driver.findElement(By.name("locale")).sendKeys(locale);
		driver.findElement(By.id("labName")).sendKeys(sLab);
		driver.findElement(By.id("title")).sendKeys(title);
		driver.findElement(By.id("institution")).sendKeys(sInst);
		
		driver.findElement(By.id("departmentId")).sendKeys(sDept);
		driver.findElement(By.id("building_room")).sendKeys(building_room);
		driver.findElement(By.id("address")).sendKeys(address);
		driver.findElement(By.id("city")).sendKeys(sCity);
		driver.findElement(By.id("state")).sendKeys(sState);
		driver.findElement(By.id("country")).sendKeys(sCountry);
		driver.findElement(By.id("zip")).sendKeys(sZip);
		driver.findElement(By.id("phone")).sendKeys(sPhone);
		driver.findElement(By.id("fax")).sendKeys(sFax);
		
		//Submit New PI Form
		Assert.assertNotNull(driver.findElement(By.xpath("//input[@type='submit']")));
		driver.findElement(By.xpath("//input[@type='submit']")).click();
		Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:8080/wasp/auth/newpi/created.do");
				
     	
    }
  	/*  TO DO:
  	@Test (dependsOnGroups="a")
  	public void confirmEmailAuth() {
        driver.get("http://localhost:8080/wasp/auth/confirmemail/authcodeform.do");
        driver.findElement(By.name("email")).sendKeys(sEmail);
        submitLogin = driver.findElement(By.xpath("//input[@type='submit']"));
    	Assert.assertNotNull(driver.findElement(By.xpath("//input[@type='submit']")));
    }
    */
    @AfterClass
    public void tearDown(){
        //driver.close();
        
    } 
    	  
}
