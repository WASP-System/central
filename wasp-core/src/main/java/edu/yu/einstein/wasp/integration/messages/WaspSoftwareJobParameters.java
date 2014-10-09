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

	public static final String CELL_LIBRARY_ID_LIST = "cellLibraryIdList"; // Sample Source reference
	
	public static final String FILEGROUP_ID_INPUT = "inputFgId";
	
	public static final String FILEGROUP_ID_LIST_INPUT = "inputFgIdList";
	
	public static final String FILEGROUP_ID_OUTPUT = "outputFgId";
	
	public static final String FILEGROUP_ID_LIST_OUTPUT = "outputFgIdList";
	
	public static final String IS_DEDUP = "isDedup";
	
	
	/**
	 * ID for a string representation of a genome in the format: TaxonID:Genome Name:Build ID
	 * eg "10090:GRCm38:70" 
	 */
	public static final String GENOME = "genome";  
	
	/**
	 * Returns a set of cellLibrary (SourceSample) ids from a comma delimited list provided (typically a batch job parameter associated with key
	 * CELL_LIBRARY_ID)
	 * @param parameterValue
	 * @return
	 */
	public static List<Integer> getCellLibraryIdList(String parameterValue){
		List<Integer> cellLibraryIds = new ArrayList<Integer>();
		for (String id: parameterValue.split(","))
			cellLibraryIds.add(Integer.valueOf(id));
		return cellLibraryIds;
	}
	
	/**
	 * Returns comma delimited string of cellLibrary (SourceSample) ids from the given Set. Use this as a parameter value when using key CELL_LIBRARY_ID.
	 * @param cellLibraryIds
	 * @return
	 */
	public static String getCellLibraryListAsParameterValue(List<Integer> cellLibraryIds){
		String value = "";
		for (Integer id: cellLibraryIds){
			value += id.toString() + ",";
		}
		value = StringUtils.removeEnd(value, ",");
		return value;
	}
	

}
