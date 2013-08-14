package edu.yu.einstein.wasp.charts;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;

import edu.yu.einstein.wasp.exception.ChartException;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.viewpanel.WebContent;

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
	
	public static String getUniqueContainerId(){
		return "_" + UUID.randomUUID().toString();
	}
	
	/**
	 * Return a chart container with both 'chart' and 'description' divs populated with provided contents
	 * @param chartDivContents
	 * @param descriptionDivContents
	 * @return
	 */
	public static String getSimpleContainerCode(String chartDivContents, String descriptionDivContents){
		return getSimpleContainerCode(DEFAULT_DIV_PREFIX, chartDivContents, descriptionDivContents);
	}
	
	/**
	 * Return a chart container with both 'chart' and 'description' divs populated with provided contents.
	 * A random UUID will be set as the containerId value :
	 * <ul>
	 * <li>container div will have class of '[prefix]_container' and id of '[prefix]_container_[containerId]'</li>
	 * <li>chart div will have class of '[prefix]_chart' and id of '[prefix]_chart_[containerId]'</li>
	 * <li>description div will have class of '[description]_chart' and id of '[description]_chart_[containerId]'</li>
	 * </ul>
	 * 
	 * @param prefix (for element id and class name)
	 * @param chartDivContents
	 * @param descriptionDivContents
	 * @param id (as suffix for id of element). If this value is not set, it will be set to a random UUID
	 * @return
	 */
	public static String getSimpleContainerCode(String prefix, String chartDivContents, String descriptionDivContents){
		return getSimpleContainerCode(prefix, chartDivContents, descriptionDivContents, getUniqueContainerId());
	}
	
	/**
	 * Return a chart container with both 'chart' and 'description' divs populated with provided contents.
	 * <ul>
	 * <li>container div will have class of '[prefix]_container' and id of '[prefix]_container_[containerId]'</li>
	 * <li>chart div will have class of '[prefix]_chart' and id of '[prefix]_chart_[containerId]'</li>
	 * <li>description div will have class of '[description]_chart' and id of '[description]_chart_[containerId]'</li>
	 * </ul>
	 * 
	 * @param prefix (for element id and class name)
	 * @param chartDivContents
	 * @param descriptionDivContents
	 * @param containerId (as suffix for id of element). If this value is not set, it will be set to a random UUID
	 * @return
	 */
	public static String getSimpleContainerCode(String prefix, String chartDivContents, String descriptionDivContents, String containerId){
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='" + prefix + CONTAINER_DIV_SUFFIX + "' id='" + prefix + CONTAINER_DIV_SUFFIX + containerId + "'>\n");
		sb.append("<div class='" + prefix + CONTENTS_DIV_SUFFIX + "' id='" + prefix + CONTENTS_DIV_SUFFIX + containerId + "'>" + chartDivContents + "</div>\n");
		sb.append("<div class='" + prefix + DESCRIPTION_DIV_SUFFIX + "' id='" + prefix + DESCRIPTION_DIV_SUFFIX + containerId + "'><p>" + descriptionDivContents.replaceAll("[\n\r]", "<br /><br />") + "</p></div>\n");
		sb.append("</div>\n");
		return sb.toString();
	}
	
	/**
	 * each row represents a key-value pair where the first data point is heading (key) and the second data point is it's value.
	 * @param basicStats
	 * @return
	 * @throws ChartException 
	 */
	public static WebContent getKeyValueTableRepresentation(final WaspChart basicStats) throws ChartException{
		return getKeyValueTableRepresentation(basicStats, null);
	}
	
	/**
	 * each row represents a key-value pair where the first data point is heading (key) and the second data point is it's value.
	 * Providing a MessageService instance enables internationalization of
	 * title and description (assumes chart title and description parameter values are localization property keys).
	 * @param basicStats
	 * @param messageService
	 * @return
	 * @throws ChartException 
	 */
	public static WebContent getKeyValueTableRepresentation(final WaspChart basicStats, MessageService messageService) throws ChartException{
		String description;
		String title;
		if (messageService == null){
			description = basicStats.getDescription();
			title = basicStats.getTitle();
		} else {
			description = basicStats.getLocalizedDescription(messageService);
			title = basicStats.getLocalizedTitle(messageService);
		}
		List<List<Object>> data = basicStats.getDataSeries().get(0).getData();
		StringBuilder sb = new StringBuilder();
		sb.append("<h3>" + title + "</h3>\n");
		sb.append("<table class='keyValueTable' >\n");
		if (data.isEmpty()){
			sb.append("<tr><td>No data to display</td></tr>\n");
		} else {
			for (List<Object> row : data){
				if (row.size() != 2)
					throw new ChartException("Data is of wrong format for this representation (must be two columns)");
				String key = (String) row.get(0);
				if (messageService != null)
					key = messageService.getMessage(key);
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
		}
		sb.append("</table>\n");
		WebContent content = new WebContent();
		content.setHtmlCode(getSimpleContainerCode(sb.toString(), description));
		return content;
	}
	
	/**
	 * First row is column labels followed by rows of data.
	 * @param basicStats
	 * @return
	 * @throws JSONException
	 */
	public static WebContent getTableRepresentation(final WaspChart basicStats){
		return getTableRepresentation(basicStats, null);
	}
	
	/**
	 * First row is column labels followed by rows of data.
	 * Providing a MessageService instance enables internationalization of
	 * title and description (assumes chart title and description parameter values are localization property keys).
	 * @param basicStats
	 * @param messageService
	 * @return
	 * @throws JSONException
	 */
	public static WebContent getTableRepresentation(final WaspChart basicStats, MessageService messageService){
		String description;
		String title;
		List<String> colLabels;
		if (messageService == null){
			description = basicStats.getDescription();
			title = basicStats.getTitle();
			colLabels = basicStats.getDataSeries().get(0).getColLabels();
		} else {
			description = basicStats.getLocalizedDescription(messageService);
			title = basicStats.getLocalizedTitle(messageService);
			colLabels = basicStats.getDataSeries().get(0).getLocalizedColLabels(messageService);
		}
		List<List<Object>> data = basicStats.getDataSeries().get(0).getData();
		StringBuilder sb = new StringBuilder();
		sb.append("<h3>" + title + "</h3>\n");
		sb.append("<table class='standardTable' >\n");
		if (data.isEmpty()){
			sb.append("<tr><td>No data to display</td></tr>\n");
		} else {
			sb.append("<tr>\n");
			for (String headerElement : colLabels)
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
		}
		sb.append("</table>\n");
		WebContent content = new WebContent();
		content.setHtmlCode(getSimpleContainerCode(sb.toString(), description));
		return content;
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
