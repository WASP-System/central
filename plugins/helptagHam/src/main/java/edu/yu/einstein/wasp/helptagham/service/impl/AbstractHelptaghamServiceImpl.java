/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptagham.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.helptagham.service.HelptaghamService;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

@Transactional("entityManager")
public abstract class AbstractHelptaghamServiceImpl extends WaspServiceImpl implements HelptaghamService {
	
	@Autowired
	private FileService fileService;

	@Override
	public String performAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileHandle createAndSaveInnerFileHandle(String fileName, FileType fileType) {
		FileHandle fileHandle = new FileHandle();
		fileHandle.setFileName(fileName);
		fileHandle.setFileType(fileType);
		fileHandle = fileService.addFile(fileHandle);
		return fileHandle;
	}

	@Override
	public FileGroup createAndSaveInnerFileGroup(FileHandle fileHandle, Software software, String description) {
		FileGroup fileGroup = new FileGroup();
		fileGroup.setDescription(description);
		fileGroup.setFileType(fileHandle.getFileType());
		fileGroup.setSoftwareGeneratedById(software.getId());
		fileGroup.setIsActive(0);
		// modelScriptFG.setDerivedFrom(derrivedFromFileGroups);
		Set<FileHandle> fileHandleSet = new HashSet<FileHandle>();
		fileHandleSet.add(fileHandle);
		fileGroup.setFileHandles(fileHandleSet);
		fileGroup = fileService.addFileGroup(fileGroup);
		return fileGroup;
	}

}
