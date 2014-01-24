package edu.yu.einstein.wasp.plugin.illumina.batch.tasklet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.codehaus.plexus.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionExponential;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamFileService;
import edu.yu.einstein.wasp.plugin.babraham.software.TrimGalore;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.plugin.illumina.software.IlluminaHiseqSequenceRunProcessor;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * 
 * 
 * @author calder
 * 
 */
@Component
public class RegisterTrimTasklet extends WaspTasklet {

	private RunService runService;
	
	@Autowired
	private SampleService sampleService;

	private int runId;
	private Run run;

	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private FastqService fastqService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileTypeService fileTypeService;

	@Autowired
	private TrimGalore trim_galore;
	
	@Autowired 
	private FileType fastqFileType;
	
	@Autowired
	private IlluminaHiseqSequenceRunProcessor casava;
	
	private String workingDirectory;
	private GridWorkService workService;
	private GridTransportConnection transportConnection;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BabrahamFileService babrahamFileService;

	public RegisterTrimTasklet() {
		// required by cglib
	}
	
	public RegisterTrimTasklet(Integer runId) {
		this.runId = runId;
		this.run = runService.getRunById(runId);
	}

	@Override
	@RetryOnExceptionExponential
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {

	    run = runService.getRunById(runId);
            
            Set<SampleSource> cellLibraries = runService.getCellLibrariesOnSuccessfulRunCells(run);

		// if the work has already been started, then check to see if it is finished
		// if not, throw an exception that is caught by the repeat policy.
		RepeatStatus repeatStatus = super.execute(contrib, context);
		if (repeatStatus.equals(RepeatStatus.FINISHED)) {
		    
		    WorkUnit w = getBareWorkUnit();
		    
		    workService = hostResolver.getGridWorkService(w);
	            transportConnection = workService.getTransportConnection();
		    
		    // the work has completed, we're going to sweep up the summary
		    // statistics and add them to fileGroup metadata.
		    for (SampleSource cellLibrary : cellLibraries) {
		        Set<FileGroup> fgs = fileService.getFilesForCellLibraryByType(cellLibrary, fastqService.getFastqFileType());
		        Iterator<FileGroup> fgi = fgs.iterator();
		        while (fgi.hasNext()) {
		            FileGroup fg = (FileGroup) fgi.next();
		            HashSet<String> atts = new HashSet<String>();
		            atts.add(FastqService.FASTQ_ATTRIBUTE_TRIMMED);
		            if (fileTypeService.hasAttributes(fg, atts)) {
		                FileGroup originalFastq = (FileGroup) fg.getDerivedFrom().iterator().next();
		                if (fastqService.getNumberOfReadSegments(fg) == 1) {
		                    w.setCommand("cat " + originalFastq.getId() + "_1_sum_trim_counts.txt");
		                } else {
		                    w.setCommand("cat " + originalFastq.getId() + "_1_sum_trim_counts.txt <(echo :::::) " + originalFastq.getId() + "_2_sum_trim_counts.txt");
		                }
		            }
		            GridResult r = transportConnection.sendExecToRemote(w);
		            BufferedReader br = new BufferedReader(new InputStreamReader(r.getStdOutStream()));
		            
		            JSONArray ja = new JSONArray();
		            JSONObject set = new JSONObject();
		            set.put("readSegment", 1);
		            set.put("values", new JSONObject());
		            try {
		                String line = null;
		                while ((line = br.readLine()) != null) {
		                    
		                    line = StringUtils.chomp(line);
		                    
		                    if (line.startsWith(":::::")) {
		                        ja.put(set);
		                        set = new JSONObject();
		                        set.put("readSegment", 2);
		                        set.put("values", new JSONObject());
		                        continue;
		                    }
		                    
		                    String[] elements = line.split("\\t");
		                    JSONObject vals = (JSONObject) set.get("values");
		                    vals.put(elements[0], new JSONArray(Arrays.copyOfRange(elements, 1, 3)));
		                }
		            } finally {
		                br.close();
		            }
		            babrahamFileService.setFastqTrimJSON(fg, ja);
		        }
		    }
		    
		    return RepeatStatus.FINISHED;
		}

		
		// first execution code begins here.
		List<GridResult> results = new LinkedList<GridResult>();
	        
	        for (SampleSource cellLibrary : cellLibraries) {
	            Set<FileGroup> fgs = fileService.getFilesForCellLibraryByType(cellLibrary, fastqService.getFastqFileType());
	            if (fgs.size() != 1) {
	                String m = "cell library " + cellLibrary.getId() + ":" + cellLibrary.getUUID() + " was expected to have 1 set of FASTQ files, found " + fgs.size();
	                logger.error(m);
	                throw new WaspRuntimeException(m);
	            }
	            
	            WorkUnit w = getBareWorkUnit();
	            
	            String jobRoot = transportConnection.getConfiguredSetting("results.dir");
	            
	            workService = hostResolver.getGridWorkService(w);
	            transportConnection = workService.getTransportConnection();
	            
	            Set<FileHandle> fastq = new TreeSet<FileHandle>(new FastqComparator(fastqService));
	            
	            FileGroup fastqG = fgs.iterator().next();
	            fastq.addAll(fastqG.getFileHandles());
	            
	            Set<FileHandle> trimmed_fastq = new HashSet<FileHandle>();
	            
	            Integer rs = fastqService.getNumberOfReadSegments(fastqG);
	            
	            Iterator<FileHandle> fhi = fastq.iterator();
	            String output = jobRoot + "/" + runId + "/fastq".replaceAll("//", "/").replaceAll("//", "/");
	            
	            w.setResultsDirectory(output);
	            
	            int fileN = 0;

	            w.setCommand("shopt -u nullglob");
	            w.addCommand("rm -f *_trim_counts.txt");
	            
	            while (fhi.hasNext()) {
	                FileHandle fh = fhi.next();
	                w.addRequiredFile(fh);
	                trimmed_fastq.add(doFile(w, fileN++, fh, fastqG));

	                if (rs == 2) {
	                    fh = fhi.next();
	                    trimmed_fastq.add(doFile(w, fileN++, fh, fastqG));
	                }
	            }
	            
	            w.addCommand(sortCommand(fastqG.getId(), 1));
	            if (rs == 2) {
	                w.addCommand(sortCommand(fastqG.getId(), 2));
	            }
	            
	            FileGroup resultFiles = new FileGroup();
	            HashSet<FileGroup> derivedFrom = new HashSet<FileGroup>();
	            derivedFrom.add(fastqG);
	            resultFiles.setDerivedFrom(derivedFrom);
	            Set<FileHandle> resultFileHandles = new TreeSet<FileHandle>(new FastqComparator(fastqService));
	            resultFileHandles.addAll(trimmed_fastq);
	            resultFiles.setFileHandles(resultFileHandles);
	            fileService.addFileGroup(resultFiles);
	            fastqService.copyFastqFileGroupMetadata(fastqG, resultFiles);
	            fileTypeService.addAttribute(resultFiles, FastqService.FASTQ_ATTRIBUTE_TRIMMED);
	            GridResult result = workService.execute(w);
	            results.add(result);
	            logger.debug("started registration of trimmed illumina output: " + result.getUuid());
	            
	        }

		
		
		
		//place the grid result in the step context
		WaspTasklet.storeStartedResult(context, result);
		
		return RepeatStatus.CONTINUABLE;

	}
	
	private FileHandle doFile(WorkUnit w, int fileNumber, FileHandle fileHandle, FileGroup fileGroup) throws MetadataException {
	    Integer rs = fastqService.getNumberOfReadSegments(fileGroup);
	    String prefix = "";
	    prefix = "_" + fastqService.getFastqReadSegmentNumber(fileHandle);
	    w.addCommand("sed -n '/^length/,/^$/p' ${" + WorkUnit.INPUT_FILE + "[" + fileNumber + "]}_trimming_report.txt | tail -n +2 | head -n -1 >> " + fileGroup.getId() + prefix + "_trim_counts.txt" );
	    if (rs == 2) {
	        // paired-end read names end in "_val_?.fq.gz" while single-end reads end with "_trimmed.fq.gz"
	        prefix = "_val_" + prefix;
	    } else {
	        prefix = "_trimmed";
	    }
            String trimmedName = fileHandle.getFileName().replace(".fastq.gz", prefix + ".fq.gz");
            w.addCommand("ln -s " + trimmedName + " ${" + WorkUnit.OUTPUT_FILE + "[" + fileNumber + "]}" );
            return createResultFile(fileHandle, trimmedName);
	}
	
	private String sortCommand(Integer fileId, Integer readSegment) {
	    String filePrefix = fileId + "_" + readSegment;
	    return "sort -nk1,1 " + filePrefix + "_trim_counts.txt | awk '{if (! a[$1]> 0) { a[$1]==0; b[$1]==0; c[$1]==0 }; " + 
                    "a[$1]+=$2;b[$1]+=$3;c[$1]=$4;}END{for (i in a) { print i \"\\t\" a[i] \"\\t\" b[i] \"\\t\" c[i] } }' | " + 
                    "sort -nk1,1 > " + filePrefix + "_sum_trim_counts.txt";
	}
	
	private FileHandle createResultFile(FileHandle originFile, String fileName) throws MetadataException {
	    FileHandle trimmed = new FileHandle();
            trimmed.setFileName(fileName);
            trimmed.setFileType(fastqFileType);
            fileService.addFile(trimmed);
            fastqService.copyFastqFileHandleMetadata(originFile, trimmed);
            return trimmed;
	}

	/**
	 * @return the runService
	 */
	public RunService getRunService() {
		return runService;
	}

	/**
	 * @param runService
	 *            the runService to set
	 */
	@Autowired
	public void setRunService(RunService runService) {
		this.runService = runService;
	}
	
	private WorkUnit getBareWorkUnit() throws GridException {
	    List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
            sd.add(casava);
            sd.add(trim_galore);
            
            WorkUnit w = new WorkUnit();
            w.setProcessMode(ProcessMode.SINGLE);
            w.setMode(ExecutionMode.PROCESS);
            w.setSoftwareDependencies(sd);
            w.setSecureResults(true);
            String stageDir = transportConnection.getConfiguredSetting("illumina.data.stage");
            if (!PropertyHelper.isSet(stageDir))
                throw new GridException("illumina.data.stage is not defined!");
            
            String jobRoot = transportConnection.getConfiguredSetting("results.dir");
            if (!PropertyHelper.isSet(jobRoot))
                    throw new GridException("results.dir is not defined!");
            
            workingDirectory = stageDir + "/" + run.getName() + "/wasp/sequence";
            
            w.setWorkingDirectory(workingDirectory);
            w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER);
            return w;
	}

}
