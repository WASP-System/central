package edu.yu.einstein.wasp.integration.selenium;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;



public class SelBaseTest {
	
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static Connection connection = null;
	
	@BeforeSuite (groups="integration-tests")
    public void setUp() throws Exception {
		
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try {
		    // Load the JDBC driver
		    String driverName = "com.mysql.jdbc.Driver"; // MySQL JDBC driver
		    Class.forName(driverName);
		
		    // Create a connection to the database
		    String serverName = "localhost:3306";
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
	
	@AfterSuite (groups="integration-tests")
    public void tearDown() throws SQLException{
        //driver.close();
		connection.close();
        
    } 
	

}
