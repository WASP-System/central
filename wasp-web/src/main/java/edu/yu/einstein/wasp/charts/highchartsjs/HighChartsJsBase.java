package edu.yu.einstein.wasp.charts.highchartsjs;

import java.util.List;

/**
 * Base Class for producing HighChartsJs charts (www.highcharts.com)
 * @author asmclellan
 *
 */
public abstract class HighChartsJsBase {
	
	public static class ChartType{
		public static final String BOXPLOT="boxplot";
	}
	
	protected static String getScriptIncludes() {
		StringBuilder sb = new StringBuilder();
		sb.append("<script src='http://code.highcharts.com/highcharts.js'></script>\n");
		sb.append("<script src='http://code.highcharts.com/highcharts-more.js'></script>\n");
		sb.append("<script src='http://code.highcharts.com/modules/exporting.js'></script>\n");
		return sb.toString();
	}
	
	protected static String getContainerStartCode(String chartType, String title){
		return getContainerStartCode(chartType, title, null, null);
	}
	
	protected static String getContainerStartCode(String chartType, String title, String subTitle, String legend){
		StringBuilder sb = new StringBuilder();
		sb.append("$(function () {\n$('#container').highcharts({\n");
		sb.append("chart: { type: '" + chartType +"' },\n");
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
	
	protected static String getBasicXAxisCode(String title){
		return getBasicAxisCode("xAxis", title, null, null);
	}
	
	protected static String getBasicXAxisCode(String title, List<String> categories, Integer tickInterval){
		return getBasicAxisCode("xAxis", title, categories, tickInterval);
	}
	
	protected static String getBasicYAxisCode(String title){
		return getBasicAxisCode("yAxis", title, null, null);
	}
	
	protected static String getBasicYAxisCode(String title, List<String> categories, Integer tickInterval){
		return getBasicAxisCode("yAxis", title, categories, tickInterval);
	}
	
	protected static String getBasicAxisCode(String axis, String title, List<String> categories, Integer tickInterval){
		StringBuilder sb = new StringBuilder();
		if (categories != null && !categories.isEmpty()){
			sb.append(axis + ": { categories: [");
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
	
	protected static String getContainerEndCode(){
		return "\n});\n});";
	}

}
