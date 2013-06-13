package edu.yu.einstein.wasp.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Generic Box Plot data handler
 * @author asmclellan
 *
 */
public class WaspBoxPlot extends WaspChart2D {
	
	public static final String BOX_PLOT_SERIES_NAME="box_plot";
	
	static class Elements{
		public static final String LOW="low";
		public static final String LQ="lq";
		public static final String MEDIAN="median";
		public static final String UQ="uq";
		public static final String HIGH="high";
	}
	
	public WaspBoxPlot() {
		super();
	}
	
	public void addBoxAndWhiskers(String label, Double low, Double lowerQuartile, Double median, Double upperQuartile, Double high){
		DataSeries data = this.getDataSeries(BOX_PLOT_SERIES_NAME);
		if (data == null){
			data = new DataSeries(BOX_PLOT_SERIES_NAME);
			List<String> colLabels; 
			colLabels = new ArrayList<String>();
			colLabels.add(Elements.LOW);
			colLabels.add(Elements.LQ);
			colLabels.add(Elements.MEDIAN);
			colLabels.add(Elements.UQ);
			colLabels.add(Elements.HIGH);
			data.setColLabels(colLabels);
			if (this.dataSeries == null)
				this.dataSeries = new ArrayList<DataSeries>();
			this.dataSeries.add(data);
		}
		List<Double> row = new ArrayList<Double>();
		row.add(low);
		row.add(lowerQuartile);
		row.add(median);
		row.add(upperQuartile);
		row.add(high);
		data.addRow(label, row);
	}
	
	@JsonIgnore
	public Map<String, Double> getBoxAndWhiskers(String label){
		Map<String, Double> boxAndWhisker = new HashMap<String, Double>();
		DataSeries data = this.getDataSeries(BOX_PLOT_SERIES_NAME);
		if (data == null)
			return null;
		int index = data.getRowLabels().indexOf(label);
		if (index == -1)
			return null;
		List<Double> row = (List<Double>) data.getRow(index);
		List<String> colLabels = data.getColLabels();
		for (int i=0; i < data.getColCount(); i++)
			boxAndWhisker.put(colLabels.get(i), row.get(i));
		return boxAndWhisker;
	}
}
