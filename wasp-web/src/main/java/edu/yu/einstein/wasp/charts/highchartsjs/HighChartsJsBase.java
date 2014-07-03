package edu.yu.einstein.wasp.charts.highchartsjs;

import java.awt.Color;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.charts.DataSeries;
import edu.yu.einstein.wasp.charts.WaspChart2D;
import edu.yu.einstein.wasp.charts.WebChartsBase;
import edu.yu.einstein.wasp.exception.ChartException;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.viewpanel.WebContent;

/**
 * Base Class for producing HighChartsJs charts (www.highcharts.com)
 * @author asmclellan
 *
 */
public abstract class HighChartsJsBase extends WebChartsBase{
	
	public static Logger logger = LoggerFactory.getLogger(HighChartsJsBase.class);
	
	private static String X_AXIS_NAME = "xAxis";
	
	private static String Y_AXIS_NAME = "yAxis";
	
	protected static final String HIGHCHART_DIV_PREFIX = "highchart";
	
	public enum ChartType{
		AREA, AREASPLINE, BAR, COLUMN, LINE, PIE, SCATTER, SPLINE, BOXPLOT, NONE;
	}
	
	public static Set<URI> getScriptDependencies() throws URISyntaxException {
		Set<URI> dependencies =  new LinkedHashSet<URI>(); // load order is important
		dependencies.add(new URI("http://code.highcharts.com/highcharts.js"));
		dependencies.add(new URI("http://code.highcharts.com/highcharts-more.js"));
		dependencies.add(new URI("http://code.highcharts.com/modules/exporting.js"));
		return dependencies;
	}
	
	/**
	 * Get initiation code for displaying a HighCharts chart. It is necessary to terminate by calling getContainerEndCode() later.
	 * @param chartType
	 * @param title
	 * @return
	 */
	public static String getContainerStartCode(ChartType chartType, String containerId, String title){
		return getHCScriptStartCode(chartType, containerId, title, false);
	}
	
	/**
	 * Get initiation code for displaying a HighCharts chart. It is necessary to terminate by calling getContainerEndCode() later.
	 * @param chartType
	 * @param title
	 * @param displayLegend
	 * @param description
	 * @return
	 */
	public static String getHCScriptStartCode(ChartType chartType, String containerId, String title, boolean displayLegend){
		StringBuilder sb = new StringBuilder();
		sb.append("$(function () {\n$('#" + HIGHCHART_DIV_PREFIX +  CONTENTS_DIV_SUFFIX + containerId + "').highcharts({\n");
		sb.append("chart: { type: '" + chartType.toString().toLowerCase() +"' },\n");
		if (title != null && !title.isEmpty())
			sb.append("title: { text: '" + title + "' },\n");
		//if (subTitle != null && !subTitle.isEmpty())
		//	sb.append("subtitle: { text: '" + subTitle + "' },\n");
		sb.append("legend: { enabled: " + displayLegend + " },\n");
		return sb.toString();
	}
	
	public static String getBasicXAxisCode(String title){
		return getBasicAxisCode(X_AXIS_NAME, title, null, null, null, null, false);
	}
	
	public static String getBasicXAxisCode(String title, List<String> categories){
		return getBasicAxisCode(X_AXIS_NAME, title, categories, null, null, null, false);
	}
	
	public static String getBasicXAxisCode(String title, Integer tickInterval){
		return getBasicAxisCode(X_AXIS_NAME, title, null, tickInterval, null, null, false);
	}
	
	public static String getBasicXAxisCode(String title, List<String> categories, Integer tickInterval){
		return getBasicAxisCode(X_AXIS_NAME, title, categories, tickInterval, null, null, false);
	}
	
	public static String getBasicYAxisCode(String title){
		return getBasicAxisCode(Y_AXIS_NAME, title, null, null, null, null, false);
	}
	
	public static String getBasicYAxisCode(String title, List<String> categories, Integer tickInterval){
		return getBasicAxisCode(Y_AXIS_NAME, title, categories, tickInterval, null, null, false);
	}
	
	public static String getBasicYAxisCode(String title, List<String> categories){
		return getBasicAxisCode(Y_AXIS_NAME, title, categories, null, null, null, false);
	}
	
	public static String getBasicYAxisCode(String title, Integer tickInterval){
		return getBasicAxisCode(Y_AXIS_NAME, title, null, tickInterval, null, null, false);
	}
	
	public static String getBasicXAxisCode(String title, Number min, Number max){
		return getBasicAxisCode(X_AXIS_NAME, title, null, null, min, max, false);
	}
	
	public static String getBasicXAxisCode(String title, List<String> categories, Number min, Number max){
		return getBasicAxisCode(X_AXIS_NAME, title, categories, null, min, max, false);
	}
	
	public static String getBasicXAxisCode(String title, Integer tickInterval, Number min, Number max){
		return getBasicAxisCode(X_AXIS_NAME, title, null, tickInterval, min, max, false);
	}
	
	public static String getBasicXAxisCode(String title, List<String> categories, 
			Integer tickInterval, Number min, Number max){
		return getBasicAxisCode(X_AXIS_NAME, title, categories, tickInterval, min, max, false);
	}
	
	public static String getBasicXAxisCode(String title, List<String> categories, Integer tickInterval, 
			Number min, Number max, boolean reversed) {
		return getBasicAxisCode(X_AXIS_NAME, title, categories, tickInterval, min, max, reversed);
	}
	
	public static String getBasicYAxisCode(String title, Number min, Number max){
		return getBasicAxisCode(Y_AXIS_NAME, title, null, null, min, max, false);
	}
	
	public static String getBasicYAxisCode(String title, List<String> categories, Integer tickInterval, Number min, Number max){
		return getBasicAxisCode(Y_AXIS_NAME, title, categories, tickInterval, min, max, false);
	}
	
	public static String getBasicYAxisCode(String title, List<String> categories, Number min, Number max){
		return getBasicAxisCode(Y_AXIS_NAME, title, categories, null, min, max, false);
	}
	
	public static String getBasicYAxisCode(String title, Integer tickInterval, Number min, Number max){
		return getBasicAxisCode(Y_AXIS_NAME, title, null, tickInterval, min, max, false);
	}
	
	public static String getBasicYAxisCode(String title, Integer tickInterval, 
			Number min, Number max, boolean reversed){
		return getBasicAxisCode(Y_AXIS_NAME, title, null, tickInterval, min, max, reversed);
	}
	
	
	private static String getBasicAxisCode(String axis, String title, List<String> categories, 
			Integer tickInterval, Number min, Number max, boolean reversed){
		StringBuilder sb = new StringBuilder();
		sb.append(axis + ": { ");
		if (reversed)
			sb.append("reversed: true,\n");
		if (categories != null && !categories.isEmpty()){
			sb.append("categories: [");
			boolean isFirst = true;
			for (String cat: categories){
				if (isFirst)
					isFirst = false;
				else
					sb.append(", ");
				sb.append("'" + cat + "'");
			}
			sb.append("],\n");
		}
		if (tickInterval != null)
			sb.append("tickInterval: " + tickInterval + ",\n");
		if (min != null)
			sb.append("min: " + min + ",\n");
		if (max != null)
			sb.append("max: " + max + ",\n");
		sb.append("title: { text: '" + title + "' }\n");
		sb.append("},\n");
		return sb.toString();
	}
	
	public static String getBasicSeriesCode(final BasicHighChartsSeries series) throws ChartException{
		return getBasicSeriesCode(series, null);
	}
	
		
	public static String getBasicSeriesCode(final BasicHighChartsSeries series, MessageService messageService) throws ChartException{
		try{
			Set<BasicHighChartsSeries> seriesSet = new HashSet<BasicHighChartsSeries>();
			seriesSet.add(series);
			return getBasicSeriesCode(seriesSet, messageService);
		} catch(Exception e){
			throw new ChartException("Unexpected error caught rendering chart", e);
		}
	}
	
	public static String getBasicSeriesCode(final Set<BasicHighChartsSeries> seriesSet) throws ChartException{
		return getBasicSeriesCode(seriesSet, null);
	}
	
	public static String getBasicSeriesCode(final Set<BasicHighChartsSeries> seriesSet, MessageService messageService) throws ChartException{
		try{
			StringBuilder sb = new StringBuilder();
			sb.append("series: [");
			int seriesCount = 0;
			for (BasicHighChartsSeries series : seriesSet){
				if (seriesCount++ > 0)
					sb.append(",");
				sb.append(series.getInnerHtml(messageService));
			}
			sb.append("]\n");	
			return sb.toString();
		} catch(Exception e){
			throw new ChartException("Unexpected error caught rendering chart", e);
		}
	}
	
	
	/**
	 * Terminates a chart. Do not call this methods unless getContainerStartCode() has been called first
	 * @return
	 */
	public static String getHCScriptEndCode(){
		return "\n});});";
	}
	
	/**
	 * Get a basic spline chart.
	 * @param chart
	 * @return
	 * @throws ChartException 
	 */
	public static WebContent getBasicSpline(final WaspChart2D chart) throws ChartException {
		return getBasicSpline(chart, null, null, null, null, null, null, null);
	}
	
	/**
	 * Get a basic spline chart. Providing a MessageService instance enables internationalization of
	 * title and description (assumes chart title and description parameter values are localization property keys).
	 * @param chart
	 * @param messageService
	 * @return
	 * @throws ChartException 
	 */
	public static WebContent getBasicSpline(final WaspChart2D chart, MessageService messageService) throws ChartException {
		return getBasicSpline(chart, null, null, null, null, null, null, messageService);
	}
	
	/**
	 * Get a basic spline chart. Except for chart, which must be provided, all other parameter values may be null
	 * in which case HighCharts default values will be used.
	 * @param chart
	 * @param xTickInterval
	 * @param yTickInterval
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @return
	 * @throws ChartException 
	 */
	public static WebContent getBasicSpline(final WaspChart2D chart, Integer xTickInterval, Integer yTickInterval, Integer xMin, Integer xMax, Integer yMin, Integer yMax) throws ChartException{
		return getBasicSpline(chart, xTickInterval, yTickInterval, xMin, xMax, yMin, yMax, null);
	}
	
	/**
	 * Get a basic spline chart. Except for chart, which must be provided, all other parameter values may be null
	 * in which case HighCharts default values will be used.
	 * Providing a MessageService instance enables internationalization of
	 * title and description (assumes chart title and description parameter values are localization property keys).
	 * @param chart
	 * @param xTickInterval
	 * @param yTickInterval
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @param messageService
	 * @return
	 * @throws ChartException 
	 */
	public static WebContent getBasicSpline(final WaspChart2D chart, Integer xTickInterval, Integer yTickInterval, Integer xMin, Integer xMax, Integer yMin, Integer yMax, MessageService messageService) throws ChartException{
		try{
			DataSeries ds = chart.getDataSeries().get(0);
			WebContent content = new WebContent();
			String containerId = getUniqueContainerId();;
			String description;
			String title;
			String xAxisLabel;
			String yAxisLabel;
			List<String> rowLabels;
			if (messageService == null){
				description = chart.getDescription();
				title = chart.getTitle();
				xAxisLabel = chart.getxAxisLabel();
				yAxisLabel = chart.getyAxisLabel();
				rowLabels = ds.getRowLabels();
			} else {
				description = chart.getLocalizedDescription(messageService);
				title = chart.getLocalizedTitle(messageService);
				xAxisLabel = chart.getLocalizedXAxisLabel(messageService);
				yAxisLabel = chart.getLocalizedYAxisLabel(messageService);
				rowLabels = ds.getLocalizedRowLabels(messageService);
			}
			content.setHtmlCode(getSimpleContainerCode(HIGHCHART_DIV_PREFIX, "", description, containerId));
			
			StringBuilder sb = new StringBuilder();
			
			
			sb.append(getHCScriptStartCode(ChartType.SPLINE, containerId, title, false));
			sb.append(getBasicXAxisCode(xAxisLabel, rowLabels, xTickInterval));
			sb.append(getBasicYAxisCode(yAxisLabel, yMin, yMax));
			sb.append(getBasicSeriesCode(new BasicHighChartsSeries(ds, false, false, Color.RED), messageService));
			sb.append(getHCScriptEndCode());
			content.setScriptCode(sb.toString());
			content.setScriptDependencies(getScriptDependencies());
			return content;
		} catch(Exception e){
			throw new ChartException("Unexpected error caught rendering chart", e);
		}
	}
	
	

}
