/**
 * 
 */
package edu.yu.einstein.wasp.mps.bwa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.NullResourceException;
import edu.yu.einstein.wasp.exception.ParameterValueRetrievalException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.filetype.FastqComparator;
import edu.yu.einstein.wasp.filetype.service.FastqService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Genome;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
import edu.yu.einstein.wasp.software.alignment.ReferenceBasedAligner;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;

/**
 * @author calder
 *
 */
public class BWASoftwareComponent extends ReferenceBasedAligner {
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private GenomeService genomeService;
	
	@Autowired
	private FastqService fastqService;
	
	@Autowired
	private RunService runService;
	
	@Autowired
	private AdaptorService adaptorService;
	
	// @Autowired
	// private SoftwarePackage picard;
	
	//@Autowired
	//private SoftwarePackage samtools;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6631761128215948999L;
	private String version = "0.6.2"; // hard coded as this is likely the final version.
	private String softwareName = "bwa"; 
	
	public WorkUnit getAln(SampleSource libraryCell, FileGroup fg, Map<String,Object> jobParameters) {
		WorkUnit w = prepareWorkUnit(fg);
		
		String alnOpts = "";
		
		for (String opt : jobParameters.keySet()) {
			if (!opt.startsWith("aln"))
				continue;
			String key = opt.replace("aln", "");
			if (key.equals("-N"))
				if (jobParameters.get(opt).toString().equals("yes")) {
					alnOpts += " " + key;
					continue;
				} else {
					continue;
				}
			alnOpts += " " + key + " " + jobParameters.get(opt).toString();
		}
		
		String command = "bwa aln " + alnOpts + " -t ${" + WorkUnit.NUMBER_OF_THREADS + "} " + 
				getGenomeIndexPath(getGenomeBuild(libraryCell)) + " " +
				"${" + WorkUnit.INPUT_FILE + "[" + WorkUnit.TASK_ARRAY_ID + "]} " +
				"> sai.${" + WorkUnit.TASK_OUTPUT_FILE + "}"; 
		
		logger.debug("Will conduct bwa aln with string: " + command);
		
		w.setCommand(command);
		
		return w;
	}
	
	public WorkUnit getSam(SampleSource cellLibrary, String scratchDirectory, String namePrefix, FileGroup fg, Map<String,Object> jobParameters) {
		WorkUnit w = prepareWorkUnit(fg);
		w.setProcessMode(ProcessMode.SINGLE);
		w.setProcessorRequirements(1);
		w.setWorkingDirectory(scratchDirectory);
		
		Integer numberOfReadSegments = fastqService.getNumberOfReadSegments(fg);
		Assert.assertTrue(numberOfReadSegments > 0 && numberOfReadSegments <= 2);
		
		String method = "samse";
		if (numberOfReadSegments.equals(2)) 
			method = "sampe";
		
		String alnOpts = "";
		
		for (String opt : jobParameters.keySet()) {
			if (!opt.startsWith("sampe"))
				continue;
			String key = opt.replace("sampe", "");
			if (!key.equals("-n") && !method.equals("sampe"))
				continue;
			alnOpts += " " + key + " " + jobParameters.get(opt).toString();
		}
		
		String command = getSaiVars(namePrefix, w, method);
		
		if (method.equals("samse")) {
		
			command += "bwa " + method + " " + alnOpts + " -r " + this.getReadGroupString(cellLibrary) + " " +
					getGenomeIndexPath(getGenomeBuild(cellLibrary)) + " " +
					"${sai[" + WorkUnit.TASK_ARRAY_ID + "]} " + "${" + WorkUnit.INPUT_FILE + "[" + WorkUnit.TASK_ARRAY_ID + "]} " +
					"> sam.${" + WorkUnit.TASK_OUTPUT_FILE + "}"; 
		
			w.setNumberOfTasks(w.getRequiredFiles().size());
		} else {
			
			command += "FI=$((" + WorkUnit.TASK_ARRAY_ID + "*2))\n";
			command += "FI2=$((FI+1))\n";
			command += "bwa " + method + " " + alnOpts + " -r " + this.getReadGroupString(cellLibrary) + " " +
					getGenomeIndexPath(getGenomeBuild(cellLibrary)) + " " +
					"${sai1[" + WorkUnit.TASK_ARRAY_ID + "]} " + 
					"${sai2[" + WorkUnit.TASK_ARRAY_ID + "]} " +
					"${" + WorkUnit.INPUT_FILE + "[FI]} " +
					"${" + WorkUnit.INPUT_FILE + "[FI2]} " +
					"> sam.${" + WorkUnit.TASK_OUTPUT_FILE + "}"; 
			w.setNumberOfTasks(w.getRequiredFiles().size()/2);
		}
		
		logger.debug("Will conduct bwa aln with string: " + command);
		
		w.setCommand(command);
		
		return w;
	}
	
	private String getSaiVars(String namePrefix, WorkUnit w, String method) {
		String varStr = "";
		int lim = w.getRequiredFiles().size();
		if (method.equals("samse")) {
			for (int i = 0; i < lim; i++) {
				varStr += "sai[" + i + "]=sai." + namePrefix + ":" + i + ".out\n";
			}
			return varStr;
		} else {
			int n = 0;
			for (int i = 0; i < lim; i=i+2) {
				varStr += "sai1[" + n + "]=sai." + namePrefix + ":" + i + ".out\n";
				int i2 = i + 1;
				varStr += "sai2[" + n + "]=sai." + namePrefix + ":" + i2 + ".out\n";		
				n++;
			}
		}
		return varStr;
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
		
		Sample cell = sampleService.getCell(cellLibrary);
		Sample platformUnit;
		try {
			platformUnit = sampleService.getPlatformUnitForCell(cell);
		} catch (Exception e) {
			logger.error(e.toString());
			throw new RuntimeException(e.toString());
		}
		Sample library = sampleService.getLibrary(cellLibrary);
		// Sample sample = sampleService.get

		String id;
		String ks;
		String lb;
		String pl;
		String pu;
		String sm;
		try {
			id = cellLibrary.getUUID().toString();
			ks = adaptorService.getAdaptor(library).getBarcodesequence();
			lb = library.getName();
			pl = "ILLUMINA"; // TODO: fix
			pu = platformUnit.getName();
			sm = library.getName(); // TODO: get the real sample name
		} catch (Exception e) {
			logger.error(e.toString());
			throw new RuntimeException(e.toString());
		}
		return new StringBuilder()
				.append("\'@RG\\t")
				.append("ID:" + id + "\\t")
				.append("KS:" + ks + "\\t")
				.append("LB:" + lb + "\\t")
				.append("PL:" + pl + "\\t")
				.append("PU:" + pu + "\\t")
				.append("SM:" + sm)
				.append("\'")
				.toString();

	}
	
	private WorkUnit prepareWorkUnit(FileGroup fg) {
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
	
	private Build getGenomeBuild(SampleSource cellLibrary) {
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
	
	private String getGenomeIndexPath(Build build) {
		String index = genomeService.getRemoteBuildPath(build) + "/bwa/" + build.getGenome().getName() +"."+ build.getName();
		return index;
	}
	
	@Override
	public Genome getGenome(String genome) {
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

	@Override
	public String getSoftwareVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSoftwareVersion(String softwareVersion) {
		// TODO Auto-generated method stub
		
	}

}
