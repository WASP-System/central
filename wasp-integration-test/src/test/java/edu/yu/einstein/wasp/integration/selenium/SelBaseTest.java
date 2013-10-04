package edu.yu.einstein.wasp.integration.selenium;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;



public class SelBaseTest {
	
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static Connection connection = null;
	public static String baseUrl;
	
	@BeforeSuite
    @Parameters ("environment")
	public void setUp(@Optional("default")String environment) throws Exception {
		baseUrl = "localhost:8080";
    	if (environment.equals("production")) {
    		baseUrl = "barcelona.einstein.yu.edu:8080";

    	}
		
		//final String firebugPath = "/Users/nvolnova/Documents/Firefox/firebug-1.8.4-fx.xpi";
	    //FirefoxProfile profile = new FirefoxProfile();
	    //profile.setEnableNativeEvents(true);

		//ProfilesIni allProfiles = new ProfilesIni();
		//FirefoxProfile profile = allProfiles.getProfile("wasp_selenium_tests");

	    //profile.addExtension(new FileHandle(firebugPath));
	    //profile.setPreference("extensions.firebug.currentVersion", "1.8.1"); // Avoid startup screen

	    String Xport = System.getProperty("Importal.xvfb.id",":99");
	    FirefoxBinary firefoxBinary = new FirefoxBinary();
	    firefoxBinary.setEnvironmentProperty("DISPLAY", Xport);
	    
	    // Add more if needed
	    driver = new FirefoxDriver(firefoxBinary, null);
	    driver.manage().window().setSize(new Dimension(1920, 1080));	   
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    
	    /*
	    driver = new SafariDriver();
	    driver.manage().window().setSize(new Dimension(1920, 1080));	   
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        */
	    
        // Take snapshot of browser
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File("ffsnapshot.png"));
		
       
		try {
		    // Load the JDBC driver
		    String driverName = "com.mysql.jdbc.Driver"; // MySQL JDBC driver
		    Class.forName(driverName);
		
		    // Create a connection to the database
		    String serverName ="localhost:3306";
		    String mydatabase = "wasp";
		    String url = "jdbc:mysql://" + serverName +  "/" + mydatabase; // a JDBC url
		    String username = "wasp";
		    String password = "waspV2";
		    connection = DriverManager.getConnection(url, username, password);
		} 
		catch (ClassNotFoundException e) {
		    // Could not find the database driver
		}
		catch (SQLException e) {
		    // Could not connect to the database
		}
		
	}
	
	@AfterSuite
    public void tearDown() throws SQLException{
        connection.close();
        //driver.quit();
        
    } 
	

}
