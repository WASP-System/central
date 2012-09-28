package edu.yu.einstein.wasp.batchint;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class StartSpring {
  public static void main(final String[] args) throws Exception {
    @SuppressWarnings("unused")
	ApplicationContext ctx = new ClassPathXmlApplicationContext("batchint-launch-context.xml");
  }
}
