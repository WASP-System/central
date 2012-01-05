package edu.yu.einstein.wasp.integration.selenium;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
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

public class SelAddNewUser extends SelBaseTest {
		
	/**
	 * Simulates Wasp login by different users using WaspTestData.xls
	 *
	 * 
	 */
	private WebElement submitLogin;
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
                "Test_001", "addNewUser");
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
  	@Test (groups = "integration-tests", dataProvider = "DP2")
	public void navigateNewUserForm(String sLogin, String fName, String lName, String email, 
										String password, String locale, String primaryuserid, String title, 
										String building_room, String address, String phone, String fax, 
										String captcha, String sNewUserUrlCreated, String confEmailOkUrl ) throws Exception {  
   		
  		driver.get("http://localhost:8080/wasp/auth/login.do");
  	    Assert.assertEquals(SeleniumHelper.verifyTextPresent("New User", driver), true);
		driver.findElement(By.linkText("New User")).click();
		driver.findElement(By.id("login")).sendKeys(sLogin);
		driver.findElement(By.id("firstName")).sendKeys(fName);
		driver.findElement(By.id("lastName")).sendKeys(lName);
		driver.findElement(By.id("email")).sendKeys(email);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.name("password2")).sendKeys(password);
		/*
		WebElement element = driver.findElement(By.name("password2"));
		Assert.assertNotNull(js.executeScript("return arguments[0].sendKeys("+password+");", element));
		*/
		
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
    	
    	Assert.assertEquals(driver.getCurrentUrl(), sNewUserUrlCreated);
    }
  	
  	@Test (groups="integration-tests")
  	public void confirmEmailAuth() throws SQLException {
  		Statement s = connection.createStatement();
  		s.executeQuery("Select cea.authcode, up.email from confirmemailauth cea, userpending up where up.userpendingid=cea.userpendingid");
  		ResultSet rs = s.getResultSet();
  		
  		while (rs.next ())  {
  			String sAuthCode = rs.getString("authcode");
  			String sEmail = rs.getString("email");
  			
  			driver.get("http://localhost:8080/wasp/auth/confirmUserEmail.do?authcode="+ sAuthCode+"&email="+sEmail);
  	        
  			//Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:8080/wasp/auth/newpi/emailok.do");
  			CharSequence csValue = "/wasp/auth/newuser/emailok.do";
  			Assert.assertTrue(driver.getCurrentUrl().contains(csValue),"Confirm eamil URL= "+driver.getCurrentUrl()+"");
  	    }
  	    rs.close ();
  	    s.close ();
        
  	}
    
    @AfterClass
    public void tearDown(){
        //driver.close();
        
    } 
    	  
}

