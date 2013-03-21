package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.SoftwareMeta;

/**
 * 
 * @author asmclellan
 *
 */
public interface SoftwareLoadService extends WaspLoadService {
	
	public <T extends Software> T update(ResourceType resourceType, List<SoftwareMeta> meta, String iname, String name, int isActive, Class<T> clazz);
	
	public ResourceType getSoftwareTypeByIName(String iname);

}
