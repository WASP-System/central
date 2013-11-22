package edu.yu.einstein.wasp.util;


import java.io.File;

import junit.framework.Assert;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import edu.yu.einstein.wasp.integration.selenium.SelBaseTest;

/**
 * 
 * @author nvolnova
 *
 */
public class SeleniumHelper extends SelBaseTest{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumHelper.class);
	
	/**
	* 
	* @param value
	* @return boolean
	*/
	public static boolean verifyTextPresent(String value) {
	
		CharSequence csValue = value;
		
		if (driver.getPageSource().contains(csValue)) {
			LOGGER.debug("Text - "+value+" - present");
			return true;
		}
		return false;
	}
	
	 /**
	 * 
	 * @param xlFilePath
	 * @param sheetName
	 * @param tableName
	 * @return tabArray
	 */
	  public static String[][] getTableArray(String xlFilePath, String sheetName, String tableName){
	    String[][] tabArray=null;
	    try{
	        LOGGER.debug("xlFilePath="+xlFilePath);
	        Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
	        if (workbook == null) LOGGER.debug("workbook is null");

	        Sheet sheet = workbook.getSheet(sheetName);
	        if (sheet == null) LOGGER.debug("sheet is null");

	        LOGGER.debug("sheet name="+sheetName);
	        int startRow;
	        int startCol;
	        int endRow;
	        int endCol;
	        int ci; 
	        int cj;
	        LOGGER.debug("table name="+tableName);
	        Cell tableStart=sheet.findCell(tableName);
	        LOGGER.debug("tableStart="+tableStart+"\n table name="+tableName);
	        startRow=tableStart.getRow();
	        LOGGER.debug("got row start");

	        startCol=tableStart.getColumn();
	
	        Cell tableEnd= sheet.findCell(tableName, startCol+1,startRow+1, 100, 64000,  false);                               
	        endRow=tableEnd.getRow();
	        endCol=tableEnd.getColumn();
	        LOGGER.debug("startRow="+startRow+", endRow="+endRow+", " +
	                "startCol="+startCol+", endCol="+endCol);
	        tabArray=new String[endRow-startRow-1][endCol-startCol-1];
	        ci=0;
	
	        for (int i=startRow+1;i<endRow;i++,ci++){
	            cj=0;
	            for (int j=startCol+1;j<endCol;j++,cj++){
	            	tabArray[ci][cj]=sheet.getCell(j,i).getContents();
	    	        LOGGER.debug("contents="+sheet.getCell(j,i).getContents());

	            }
	        }
	    }
	    catch (Exception e)    {
	        LOGGER.debug("error in getTableArray()");
	    }
	
	    return tabArray;
	  }

	  /**
	   * 
	   * @param sUserName
	   * @param sUserPass
	   * @param driver
	   */
	  public static void login(String sUserName, String sUserPass) {
		  driver.get("http://"+baseUrl+"/wasp");
	  	  if (SeleniumHelper.verifyTextPresent("Logout")) {
	  			driver.findElement(By.linkText("Logout")).click();
	  	  } 
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
		  Assert.assertEquals(driver.getCurrentUrl(), "http://"+baseUrl+"/wasp/dashboard.do");
	  }
	  
	  public static boolean isElementPresent(WebDriver driver, String xpathLocator, Boolean displayCustomMessage, String customMessage) {
	        try {
	            
	        	driver.findElement(By.xpath(xpathLocator));
	        
	        } catch (org.openqa.selenium.NoSuchElementException Ex) {
	            if (displayCustomMessage) {
	                if (!customMessage.equals("")) {
	                    LOGGER.debug(customMessage);
	                }
	            } else {
	                LOGGER.debug("Unable to locate Element: " + xpathLocator);
	            }
	            return false;
	        }
	        return true;
	    }


}
