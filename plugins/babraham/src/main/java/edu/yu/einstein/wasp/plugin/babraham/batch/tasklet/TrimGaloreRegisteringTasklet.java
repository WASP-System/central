/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.batch.tasklet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.babraham.plugin.TrimGalorePlugin;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.plugin.babraham.software.TrimGalore;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.FileService;
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

    private int runId;
    private int cellLibraryId;
    /**
     * Name of the bean that produced the sequence data.
     */
    private String softwareClass;

    @Autowired
    private SampleService sampleService;
    
    @Autowired
    private FileService fileService;

    @Autowired
    private AdaptorService adaptorService;

    @Autowired
    private TrimGalore trimGalore;
    
    @Autowired
    private BabrahamService babrahamService;

    @Autowired
    private GridHostResolver hostResolver;

    @Autowired
    private RunService runService;

    public TrimGaloreRegisteringTasklet(String runId, String softwareName, String cellLibraryId) {
        this.runId = Integer.parseInt(runId);
        this.cellLibraryId = Integer.parseInt(cellLibraryId);
        this.softwareClass = softwareName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional("entityManager")
    public GridResult doExecute(ChunkContext context) throws Exception {

        SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);

        WorkUnit w = trimGalore.getRegisterTrimmedCommand(runId, cellLibrary.getId(), softwareClass);

        GridResult result = hostResolver.execute(w);
        
        // save results files id's in StepExecutionContext
        List<Integer> resultsFileIds = new ArrayList<>();
        for (FileHandle fh : w.getResultFiles())
        	resultsFileIds.add(fh.getId());
        context.getStepContext().getStepExecution().getExecutionContext().putString("resultsFilesIdStr", StringUtils.collectionToCommaDelimitedString(resultsFileIds));
        
       return result;

    }
    
    /** 
	 * {@inheritDoc}
     * @throws SampleTypeException 
     * @throws ParserConfigurationException 
     * @throws SAXException 
     * @throws IOException 
     * @throws JSONException 
     * @throws GridException 
     * @throws MetadataException 
	 */
	@Override
	@Transactional("entityManager")
	public void doPreFinish(ChunkContext context) throws SampleTypeException, MetadataException, GridException, JSONException, IOException, SAXException, ParserConfigurationException {
		GridResult r = getGridResult(context);
	        SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);
		// get results files and make them active
		ExecutionContext stepExecutionContext = context.getStepContext().getStepExecution().getExecutionContext();
		logger.trace("attempting file group activation");
		if (stepExecutionContext.containsKey("resultsFilesIdStr")){
		    logger.trace("resultsFilesIdStr: " + stepExecutionContext.getString("resultsFilesIdStr"));
			for (String fhIdStr : StringUtils.commaDelimitedListToStringArray(stepExecutionContext.getString("resultsFilesIdStr"))) {
			    FileGroup fg = fileService.getFileHandleById(Integer.parseInt(fhIdStr)).getFileGroup().iterator().next();
			    logger.trace("file group " + fg.getId() + " which contains file group " + fhIdStr + " is going to be set to be active");
			    fg.setIsActive(1);
			    fg.getSampleSources().add(cellLibrary);
			    fileService.getFileGroupDao().merge(fg);
			    babrahamService.saveJsonForParsedSoftwareOutput(trimGalore.parseOutput(r, fg), TrimGalorePlugin.TRIM_GALORE_PLOT_KEY, trimGalore, fg.getId());
			}
		}
			
	}

    @Override
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {

        Assert.assertParameterNotNullNotZero(runId, "Must set RUN_ID");
        Assert.assertParameterNotNullNotZero(cellLibraryId, "Must set CELL_LIBRARY_ID");
        Assert.assertParameterNotNull(softwareClass, "Must set SOFTWARE_CLASS_NAME");

        super.beforeStep(stepExecution);
    }

	@Override
	@Transactional("entityManager")
	public void doCleanupBeforeRestart(StepExecution stepExecution) throws Exception {
		ExecutionContext stepExecutionContext = stepExecution.getExecutionContext();
		logger.debug("Cleaning filegroup entries");
		if (stepExecutionContext.containsKey("resultsFilesIdStr")){
		    logger.trace("resultsFilesIdStr: " + stepExecutionContext.getString("resultsFilesIdStr"));
			for (String fhIdStr : StringUtils.commaDelimitedListToStringArray(stepExecutionContext.getString("resultsFilesIdStr"))) {
			    FileGroup fg = fileService.getFileHandleById(Integer.parseInt(fhIdStr)).getFileGroup().iterator().next();
			    logger.trace("file group " + fg.getId() + " which contains file group " + fhIdStr + " is going to be deleted");
			    fileService.removeWithAllAssociatedFilehandles(fg);
			}
		}
	}

}
