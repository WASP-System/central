package edu.yu.einstein.wasp.integration;

import org.testng.annotations.Test;
import org.testng.Reporter;
import org.testng.annotations.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.remote.RemoteWebDriver;

import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.server.SeleniumServer;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
//import java.net.URL;

import jxl.*; 

/**
 * 
 * @author nvolnova
 *
 */

public class SeleniumWaspTest {
		
	/**
	 * Simulates Wasp login by different users using WaspTestData.xls
	 *
	 * 
	 */
	
	
	
	static WebDriver driver;
	WebDriverWait wait;
	
	/**
	 * @BeforeClass has a groups() attribute as well, and it’s respected when you run group test suites. 
	 * If you want it to run before all methods, you need to use the alwaysRun = true:
	 * @throws Exception
	 */
	@BeforeClass (alwaysRun = true)
    public void setUp() throws Exception {
		driver = new FirefoxDriver();
		wait = new WebDriverWait(driver, 3000);
		//driver.get("http://localhost:8080/wasp/auth/login.do");
		driver.get("http://wellington.einstein.yu.edu:8080/wasp/auth/login.do");
		
	}

    /**
     * 
     * @return retObjArr
     * @throws Exception
     */
    @DataProvider(name = "DP1")
    public Object[][] createData1() throws Exception{
        Object[][] retObjArr=getTableArray("WaspTestData.xls",
                "Test1", "loginDataTable");
        System.out.println("Contents of retObjArr[0][0]" + retObjArr[0][0].toString());
        System.out.println("Contents of retObjArr[0][1]" + retObjArr[0][1].toString());
        System.out.println("Contents of retObjArr[0][2]" + retObjArr[0][2].toString());
        System.out.println("Contents of retObjArr[1][0]" + retObjArr[1][0].toString());
        System.out.println("Contents of retObjArr[1][1]" + retObjArr[1][1].toString());
        System.out.println("Contents of retObjArr[1][2]" + retObjArr[1][2].toString());
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
	public void testWaspLogin(String sUserName, String sUserPass, String sExpectedTxt, String sExpectedUrl) throws Exception {   
    	
	   	WebElement userName = driver.findElement(By.name("j_username"));
    	WebElement userPassword = driver.findElement(By.name("j_password"));
    	userName.clear();
    	userPassword.clear();
    	userName.sendKeys(sUserName);
    	userPassword.sendKeys(sUserPass);
    	
    	WebElement submitLogin = driver.findElement(By.xpath("//input[@type='submit']"));
    	if(submitLogin !=null){
    		submitLogin.click();
    		
    	}
    	else {
    		Reporter.log("Element: " +submitLogin+ ", is not available on page - "
                    + driver.getCurrentUrl());
    	}
    	   	  	
    	 
    	//TO DO:
        //Assert.assertEquals(driver.getCurrentUrl(),sExpectedUrl);
        //Assert.assertEquals(driver.findElement(By.tagName("h1")).getText(), sExpectedTxt);
        
        /*
        if (driver.findElement(By.linkText("Logout")) !=null) {
        	driver.findElement(By.linkText("Logout")).click();
        }
        else {
        	driver.get("http://localhost:8080/wasp/auth/login.do");
        }
        */
        
        if(verifyTextPresent("Logout")) {
        	driver.findElement(By.linkText("Logout")).click();
        }
        else {
        	//driver.get("http://localhost:8080/wasp/auth/login.do");
        	driver.get("http://wellington.einstein.yu.edu:8080/wasp/auth/login.do");
        }
    }
    
    @AfterClass
    public void tearDown(){
        //driver.close();
        
    } 
    /**
     * 
     * @param xlFilePath
     * @param sheetName
     * @param tableName
     * @return tabArray
     */
	public String[][] getTableArray(String xlFilePath, String sheetName, String tableName){
        String[][] tabArray=null;
        try{
            Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
            
            Sheet sheet = workbook.getSheet(sheetName);
            int startRow,startCol, endRow, endCol,ci,cj;
            Cell tableStart=sheet.findCell(tableName);
            startRow=tableStart.getRow();
            startCol=tableStart.getColumn();

            Cell tableEnd= sheet.findCell(tableName, startCol+1,startRow+1, 100, 64000,  false);                               
            endRow=tableEnd.getRow();
            endCol=tableEnd.getColumn();
            System.out.println("startRow="+startRow+", endRow="+endRow+", " +
                    "startCol="+startCol+", endCol="+endCol);
            tabArray=new String[endRow-startRow-1][endCol-startCol-1];
            ci=0;

            for (int i=startRow+1;i<endRow;i++,ci++){
                cj=0;
                for (int j=startCol+1;j<endCol;j++,cj++){
                	tabArray[ci][cj]=sheet.getCell(j,i).getContents();
                }
            }
        }
        catch (Exception e)    {
            System.out.println("error in getTableArray()");
        }

        return(tabArray);
    }
    
	public boolean verifyTextPresent(String value)
	{
		CharSequence csValue = value;
		
		if (driver.getPageSource().contains(csValue)) {
			System.out.println("Text - "+value+" - present");
			return true;
		}
		else {
			return false;
		}
		
	}

}

