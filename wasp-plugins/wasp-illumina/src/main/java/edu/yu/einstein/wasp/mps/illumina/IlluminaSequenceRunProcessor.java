/**
 * 
 */
package edu.yu.einstein.wasp.mps.illumina;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.mps.SequenceRunProcessor;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.SampleWrapper;

/**
 * @author calder
 * 
 */
public class IlluminaSequenceRunProcessor implements SequenceRunProcessor {

	private static Log logger = LogFactory.getLog(SequenceRunProcessor.class);

	@Autowired
	private SampleService sampleService;

	@Autowired
	private SampleSourceDao sampleSourceDao;

	@Autowired
	private AdaptorService adaptorService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preProcess(Sample platformUnit, String pathToData,
			String outputFolder, GridWorkService gws) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processSequenceRun(Sample platformUnit, String pathToData,
			String outputFolder, GridWorkService gws) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postProcess(Sample platformUnit, String pathToData,
			String outputFolder, GridWorkService gws) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stage(Sample platformUnit, String pathToData,
			String outputFolder, GridWorkService gws) {
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
	private File createSampleSheet(Sample platformUnit) throws IOException,
			MetadataException, SampleTypeException {

		File f = File.createTempFile("wasp_iss", ".txt");
		logger.debug("created temporary file: "
				+ f.getAbsolutePath().toString());
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
		bw.write(getSampleSheetHeader());
		bw.newLine();

		// throws SampleTypeException
		Map<Integer, Sample> cells = sampleService
				.getIndexedCellsOnPlatformUnit(platformUnit);

		for (Integer cellid : cells.keySet()) { // for each cell in platform
												// unit
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
				String jobname = "nukk";
				String control = "N";
				String recipe = "recipe";

				String line = buildLine(cell, genome, adaptor, sample, control, recipe, jobname);

				logger.debug("sample sheet: " + line);

				bw.write(line);
				bw.newLine();
			}
			
			// if there is one control sample in the lane and no libraries, set the control flag
			if ((libraries.size() == 0) && (all.size() == 1)) {
				Adaptor a = new Adaptor();
				a.setBarcodesequence("");
				Sample s = new Sample();
				s.setName("control");
				s.setSampleId(-1);
				
				String line = buildLine(cell, "PhiX", a, s, "Y", "control", "control");
				bw.write(line);
				bw.newLine();
			}
		}
		bw.close();
		return f;
	}

	private String buildLine(Sample cell, String genome, Adaptor adaptor, Sample sample, String control, String recipe, String jobname) {
		return new StringBuilder()
						.append(cell.getName() + ",")
						.append(sample.getSampleId() + ",")
						.append(genome + ",")
						.append(adaptor.getBarcodesequence() + ",")
						.append(sample.getName() + ",")
						.append("control" + ",") // TODO:control
						.append("recipe" + ",") // TODO:recipe
						.append("WASP" + ",")
						.append(jobname)
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
