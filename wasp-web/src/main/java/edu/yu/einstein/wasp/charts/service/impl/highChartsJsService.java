package edu.yu.einstein.wasp.charts.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.service.chartService;

/**
 * Highcharts JS implementation (http://www.highcharts.com)
 * @author asmclellan
 *
 */
@Service
@Transactional
public class highChartsJsService implements chartService {

	public highChartsJsService() {
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
