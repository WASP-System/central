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
		AREA, AREASPLINE, BAR, COLUMN, LINE, PIE, SCATTER, SPLINE, BOXPLOT;
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
		return getContainerStartCode(chartType, title, null, null);
	}
	
	public static String getContainerStartCode(ChartType chartType, String title, String subTitle, String legend){
		StringBuilder sb = new StringBuilder();
		String id = "highChartContainer_" + UUID.randomUUID().toString();
		sb.append("<div id='" + id + "' style='height: 400px; margin: auto; min-width: 400px; max-width: 600px'></div>\n<script>\n");
		sb.append("$(function () {\n$('#" + id + "').highcharts({\n");
		sb.append("chart: { type: '" + chartType.toString().toLowerCase() +"' },\n");
		if (title != null && !title.isEmpty())
			sb.append("title: { text: '" + title + "' },\n");
		if (subTitle != null && !subTitle.isEmpty())
			sb.append("subtitle: { text: '" + subTitle + "' },\n");
		if (legend == null || legend.isEmpty())
			sb.append("legend: { enabled: false },\n");
		else
			sb.append("legend: { title: { text: '" + legend + "'} },\n");
		return sb.toString();
	}
	
	public static String getBasicXAxisCode(String title){
		return getBasicAxisCode(X_AXIS_NAME, title, null, null);
	}
	
	public static String getBasicXAxisCode(String title, List<String> categories){
		return getBasicAxisCode(X_AXIS_NAME, title, categories, null);
	}
	
	public static String getBasicXAxisCode(String title, Integer tickInterval){
		return getBasicAxisCode(X_AXIS_NAME, title, null, tickInterval);
	}
	
	public static String getBasicXAxisCode(String title, List<String> categories, Integer tickInterval){
		return getBasicAxisCode(X_AXIS_NAME, title, categories, tickInterval);
	}
	
	public static String getBasicYAxisCode(String title){
		return getBasicAxisCode(Y_AXIS_NAME, title, null, null);
	}
	
	public static String getBasicYAxisCode(String title, List<String> categories, Integer tickInterval){
		return getBasicAxisCode(Y_AXIS_NAME, title, categories, tickInterval);
	}
	
	public static String getBasicYAxisCode(String title, List<String> categories){
		return getBasicAxisCode(Y_AXIS_NAME, title, categories, null);
	}
	
	public static String getBasicYAxisCode(String title, Integer tickInterval){
		return getBasicAxisCode(Y_AXIS_NAME, title, null, tickInterval);
	}
	
	private static String getBasicAxisCode(String axis, String title, List<String> categories, Integer tickInterval){
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
		DataSeries ds = chart.getDataSeries().get(0);
		StringBuilder sb = new StringBuilder();
		sb.append(getContainerStartCode(ChartType.SPLINE, chart.getTitle(), null, chart.getLegend()));
		sb.append(getBasicXAxisCode(chart.getxAxisLabel(), ds.getRowLabels()));
		sb.append(getBasicYAxisCode(chart.getyAxisLabel()));
		sb.append(getBasicSeriesCode(new BasicHighChartsSeries(ds, false, false, Color.RED)));
		sb.append(getContainerEndCode());
		return sb.toString();
	}

}
