package edu.yu.einstein.wasp.load;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.SampleTypeLoadService;

/**
 * update/inserts db copy of type sample from bean definition takes in
 * properties - iname - name - uifields (List<UiFields) - sampleType (string to
 * sampletype iname table)
 * 
 */

public class SampleTypeLoader extends WaspLoader {

	@Autowired
	private SampleTypeLoadService sampleTypeLoadService;

	private String sampleTypeCategoryIname;

	private Integer isActive;

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getSampleTypeCategoryIname() {
		return sampleTypeCategoryIname;
	}

	public void setSampleTypeCategoryIname(String sampleTypeCategoryIname) {
		this.sampleTypeCategoryIname = sampleTypeCategoryIname;
	}

	public SampleTypeLoader() {
	}

	public SampleTypeLoader(SampleTypeLoader inheritFromLoadService) {
		super(inheritFromLoadService);
	}

	@PostConstruct
	public void init() throws Exception {
		sampleTypeLoadService.update(iname, name, sampleTypeCategoryIname, isActive, uiFields);
		sampleTypeLoadService.updateUiFields(uiFields);

	}

}
