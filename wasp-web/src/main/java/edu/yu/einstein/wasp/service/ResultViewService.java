package edu.yu.einstein.wasp.service;

import java.util.List;
import java.util.Map;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.viewpanel.GridPanel;

public interface ResultViewService extends WaspService {

	/**
	 * Return a list of key-value data for all filegroups for which ids are provided
	 * @param fgIdList 
	 * @return
	 * @throws WaspException
	 */
	public List<Map<String, String>> getFileDataMapList(List<Integer> fgIdList) throws WaspException;

	/**
	 * get grid panel for displaying file data
	 * @param fileTypeId
	 * @param libraryId
	 * @param cellId
	 * @return
	 * @throws WaspException
	 */
	public GridPanel getFileGridPanel(Integer fileTypeId, Integer libraryId, Integer cellId) throws WaspException;

}
