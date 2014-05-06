package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.SoftwareMeta;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * 
 * @author asmclellan
 *
 */
public interface SoftwareLoadService extends WaspLoadService {
	
	public <T extends SoftwarePackage> T update(ResourceType resourceType, List<SoftwareMeta> meta, String iname, String name, String description, String version, List<SoftwarePackage> softwareDependencies, int isActive, Class<T> clazz);
	
	public ResourceType getSoftwareTypeByIName(String iname);

}
