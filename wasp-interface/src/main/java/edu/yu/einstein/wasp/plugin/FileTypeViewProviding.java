package edu.yu.einstein.wasp.plugin;

import java.util.List;
import java.util.Map;


public interface FileTypeViewProviding extends WebInterfacing {
	
	public List<Map> getFileDetails(Integer fileGroupId);

}
