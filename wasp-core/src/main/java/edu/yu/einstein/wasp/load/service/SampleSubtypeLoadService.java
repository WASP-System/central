package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.model.SampleSubtypeMeta;
import edu.yu.einstein.wasp.model.UiField;

public interface SampleSubtypeLoadService extends WaspLoadService {
	
	public void update(String sampleTypeString, List<SampleSubtypeMeta> meta, String iname, String name, Integer isActive, List<UiField> uiFields, String applicableRoles, List<String> compatibleResourcesByIName);
	
	public void validateApplicableRoles(String applicableRolesString);

}
