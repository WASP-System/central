package edu.yu.einstein.wasp.load.service;

import java.util.List;

import edu.yu.einstein.wasp.model.UiField;

/**
 * 
 * @author asmclellan
 *
 */
public interface WaspLoadService {
	
	public void updateUiFields(List<UiField> uiFields);
	
	public void updateUiFields(String area, List<UiField> uiFields);

	public List<String> getAreaListFromUiFields(List<UiField> uiFields);

}
