package edu.yu.einstein.wasp.charts.highchartsjs;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.charts.DataSeries;
import edu.yu.einstein.wasp.charts.WaspChart2D;

/**
 * Base Class for producing HighChartsJs charts (www.highcharts.com)
 * @author asmclellan
 *
 */
public abstract class HighChartsJsBase {
	
	public static Logger logger = LoggerFactory.getLogger(HighChartsJsBase.class);
	
	private static String X_AXIS_NAME = "xAxis";
	
	private static String Y_AXIS_NAME = "yAxis";
	
	public enum ChartType{
		AREA, AREASPLINE, BAR, COLUMN, LINE, PIE, SCATTER, SPLINE, BOXPLOT, NONE;
	}
	
	public static Set<URL> getScriptDependencies() {
		Set<URL> dependencies =  new HashSet<URL>();
		try {
			dependencies.add(new URL("http://code.highcharts.com/highcharts.js"));
			dependencies.add(new URL("http://code.highcharts.com/highcharts-more.js"));
			dependencies.add(new URL("http://code.highcharts.com/modules/exporting.js"));
		} catch (MalformedURLException e) {
			logger.warn(e.getLocalizedMessage());
		}
		
		return dependencies;
	}
	
	public static String getContainerStartCode(ChartType chartType, String title){
		return getContainerStartCode(chartType, title, false, null);
	}
	
	public static String getContainerStartCode(ChartType chartType, String title, boolean displayLegend, String description){
		StringBuilder sb = new StringBuilder();
		String id = "highChart_Target_" + UUID.randomUUID().toString();
		sb.append("<div class='highchart_container'>\n");
		if (!chartType.equals(ChartType.NONE))
			sb.append("<div class='highchart_chart' id='" + id + "'></div>\n");
		if (description != null)
			sb.append("<div class='highchart_Description'><p>" + description.replaceAll("[\n\r]", "<br /><br />") + "</p></div>\n");
		sb.append("</div>\n");
		if (!chartType.equals(ChartType.NONE)){
			sb.append("<script>\n$(function () {\n$('#" + id + "').highcharts({\n");
			sb.append("chart: { type: '" + chartType.toString().toLowerCase() +"' },\n");
			if (title != null && !title.isEmpty())
				sb.append("title: { text: '" + title + "' },\n");
			//if (subTitle != null && !subTitle.isEmpty())
			//	sb.append("subtitle: { text: '" + subTitle + "' },\n");
			sb.append("legend: { enabled: " + displayLegend + " },\n");
		}
		return sb.toString();
	}
	
	public static String getBasicXAxisCode(String title){
		return getBasicAxisCode(X_AXIS_NAME, title, null, null, null, null);
	}
	
	public static String getBasicXAxisCode(String title, List<String> categories){
		return getBasicAxisCode(X_AXIS_NAME, title, categories, null, null, null);
	}
	
	public static String getBasicXAxisCode(String title, Integer tickInterval){
		return getBasicAxisCode(X_AXIS_NAME, title, null, tickInterval, null, null);
	}
	
	public static String getBasicXAxisCode(String title, List<String> categories, Integer tickInterval){
		return getBasicAxisCode(X_AXIS_NAME, title, categories, tickInterval, null, null);
	}
	
	public static String getBasicYAxisCode(String title){
		return getBasicAxisCode(Y_AXIS_NAME, title, null, null, null, null);
	}
	
	public static String getBasicYAxisCode(String title, List<String> categories, Integer tickInterval){
		return getBasicAxisCode(Y_AXIS_NAME, title, categories, tickInterval, null, null);
	}
	
	public static String getBasicYAxisCode(String title, List<String> categories){
		return getBasicAxisCode(Y_AXIS_NAME, title, categories, null, null, null);
	}
	
	public static String getBasicYAxisCode(String title, Integer tickInterval){
		return getBasicAxisCode(Y_AXIS_NAME, title, null, tickInterval, null, null);
	}
	
	public static String getBasicXAxisCode(String title, Number min, Number max){
		return getBasicAxisCode(X_AXIS_NAME, title, null, null, min, max);
	}
	
	public static String getBasicXAxisCode(String title, List<String> categories, Number min, Number max){
		return getBasicAxisCode(X_AXIS_NAME, title, categories, null, min, max);
	}
	
	public static String getBasicXAxisCode(String title, Integer tickInterval, Number min, Number max){
		return getBasicAxisCode(X_AXIS_NAME, title, null, tickInterval, min, max);
	}
	
	public static String getBasicXAxisCode(String title, List<String> categories, Integer tickInterval, Number min, Number max){
		return getBasicAxisCode(X_AXIS_NAME, title, categories, tickInterval, min, max);
	}
	
	public static String getBasicYAxisCode(String title, Number min, Number max){
		return getBasicAxisCode(Y_AXIS_NAME, title, null, null, min, max);
	}
	
	public static String getBasicYAxisCode(String title, List<String> categories, Integer tickInterval, Number min, Number max){
		return getBasicAxisCode(Y_AXIS_NAME, title, categories, tickInterval, min, max);
	}
	
	public static String getBasicYAxisCode(String title, List<String> categories, Number min, Number max){
		return getBasicAxisCode(Y_AXIS_NAME, title, categories, null, min, max);
	}
	
	public static String getBasicYAxisCode(String title, Integer tickInterval, Number min, Number max){
		return getBasicAxisCode(Y_AXIS_NAME, title, null, tickInterval, min, max);
	}
	
	
	private static String getBasicAxisCode(String axis, String title, List<String> categories, Integer tickInterval, Number min, Number max){
		StringBuilder sb = new StringBuilder();
		sb.append(axis + ": { ");
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
	
		
	public static String getBasicSeriesCode(final BasicHighChartsSeries series) throws JSONException{
		Set<BasicHighChartsSeries> seriesSet = new HashSet<BasicHighChartsSeries>();
		seriesSet.add(series);
		return getBasicSeriesCode(seriesSet);
	}
	
	public static String getBasicSeriesCode(final Set<BasicHighChartsSeries> seriesSet) throws JSONException{
		StringBuilder sb = new StringBuilder();
		sb.append("series: [");
		int seriesCount = 0;
		for (BasicHighChartsSeries series : seriesSet){
			if (seriesCount++ > 0)
				sb.append(",");
			sb.append(series.getInnerHtml());
		}
		sb.append("]\n");	
		return sb.toString();
	}
	
	public static String getContainerEndCode(){
		return "\n});\n});\n</script>";
	}
	
	
	public static String getBasicSpline(final WaspChart2D chart) throws JSONException{
		return getBasicSpline(chart, null, null, null);
	}
	
	public static String getBasicSpline(final WaspChart2D chart, Integer xTickInterval) throws JSONException{
		return getBasicSpline(chart, xTickInterval, null, null);
	}
	
	public static String getBasicSpline(final WaspChart2D chart, Integer yMin, Integer yMax) throws JSONException{
		return getBasicSpline(chart, null, yMin, yMax);
	}
	
	
	public static String getBasicSpline(final WaspChart2D chart, Integer xTickInterval, Integer yMin, Integer yMax) throws JSONException{
		DataSeries ds = chart.getDataSeries().get(0);
		StringBuilder sb = new StringBuilder();
		sb.append(getContainerStartCode(ChartType.SPLINE, chart.getTitle(), false, chart.getDescription()));
		sb.append(getBasicXAxisCode(chart.getxAxisLabel(), ds.getRowLabels(), xTickInterval));
		sb.append(getBasicYAxisCode(chart.getyAxisLabel(), yMin, yMax));
		sb.append(getBasicSeriesCode(new BasicHighChartsSeries(ds, false, false, Color.RED)));
		sb.append(getContainerEndCode());
		return sb.toString();
	}

}
