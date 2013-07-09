package edu.yu.einstein.wasp.integration.selenium;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestSeleniumProfileSetup extends SelBaseTest{
  
  @Test (groups="test-webdriver")
  public void testWebdriver() {
	  driver.get("http://"+baseUrl+"");
  }
 
}
