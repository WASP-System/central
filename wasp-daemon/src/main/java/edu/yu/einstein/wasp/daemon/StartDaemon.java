package edu.yu.einstein.wasp.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartDaemon {

	private static final Logger logger = LoggerFactory.getLogger(StartDaemon.class);

	public static void main(final String[] args) throws Exception {
		@SuppressWarnings("unused")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/spring/daemon-launch-context.xml");
		logger.info("\n\nSpring Batch Daemon Application Launched Successfully...");
	}
}
