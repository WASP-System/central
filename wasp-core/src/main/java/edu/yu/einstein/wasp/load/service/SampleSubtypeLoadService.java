package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeMeta;
import edu.yu.einstein.wasp.model.SampleType;

public interface SampleSubtypeLoadService extends WaspLoadService {
	
	public SampleSubtype update(String iname, String name, SampleType sampleType, int isActive,  List<ResourceCategory> compatibleResources, 
			String applicableRoles, List<SampleSubtypeMeta> meta, List<String> areaList);
	
	public void validateApplicableRoles(String applicableRolesString);


}
