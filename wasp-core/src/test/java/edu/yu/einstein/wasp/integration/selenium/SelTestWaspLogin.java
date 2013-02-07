package edu.yu.einstein.wasp.integration.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.test.util.SeleniumHelper;

/**
 * 
 * @author nvolnova
 *
 */

public class SelTestWaspLogin extends SelBaseTest {
		
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
	@Override
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
                "Test_001", "user_login");
        return(retObjArr);
    }
    /**
     * 
     * @param sUserName
     * @param sUserPass
     * @param sExpectedTxt
     * @param sExpectedUrl
     * @throws Exception
     */
  	@Test (groups = "integration-tests",  dataProvider = "DP1")
	public void testWaspLogin(String sUserName, String sUserPass, String sText, String sExpectedUrl) throws Exception {   
  		
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
		
		
        if(SeleniumHelper.verifyTextPresent("Logout", driver)) {
        	driver.findElement(By.linkText("Logout")).click();
        }
        else {
        	driver.get("http://localhost:8080/wasp/auth/login.do");
        }
    }
    
    @Override
	@AfterClass
    public void tearDown(){
        //driver.close();
        
    } 
   

}

