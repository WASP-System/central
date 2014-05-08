package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.gatk.software.GATKSoftwareComponent;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.SampleService;

public abstract class AbstractGatkTasklet extends WaspRemotingTasklet {
	
	public static int THREADS_2 = 2;
	
	public static int THREADS_4 = 4;
	
	public static int THREADS_8 = 8;
	
	public static int MEMORY_GB_2 = 2;
	
	public static int MEMORY_GB_4 = 4;
	
	public static int MEMORY_GB_8 = 8;
	
	@Autowired
	protected GenomeService genomeService;
	
	@Autowired
	protected FileService fileService;
	
	@Autowired
	protected GATKSoftwareComponent gatk;
	
	@Autowired
	protected GridHostResolver gridHostResolver;

	protected LinkedHashSet<Integer> inputFilegroupIds;
	
	protected LinkedHashSet<Integer> outputFilegroupIds;
	
	protected Integer jobId;
	
	protected String scratchDirectory;

	public AbstractGatkTasklet(String inputFilegroupIds, String outputFilegroupIds) {
		this.inputFilegroupIds = getFileGroupIdsFromCommaDelimitedString(inputFilegroupIds);
		this.outputFilegroupIds = getFileGroupIdsFromCommaDelimitedString(outputFilegroupIds);
	}
	
	@Transactional("entityManager")
	public Build getBuildForFg(FileGroup fileGroup){
		Set<SampleSource> fgCl = fileGroup.getSampleSources();
		if (fgCl == null || fgCl.isEmpty())
			return null;
		return genomeService.getGenomeBuild(fgCl.iterator().next());
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
	
	public static String getFileGroupIdsAsCommaDelimitedString(LinkedHashSet<FileGroup> filegroups){
		LinkedHashSet<Integer> filegroupIds = new LinkedHashSet<>();
		for (FileGroup fg : filegroups)
			filegroupIds.add(fg.getId());
		return StringUtils.collectionToCommaDelimitedString(filegroupIds);
		
	}
	
	public static LinkedHashSet<Integer> getFileGroupIdsFromCommaDelimitedString(String fileGroupIdListString){
		LinkedHashSet<Integer> filegroupIds = new LinkedHashSet<>();
		for (String fgIdStr : StringUtils.commaDelimitedListToSet(fileGroupIdListString))
			filegroupIds.add(Integer.parseInt(fgIdStr));
		return filegroupIds;
	}
	
	@Transactional("entityManager")
	public static LinkedHashSet<FileGroup> getFileGroupsFromCommaDelimitedString(String fileGroupIdListString, FileService fileService){
		LinkedHashSet<FileGroup> filegroups = new LinkedHashSet<>();
		for (String fgIdStr : StringUtils.commaDelimitedListToSet(fileGroupIdListString))
			filegroups.add(fileService.getFileGroupById(Integer.parseInt(fgIdStr)));
		return filegroups;
	}
	
	@Transactional("entityManager")
	public static LinkedHashSet<FileGroup> getFileGroupsFromFileGroupIdList(LinkedHashSet<Integer> filegroupIds, FileService fileService){
		LinkedHashSet<FileGroup> filegroups = new LinkedHashSet<>();
		for (Integer fgId : filegroupIds)
			filegroups.add(fileService.getFileGroupById(fgId));
		return filegroups;
	}
	
	public static String getSampleFgMapAsJsonString(Map<Sample, FileGroup> sampleFileGroups){
		JSONObject jsonObject = new JSONObject();
		for (Sample sample: sampleFileGroups.keySet())
			jsonObject.put(sample.getId().toString(), sampleFileGroups.get(sample).getId());
		return jsonObject.toString();
	}
	
	@Transactional("entityManager")
	public static Map<Sample, FileGroup> getSampleFgMapFromJsonString(String jsonString, SampleService sampleService, FileService fileService){
		JSONObject jsonObject = new JSONObject(jsonString);
		Map<Sample, FileGroup> sampleFileGroups = new HashMap<>();
		for (Object sampleIdObj : jsonObject.keySet()){
			Sample sample = sampleService.getSampleById((Integer) sampleIdObj);
			sampleFileGroups.put(sample, fileService.getFileGroupById(jsonObject.getInt(sampleIdObj.toString())));
		}
		return sampleFileGroups;
	}
	
	@Transactional("entityManager")
	@Override
	public void doPreFinish(ChunkContext context) throws Exception {
		for (Integer fgId : this.getOutputFilegroupIds())
			fileService.getFileGroupById(fgId).setIsActive(1);
	}

}
