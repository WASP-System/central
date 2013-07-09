package edu.yu.einstein.wasp.charts;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;

/**
 * Base class for producing web-charts / tables
 * @author asmclellan
 *
 */
public class WebChartsBase {
	
	protected static String CONTAINER_DIV_SUFFIX = "_container";
	protected static String CONTENTS_DIV_SUFFIX = "_contents";
	protected static String DESCRIPTION_DIV_SUFFIX = "_description";
	protected static String DEFAULT_DIV_PREFIX = "chart";
	
	/**
	 * Return a chart container with both 'chart' and 'description' divs populated with provided contents
	 * @param chartDivContents
	 * @param descriptionDivContents
	 * @return
	 */
	public static String getSimpleContainerCode(String chartDivContents, String descriptionDivContents){
		StringBuilder id = new StringBuilder();
		return getSimpleContainerCode(DEFAULT_DIV_PREFIX, chartDivContents, descriptionDivContents, id);
	}
	
	/**
	 * Return a chart container with both 'chart' and 'description' divs populated with provided contents.
	 * If supplied id is empty a random UUID will be set as the id value (modifies provided object):
	 * <ul>
	 * <li>container div will have class of '[prefix]_container' and id of '[prefix]_container_[id]'</li>
	 * <li>chart div will have class of '[prefix]_chart' and id of '[prefix]_chart_[id]'</li>
	 * <li>description div will have class of '[description]_chart' and id of '[description]_chart_[id]'</li>
	 * </ul>
	 * 
	 * @param prefix (for element id and class name)
	 * @param chartDivContents
	 * @param descriptionDivContents
	 * @param id (as suffix for id of element). If this value is not set, it will be set to a random UUID
	 * @return
	 */
	public static String getSimpleContainerCode(String prefix, String chartDivContents, String descriptionDivContents, StringBuilder id){
		StringBuilder sb = new StringBuilder();
		if (id.toString().isEmpty())
			id.append("_" + UUID.randomUUID().toString());
		sb.append("<div class='" + prefix + CONTAINER_DIV_SUFFIX + "' id='" + prefix + CONTAINER_DIV_SUFFIX + id + "'>\n");
		sb.append("<div class='" + prefix + CONTENTS_DIV_SUFFIX + "' id='" + prefix + CONTENTS_DIV_SUFFIX + id + "'>" + chartDivContents + "</div>\n");
		sb.append("<div class='" + prefix + DESCRIPTION_DIV_SUFFIX + "' id='" + prefix + DESCRIPTION_DIV_SUFFIX + id + "'><p>" + descriptionDivContents.replaceAll("[\n\r]", "<br /><br />") + "</p></div>\n");
		sb.append("</div>\n");
		return sb.toString();
	}
	
	/**
	 * each row represents a key-value pair where the first data point is heading (key) and the second data point is it's value.
	 * @param basicStats
	 * @return
	 * @throws JSONException
	 */
	public static String getKeyValueTableRepresentation(final WaspChart basicStats) throws Exception{
		List<List<Object>> data = basicStats.getDataSeries().get(0).getData();
		StringBuilder sb = new StringBuilder();
		sb.append("<h3>" + basicStats.getTitle() + "</h3>\n");
		sb.append("<table class='keyValueTable' >\n");
		for (List<Object> row : data){
			if (row.size() != 2)
				throw new Exception("Data is of wrong format for this representation (must be two columns)");
			String key = (String) row.get(0);
			String strVal = (String) row.get(1);
			try{
				Double.parseDouble(strVal);
				// got here so must be castable to double
				if (strVal.contains("."))
					strVal = getRounded(strVal, 2);
				else
					strVal = getDecimalSeperated(strVal);
			} catch(NumberFormatException e){}
			sb.append("<tr><th>" + key + ": </th><td>" + strVal + "</td></tr>\n");
		}
		sb.append("</table>\n");
		return getSimpleContainerCode(sb.toString(), basicStats.getDescription());
	}
	
	/**
	 * First row is column labels followed by rows of data.
	 * @param basicStats
	 * @return
	 * @throws JSONException
	 */
	public static String getTableRepresentation(final WaspChart basicStats) throws JSONException{
		List<List<Object>> data = basicStats.getDataSeries().get(0).getData();
		StringBuilder sb = new StringBuilder();
		sb.append("<h3>" + basicStats.getTitle() + "</h3>\n");
		sb.append("<table class='standardTable' >\n");
		sb.append("<tr>\n");
		for (String headerElement : basicStats.getDataSeries().get(0).getColLabels())
			sb.append("<th>" + headerElement + "</th>\n");
		sb.append("</tr>\n");
		for (List<Object> row : data){
			sb.append("<tr>\n");
			for (Object element : row){
				String strVal = (String) element;
				try{
					Double.parseDouble(strVal);
					// got here so must be castable to double
					if (strVal.contains("."))
						strVal = getRounded(strVal, 2);
					else
						strVal = getDecimalSeperated(strVal);
				} catch(NumberFormatException e){}
				sb.append("<td>" + strVal + "</td>\n");
			}
			sb.append("</tr>\n");
		}
		sb.append("</table>\n");
		return getSimpleContainerCode(sb.toString(), basicStats.getDescription());
	}
	
	public static String getRounded(String number, int decimalPlaces){
		BigDecimal bd = new BigDecimal(number);
		return bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP).toPlainString();
	}
	
	public static String getDecimalSeperated(String number){
		BigDecimal bd = new BigDecimal(number);
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
		formatter.getDecimalFormatSymbols().setDecimalSeparator(',');
		return formatter.format(bd.longValue());
	}

}
