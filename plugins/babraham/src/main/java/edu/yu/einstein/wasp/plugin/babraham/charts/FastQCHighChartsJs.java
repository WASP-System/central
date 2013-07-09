package edu.yu.einstein.wasp.plugin.babraham.charts;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.json.JSONException;

import edu.yu.einstein.wasp.charts.DataSeries;
import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.WaspChart2D;
import edu.yu.einstein.wasp.charts.highchartsjs.BasicHighChartsSeries;
import edu.yu.einstein.wasp.charts.highchartsjs.BasicHighChartsSeries.Type;
import edu.yu.einstein.wasp.charts.highchartsjs.HighChartsJsBase;

/**
 * Highcharts JS implementation of Babraham plots (http://www.highcharts.com)
 * @author asmclellan
 *
 */
public class FastQCHighChartsJs extends HighChartsJsBase {

	private static final String HEX_RED = "#F6CECE";
	private static final String HEX_YELLOW = "#F5ECCE";
	private static final String HEX_GREEN = "#CEF6CE";
	private static final String HEX_BLUE = "#0101DF";

	
	public static String getPerBaseSeqQualityPlotHtml(final WaspBoxPlot waspBoxPlot) throws JSONException {
		DataSeries boxPlotDS = waspBoxPlot.getDataSeries(WaspBoxPlot.BoxPlotSeries.BOX_AND_WHISKER);
		DataSeries meanDS = waspBoxPlot.getDataSeries(WaspBoxPlot.BoxPlotSeries.RUNNING_MEAN);
		
		StringBuilder sb = new StringBuilder();
		sb.append(getContainerStartCode(ChartType.BOXPLOT, waspBoxPlot.getTitle(), false, waspBoxPlot.getDescription()));
		sb.append(getBasicXAxisCode(waspBoxPlot.getxAxisLabel(), boxPlotDS.getRowLabels(), 5));
		sb.append("plotOptions: { series: { groupPadding: 0} },\n");
		sb.append("yAxis: { title: { text: '" + waspBoxPlot.getyAxisLabel() + "' },\n ");
		sb.append("plotBands: ["); 
		sb.append("{ color: '" + HEX_RED + "', from: 0, to: 20 },");
		sb.append("{ color: '" + HEX_YELLOW + "', from: 20, to: 28 },");
		sb.append("{ color: '" + HEX_GREEN + "', from: 28, to: 100 }");
		sb.append("]},\n");
		Set<BasicHighChartsSeries> seriesSet = new LinkedHashSet<BasicHighChartsSeries>();
		seriesSet.add(new BasicHighChartsSeries(boxPlotDS));
		seriesSet.add(new BasicHighChartsSeries(meanDS, Type.SPLINE, false, false, Color.RED));
		sb.append(getBasicSeriesCode(seriesSet));
		sb.append(getContainerEndCode());
		return sb.toString();
	}
	
	public static String getSplineForBases(final WaspChart2D chart) throws JSONException{
		DataSeries dsA = chart.getDataSeries("% A");
		DataSeries dsC = chart.getDataSeries("% C");
		DataSeries dsT = chart.getDataSeries("% T");
		DataSeries dsG = chart.getDataSeries("% G");
		StringBuilder sb = new StringBuilder();
		sb.append(getContainerStartCode(ChartType.SPLINE, chart.getTitle(), true, chart.getDescription()));
		sb.append(getBasicXAxisCode(chart.getxAxisLabel()));
		sb.append(getBasicYAxisCode(chart.getyAxisLabel(), 0, 100));
		Set<BasicHighChartsSeries> seriesSet = new HashSet<BasicHighChartsSeries>();
		seriesSet.add(new BasicHighChartsSeries(dsG, false, false, Color.RED));
		seriesSet.add(new BasicHighChartsSeries(dsA, false, false, Color.BLUE));
		seriesSet.add(new BasicHighChartsSeries(dsT, false, false, Color.GREEN));
		seriesSet.add(new BasicHighChartsSeries(dsC, false, false, Color.BLACK));
		sb.append(getBasicSeriesCode(seriesSet));
		sb.append(getContainerEndCode());
		return sb.toString();
	}
	
	public static String getSplineForPerSequenceGC(final WaspChart2D chart) throws JSONException{
		DataSeries dsActual = chart.getDataSeries("GC count per read");
		DataSeries dsTheory = chart.getDataSeries("Theoretical Distribution");
		StringBuilder sb = new StringBuilder();
		sb.append(getContainerStartCode(ChartType.SPLINE, chart.getTitle(), true, chart.getDescription()));
		sb.append(getBasicXAxisCode(chart.getxAxisLabel(), 0, 100));
		sb.append(getBasicYAxisCode(chart.getyAxisLabel()));
		Set<BasicHighChartsSeries> seriesSet = new HashSet<BasicHighChartsSeries>();
		seriesSet.add(new BasicHighChartsSeries(dsActual, false, false, Color.RED));
		seriesSet.add(new BasicHighChartsSeries(dsTheory, false, false, Color.BLUE));
		sb.append(getBasicSeriesCode(seriesSet));
		sb.append(getContainerEndCode());
		return sb.toString();
	}
	

	
}
