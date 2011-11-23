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

public class SeleniumWaspAddNewUser extends SeleniumBaseTest {
		
	/**
	 * Simulates Wasp login by different users using WaspTestData.xls
	 *
	 * 
	 */
	private WebElement submitLogin;
	private String sEmail;
	private String sConfirmedEmailOkUrl;
	
	
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
    @DataProvider(name = "DP2")
    public Object[][] createData1() throws Exception{
        Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
                "Test1", "addNewUser");
        /*
        System.out.println("Contents of retObjArr[0][0]" + retObjArr[0][0].toString());
        System.out.println("Contents of retObjArr[0][1]" + retObjArr[0][1].toString());
        System.out.println("Contents of retObjArr[0][2]" + retObjArr[0][2].toString());
        System.out.println("Contents of retObjArr[1][0]" + retObjArr[1][0].toString());
        System.out.println("Contents of retObjArr[1][1]" + retObjArr[1][1].toString());
        System.out.println("Contents of retObjArr[1][2]" + retObjArr[1][2].toString());
        */
        return(retObjArr);
    }
    
   /**
    * 
    * @param fName
    * @param lName
    * @param email
    * @param password
    * @param locale
    * @param primaryuserid
    * @param title
    * @param building_room
    * @param address
    * @param phone
    * @param fax
    * @param captcha
    * @param url
    * @param confEmailOkUrl
    * @throws Exception
    */
  	@Test (groups = "a", dataProvider = "DP2")
	public void navigateNewUserForm(String fName, String lName, String email, String password, String locale, String primaryuserid, String title, String building_room, String address, String phone, String fax, String captcha, String url, String confEmailOkUrl ) throws Exception {  
  		sEmail = email;
  		sConfirmedEmailOkUrl = confEmailOkUrl;
  		
  		driver.get("http://localhost:8080/wasp/auth/login.do");
  	    Assert.assertEquals(SeleniumHelper.verifyTextPresent("New User", driver), true);
		driver.findElement(By.linkText("New User")).click();
		driver.findElement(By.id("firstName")).sendKeys(fName);
		driver.findElement(By.id("lastName")).sendKeys(lName);
		driver.findElement(By.id("email")).sendKeys(email);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.name("password2")).sendKeys(password);
		driver.findElement(By.name("locale")).sendKeys(locale);
		driver.findElement(By.id("primaryuserid")).sendKeys(primaryuserid);
		driver.findElement(By.id("title")).sendKeys(title);
		driver.findElement(By.id("building_room")).sendKeys(building_room);
		driver.findElement(By.id("address")).sendKeys(address);
		driver.findElement(By.id("phone")).sendKeys(address);
		driver.findElement(By.id("fax")).sendKeys(fax);
    	
		driver.findElement(By.xpath("//img[@src='/wasp/stickyCaptchaImg.png']"));
		
		
		
		driver.findElement(By.name("captcha")).sendKeys("");
		
    	submitLogin = driver.findElement(By.xpath("//input[@type='submit']"));
    	if(submitLogin !=null){
    		submitLogin.click();
    		
    	}
    	else {
    		Reporter.log("Element: " +submitLogin+ ", is not available on page - "
                    + driver.getCurrentUrl());
    	}
    	
    	 
    	//TO DO:
    	
          //Assert.assertEquals(driver.findElement(By.tagName("h1")).getText(), sExpectedTxt);
    }
  	/*
  	@Test (dependsOnGroups="a")
  	public void confirmEmailAuth() {
        driver.get("http://localhost:8080/wasp/auth/confirmemail/authcodeform.do");
        driver.findElement(By.name("email")).sendKeys(sEmail);
        submitLogin = driver.findElement(By.xpath("//input[@type='submit']"));
    	if(submitLogin !=null){
    		submitLogin.click();
    		
    	}
    	else {
    		Reporter.log("Element: " +submitLogin+ ", is not available on page - "
                    + driver.getCurrentUrl());
    	}
    	Assert.assertEquals(driver.getCurrentUrl(), sConfirmedEmailOkUrl);
    }
    */
    @AfterClass
    public void tearDown(){
        //driver.close();
        
    } 
    	  
}

