package edu.yu.einstein.wasp.charts;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Generic Box Plot data handler
 * @author asmclellan
 *
 */
public class WaspBoxPlot extends WaspChart {
	
	static class Elements{
		public static final String LOW="low";
		public static final String LQ="lq";
		public static final String MEDIAN="median";
		public static final String UQ="uq";
		public static final String high="high";
	}
	
	public Set<Map<String, Double>> data;

	public WaspBoxPlot() {
		super();
		data = new LinkedHashSet<Map<String,Double>>();
	}

	public Set<Map<String, Double>> getData() {
		return data;
	}

	public void setData(Set<Map<String, Double>> data) {
		this.data = data;
	}
	
	public void addDataPoint(Double low, Double lowerQuartile, Double median, Double upperQuartile, Double high){
		Map<String, Double> dataPoint = new HashMap<String, Double>();
		dataPoint.put(Elements.LOW, low);
		dataPoint.put(Elements.LQ, lowerQuartile);
		dataPoint.put(Elements.MEDIAN, median);
		dataPoint.put(Elements.UQ, upperQuartile);
		dataPoint.put(Elements.high, high);
		data.add(dataPoint);
	}

}
