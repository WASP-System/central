/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.batch.tasklet;

import java.util.Set;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.babraham.batch.tasklet.jobparameters.TrimGaloreParameters;
import edu.yu.einstein.wasp.plugin.babraham.software.TrimGalore;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * 
 * Performs the registration of files on a per- cellLibrary basis.
 * 
 * @author calder
 * 
 */
public class TrimGaloreRegisteringTasklet extends WaspRemotingTasklet {

    private int cellLibraryId;
    private String softwareClass;

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

    public TrimGaloreRegisteringTasklet(int cellLibraryId, String softwareClassName) {
        this.cellLibraryId = cellLibraryId;
        this.softwareClass = softwareClassName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doExecute(ChunkContext context) throws Exception {

        SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);

        WorkUnit w = trimGalore.getRegisterTrimmedCommand(cellLibrary.getId(), softwareClass);

        GridResult result = hostResolver.execute(w);

        storeStartedResult(context, result);

    }

    @Override
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {

        Assert.assertParameterNotNullNotZero(cellLibraryId, "Must set CELL_LIBRARY_ID");
        Assert.assertParameterNotNull(softwareClass, "Must set SOFTWARE_CLASS_NAME");

        super.beforeStep(stepExecution);
    }

}
