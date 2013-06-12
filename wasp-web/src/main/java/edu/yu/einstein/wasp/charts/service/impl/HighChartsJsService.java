package edu.yu.einstein.wasp.charts.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.service.ChartService;

/**
 * Highcharts JS implementation (http://www.highcharts.com)
 * @author asmclellan
 *
 */
@Service
@Transactional
public class HighChartsJsService implements ChartService {

	public HighChartsJsService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBoxPlotHtml(WaspBoxPlot waspBoxPlot) {
		// TODO Auto-generated method stub
		return null;
	}

}
