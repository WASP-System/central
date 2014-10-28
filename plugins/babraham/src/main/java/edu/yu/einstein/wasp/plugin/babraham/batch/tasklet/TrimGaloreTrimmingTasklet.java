/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.batch.tasklet;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.babraham.batch.tasklet.jobparameters.TrimGaloreParameters;
import edu.yu.einstein.wasp.plugin.babraham.software.TrimGalore;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * 
 * Performs the actual job of trimming, executed as a MANY step on a per-
 * FileHandle basis.
 * 
 * @author calder
 * 
 */
public class TrimGaloreTrimmingTasklet extends WaspRemotingTasklet {

    private String software;
    private int cellLibraryId;
    private int fileGroupId;
    private int fileNumber;
    private int readSegments;
    private int runId;
    
    @Autowired
    private SampleService sampleService;

    @Autowired
    private AdaptorService adaptorService;

    @Autowired
    @Qualifier("trim_galore")
    private TrimGalore trimGalore;

    @Autowired
    private GridHostResolver hostResolver;

    public TrimGaloreTrimmingTasklet(String softwareName, int runId, int cellLibraryId, int fileGroupId, int fileNumber, int readSegments) {
        this.software = softwareName;
        this.runId = runId;
        this.cellLibraryId = cellLibraryId;
        this.fileGroupId = fileGroupId;
        this.fileNumber = fileNumber;
        this.readSegments = readSegments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GridResult doExecute(ChunkContext context) throws Exception {
        SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);
        Sample library = sampleService.getLibrary(cellLibrary);

        TrimGaloreParameters params = new TrimGaloreParameters();

        String adapter = adaptorService.getAdaptor(library).getSequence();
        if (adapter.length() > 13)
            adapter = adapter.substring(0, 13);
        params.setAdapter(adapter);
        if (readSegments > 1)
            params.setAdapter2(adapter);

        WorkUnit w = trimGalore.getTrimCommand(params, software, runId, cellLibraryId, fileGroupId, fileNumber);
        logger.debug("submitted trim step, going for hibernation");
        return hostResolver.execute(w);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.trace("before TrimGaloreTrimmingTasklet");
        super.beforeStep(stepExecution);
    }
    
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
    	logger.trace("after TrimGaloreTrimmingTasklet");
    	return super.afterStep(stepExecution);
    }

	@Override
	public void doCleanupBeforeRestart(ChunkContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}
    

}
