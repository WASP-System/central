/**
 * 
 */
package edu.yu.einstein.wasp.mps.illumina;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
import edu.yu.einstein.wasp.software.sequencer.SequenceRunProcessor;

/**
 * @author calder
 * 
 */
public class IlluminaSequenceRunProcessor extends SequenceRunProcessor {
	
	private static final long serialVersionUID = -3322619814370790116L;
	private String softwareVersion = "1.8.2"; // hard coded as this is likely the final softwareVersion.
	private String softwareName = "casava"; 

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SampleService sampleService;

	@Autowired
	private AdaptorService adaptorService;
	
	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private String truseqIndexedDnaArea;

	/**
	 * Called first to set up analysis run.
	 * 
	 * @param platformUnit
	 * @param ghs
	 * @throws GridExecutionException
	 * @throws GridAccessException
	 * @throws GridUnresolvableHostException
	 */
	public void doSampleSheet(Run run) throws GridUnresolvableHostException, GridAccessException, GridExecutionException {
		
		logger.debug("sample sheet for " + run.getName() + ":" + run.getPlatformUnit().getName());
		
		logger.debug(sampleService.toString());
		
		WorkUnit w = new WorkUnit();
				
		Sample platformUnit = run.getPlatformUnit();
		
		if (sampleService.isPlatformUnit(platformUnit)) {
			// isPlatformUnit?
			logger.debug("platform unit: " + platformUnit.getName());
		} else {
			logger.error("Not a platform unit: " + platformUnit.getName());
			throw new edu.yu.einstein.wasp.exception.InvalidParameterException(platformUnit.getSampleId() + " is not a platform unit");
		}
		
		List<SoftwarePackage> sp = new ArrayList<SoftwarePackage>();
		sp.add(this);
		w.setCommand("touch start-illumina-pipeline.txt");
		w.setSoftwareDependencies(sp);
		w.setProcessMode(ProcessMode.SINGLE);
		
		GridWorkService gws = hostResolver.getGridWorkService(w);
		GridFileService gfs = gws.getGridFileService();
		String hostname = hostResolver.getHostname(w);
		logger.debug("sending illumina processing job to " + hostname);
		
		String directory = "";
		
		try {
			directory = gws.getTransportService().getConfiguredSetting("illumina.data.dir") + "/" + run.getName();
			logger.debug("configured remote directory as " + directory);
			File f = createSampleSheet(run);
			String newDir = directory + "/Data/Intensities/BaseCalls/";
			
			w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
			w.setResultsDirectory(newDir);
			
			gfs.put(f, newDir + "SampleSheet.csv");
			logger.debug("deleting temporary local sample sheet " + f.getAbsolutePath());
			f.delete();
		} catch (Exception e) {
			logger.error("problem: " + e.getLocalizedMessage());
			throw new GridExecutionException("unable to pre-process illumina sample sheet", e);
		}
		
		logger.debug("touching remote file");
		w.setWorkingDirectory(directory);		
		GridResult result = hostResolver.execute(w);
		
		while (!gws.isFinished(result)) {
			try {
				logger.debug("job is not finished");
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.error(e.getLocalizedMessage());
				throw new GridExecutionException("unable to sleep for sample sheet", e);
			}
		}

	}


	/**
	 * 
	 */
	public void processSequenceRun(Run run, GridHostResolver ghs) throws GridUnresolvableHostException, GridAccessException, GridExecutionException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	public void postProcess(Run run, GridHostResolver ghs) throws GridUnresolvableHostException, GridAccessException, GridExecutionException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	public void stage(Run run, GridHostResolver ghs) throws GridUnresolvableHostException, GridAccessException, GridExecutionException {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * generate an illumina samplesheet. Only useful for sequence conversion and
	 * truseq demultiplexing.
	 * 
	 * @param platformUnit
	 * @return
	 * @throws IOException
	 * @throws MetadataException
	 * @throws SampleTypeException
	 */
	private File createSampleSheet(Run run) throws IOException,
			MetadataException, SampleTypeException {

		File f = File.createTempFile("wasp_iss", ".txt");
		logger.debug("created temporary file: " + f.getAbsolutePath().toString());
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
		
		bw.write(getSampleSheet(run));
		
		bw.close();
		return f;
	}
	
	public String getSampleSheet(Run run) throws SampleTypeException, MetadataException {

		String sampleSheet = getSampleSheetHeader();
		
		Sample platformUnit = run.getPlatformUnit();
		
		int numberOfCells = sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnit);
		
		boolean[] cellMarked = new boolean[numberOfCells];
		
		// throws SampleTypeException
		Map<Integer, Sample> cells = sampleService.getIndexedCellsOnPlatformUnit(platformUnit);
		
		logger.debug(cells.size() + " cells on platform unit " + platformUnit.getName());

		for (Integer cellid : cells.keySet()) { // for each cell in platform unit
			Sample cell = cells.get(cellid);

			// throws SampleTypeException

			List<Sample> all = sampleService.getLibrariesOnCell(cell);
			
			List<Sample> libraries = sampleService.getLibrariesOnCellWithoutControls(cell);

			for (Sample library : all) {
				
				// if the sample is not TruSeq, do not place it in the sample sheet
				if (! adaptorService.getAdaptor(library).getAdaptorset().getIName().equals(truseqIndexedDnaArea))
					continue;
				
				cellMarked[cellid-1] = true;
				
				// if there is one control sample in the lane and no libraries, set the control flag
				if ((libraries.size() == 0) && (all.size() == 1)) {
					Sample control = all.get(0);
					
					String line = buildLine(platformUnit, cellid, control.getName(), control, "Y", "control");
					sampleSheet += "\n" + line;
					continue;
				}

				// necessary?
				String iname = library.getSampleType().getIName();
				if ((!iname.equals("library") && (!iname
						.equals("facilityLibrary")))) {
					logger.warn("Expected library, saw " + iname);
					continue;
				}

				Sample sample = library.getParent();
				
				if (sample == null)
					sample = library;

				// TODO:implement this job service method to get JobSampleMeta
				// from sample and platform unit
				String genome = "";
				String control = "N";
				String recipe = "WASP";

				String line = buildLine(platformUnit, cellid, genome, sample, control, recipe);

				logger.debug("sample sheet: " + line);

				sampleSheet += "\n" + line;
			}
			
			// if the cell has not been marked (has TruSeq sample), create a dummy sample for 
			// the lane if further demultiplexing is required
			// or a single sample for the entire lane
				
			if (cellMarked[cellid-1] == false) {
				Sample placeholder = new Sample();
				Adaptor adaptor = new Adaptor();
				placeholder.setSampleId(-1);
				
				String line = buildLine(platformUnit, cellid, "", placeholder, "N", "WASP");
				
				sampleSheet += "\n" + line;
			}
			
		}
		return sampleSheet;
		
	}

	private String buildLine(Sample platformUnit, Integer cellIndex, String genome, Sample sample, String control, String recipe) {
		
		String adapter = null;
		try {
			adapter = adaptorService.getAdaptor(sample).getBarcodesequence();
		} catch (Exception e) {
			// if it is null or otherwise not set, it does not exist
			adapter = "";
		}
		String sampleId = sample.getSampleId().toString();
		String sampleName = sample.getName();
		
		if (sampleId.equals("-1")) {
			sampleId = "lane_" + cellIndex;
			sampleName = sampleId;
		}
		return new StringBuilder()
						.append(platformUnit.getName() + ",")
						.append(cellIndex + ",")
						.append(sampleId + ",")
						.append(genome + ",")
						.append(adapter + ",")
						.append(sampleName + ",")
						.append(control + ",")
						.append(recipe + ",") // TODO:recipe
						.append("WASP" + ",")
						.append("WASP")
						.toString();
	}

	/**
	 * Returns standard Illumina 1.8.2 SampleSheet header
	 * 
	 * FCID Flow cell ID Lane Positive integer, indicating the lane number (1-8)
	 * SampleID ID of the sample SampleRef The name of the reference Index Index
	 * sequence(s) Description Description of the sample Control Y indicates
	 * this lane is a control lane, N means sample Recipe Recipe used during
	 * sequencing Operator Name or ID of the operator SampleProject The project
	 * the sample belongs to
	 * 
	 * @return
	 */
	private String getSampleSheetHeader() {
		return "FCID,Lane,SampleID,SampleRef,Index,Description,Control,Recipe,Recipe,Operator,SampleProject";
	}

	@Override
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	@Override
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

}
