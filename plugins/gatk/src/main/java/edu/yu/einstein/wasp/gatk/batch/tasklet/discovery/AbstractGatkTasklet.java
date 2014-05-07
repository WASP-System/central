package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.service.FileService;

public abstract class AbstractGatkTasklet extends WaspRemotingTasklet {

	protected Set<Integer> inputFilegroupIds;
	
	protected Set<Integer> outputFilegroupIds;
	
	protected String scratchDirectory;

	public AbstractGatkTasklet(String inputFilegroupIds, String outputFilegroupIds) {
		this.inputFilegroupIds = getFileGroupIdsFromCommaDelimitedString(inputFilegroupIds);
		this.outputFilegroupIds = getFileGroupIdsFromCommaDelimitedString(outputFilegroupIds);
	}
	
	public String getScratchDirectory() {
		return scratchDirectory;
	}

	public void setScratchDirectory(String scratchDirectory) {
		this.scratchDirectory = scratchDirectory;
	}

	public Set<Integer> getInputFilegroupIds() {
		return inputFilegroupIds;
	}

	public Set<Integer> getOutputFilegroupIds() {
		return outputFilegroupIds;
	}
	
	public static String getFileGroupIdsAsCommaDelimitedString(Set<Integer> filegroupIds){
		return StringUtils.collectionToCommaDelimitedString(filegroupIds);
	}
	
	public static String getFileGroupsAsCommaDelimitedString(Set<FileGroup> filegroups){
		Set<Integer> filegroupIds = new HashSet<>();
		for (FileGroup fg : filegroups)
			filegroupIds.add(fg.getId());
		return getFileGroupIdsAsCommaDelimitedString(filegroupIds);
		
	}
	
	public static Set<Integer> getFileGroupIdsFromCommaDelimitedString(String fileGroupIdListString){
		Set<Integer> filegroupIds = new HashSet<>();
		for (String fgIdStr : StringUtils.commaDelimitedListToSet(fileGroupIdListString))
			filegroupIds.add(Integer.parseInt(fgIdStr));
		return filegroupIds;
	}
	
	@Transactional("entityManager")
	public static Set<FileGroup> getFileGroupsFromCommaDelimitedString(String fileGroupIdListString, FileService fileService){
		Set<FileGroup> filegroups = new HashSet<>();
		for (String fgIdStr : StringUtils.commaDelimitedListToSet(fileGroupIdListString))
			filegroups.add(fileService.getFileGroupById(Integer.parseInt(fgIdStr)));
		return filegroups;
	}
	
	@Transactional("entityManager")
	public static Set<FileGroup> getFileGroupsFromFileGroupIdList(Set<Integer> filegroupIds, FileService fileService){
		Set<FileGroup> filegroups = new HashSet<>();
		for (Integer fgId : filegroupIds)
			filegroups.add(fileService.getFileGroupById(fgId));
		return filegroups;
	}

}
