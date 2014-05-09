package edu.yu.einstein.wasp.daemon.test.tasklet;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.daemon.batch.tasklets.LaunchManyJobsTasklet;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;

/**
 * @author calder
 *
 */
public class TestLaunchManyJobsTasklet extends LaunchManyJobsTasklet {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private String method;
    
    public TestLaunchManyJobsTasklet(String testMethod) {
        method = testMethod;
        logger.debug("TestLaunchManyJobsTasklet configured to run in mode " + method);
    }

    @Override
    public void doExecute() {
        if (method.equals("none"))
        	return;
        try {
            for (int i = 0; i < 5; i++) {
                Map<String,String> jobParameters = new HashMap<String,String>();
                jobParameters.put("timestamp", String.valueOf(System.currentTimeMillis()));
                jobParameters.put("i", String.valueOf(i));
                logger.debug("requesting launch of test.manySleepSteps for " + i +":"+ method);
                if (method.equals("sleep") || i > 0) {
                    logger.debug("method sleep");
                    requestLaunch("test.manySleepSteps", jobParameters);
                } else {
                    logger.debug("method abandon");
                    requestLaunch("test.manyAbandonSteps", jobParameters);
                }
            }
            
        } catch (Exception e) {
            logger.warn("there was an error " + e.toString());
            throw new WaspRuntimeException(e);
        }
        
    }

}
