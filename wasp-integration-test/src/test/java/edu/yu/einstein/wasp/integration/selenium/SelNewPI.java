package edu.yu.einstein.wasp.integration.selenium;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.util.SeleniumHelper;

/**
 * 
 * @author nvolnova
 *
 */

public class SelNewPI extends SelBaseTest {
		
	/**
	 * Creates PIs using WaspTestData.xls
	 *
	 * 
	 */
	
	String sEmail;
	

	/**
     * 
     * @param sUrl
     * @param sLogin
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
     * @param sNewUserPICreated
     * @throws Exception
     */
  	@Test (groups = "integration-tests", dataProvider = "DP1")
	public void navigateNewPIForm(String sUrl, String sLogin, String sFName, String sLName, 
									String sEmail, String pwd, String locale, String sLab, 
									String title, String sInst, String sDept, String building_room, 
									String address, String sCity, String sState, String sCountry, 
									String sZip, String sPhone, String sFax, String sNewUserPICreated) throws Exception {  
  		Assert.assertNotNull(driver);	
  		driver.get("http://"+baseUrl+"/wasp/auth/login.do");
    	Assert.assertTrue(driver.findElements(By.xpath("//a[contains(@href,'/wasp/auth/newpi/institute.do')]")).size() != 0, "Cannot locate New PI link on login page");
		driver.findElement(By.xpath("//a[contains(@href,'/wasp/auth/newpi/institute.do')]")).click();
		Assert.assertEquals(driver.getCurrentUrl(), "http://"+baseUrl+"/wasp/auth/newpi/institute.do");
		
		//Select Institute
    	Assert.assertTrue(driver.findElements(By.name("instituteSelect")).size() != 0, "Cannot locate 'Select Institute' drop-down");
		driver.findElement(By.name("instituteSelect")).sendKeys(sInst);
 		//TO DO:  check for read-only attribute of 'other' input field.
		pause(4000);
		//Submit
		Assert.assertNotNull(driver.findElement(By.xpath("//input[@type='submit']")));
		driver.findElement(By.xpath("//input[@type='submit']")).click();
		
		//Fill out New PI Form
		Assert.assertEquals(driver.getCurrentUrl(), "http://"+baseUrl+"/wasp/auth/newpi/institute.do");

		driver.findElement(By.id("login")).sendKeys(sLogin);
		driver.findElement(By.id("firstName")).sendKeys(sFName);
		driver.findElement(By.id("lastName")).sendKeys(sLName);
		driver.findElement(By.id("email")).sendKeys(sEmail);
		driver.findElement(By.id("password")).sendKeys(pwd);
		driver.findElement(By.name("password2")).sendKeys(pwd);
		driver.findElement(By.name("locale")).sendKeys(locale);
		driver.findElement(By.id("labName")).sendKeys(sLab);
		pause(3000);
		driver.findElement(By.id("title")).sendKeys(title);
		driver.findElement(By.id("institution")).sendKeys(sInst);
		pause(3000);
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
    	Assert.assertTrue(driver.findElements(By.xpath("//input[@type='submit']")).size() != 0, "Cannot locate Apply for Account submit button");
		driver.findElement(By.xpath("//input[@type='submit']")).click();

		Assert.assertEquals(driver.getCurrentUrl(), "http://"+baseUrl+"/wasp/auth/newpi/created.do");
				
     	
    }
  	public static void pause(final int iTimeInMillis) {
  	    
        try
        {
          Thread.sleep(iTimeInMillis);
        }
        catch(InterruptedException ex)
        {
          System.out.println(ex.getMessage());
        }
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
                "Test_001", "addNewPI");
        Assert.assertNotNull(retObjArr, "object is null");

        return(retObjArr);
    }
    
    /**
     * 
     * @throws SQLException
     */
  	@Test (groups="integration-tests")
  	public void confirmEmailAuth() {
    	
  		try {
	  		Statement s = connection.createStatement();
	  		s.executeQuery("Select cea.authcode, up.email from confirmemailauth cea, userpending up where up.id=cea.userpendingid");
	  		ResultSet rs = s.getResultSet();
	  		
	  		while (rs.next ())  {
	  			String sAuthCode = rs.getString("authcode");
	  			String sEmail = rs.getString("email");
	  			
	  			driver.get("http://"+baseUrl+"/wasp/auth/confirmPIEmail.do?authcode="+ sAuthCode+"&email="+sEmail);
	  	        
	  			CharSequence csValue = "/wasp/auth/newpi/emailok.do";
	  			Assert.assertTrue(driver.getCurrentUrl().contains(csValue));
	  	    }
	  	    rs.close ();
	  	    s.close ();
  		}
  		catch (SQLException e){
  		
  		}
        
  	}
    	  
}

