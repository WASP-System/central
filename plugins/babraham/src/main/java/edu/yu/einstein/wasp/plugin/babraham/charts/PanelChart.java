package edu.yu.einstein.wasp.plugin.babraham.charts;

import java.net.URI;

import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.WaspChart;
import edu.yu.einstein.wasp.charts.WaspChart2D;
import edu.yu.einstein.wasp.exception.ChartException;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.plugin.ViewPanel;
import edu.yu.einstein.wasp.viewpanel.Panel;
import edu.yu.einstein.wasp.viewpanel.WebContent;

public class PanelChart{
	
	public static String BABRAHAM_CHARTS_CSS_PATH = "/wasp/css/babraham.css";
	
	private static ViewPanel getViewPanel(String title, int order, WebContent content) throws PanelException{
		try{
			Panel panel = new Panel();
			panel.setTitle(title);
			panel.setResizable(true);
			panel.setMaximizable(true);
			panel.setOrder(order);
			content.addCssDependency(new URI(BABRAHAM_CHARTS_CSS_PATH));
			panel.setContent(content);
			return panel;
		} catch (Exception e){
			throw new PanelException("Caught unexpected exception generating Panel", e);
		}
	}
	
	public static ViewPanel getQCResultsSummaryPanel(WaspChart chart) throws PanelException{
		try {
			return getViewPanel(chart.getTitle(), 1, BabrahamHighChartsJs.getQCSummaryTableRepresentation(chart));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating Panel", e);
		}
	}
	
	public static ViewPanel getBasicStatsPanel(WaspChart chart) throws PanelException{
		try {
			return getViewPanel(chart.getTitle(), 2, BabrahamHighChartsJs.getKeyValueTableRepresentation(chart));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating Panel", e);
		}
	}
	
	public static ViewPanel getPerBaseSeqQualityPanel(WaspBoxPlot chart) throws PanelException{
		try {
			return getViewPanel(chart.getTitle(), 3, BabrahamHighChartsJs.getPerBaseSeqQualityPlotHtml(chart));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating Panel", e);
		}
	}
	
	public static ViewPanel getPerSeqQualityPanel(WaspChart2D chart) throws PanelException {
		try {
			return getViewPanel(chart.getTitle(), 4, BabrahamHighChartsJs.getBasicSpline(chart,2, null, null, null, 0, null));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating Panel", e);
		}
	}
	
	public static ViewPanel getGetPerBaseSeqContentPanel(WaspChart2D chart) throws PanelException {
		try {
			return getViewPanel(chart.getTitle(), 5, BabrahamHighChartsJs.getSplineForBases(chart));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating Panel", e);
		}
	}
	
	public static ViewPanel getPerBaseGcContentPanel(WaspChart2D chart) throws PanelException {
		try {
			return getViewPanel(chart.getTitle(), 6, BabrahamHighChartsJs.getBasicSpline(chart, 5, null, null, null, 0, 100));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating Panel", e);
		}
	}
	
	public static ViewPanel getPerSeqGcContentPanel(WaspChart2D chart) throws PanelException{
		try {
			return getViewPanel(chart.getTitle(), 7, BabrahamHighChartsJs.getSplineForPerSequenceGC(chart));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating Panel", e);
		}
	}
	
	public static ViewPanel getPerBaseNContentPanel(WaspChart2D chart) throws PanelException {
		try {
			return getViewPanel(chart.getTitle(), 8, BabrahamHighChartsJs.getBasicSpline(chart, 5, null, null, null, 0, 100));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating Panel", e);
		}
	}
	
	
	public static ViewPanel getSeqLengthDistributionPanel(WaspChart2D chart) throws PanelException {
		try {
			return getViewPanel(chart.getTitle(), 9, BabrahamHighChartsJs.getBasicSpline(chart, 10, null, null, null, 0, null));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating Panel", e);
		}
	}
	
	public static ViewPanel getSeqDuplicationPanel(WaspChart2D chart) throws PanelException {
		try {
			return getViewPanel(chart.getTitle(), 10, BabrahamHighChartsJs.getBasicSpline(chart, null, null, null, null, 0, null));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating Panel", e);
		}
	}

	public static ViewPanel getOverrepresentedSeqPanel(WaspChart chart) throws PanelException {
		return getViewPanel(chart.getTitle(), 11, BabrahamHighChartsJs.getTableRepresentation(chart));
	}
	
	public static ViewPanel getKmerProfilesPanel(WaspChart chart) throws PanelException{
		return getViewPanel(chart.getTitle(), 12, BabrahamHighChartsJs.getTableRepresentation(chart));
	}

}
