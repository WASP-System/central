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

import javax.annotation.PostConstruct;

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
@Component
public class IlluminaSequenceRunProcessor extends SequenceRunProcessor {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SampleService sampleService;

	@Autowired
	private AdaptorService adaptorService;
	
	@Autowired
	private GridHostResolver hostResolver;

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
		WorkUnit w = new WorkUnit();
				
		Sample platformUnit = run.getPlatformUnit();
		
		if (sampleService.sampleIsPlatformUnit(platformUnit)) {
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
			gfs.put(f, directory + "/" + "SampleSheet.csv");
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
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error(e.getLocalizedMessage());
				throw new GridExecutionException("unable to sleep for sample sheet", e);
			}
		}

	}

	@Override
	@PostConstruct
	public void init() throws Exception {
		super.init();
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

		String sampleSheet = getSampleSheetHeader() + "\n";
		
		Sample platformUnit = run.getPlatformUnit();
		
		// throws SampleTypeException
		Map<Integer, Sample> cells = sampleService.getIndexedCellsOnPlatformUnit(platformUnit);

		for (Integer cellid : cells.keySet()) { // for each cell in platform unit
			Sample cell = cells.get(cellid);

			// throws SampleTypeException

			List<Sample> all = sampleService.getLibrariesOnCellWithoutControls(cell);
			
			List<Sample> libraries = sampleService.getLibrariesOnCellWithoutControls(cell);

			for (Sample library : libraries) {

				Adaptor adaptor = adaptorService.getAdaptor(library);

				// necessary?
				String iname = library.getSampleType().getIName();
				if ((!iname.equals("library") && (!iname
						.equals("facilityLibrary")))) {
					logger.warn("Expected library, saw " + iname);
					continue;
				}

				Sample sample = library.getParent();

				// TODO:implement this job service method to get JobSampleMeta
				// from sample and platform unit
				String genome = "null";
				String jobname = "null";
				String control = "N";
				String recipe = "recipe";

				String line = buildLine(cell, genome, adaptor, sample, control, recipe);

				logger.debug("sample sheet: " + line);

				sampleSheet += line + "\n";
			}
						
			// if there is one control sample in the lane and no libraries, set the control flag
			if ((libraries.size() == 0) && (all.size() == 1)) {
				Adaptor a = new Adaptor();
				a.setBarcodesequence("");
				Sample s = new Sample();
				s.setName("control");
				s.setSampleId(-1);
				
				String line = buildLine(cell, "PhiX", a, s, "Y", "control");
				sampleSheet += line + "\n";
			}
		}
		return sampleSheet;
		
	}

	private String buildLine(Sample cell, String genome, Adaptor adaptor, Sample sample, String control, String recipe) {
		return new StringBuilder()
						.append(cell.getName() + ",")
						.append(sample.getSampleId() + ",")
						.append(genome + ",")
						.append(adaptor.getBarcodesequence() + ",")
						.append(sample.getName() + ",")
						.append(control + ",")
						.append("recipe" + ",") // TODO:recipe
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
		return "FCID,Lane,SampleID,SampleRef,Index,Description,Control,Recipe,Recipe,Operator,SampleProject\n";
	}

}
