/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.batch.tasklet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.LaunchManyJobsTasklet;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.babraham.software.TrimGalore;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * 
 * Dispatches registration of cell library files.
 * 
 * @author calder
 * 
 */
public class TrimGaloreDoRegisterTasklet extends LaunchManyJobsTasklet {

    private int runId;
    private String software;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private AdaptorService adaptorService;

    @Autowired
    private TrimGalore trimGalore;

    @Autowired
    private GridHostResolver hostResolver;

    @Autowired
    private RunService runService;
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public TrimGaloreDoRegisterTasklet(int runId, String softwareName) {
        this.runId = runId;
        this.software = softwareName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doExecute() {

        Run run = runService.getRunById(runId);

        Set<SampleSource> cellLibraries = runService.getCellLibrariesOnSuccessfulRunCellsWithoutControls(run);

        for (SampleSource cellLibrary : cellLibraries) {
            Map<String, String> jobParameters = new HashMap<String, String>();

            jobParameters.put(WaspJobParameters.RUN_ID, String.valueOf(runId));
            jobParameters.put(WaspJobParameters.CELL_LIBRARY_ID, cellLibrary.getId().toString());
            jobParameters.put(WaspJobParameters.BEAN_NAME, software);

            try {
                logger.debug("requesting launch of " + TrimGalore.MANY_REGISTRATION_NAME + " for cellLibrary " + cellLibrary.getId());
                requestLaunch(TrimGalore.MANY_REGISTRATION_NAME, jobParameters);
            } catch (WaspMessageBuildingException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
                throw new WaspRuntimeException(e);
            }
        
        }

    }

    @Override
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {

        Assert.assertParameterNotNullNotZero(runId, "Must set RUN_ID");
        Assert.assertParameterNotNull(software, "Must set BEAN_NAME");

        super.beforeStep(stepExecution);
    }

}
