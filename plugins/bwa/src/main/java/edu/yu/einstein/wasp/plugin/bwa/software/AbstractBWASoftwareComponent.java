package edu.yu.einstein.wasp.plugin.bwa.software;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.bwa.service.impl.BwaServiceImpl.BwaIndexType;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.plugin.mps.software.alignment.ReferenceBasedAligner;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * 
 * @author calder / asmclellan
 *
 */
public abstract class AbstractBWASoftwareComponent extends ReferenceBasedAligner{
	
	private static final long serialVersionUID = -4696544666854435125L;

	@Autowired
	protected SampleService sampleService;
	
	@Autowired
	protected JobService jobService;
	
	@Autowired
	protected GenomeService genomeService;
	
	@Autowired
	protected FastqService fastqService;
	
	@Autowired
	protected AdaptorService adaptorService;
	
	public AbstractBWASoftwareComponent(){
		setSoftwareVersion("0.7.6a"); // this default may be overridden in wasp.site.properties
	}

	public  Build getGenomeBuild(SampleSource cellLibrary) throws ParameterValueRetrievalException {
		logger.debug("getting genome build for cellLibrary id=" + cellLibrary.getId());
		Build build = null;
		try {
			Sample library = sampleService.getLibrary(cellLibrary);
			logger.debug("looking for genome build associated with sample: " + library.getId());
			build = genomeService.getBuild(library);
			logger.debug("genome build: " + genomeService.getDelimitedParameterString(build));
		} catch (ParameterValueRetrievalException e) {
		    String mess = "cell library " + cellLibrary.getId() + " does not have associated genome build metadata annotation";
		    logger.info(mess);
		    throw e;
		}
		return build;
	}
	
	public String getReadGroupString(SampleSource cellLibrary) {
		
//		ID* Read group identifier. Each @RG line must have a unique ID. The value of ID is used in the RG tags of alignment records.
//		  Must be unique among all read groups in header section. Read group IDs may be modified when merging SAM files in order to handle collisions.
//		CN Name of sequencing center producing the read.
//		DS Description
//		DT Date the run was produced (ISO8601 date or date/time).
//		FO Flow order. The array of nucleotide bases that correspond to the nucleotides used for each flow of each read. Multi-base flows are encoded 
//		  in IUPAC format, and non-nucleotide flows by various other characters. Format: /\*|[ACMGRSVTWYHKDBN]+/
//		KS The array of nucleotide bases that correspond to the key sequence of each read.
//		LB Library.
//		PG Programs used for processing the read group.
//		PI Predicted median insert size.
//		PL Platform/technology used to produce the reads. Valid values: CAPILLARY, LS454, ILLUMINA, SOLID, HELICOS, IONTORRENT and PACBIO.
//		PU Platform unit (e.g. flowcell-barcode.lane for Illumina or slide for SOLiD). Unique identifier.
//		SM Sample. Use pool name where a pool is being sequenced.
		
		Sample platformUnit = null;
		try {
			Sample cell = sampleService.getCell(cellLibrary);
			if (cell != null) // cell may be null if importing external data
				platformUnit = sampleService.getPlatformUnitForCell(sampleService.getCell(cellLibrary));
		} catch (Exception e) {
			logger.error(e.toString());
			throw new RuntimeException(e.toString());
		}
		Sample library = sampleService.getLibrary(cellLibrary);
		// Sample sample = sampleService.get

		String id = "";
		String ks = "";
		String lb = "";
		String pl = "";
		String pu = "";
		String sm = "";
		try {
			id = cellLibrary.getUUID().toString();
			try {
				ks = adaptorService.getAdaptor(library).getBarcodesequence();
			} catch (WaspException e){
				logger.info("Not adding adaptor barcode information to readgroup as not available");
			}
			lb = library.getUUID().toString();
			pl = "ILLUMINA"; // TODO: fix 
			if (platformUnit != null)
				pu = platformUnit.getName();
			else
				logger.info("Not adding platform unit information to readgroup as not available");
			sm = library.getUUID().toString();
			Sample s = library.getParent();
			if (s != null)
				sm = s.getUUID().toString();
		} catch (Exception e) {
			logger.error(e.toString());
			throw new RuntimeException(e.toString());
		}
		StringBuilder sb = new StringBuilder();
		sb.append("\'@RG\\t");
		sb.append("ID:" + id + "\\t");
		if (!ks.isEmpty())
			sb.append("KS:" + ks + "\\t");
		sb.append("LB:" + lb + "\\t");
		sb.append("PL:" + pl + "\\t");
		if (!pu.isEmpty())
			sb.append("PU:" + pu + "\\t");
		sb.append("SM:" + sm);
		sb.append("\'");
		return sb.toString();

	}
	
	public WorkUnit prepareWorkUnit(FileGroup fg) {
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		
		c.setMode(ExecutionMode.TASK_ARRAY);
		c.setNumberOfTasks(fg.getFileHandles().size());
		
		c.setProcessMode(ProcessMode.MAX);
		
		c.setMemoryRequirements(8);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		//sd.add(picard);
		//sd.add(samtools);
		c.setSoftwareDependencies(sd);
		c.setResultsDirectory(WorkUnitGridConfiguration.SCRATCH_DIR_PLACEHOLDER);

		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		fhlist.addAll(fg.getFileHandles());
		Collections.sort(fhlist, new FastqComparator(fastqService));
		
		WorkUnit w = new WorkUnit(c);
		w.setRequiredFiles(fhlist);
		
		w.setSecureResults(false);
		
		
		return w;
	}
	
	protected String getOptString(String optPrefix, Map<String,Object> jobParameters){
		String optString = "";
		for (String opt : jobParameters.keySet()) {
			if (!opt.startsWith(optPrefix))
				continue;
			String key = opt.replace(optPrefix, "");
			if (jobParameters.get(opt).toString().equals("yes")){ 
				optString += " " + key;
				continue;
			}
			if (jobParameters.get(opt).toString().equals("no") || jobParameters.get(opt).toString().equals("null"))
				continue;
			optString += " " + key + " " + jobParameters.get(opt).toString();
		}
		return optString;
	}
	
	protected String getGenomeIndexPath(Build build, BwaIndexType type) {
		String indexType = type.toString().toLowerCase();
		String index = genomeService.getRemoteBuildPath(build) + "/bwa/" + indexType + "/" + build.getGenomeBuildNameString();
		return index;
	}
	
	protected String getGenomeIndexPath(Build build) {
		return getGenomeIndexPath(build, BwaIndexType.GENOME);
	}

}
