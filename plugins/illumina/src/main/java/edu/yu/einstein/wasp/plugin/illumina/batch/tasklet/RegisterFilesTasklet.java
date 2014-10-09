package edu.yu.einstein.wasp.plugin.illumina.batch.tasklet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.daemon.batch.tasklets.AbandonMessageHandlingTasklet;
import edu.yu.einstein.wasp.dao.FileGroupMetaDao;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.InvalidFileTypeException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.SoftwareManager;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.plugin.fileformat.service.impl.FastqServiceImpl;
import edu.yu.einstein.wasp.plugin.illumina.service.WaspIlluminaService;
import edu.yu.einstein.wasp.plugin.illumina.software.IlluminaHiseqSequenceRunProcessor;
import edu.yu.einstein.wasp.plugin.mps.SequenceReadProperties;
import edu.yu.einstein.wasp.plugin.mps.SequenceReadProperties.ReadType;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.MetaMessageService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * 
 * 
 * @author calder
 * 
 */
public class RegisterFilesTasklet extends AbandonMessageHandlingTasklet {

    @Autowired
    private RunService runService;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private FileService fileService;

    @Autowired
    private FastqService fastqService;

    @Autowired
    private MetaMessageService metaMessageService;

    @Autowired
    private GridHostResolver hostResolver;

    @Autowired
    private IlluminaHiseqSequenceRunProcessor casava;

    private GridTransportConnection transportConnection;

    @Autowired
    private FileType fastqFileType;

    @Autowired
    private FileType waspIlluminaHiseqQcMetricsFileType;
    
    @Autowired
    private WaspIlluminaService waspIlluminaService;
    
    @Autowired
    private FileGroupMetaDao fileGroupMetaDao;

    private int runId;
    private Run run;
    private String workingDirectory;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public RegisterFilesTasklet() {
        // required by cglib
    }

    public RegisterFilesTasklet(Integer runId) {
        this.runId = runId;
    }

    private GridWorkService workService;

    @Override
    @Transactional("entityManager")
    public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
        
        //TODO: this should execute WorkUnits in Grid Task, not in sendExecToRemote.

        run = runService.getRunById(runId);

        List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
        sd.add(casava);

        WorkUnit w = new WorkUnit();
        w.setProcessMode(ProcessMode.SINGLE);
        // set casava as software dependency to ensure we get sent to the
        // correct host
        w.setSoftwareDependencies(sd);
        // save the GridWorkService so we can send many jobs there
        workService = hostResolver.getGridWorkService(w);

        transportConnection = workService.getTransportConnection();

        String stageDir = transportConnection.getConfiguredSetting("illumina.data.stage");
        if (!PropertyHelper.isSet(stageDir))
            throw new GridException("illumina.data.stage is not defined!");

        workingDirectory = stageDir + "/" + run.getName() + "/";

        w.setWorkingDirectory(workingDirectory);

        w.setCommand("mkdir -p wasp && cd wasp && ln -fs ../reports . && mkdir -p sequence && cd sequence");
        // rename files with spaces in names
        w.addCommand("shopt -s nullglob");
        w.addCommand("for f in ../reports/*' '* ; do mv -f \"$f\" \"${f// /_}\"; done");
        w.addCommand("rm -f " + WorkUnit.OUTPUT_FILE_PREFIX + "*");

        Set<SampleSource> allCellLib = new HashSet<SampleSource>();
        Sample platformUnit = run.getPlatformUnit();
        logger.debug("Registering files for " + platformUnit.getName());

        int ncells = sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnit);
        Map<Integer, Sample> cells = sampleService.getIndexedCellsOnPlatformUnit(platformUnit);

        // for each cell, get the libraries and link them.
        for (int c = 1; c <= ncells; c++) {

            Sample cell = cells.get(c);
            logger.debug("looking for libraries on " + platformUnit.getName() + "'s cell #" + c);
            if (cell == null) {
                logger.warn("Cell " + c + " was null");
                continue;
            }
            List<Sample> libraries = sampleService.getLibrariesOnCell(cell);
            logger.debug("found " + libraries.size() + " libraries");

            for (Sample s : libraries) {
                SampleSource cellLib = sampleService.getCellLibrary(cell, s);
                allCellLib.add(cellLib);
                if (sampleService.isControlLibrary(s)) {
                    logger.debug("skipping library " + s.getId() + " as it was marked as control ");
                    continue;
                }
                logger.debug("setting up for sample: " + s.getId() + " using Library-cell: " + cellLib.getId());
                // generate symlinks for fastq
                w.addCommand("for x in ../../Project_WASP/Sample_" + cellLib.getId() + "/" + cellLib.getId() + "*.fastq.gz ; do \n"
                        + "  ln -sf `readlink -f ${x}` .\ndone");
            }

        }

        // list available sequence files for registration
        w.addCommand("shopt -u nullglob");
        transportConnection.sendExecToRemote(w);

        logger.debug("symlinked results");

        w = new WorkUnit();
        w.setProcessMode(ProcessMode.SINGLE);
        w.setSoftwareDependencies(sd);
        w.setWorkingDirectory(workingDirectory);
        w.setCommand("cd wasp/sequence && ls -1 *.fastq.gz");

        // method to send commands without wrapping them in a submission script.
        // will return when complete (also gives access to stdout), only do this
        // when the work is expected to be very short running.
        GridResult r = transportConnection.sendExecToRemote(w);

        BufferedReader br = new BufferedReader(new InputStreamReader(r.getStdOutStream()));

        Integer readSegments = waspIlluminaService.getNumberOfReadSegments(run);

        this.createSequenceFiles(platformUnit, allCellLib, br, readSegments);

        w = new WorkUnit();
        w.setProcessMode(ProcessMode.SINGLE);
        w.setSoftwareDependencies(sd);
        w.setWorkingDirectory(workingDirectory + "wasp/reports");
        w.setCommand("find . -type f -print | sed \'s/^\\.\\///\'");

        logger.debug("registering Illumina report files");

        r = transportConnection.sendExecToRemote(w);
        br = new BufferedReader(new InputStreamReader(r.getStdOutStream()));
        this.createQCFiles(run, br);
        
        return RepeatStatus.FINISHED;

    }

    private void createSequenceFiles(Sample platformUnit, Set<SampleSource> cellLibs, BufferedReader br, Integer readSegments) throws SampleException,
            InvalidFileTypeException, MetadataException {
        String line;
        FastqServiceImpl fqs = (FastqServiceImpl) fastqService;
        logger.debug("parsing output to create sequence files for " + platformUnit.getName());
        try {
            FileGroup pufg = new FileGroup();
            pufg.setIsActive(1);
            pufg.setIsArchived(0);
            pufg.setFileType(fastqFileType);
            pufg.setSoftwareGeneratedBy(casava);
            fileService.addFileGroup(pufg);

            FileGroup jobQCfg = new FileGroup();
            jobQCfg.setFileType(waspIlluminaHiseqQcMetricsFileType);
            jobQCfg.setSoftwareGeneratedBy(casava);
            fileService.addFileGroup(pufg);

            Map<SampleSource, FileGroup> cellLibSeqfg = new HashMap<SampleSource, FileGroup>();

            line = br.readLine();

            SampleSource cellLib = null;

            while (line != null) {
                logger.debug(line);
                Matcher l = Pattern.compile("^(.*?)_(.*?)_L(.*?)_R(.*?)_(.*?).fastq.gz$").matcher(line);

                if (!l.find()) {
                    logger.error("unable to parse file name: " + line);
                    throw new SampleException("Unexpected sample file name format");
                }

                Integer cellLibId = new Integer(l.group(1));
                String barcode = l.group(2);
                Integer lane = new Integer(l.group(3));
                Integer read = new Integer(l.group(4));
                Integer fileNum = new Integer(l.group(5));

                logger.debug("file: " + cellLibId + ":" + barcode + ":" + lane + ":" + read + ":" + fileNum);
                logger.debug("from: " + line);

                if (cellLib == null || !cellLib.getId().equals(cellLibId)) {
                    cellLib = sampleService.getSampleSourceDao().findById(cellLibId);
                    logger.debug("saw library-cell: " + cellLibId + "(" + sampleService.getLibrary(cellLib).getName() + ")");
                    if (!cellLibs.contains(cellLib)) {
                        logger.warn("library " + cellLib.getId() + " is not in the list of cell-libraries to process");
                        continue;
                    }
                    // create a group for samplefiles
                    FileGroup sampleGroup = new FileGroup();
                    sampleGroup.setIsActive(1);
                    sampleGroup.setIsArchived(0);
                    sampleGroup.setFileType(fastqFileType);
                    sampleGroup.setSoftwareGeneratedBy(casava);
                    fileService.addFileGroup(sampleGroup);
                    Integer rsn = readSegments;
                    if (rsn == 2 && !waspIlluminaService.assayAllowsPairedEndData(cellLib))
                    	rsn = 1;
                    fqs.setNumberOfReadSegments(sampleGroup, rsn);
                    cellLibSeqfg.put(cellLib, sampleGroup);

                }
                
                
                // do not register files for assays that do not allow paired sequence data
				if (read == 1 || (read == 2 && waspIlluminaService.assayAllowsPairedEndData(cellLib))) {

					FileHandle file = new FileHandle();
					file.setFileURI(workService.getGridFileService().remoteFileRepresentationToLocalURI(workingDirectory + "wasp/sequence/" + line));
					String actualBarcode = sampleService.getLibraryAdaptor(sampleService.getLibrary(cellLib)).getBarcodesequence();
					if (!actualBarcode.equals(barcode)) {
						logger.error("cell library " + cellLibId + " barcode " + actualBarcode + " does not match file's indicaded barcode: " + barcode);
						throw new edu.yu.einstein.wasp.exception.SampleIndexException("sample barcode does not match");
					}

					String uri = file.getFileURI().toString();
					file.setFileName(uri.substring(uri.lastIndexOf('/') + 1));
					file.setFileType(fastqFileType);
					fileService.addFile(file);
					fqs.setSingleFile(file, false);
					fqs.setFileNumber(file, fileNum);
					fqs.setFastqReadSegmentNumber(file, read);

					SoftwareManager sm = transportConnection.getSoftwareManager();
					String failed = sm.getConfiguredSetting("casava.with-failed-reads");

					boolean f = false;
					if (PropertyHelper.isSet(failed) && failed == "true")
						f = true;
					fqs.setContainsFailed(file, f);

					fqs.setLibraryUUID(file, sampleService.getLibrary(cellLib));

					// add file to pu samplefile and library samplefile
					pufg.getFileHandles().add(file);
					cellLibSeqfg.get(cellLib).getFileHandles().add(file);

					logger.debug("created file " + file.getFileURI().toASCIIString() + " id: " + file.getId());

				}

                line = br.readLine();
            }

            pufg.setDescription(platformUnit.getName() + " FASTQ files");
            fileService.addFileGroup(pufg);
            fqs.setNumberOfReadSegments(pufg, readSegments);

            platformUnit.getFileGroups().add(pufg);
            sampleService.getSampleDao().save(platformUnit);
            // register and md5sum
            fileService.register(pufg.getFileHandles());

            for (SampleSource cl : cellLibSeqfg.keySet()) {
            	
                // save all samplefile groups
                logger.debug("saving cell library " + cl.getId());
                FileGroup sfg = cellLibSeqfg.get(cl);
                sfg.setDescription(sampleService.getLibrary(cl).getName() + " FASTQ files");
                sfg.setFileType(this.fastqFileType);
                sfg.setSoftwareGeneratedBy(casava);
                fileService.addFileGroup(sfg);
                cl.getFileGroups().add(sfg);
                sampleService.getSampleSourceDao().save(cl);
                

            	List<Integer> lens = waspIlluminaService.getLengthOfReadSegments(run);
            	int segs = waspIlluminaService.getNumberOfReadSegments(run);
            	ReadType rt;
            	if (segs == 1) {
            		rt = ReadType.SINGLE;
            	} else if (segs == 2) {
            		rt = ReadType.PAIRED;
            	} else {
            		logger.warn("ReadType with " + segs + " read segments is unknown!");
            		rt = new ReadType("UNKNOWN: " + segs);
            	}
            	
            	SequenceReadProperties srp = new SequenceReadProperties(rt, lens.get(0) - 1); // illumina sequences read length +1
            	
            	SequenceReadProperties.setSequenceReadProperties(srp, sfg, fileGroupMetaDao, FileGroupMeta.class);
                
            }

        } catch (Exception e) {
            logger.warn("unable to register files: " + e.getLocalizedMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private void createQCFiles(Run run, BufferedReader br) {
        String line;
        FileGroup qcfg = new FileGroup();
        qcfg.setFileType(waspIlluminaHiseqQcMetricsFileType);
        qcfg.setSoftwareGeneratedBy(casava);
        qcfg.setIsActive(1);
        qcfg.setIsArchived(0);

        try {
            line = br.readLine();

            while (line != null) {
                FileHandle file = new FileHandle();
                file.setFileURI(workService.getGridFileService().remoteFileRepresentationToLocalURI(workingDirectory + "wasp/reports/" + line));
                file.setFileName(line);
                line = br.readLine();
                qcfg.getFileHandles().add(file);
                file.setFileType(waspIlluminaHiseqQcMetricsFileType);
                fileService.addFile(file);
            }
            qcfg.setDescription("Illumina QC files");
            fileService.addFileGroup(qcfg);
            fileService.register(qcfg.getFileHandles());
            fileService.setSampleFile(qcfg, run.getPlatformUnit());
        } catch (Exception e) {
            logger.warn("unable to register files: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

    }
}
