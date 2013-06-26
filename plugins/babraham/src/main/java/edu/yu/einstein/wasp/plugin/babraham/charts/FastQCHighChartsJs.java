package edu.yu.einstein.wasp.plugin.babraham.charts;

import java.util.List;

import edu.yu.einstein.wasp.charts.DataSeries;
import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.highchartsjs.HighChartsJsBase;

/**
 * Highcharts JS implementation of Babraham plots (http://www.highcharts.com)
 * @author asmclellan
 *
 */
public class FastQCHighChartsJs extends HighChartsJsBase {

	private static final String RED = "#FA8258";
	private static final String YELLOW = "#F3F781";
	private static final String GREEN = "#C8FE2E";
	private static final String BLUE = "#0101DF";

	
	public static String getPerBaseSeqQualityPlotHtml(final WaspBoxPlot waspBoxPlot) {
		DataSeries boxPlotDS = waspBoxPlot.getDataSeries(WaspBoxPlot.BoxPlotSeries.BOX_AND_WHISKER);
		DataSeries meanDS = waspBoxPlot.getDataSeries(WaspBoxPlot.BoxPlotSeries.RUNNING_MEAN);
		StringBuilder sb = new StringBuilder();
		sb.append(getScriptIncludes());
		sb.append(getContainerStartCode(ChartType.BOXPLOT, waspBoxPlot.getTitle(), null, waspBoxPlot.getLegend()));
		
		sb.append(getBasicXAxisCode(waspBoxPlot.getxAxisLabel(), boxPlotDS.getRowLabels()));
		
		sb.append("yAxis: { title: { text: '" + waspBoxPlot.getyAxisLabel() + "' },\n ");
		sb.append("plotBands: ["); 
		sb.append("{ color: '" + RED + "', from: 0, to: 20 },");
		sb.append("{ color: '" + YELLOW + "', from: 20, to: 28 },");
		sb.append("{ color: '" + GREEN + "', from: 28, to: 50 }");
		sb.append("]},\n");
		
		sb.append("series: [{ name: '" + boxPlotDS.getName() + "', data: [\n");
		for (int i=0; i< boxPlotDS.getRowCount(); i++){
			sb.append("[");
			boolean isFirst = true;
			for (Object dataPoint : (List<Object>) boxPlotDS.getRow(i)){
				if (isFirst)
					isFirst = false;
				else
					sb.append(", ");
				sb.append(dataPoint.toString());
			}
			sb.append("]");
			if (i < meanDS.getRowCount() - 1)
				sb.append(",");
			sb.append("\n");
		}
		sb.append("]},\n");
		sb.append("{ name: '" + meanDS.getName() + "', type: 'spline', color: '" + BLUE + "', data: [\n");
		for (int i=0; i< meanDS.getRowCount(); i++){
			sb.append("[");
			boolean isFirst = true;
			for (Object dataPoint : (List<Object>) meanDS.getRow(i)){
				if (isFirst)
					isFirst = false;
				else
					sb.append(", ");
				sb.append(dataPoint.toString());
			}
			sb.append("]");
			if (i < meanDS.getRowCount() - 1)
				sb.append(",");
			sb.append("\n");
		}
		sb.append("]\n");
		sb.append("}]\n");
		
		sb.append(getContainerEndCode());
		return sb.toString();
	}


}
