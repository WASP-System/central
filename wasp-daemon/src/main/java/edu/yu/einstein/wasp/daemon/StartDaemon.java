package edu.yu.einstein.wasp.daemon;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class StartDaemon {
  public static void main(final String[] args) throws Exception {
    @SuppressWarnings("unused")
	ApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/spring/daemon-launch-context.xml");
  }
}
