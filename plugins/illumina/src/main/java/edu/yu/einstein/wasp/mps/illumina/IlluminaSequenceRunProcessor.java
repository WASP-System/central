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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleCell;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.mps.SequenceRunProcessor;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.MetaHelper;

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

	/**
	 * {@inheritDoc}	
	 */
	@Override
	public void preProcess(Sample platformUnit, String pathToData, String outputFolder, GridWorkService gws) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}	
	 */
	@Override
	public void processSequenceRun(Sample platformUnit, String pathToData, String outputFolder, GridWorkService gws) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}	
	 */
	@Override
	public void postProcess(Sample platformUnit, String pathToData, String outputFolder, GridWorkService gws) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}	
	 */
	@Override
	public void stage(Sample platformUnit, String pathToData, String outputFolder, GridWorkService gws) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * generate an illumina samplesheet.  Only useful for sequence conversion and truseq demultiplexing. 
	 * 
	 * @param platformUnit
	 * @return
	 * @throws IOException
	 * @throws MetadataException
	 */
	private File createSampleSheet(Sample platformUnit) throws IOException, MetadataException {
		
		File f = File.createTempFile("wasp_iss", ".txt");
		logger.debug("created temporary file: " + f.getAbsolutePath().toString());
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
		bw.write(getSampleSheetHeader());
		bw.newLine();
		for (Sample cell : sampleSourceDao.getDerivedSamplesByParentSampleId(platformUnit.getSampleId())) {
			if (!cell.getSampleType().getIName().equals("cell"))
				continue;
			
			List<SampleMeta> cellMD = cell.getSampleMeta();
			
			for (Sample lane : sampleSourceDao.getDerivedSamplesByParentSampleId(cell.getSampleId())) {
				String iname = lane.getSampleType().getIName();
				if ( (!iname.equals("library") && (!iname.equals("facilityLibrary"))))
					continue;
				
				List<SampleMeta> libraryMD = lane.getSampleMeta();
				
				MetaHelper sampleMeta = new MetaHelper("sample", SampleMeta.class);
				
				boolean truseq;
				try {
					truseq = sampleMeta.getMetaValueByName("truseq", libraryMD).equals("true");
				} catch (MetadataException e1) {
					//not truseq
					continue;
				}
				
				if (!truseq)
					continue;
					
				//FCID
				bw.write(platformUnit.getName() + ",");
				
				//Lane
				sampleMeta.setArea("cellinstance");
				bw.write(sampleMeta.getMetaValueByName("lanenumber", cellMD) + ",");
				//SampleID				
				sampleMeta.setArea("genericLibrary");
				bw.write(lane.getSampleId() + ",");
				//SampleRef
				bw.write(sampleMeta.getMetaValueByName("organism", libraryMD) + ",");
				//Index
				bw.write(sampleMeta.getMetaValueByName("barcode", libraryMD) + ",");
				//Description
				bw.write(lane.getName() + ",");
				//Control
				String control = sampleMeta.getMetaValueByName("control", libraryMD);
				String yn = (control.equals("true")) ? "Y" : "N";
				bw.write(yn + ",");
				//Recipe
				String recipe;
				try {
					recipe = sampleMeta.getMetaValueByName("recipe");
				} catch (MetadataException e) {
					recipe = "r1";
				}
				bw.write(recipe + ",");
				//Operator
				bw.write("wasp" + ",");
				//SampleProject
				bw.write(lane.getJob().getJobId());
				bw.newLine();
				
			}
		}
		bw.close();
		return f;
	}

	/**
	 * Returns standard Illumina 1.8.2 SampleSheet header
	 * 
	 * FCID			Flow cell ID Lane Positive integer, indicating the lane number (1-8)
	 * SampleID 	ID of the sample 
	 * SampleRef 	The name of the reference 
	 * Index 		Index sequence(s) 
	 * Description 	Description of the sample 
	 * Control 		Y indicates this lane is a control lane, N means sample 
	 * Recipe 		Recipe used during sequencing 
	 * Operator 	Name or ID of the operator 
	 * SampleProject	The project the sample belongs to
	 * 
	 * @return
	 */
	private String getSampleSheetHeader() {
		return "FCID,Lane,SampleID,SampleRef,Index,Description,Control,Recipe,Recipe,Operator,SampleProject\n";
	}

}
