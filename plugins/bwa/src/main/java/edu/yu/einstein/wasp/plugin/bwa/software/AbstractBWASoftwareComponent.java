package edu.yu.einstein.wasp.plugin.bwa.software;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.NullResourceException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.plugin.mps.software.alignment.ReferenceBasedAligner;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Genome;
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

	protected Build getGenomeBuild(SampleSource cellLibrary) {
		Build build = null;
		try {
			Sample library = sampleService.getLibrary(cellLibrary);
			logger.debug("looking for genome build associated with sample: " + library.getId());
			build = genomeService.getBuild(library);
			if (build == null) {
				String mess = "cell library does not have associated genome build metadata annotation";
				logger.error(mess);
				throw new NullResourceException(mess);
			}
			logger.debug("genome build: " + build.getGenome().getName() + "::" + build.getName());
		} catch (ParameterValueRetrievalException e) {
			logger.error(e.toString());
			e.printStackTrace();
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
			} catch (MetadataException | SampleTypeException e){
				logger.info("Not adding adaptor barcode information to readgroup as not available");
			}
			lb = library.getName();
			pl = "ILLUMINA"; // TODO: fix
			if (platformUnit != null)
				pu = platformUnit.getName();
			else
				logger.info("Not adding platform unit information to readgroup as not available");
			sm = library.getName(); // TODO: get the real sample name
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
	
	protected WorkUnit prepareWorkUnit(FileGroup fg) {
		WorkUnit w = new WorkUnit();
		
		w.setMode(ExecutionMode.TASK_ARRAY);
		w.setNumberOfTasks(fg.getFileHandles().size());
		
		w.setProcessMode(ProcessMode.MAX);
		
		w.setMemoryRequirements(8);

		List<FileHandle> fhlist = new ArrayList<FileHandle>();
		fhlist.addAll(fg.getFileHandles());
		Collections.sort(fhlist, new FastqComparator(fastqService));
		w.setRequiredFiles(fhlist);
		
		List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
		sd.add(this);
		//sd.add(picard);
		//sd.add(samtools);
		w.setSoftwareDependencies(sd);
		w.setSecureResults(false);
		
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		
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
	
	protected String getGenomeIndexPath(Build build) {
		String index = genomeService.getRemoteBuildPath(build) + "/bwa/" + build.getGenome().getName() +"."+ build.getName();
		return index;
	}

	@Override
	public Genome getGenome(String genome) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean genomeExists(Genome genome) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void buildGenome(Genome genome) throws WaspException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean metadataExists(Genome genome) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean metadataAvailable(Genome genome) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void buildMetadata(Genome genome) throws WaspException {
		// TODO Auto-generated method stub
		
	}

}
