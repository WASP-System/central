package edu.yu.einstein.interfacing.wasp.interfacing.plugin;

import java.util.Map;

import edu.yu.einstein.interfacing.wasp.Hyperlink;


public interface FileTypeViewProviding {
	
	public Map<String, Hyperlink> getFileDetails(Integer fileGroupId);

//	public Map<String, Hyperlink> getDownloadPageForCellLibraryByFileType(Integer cellLibraryId, Integer fileTypeId);

}
