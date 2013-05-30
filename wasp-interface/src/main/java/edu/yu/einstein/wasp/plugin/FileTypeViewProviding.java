package edu.yu.einstein.wasp.plugin;

import java.util.Map;

import edu.yu.einstein.wasp.Hyperlink;


public interface FileTypeViewProviding extends WebInterfacing {
	
	public Map<String, Hyperlink> getFileDetails(Integer fileGroupId);

	public Map<String, Hyperlink> getDownloadPageForCellLibraryByFileType(Integer cellLibraryId, Integer fileTypeId);

}
