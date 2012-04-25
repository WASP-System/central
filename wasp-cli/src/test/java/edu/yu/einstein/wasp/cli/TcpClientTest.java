package edu.yu.einstein.wasp.cli;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

/**
 * 
 * @author calder
 *
 */
public class TcpClientTest {
  @Test
  public void message() {
	  ApplicationContext ctx = new ClassPathXmlApplicationContext("cli-test-context.xml");
	  TcpGateway gw = (TcpGateway) ctx.getBean("clientGateway");
	  gw.echo("test");
  }
}
