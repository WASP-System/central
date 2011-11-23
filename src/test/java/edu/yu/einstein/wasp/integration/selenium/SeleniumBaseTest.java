package edu.yu.einstein.wasp.integration.selenium;


import java.util.concurrent.TimeUnit;

import org.testng.annotations.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumBaseTest {
	
	public static WebDriver driver;
	public static WebDriverWait wait;
	
	
	@BeforeSuite (groups="integration-tests")
    public void setUp() throws Exception {
		
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				
	}
	
	@AfterSuite
    public void tearDown(){
        //driver.close();
        
    } 
	
	
	
	
	

}
