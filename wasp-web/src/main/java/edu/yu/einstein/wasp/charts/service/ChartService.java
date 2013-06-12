package edu.yu.einstein.wasp.charts.service;

import edu.yu.einstein.wasp.charts.WaspBoxPlot;


public interface ChartService {
	
	/**
	 * Return an Html representation for drawing a boxplot chart
	 * @param waspBoxPlot
	 * @return
	 */
	public String getBoxPlotHtml(WaspBoxPlot waspBoxPlot); 

}
