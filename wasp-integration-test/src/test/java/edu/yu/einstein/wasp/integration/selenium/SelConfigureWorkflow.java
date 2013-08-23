package edu.yu.einstein.wasp.integration.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.util.SeleniumHelper;

public class SelConfigureWorkflow extends SelBaseTest{
 
	
	private static final Logger logger = LoggerFactory.getLogger(SelSubmitNewJob.class);

	/**
     * 
     * @return retObjArr
     * @throws Exception
     */
    @DataProvider(name = "DP1")
    public Object[][] createData1() throws Exception{
        Object[][] retObjArr=SeleniumHelper.getTableArray("WaspTestData.xls",
                "Test_001", "configureWorkflow");
        return(retObjArr);
    }
    /**
     * 
     * @param sUserName
     * @param sUserPass
     */
  	@Test (groups = {"integration-tests", "config-workflow"},  dataProvider = "DP1")
	public void configureWorkflow(String sUserName, String sUserPass, String id) throws Exception {   
    	
  		SeleniumHelper.login(sUserName, sUserPass);
    	driver.get("http://"+baseUrl+"/wasp/workflow/list.do");
   	    driver.findElement(By.xpath("//tbody/tr[@id='"+id+"']/td[4]/a")).click();
   	    
   	    int checkboxCount = driver.findElements(By.xpath("//input[@type='checkbox']")).size();
	 	List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@type='checkbox']"));
	 		for (int i = 0; i < checkboxCount; i++) {  
	 			if (checkboxes.get(i).isSelected()) {
	 				checkboxes.get(i).click();
	 			}
	    }  
   	    
   	    driver.findElement(By.xpath("//input[@id='illuminaHiSeq2000']")).click();
   	    driver.findElement(By.xpath("//input[@value='illuminaHiSeq2000;illuminaHiSeq2000.allowableUiField.readLength;50:50']")).click();
   	    driver.findElement(By.xpath("//input[@value='illuminaHiSeq2000;illuminaHiSeq2000.allowableUiField.readType;single:single']")).click();
    	
   	    driver.findElement(By.xpath("//input[@id='illuminaMiSeqPersonalSequencer']")).click();
   	    driver.findElement(By.xpath("//input[@value='illuminaMiSeqPersonalSequencer;illuminaMiSeqPersonalSequencer.allowableUiField.readLength;25:25']")).click();
   	    driver.findElement(By.xpath("//input[@value='illuminaMiSeqPersonalSequencer;illuminaMiSeqPersonalSequencer.allowableUiField.readType;single:single']")).click();
   	    
   	    driver.findElement(By.xpath("//input[@id='bowtieAligner']")).click();
   	    driver.findElement(By.xpath("//input[@id='bwa']")).click();
   	    driver.findElement(By.xpath("//input[@id='macsPeakcaller']")).click();

   	    driver.findElement(By.xpath("//input[@value='Save Choices']")).click();
   	    
		driver.findElement(By.linkText("Logout")).click();
    	
  	}
}
