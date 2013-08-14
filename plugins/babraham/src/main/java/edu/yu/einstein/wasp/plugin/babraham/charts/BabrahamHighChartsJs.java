package edu.yu.einstein.wasp.plugin.babraham.charts;

import java.awt.Color;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.yu.einstein.wasp.charts.DataSeries;
import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.WaspChart;
import edu.yu.einstein.wasp.charts.WaspChart2D;
import edu.yu.einstein.wasp.charts.highchartsjs.BasicHighChartsSeries;
import edu.yu.einstein.wasp.charts.highchartsjs.BasicHighChartsSeries.Type;
import edu.yu.einstein.wasp.charts.highchartsjs.HighChartsJsBase;
import edu.yu.einstein.wasp.exception.ChartException;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQC;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.web.Tooltip;

/**
 * Highcharts JS implementation of Babraham plots that require a custom implementation (http://www.highcharts.com)
 * @author asmclellan
 *
 */
public class BabrahamHighChartsJs extends HighChartsJsBase {

	private static final String HEX_RED = "#F6CECE";
	private static final String HEX_YELLOW = "#F5ECCE";
	private static final String HEX_GREEN = "#CEF6CE";
	private static final String HEX_BLUE = "#0101DF";
	
	private static String getFastQcCredits(MessageService messageService){
		return "<div class='chart_credit'>" + messageService.getMessage("fastqc.credit.label") + "</div>";
	}
	
	private static String getFastQScreenCredits(MessageService messageService){
		return "<div class='chart_credit'>" + messageService.getMessage("fastqscreen.credit.label") + "</div>";
	}
	
	public static WebContent getPerBaseSeqQualityPlotHtml(final WaspBoxPlot waspBoxPlot, MessageService messageService) throws ChartException {
		try {
			DataSeries boxPlotDS = waspBoxPlot.getDataSeries(WaspBoxPlot.BoxPlotSeries.BOX_AND_WHISKER);
			DataSeries meanDS = waspBoxPlot.getDataSeries(WaspBoxPlot.BoxPlotSeries.RUNNING_MEAN);
			WebContent content = new WebContent();
			content.setScriptDependencies(getScriptDependencies());
			String containerId = getUniqueContainerId();
			content.setHtmlCode(getSimpleContainerCode(HIGHCHART_DIV_PREFIX, "", waspBoxPlot.getLocalizedDescription(messageService), containerId));
			StringBuilder sb = new StringBuilder();
			sb.append(getHCScriptStartCode(ChartType.BOXPLOT, containerId, waspBoxPlot.getLocalizedTitle(messageService), false));
			sb.append(getBasicXAxisCode(waspBoxPlot.getLocalizedXAxisLabel(messageService), boxPlotDS.getRowLabels(), 5));
			sb.append("plotOptions: { series: { groupPadding: 0} },\n");
			sb.append("yAxis: { title: { text: '" + waspBoxPlot.getLocalizedYAxisLabel(messageService) + "' },\n ");
			sb.append("plotBands: ["); 
			sb.append("{ color: '" + HEX_RED + "', from: 0, to: 20 },");
			sb.append("{ color: '" + HEX_YELLOW + "', from: 20, to: 28 },");
			sb.append("{ color: '" + HEX_GREEN + "', from: 28, to: 100 }");
			sb.append("]},\n");
			Set<BasicHighChartsSeries> seriesSet = new LinkedHashSet<BasicHighChartsSeries>();
			seriesSet.add(new BasicHighChartsSeries(boxPlotDS));
			seriesSet.add(new BasicHighChartsSeries(meanDS, Type.SPLINE, false, false, Color.RED));
			sb.append(getBasicSeriesCode(seriesSet, messageService));
			sb.append(getHCScriptEndCode());
			sb.append(getFastQcCredits(messageService));
			content.setScriptCode(sb.toString());
			return content;
		} catch (Exception e) {
			throw new ChartException("Unexpected exception caught processing data", e);
		}
	}
	
	public static WebContent getSplineForBases(final WaspChart2D chart, MessageService messageService) throws ChartException{
		try {
			DataSeries dsA = chart.getDataSeries("fastqc.perSequenceContent_ds1Name.label");
			DataSeries dsC = chart.getDataSeries("fastqc.perSequenceContent_ds2Name.label");
			DataSeries dsT = chart.getDataSeries("fastqc.perSequenceContent_ds3Name.label");
			DataSeries dsG = chart.getDataSeries("fastqc.perSequenceContent_ds4Name.label");
			WebContent content = new WebContent();
			content.setScriptDependencies(getScriptDependencies());
			String containerId = getUniqueContainerId();
			content.setHtmlCode(getSimpleContainerCode(HIGHCHART_DIV_PREFIX, "", chart.getLocalizedDescription(messageService), containerId));
			StringBuilder sb = new StringBuilder();
			sb.append(getHCScriptStartCode(ChartType.SPLINE, containerId, chart.getLocalizedTitle(messageService), true));
			sb.append(getBasicXAxisCode(chart.getxAxisLabel()));
			sb.append(getBasicYAxisCode(chart.getyAxisLabel(), 0, 100));
			Set<BasicHighChartsSeries> seriesSet = new LinkedHashSet<BasicHighChartsSeries>();
			seriesSet.add(new BasicHighChartsSeries(dsG, false, false, Color.RED));
			seriesSet.add(new BasicHighChartsSeries(dsA, false, false, Color.BLUE));
			seriesSet.add(new BasicHighChartsSeries(dsT, false, false, Color.GREEN));
			seriesSet.add(new BasicHighChartsSeries(dsC, false, false, Color.BLACK));
			sb.append(getBasicSeriesCode(seriesSet, messageService));
			sb.append(getHCScriptEndCode());
			sb.append(getFastQcCredits(messageService));
			content.setScriptCode(sb.toString());
			return content;
		} catch (Exception e) {
			throw new ChartException("Unexpected exception caught processing data", e);
		}
	}
	
	public static WebContent getSplineForPerSequenceGC(final WaspChart2D chart, MessageService messageService) throws ChartException{
		try {
			DataSeries dsActual = chart.getDataSeries("fastqc.perSequenceGcContent_ds1Name.label");
			DataSeries dsTheory = chart.getDataSeries("fastqc.perSequenceGcContent_ds2Name.label");
			WebContent content = new WebContent();
			content.setScriptDependencies(getScriptDependencies());
			String containerId = getUniqueContainerId();
			content.setHtmlCode(getSimpleContainerCode(HIGHCHART_DIV_PREFIX, "", chart.getLocalizedDescription(messageService), containerId));
			StringBuilder sb = new StringBuilder();
			sb.append(getHCScriptStartCode(ChartType.SPLINE, containerId, chart.getLocalizedTitle(messageService), true));
			sb.append(getBasicXAxisCode(chart.getxAxisLabel(), 0, 100));
			sb.append(getBasicYAxisCode(chart.getyAxisLabel(), 0, null));
			Set<BasicHighChartsSeries> seriesSet = new LinkedHashSet<BasicHighChartsSeries>();
			seriesSet.add(new BasicHighChartsSeries(dsActual, false, false, Color.RED));
			seriesSet.add(new BasicHighChartsSeries(dsTheory, false, false, Color.BLUE));
			sb.append(getBasicSeriesCode(seriesSet, messageService));
			sb.append(getHCScriptEndCode());
			sb.append(getFastQcCredits(messageService));
			content.setScriptCode(sb.toString());
			return content;
		} catch (Exception e) {
			throw new ChartException("Unexpected exception caught processing data", e);
		}
	}
	
	public static WebContent getQCSummaryTableRepresentation(final WaspChart basicStats, MessageService messageService) throws ChartException {
		try{
			List<List<Object>> data = basicStats.getDataSeries().get(0).getData();
			WebContent content = new WebContent();
			content.setScriptDependencies(getScriptDependencies());
			StringBuilder sb = new StringBuilder();
			sb.append("<h3>" + basicStats.getLocalizedTitle(messageService) + "</h3>\n");
			sb.append("<table class='standardTable' >\n");
			sb.append("<tr>\n");
			sb.append("<th>" +  basicStats.getDataSeries().get(0).getColLabels().get(0) + "</th>\n");
			sb.append("<th>" + basicStats.getDataSeries().get(0).getColLabels().get(1) + "</th>\n");
			sb.append("</tr>\n");
			for (List<Object> row : data){
				sb.append("<tr>\n");
				String moduleName = (String) row.get(0);
				String result = (String) row.get(1);
				String comment = (String)  row.get(2);
				if (result.equals(FastQC.QC_ANALYSIS_RESULT_PASS))
					result = Tooltip.getSuccessHtmlString(comment);
				else if (result.equals(FastQC.QC_ANALYSIS_RESULT_WARN))
					result = Tooltip.getWarningHtmlString(comment);
				else if (result.equals(FastQC.QC_ANALYSIS_RESULT_FAIL))
					result = Tooltip.getFailureHtmlString(comment);
				sb.append("<td class='center'>" + result + "</td>\n");
				sb.append("<td>" + moduleName + "</td>\n");
				sb.append("</tr>\n");
			}
			sb.append("</table>\n");
			sb.append(getFastQcCredits(messageService));
			content.setHtmlCode(getSimpleContainerCode(sb.toString(), basicStats.getLocalizedDescription(messageService)));
			return content;
		} catch (Exception e) {
			throw new ChartException("Unexpected exception caught processing data", e);
		}
	}
	
	public static WebContent getBarChartFastQScreen(final WaspChart2D chart, MessageService messageService) throws ChartException{
		try{
			List<DataSeries> ds = chart.getDataSeries();
			if (ds.size() != 4)
				throw new ChartException("Expected 4 data series but got ");
			WebContent content = new WebContent();
			content.setScriptDependencies(getScriptDependencies());
			String containerId = getUniqueContainerId();
			content.setHtmlCode(getSimpleContainerCode(HIGHCHART_DIV_PREFIX, "", chart.getLocalizedDescription(messageService), containerId));
			StringBuilder sb = new StringBuilder();
			sb.append(getHCScriptStartCode(ChartType.COLUMN, containerId, chart.getLocalizedTitle(messageService), true));
			sb.append(getBasicXAxisCode(chart.getxAxisLabel(), ds.get(0).getRowLabels()));
			sb.append(getBasicYAxisCode(chart.getyAxisLabel(), 0, 100));
			sb.append("plotOptions: { column: { stacking: 'normal' } },\n");
			Set<BasicHighChartsSeries> seriesSet = new LinkedHashSet<BasicHighChartsSeries>();
			seriesSet.add(new BasicHighChartsSeries(ds.get(3), false, false, Color.RED));
			seriesSet.add(new BasicHighChartsSeries(ds.get(2), false, false, Color.BLUE));
			seriesSet.add(new BasicHighChartsSeries(ds.get(1), false, false, Color.ORANGE));
			seriesSet.add(new BasicHighChartsSeries(ds.get(0), false, false, Color.GREEN));
			sb.append(getBasicSeriesCode(seriesSet, messageService));
			sb.append(getHCScriptEndCode());
			sb.append(getFastQScreenCredits(messageService));
			content.setScriptCode(sb.toString());
			return content;
		} catch(Exception e){
			throw new ChartException("Unexpected error caught rendering chart", e);
		}
	}
	
	
	public static WebContent getBasicSpline(final WaspChart2D chart, Integer xTickInterval, Integer yTickInterval, Integer xMin, Integer xMax, Integer yMin, Integer yMax, MessageService messageService) throws ChartException{
		WebContent content = HighChartsJsBase.getBasicSpline(chart, xTickInterval, yTickInterval, xMin, xMax, yMin, yMax, messageService);
		content.setHtmlCode(content.getHtmlCode() + getFastQcCredits(messageService));
		return content;
	}
}
