package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.model.UiField;

public interface SampleTypeLoadService extends WaspLoadService {

	public void update(String iname, String name, String sampleTypeCategoryIname, Integer isActive,
			List<UiField> uiFields);

}
