package edu.yu.einstein.wasp.macstwo.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.macstwo.service.MacstwoService;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

@Service
public class MacstwoServiceImpl extends WaspServiceImpl implements MacstwoService {
	
	@Autowired
	private FileService fileService;

	@Override
	public String performAction() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public FileHandle createAndSaveInnerFileHandle(String fileName, FileType fileType){
		FileHandle fileHandle = new FileHandle();
		fileHandle.setFileName(fileName);
		fileHandle.setFileType(fileType);
		fileHandle = fileService.addFile(fileHandle);
		return fileHandle;
	}
	@Override
	public FileGroup createAndSaveInnerFileGroup(FileHandle fileHandle, Software software, String description){
		FileGroup fileGroup = new FileGroup();		
		fileGroup.setDescription(description);
		fileGroup.setFileType(fileHandle.getFileType());
		fileGroup.setSoftwareGeneratedBy(software);
		fileGroup.setIsActive(0);
		//modelScriptFG.setDerivedFrom(derrivedFromFileGroups);
		Set<FileHandle> fileHandleSet = new HashSet<FileHandle>();
		fileHandleSet.add(fileHandle);
		fileGroup.setFileHandles(fileHandleSet);
		fileGroup = fileService.addFileGroup(fileGroup);		
		return fileGroup;
	}
}
