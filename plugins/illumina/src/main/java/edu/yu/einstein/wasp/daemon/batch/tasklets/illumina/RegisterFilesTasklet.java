package edu.yu.einstein.wasp.daemon.batch.tasklets.illumina;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.InvalidFileTypeException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportService;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.SoftwareManager;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.mps.illumina.IlluminaSequenceRunProcessor;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.MetaMessageService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.filetype.FastqService;
import edu.yu.einstein.wasp.service.filetype.impl.FastqServiceImpl;
import edu.yu.einstein.wasp.software.SoftwarePackage;
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * 
 * 
 * @author calder
 * 
 */
public class RegisterFilesTasklet extends WaspTasklet {

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
	private IlluminaSequenceRunProcessor casava;
	
	private GridTransportService transportService;
	
	@Autowired
	private FileType fastqFileType;

	private int runId;
	private Run run;
	private String workingDirectory;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public RegisterFilesTasklet() {
		// required by cglib
	}

	public RegisterFilesTasklet(int runId) {
		this.runId = runId;
	}

	@Override
	@Transactional("entityManager")
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {

		run = runService.getRunById(runId);

		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(casava);

		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.SINGLE);
		// set casava as software dependency to ensure we get sent to the
		// correct host
		w.setSoftwareDependencies(sd);
		// save the GridWorkService so we can send many jobs there
		GridWorkService gws = hostResolver.getGridWorkService(w);
		
		transportService = gws.getTransportService();

		String stageDir = transportService.getConfiguredSetting("illumina.data.stage");
		if (!PropertyHelper.isSet(stageDir))
			throw new GridException("illumina.data.stage is not defined!");
		
		workingDirectory = stageDir + "/" + run.getName() + "/";

		w.setWorkingDirectory(workingDirectory);
		
		w.setCommand("mkdir -p wasp && cd wasp");
		
		Set<Sample> allLibraries = new HashSet<Sample>();
		
		Sample platformUnit = run.getPlatformUnit();
		
		logger.debug("Registering files for " + platformUnit.getName());

		int ncells = sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnit);

		Map<Integer, Sample> cells = sampleService.getIndexedCellsOnPlatformUnit(platformUnit);
		
		// for each cell, get the libraries and link them.
		for (int c = 1; c <= ncells; c++) {

			Sample cell = cells.get(c);
			logger.debug("looking for libraries on " + platformUnit.getName() + "'s cell #" + c);
			if (cell == null)
				continue;
			List<Sample> libraries = sampleService.getLibrariesOnCell(cell);
			logger.debug("found " + libraries.size() + " libraries");
			allLibraries.addAll(libraries);

			for (Sample s : libraries) {
				if (sampleService.isControlLibrary(s))
					continue;
				logger.debug("setting up for sample " + s.getSampleId());
				int sid = s.getSampleId();
				w.addCommand("if [ -e ../Project_WASP/Sample_" + sid + "/" + sid + "_*_001.fastq.gz ]; then \n" +
						"  ln -s ../Project_WASP/Sample_" + sid + "/" + sid + "_*fastq.gz .\nfi");
			}

		}
		
		// list available files for registration
		w.addCommand("ls -1");

		// method to send commands without wrapping them in a submission
		// script.
		// will return when complete (also gives access to stdout), only do
		// this
		// when the work is expected to be very short running.
		hostResolver.getGridWorkService(w).getTransportService().connect(w);
		GridResult r = w.getConnection().sendExecToRemote(w);

		BufferedReader br = new BufferedReader(new InputStreamReader(r.getStdOutStream()));

		String line = br.readLine();

		while (line != null) {
			File file = this.createFile(gws, allLibraries, line);
			line = br.readLine();
		}
		

		return RepeatStatus.FINISHED;

	}

	private File createFile(GridWorkService gws, Set<Sample> samples, String line) throws SampleException, InvalidFileTypeException, MetadataException {

		FastqServiceImpl fqs = (FastqServiceImpl) fastqService;

		Matcher l = Pattern.compile("^(.*?)_(.*?)_L(.*?)_R(.*?)_(.*?).fastq.gz$").matcher(line);

		if (!l.find()) {
			logger.error("unable to parse file name: " + line);
			throw new SampleException("Unexpected sample file name format");
		}

		Integer sampleId = new Integer(l.group(1));
		String barcode = l.group(2);
		Integer lane = new Integer(l.group(3));
		Integer read = new Integer(l.group(4));
		Integer fileNum = new Integer(l.group(5));

		Sample library = sampleService.getSampleById(sampleId);

		if (!samples.contains(library))
			return null;

		File file = new File();
		file.setFileURI(gws.getGridFileService().remoteFileRepresentationToLocalURI(workingDirectory + line));
		String actualBarcode = sampleService.getLibraryAdaptor(library).getBarcodesequence();
		if (!actualBarcode.equals(barcode)) {
			logger.error("library barcode " + actualBarcode + " does not match file's indicaded barcode: " + barcode);
			throw new edu.yu.einstein.wasp.exception.SampleIndexException("sample barcode does not match");
		}
		file.setFileType(fastqFileType);
		file.setSoftwareGeneratedBy(casava);

		fileService.addFile(file);
		fileService.setSampleFile(file, library);

		fqs.setSingleFile(file, false);
		fqs.setFileNumber(file, fileNum);
		fqs.setFastqReadSegmentNumber(file, read);
		
		SoftwareManager sm = transportService.getSoftwareManager();
		String failed = sm.getConfiguredSetting("casava.with-failed-reads");
		
		boolean f = false;
		if (PropertyHelper.isSet(failed) && failed == "true")
			f = true;
		fqs.setReadsMarkedFailed(file, f);
		
		
		logger.debug("created file " + file.getFileURI().toASCIIString() + " id: " + file.getFileId());

		return file;
	}

}
