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

    @Override
    public void doExecute() {
        
        try {
            for (int i = 0; i < 1; i++) {
                Map<String,String> jobParameters = new HashMap<String,String>();
                jobParameters.put("timestamp", String.valueOf(System.currentTimeMillis()));
                jobParameters.put("i", String.valueOf(i));
                logger.debug("requesting launch of test.manySleepSteps for " + i);
                requestLaunch("test.manySleepSteps", jobParameters);
            }
            
        } catch (Exception e) {
            logger.warn("there was an error " + e.toString());
            throw new WaspRuntimeException(e);
        }
        
    }

}
