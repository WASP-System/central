package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.gatk.service.GatkService;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.WaspModel;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

public abstract class AbstractGatkTasklet extends WaspRemotingTasklet {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractGatkTasklet.class);
	
	public static int THREADS_2 = 2;
	
	public static int THREADS_4 = 4;
	
	public static int THREADS_8 = 8;
	
	public static int MEMORY_GB_2 = 2;
	
	public static int MEMORY_GB_4 = 4;
	
	public static int MEMORY_GB_8 = 8;
	

	@Autowired
	protected JobService jobService;
	
	@Autowired
	protected GenomeService genomeService;
	
	@Autowired
	protected FileService fileService;
	
	@Autowired
	protected GatkService gatkService;
	
	@Autowired
	protected GATKSoftwareComponent gatk;
	
	@Autowired
	protected GridHostResolver gridHostResolver;

	protected LinkedHashSet<Integer> inputFilegroupIds;
	
	protected LinkedHashSet<Integer> outputFilegroupIds;
	
	protected Integer jobId;
	
	protected String scratchDirectory;

	public AbstractGatkTasklet(String inputFilegroupIds, String outputFilegroupIds, Integer jobId) {
		this.inputFilegroupIds = getModelIdsFromCommaDelimitedString(inputFilegroupIds);
		this.outputFilegroupIds = getModelIdsFromCommaDelimitedString(outputFilegroupIds);
		this.jobId = jobId;
		logger.trace("setting inputFilegroupIds=" + inputFilegroupIds);
		logger.trace("setting outputFilegroupIds=" + outputFilegroupIds);
	}
	
	public String getScratchDirectory() {
		return scratchDirectory;
	}

	public void setScratchDirectory(String scratchDirectory) {
		this.scratchDirectory = scratchDirectory;
	}

	public LinkedHashSet<Integer> getInputFilegroupIds() {
		return inputFilegroupIds;
	}

	public LinkedHashSet<Integer> getOutputFilegroupIds() {
		return outputFilegroupIds;
	}
	
	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	
	public static String getModelIdsAsCommaDelimitedString(LinkedHashSet<? extends WaspModel> modelObjects){
		LinkedHashSet<Integer> modelIds = new LinkedHashSet<>();
		for (WaspModel model : modelObjects)
			modelIds.add(model.getId());
		String modelStr = StringUtils.collectionToCommaDelimitedString(modelIds);
		logger.trace("returning: " + modelStr);
		return modelStr;
		
	}
	
	public static LinkedHashSet<Integer> getModelIdsFromCommaDelimitedString(String modelIdListString){
		logger.trace("extracting: " + modelIdListString);
		LinkedHashSet<Integer> modelIds = new LinkedHashSet<>();
		for (String idStr : StringUtils.commaDelimitedListToStringArray(modelIdListString))
			modelIds.add(Integer.parseInt(idStr));
		return modelIds;
	}
	
	@Transactional("entityManager")
	public static LinkedHashSet<FileGroup> getFileGroupsFromCommaDelimitedString(String fileGroupIdListString, FileService fileService){
		logger.trace("extracting: " + fileGroupIdListString);
		LinkedHashSet<FileGroup> filegroups = new LinkedHashSet<>();
		for (String fgIdStr : StringUtils.commaDelimitedListToStringArray(fileGroupIdListString))
			filegroups.add(fileService.getFileGroupById(Integer.parseInt(fgIdStr)));
		return filegroups;
	}
	
	@Transactional("entityManager")
	public static LinkedHashSet<FileGroup> getFileGroupsFromFileGroupIdList(Set<Integer> filegroupIds, FileService fileService){
		LinkedHashSet<FileGroup> filegroups = new LinkedHashSet<>();
		for (Integer fgId : filegroupIds)
			filegroups.add(fileService.getFileGroupById(fgId));
		return filegroups;
	}
	
	public static String getSampleFgMapAsJsonString(Map<Sample, FileGroup> sampleFileGroups){
		JSONObject jsonObject = new JSONObject();
		for (Sample sample: sampleFileGroups.keySet())
			jsonObject.put(sample.getId().toString(), sampleFileGroups.get(sample).getId().toString());
		logger.trace("returning json: " + jsonObject.toString());
		return jsonObject.toString();
	}
	
	@Transactional("entityManager")
	public static Map<Sample, FileGroup> getSampleFgMapFromJsonString(String jsonString, SampleService sampleService, FileService fileService){
		logger.trace("extracting json: " + jsonString);
		JSONObject jsonObject = new JSONObject(jsonString);
		Map<Sample, FileGroup> sampleFileGroups = new HashMap<>();
		for (Object sampleIdObj : jsonObject.keySet()){
			Sample sample = sampleService.getSampleById(Integer.parseInt(sampleIdObj.toString()));
			sampleFileGroups.put(sample, fileService.getFileGroupById(jsonObject.getInt(sampleIdObj.toString())));
		}
		return sampleFileGroups;
	}
	
	@Transactional("entityManager")
	@Override
	public void doPreFinish(ChunkContext context) throws Exception {
		for (Integer fgId : this.getOutputFilegroupIds()){
			logger.debug("Setting as active FileGroup with id=: " + fgId);
			fileService.getFileGroupById(fgId).setIsActive(1);
		}
	}

}
