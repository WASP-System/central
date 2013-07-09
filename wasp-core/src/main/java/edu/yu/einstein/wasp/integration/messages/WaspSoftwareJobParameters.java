package edu.yu.einstein.wasp.integration.messages;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author asmclellan
 *
 */
public class WaspSoftwareJobParameters extends WaspJobParameters {

	public static final String LIBRARY_CELL_ID_LIST = "libraryCellIdList"; // Sample Source reference
	
	
	/**
	 * ID for a string representation of a genome in the format: TaxonID:Genome Name:Build ID
	 * eg "10090:GRCm38:70" 
	 */
	public static final String GENOME = "genome";  
	
	/**
	 * Returns a set of libraryCell (SourceSample) ids from a comma delimited list provided (typically a batch job parameter associated with key
	 * LIBRARY_CELL_ID)
	 * @param parameterValue
	 * @return
	 */
	public static List<Integer> getLibraryCellIdList(String parameterValue){
		List<Integer> libraryCellIds = new ArrayList<Integer>();
		for (String id: parameterValue.split(","))
			libraryCellIds.add(Integer.valueOf(id));
		return libraryCellIds;
	}
	
	/**
	 * Returns comma delimited string of libraryCell (SourceSample) ids from the given Set. Use this as a parameter value when using key LIBRARY_CELL_ID.
	 * @param libraryCellIds
	 * @return
	 */
	public static String getLibraryCellListAsParameterValue(List<Integer> libraryCellIds){
		String value = "";
		for (Integer id: libraryCellIds){
			value += id.toString() + ",";
		}
		value = StringUtils.removeEnd(value, ",");
		return value;
	}
	

}
