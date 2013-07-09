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
	
	public static class BoxPlotSeries{
		public static final String BOX_AND_WHISKER="box and whisker";
		public static final String RUNNING_MEAN="running-mean";
		public static final String OUTLIERS="outliers";
	}
	
	
	public static class BoxAndWhiskerComponent{
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
		DataSeries data = getDataSeriesOrCreateNew(BoxPlotSeries.BOX_AND_WHISKER);
		if (data.getColLabels().isEmpty()){
			List<String> colLabels; 
			colLabels = new ArrayList<String>();
			colLabels.add(BoxAndWhiskerComponent.LOW);
			colLabels.add(BoxAndWhiskerComponent.LQ);
			colLabels.add(BoxAndWhiskerComponent.MEDIAN);
			colLabels.add(BoxAndWhiskerComponent.UQ);
			colLabels.add(BoxAndWhiskerComponent.HIGH);
			data.setColLabels(colLabels);
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
		DataSeries data = this.getDataSeries(BoxPlotSeries.BOX_AND_WHISKER);
		if (data == null)
			return null;
		int index = data.getRowLabels().indexOf(label);
		if (index == -1)
			return null;
		@SuppressWarnings("unchecked")
		List<Double> row = (List<Double>) data.getRow(index);
		List<String> colLabels = data.getColLabels();
		for (int i=0; i < data.getColCount(); i++)
			boxAndWhisker.put(colLabels.get(i), row.get(i));
		return boxAndWhisker;
	}
	
	public void addRunningMeanValue(String label, Double value){
		DataSeries data = getDataSeriesOrCreateNew(BoxPlotSeries.RUNNING_MEAN);
		data.addRowWithSingleColumn(label, value);
	}
	
	public void addRunningMeanValues(Map<String, Double> values){
		DataSeries data = getDataSeriesOrCreateNew(BoxPlotSeries.RUNNING_MEAN);
		data.addRowWithSingleColumn(values);
	}
	
	@SuppressWarnings("unchecked")
	@JsonIgnore
	public Double getRunningMeanValue(String label){
		DataSeries data = this.getDataSeries(BoxPlotSeries.RUNNING_MEAN);
		int index = data.getRowLabels().indexOf(label);
		if (index == -1)
			return null;
		return ((List<Double>) data.getRow(index)).get(0);
	}
	
	public void addOutliers(String label, List<Double> outliers){
		DataSeries data = this.getDataSeries(BoxPlotSeries.OUTLIERS);
		data.addRow(label, outliers);
	}
	
	@SuppressWarnings("unchecked")
	@JsonIgnore
	public List<Double> getOutliers(String label){
		DataSeries data = this.getDataSeries(BoxPlotSeries.OUTLIERS);
		int index = data.getRowLabels().indexOf(label);
		if (index == -1)
			return null;
		return (List<Double>) data.getRow(index);
	}
	
	@JsonIgnore
	public boolean isOutliers(){
		if (this.getDataSeries(BoxPlotSeries.OUTLIERS) == null)
			return false;
		return true;
	}
	
	@JsonIgnore
	public boolean isRunningMean(){
		if (this.getDataSeries(BoxPlotSeries.RUNNING_MEAN) == null)
			return false;
		return true;
	}
	
	
}
