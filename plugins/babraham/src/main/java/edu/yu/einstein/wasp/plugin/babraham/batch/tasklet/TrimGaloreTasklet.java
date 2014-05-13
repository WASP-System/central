/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.batch.tasklet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.LaunchManyJobsTasklet;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQC;
import edu.yu.einstein.wasp.plugin.babraham.software.TrimGalore;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * Tasklet to run trim_galore on a sequencing run.  
 * 
 * @author calder
 * 
 */
public class TrimGaloreTasklet extends LaunchManyJobsTasklet {

    @Autowired
    private BabrahamService babrahamService;

    @Autowired
    private GridHostResolver hostResolver;
    
    @Autowired
    private RunService runService;
    
    @Autowired
    private SampleService sampleService;
    
    @Autowired
    private FastqService fastqService;
    
    @Autowired
    private FileService fileService;

    @Autowired
    private TrimGalore trim_galore;
    
    @Value("${wasp.primaryfilehost}")
    private String primaryFileHost;
    
    private int runId;
    private String software;

    private static final Logger logger = LoggerFactory.getLogger(TrimGaloreTasklet.class);

    /**
     * 
     */
    public TrimGaloreTasklet() {
        // proxy
    }
    
    public TrimGaloreTasklet(int runId, String softwareName) {
        this.runId = runId;
        this.software = softwareName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional("entityManager")
    public void doExecute() {

        Run run = runService.getRunById(runId);

        Set<SampleSource> cellLibraries = runService.getCellLibrariesOnSuccessfulRunCellsWithoutControls(run);
        
        logger.debug("Preparing to perform trim_galore trimming on run: " + run.getName());

        for (SampleSource cellLibrary : cellLibraries) {
            
            logger.debug("trim_galore has identified cellLibrary " + cellLibrary.getId() + " on run " + run.getName());

            Set<FileGroup> fgs = fileService.getFilesForCellLibraryByType(cellLibrary, fastqService.getFastqFileType());
            if (fgs.size() != 1) {
                String m = "cell library " + cellLibrary.getId() + ":" + cellLibrary.getUUID() + " was expected to have 1 set of FASTQ files, found "
                        + fgs.size();
                logger.error(m);
                throw new WaspRuntimeException(m);
            }

            Set<FileHandle> fastq = new TreeSet<FileHandle>(new FastqComparator(fastqService));
            FileGroup fastqG = fgs.iterator().next();
            fastq.addAll(fastqG.getFileHandles());

            Integer rs = fastqService.getNumberOfReadSegments(fastqG);

            Iterator<FileHandle> fhi = fastq.iterator();

            Integer fileNumber = 0;

            while (fhi.hasNext()) {
                Map<String, String> jobParameters = new HashMap<String, String>();

                jobParameters.put(WaspJobParameters.BEAN_NAME, "casava");
                jobParameters.put(WaspJobParameters.RUN_ID, run.getId().toString());
                jobParameters.put(WaspJobParameters.CELL_LIBRARY_ID, cellLibrary.getId().toString());
                jobParameters.put(WaspJobParameters.FILE_GROUP_ID, fastqG.getId().toString());
                jobParameters.put(FileTypeService.FILETYPE_FILE_NUMBER_META_KEY, fileNumber.toString());
                jobParameters.put(FastqService.FASTQ_GROUP_NUMBER_OF_READ_SEGMENTS, rs.toString());

                try {
                    logger.debug("requesting launch of " + TrimGalore.MANY_FLOW_NAME + " for cellLibrary " + cellLibrary.getId());
                    requestLaunch(TrimGalore.MANY_FLOW_NAME, jobParameters);
                } catch (WaspMessageBuildingException e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }

                for (int x = 0; x < rs; x++) {
                    fhi.next();
                    fileNumber++;
                }
            }
        }

    }
    
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        
        Assert.assertParameterNotNullNotZero(runId, "Must set RUN_ID");
        Assert.assertParameterNotNull(software, "Must set BEAN_NAME");
        
        super.beforeStep(stepExecution);
    }

}
