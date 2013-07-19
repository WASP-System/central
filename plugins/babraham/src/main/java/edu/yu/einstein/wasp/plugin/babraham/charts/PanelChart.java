package edu.yu.einstein.wasp.plugin.babraham.charts;

import java.net.URI;

import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.WaspChart;
import edu.yu.einstein.wasp.charts.WaspChart2D;
import edu.yu.einstein.wasp.exception.ChartException;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.viewpanel.Panel;
import edu.yu.einstein.wasp.viewpanel.WebPanel;
import edu.yu.einstein.wasp.viewpanel.WebContent;

public class PanelChart{
	
	public static String BABRAHAM_CHARTS_CSS_PATH = "/wasp/css/babraham.css";
	
	private static Panel getViewPanel(String title, int order, WebContent content) throws PanelException{
		try{
			WebPanel webPanel = new WebPanel();
			webPanel.setTitle(title);
			webPanel.setResizable(true);
			webPanel.setMaximizable(true);
			webPanel.setOrder(order);
			content.addCssDependency(new URI(BABRAHAM_CHARTS_CSS_PATH));
			webPanel.setExecOnRenderCode(content.getScriptCode());
			webPanel.setContent(content);
			return webPanel;
		} catch (Exception e){
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	public static Panel getQCResultsSummaryPanel(WaspChart chart) throws PanelException{
		try {
			return getViewPanel(chart.getTitle(), 1, BabrahamHighChartsJs.getQCSummaryTableRepresentation(chart));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	public static Panel getBasicStatsPanel(WaspChart chart) throws PanelException{
		try {
			return getViewPanel(chart.getTitle(), 2, BabrahamHighChartsJs.getKeyValueTableRepresentation(chart));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	public static Panel getPerBaseSeqQualityPanel(WaspBoxPlot chart) throws PanelException{
		try {
			return getViewPanel(chart.getTitle(), 3, BabrahamHighChartsJs.getPerBaseSeqQualityPlotHtml(chart));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	public static Panel getPerSeqQualityPanel(WaspChart2D chart) throws PanelException {
		try {
			return getViewPanel(chart.getTitle(), 4, BabrahamHighChartsJs.getBasicSpline(chart,2, null, null, null, 0, null));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	public static Panel getGetPerBaseSeqContentPanel(WaspChart2D chart) throws PanelException {
		try {
			return getViewPanel(chart.getTitle(), 5, BabrahamHighChartsJs.getSplineForBases(chart));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	public static Panel getPerBaseGcContentPanel(WaspChart2D chart) throws PanelException {
		try {
			return getViewPanel(chart.getTitle(), 6, BabrahamHighChartsJs.getBasicSpline(chart, 5, null, null, null, 0, 100));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	public static Panel getPerSeqGcContentPanel(WaspChart2D chart) throws PanelException{
		try {
			return getViewPanel(chart.getTitle(), 7, BabrahamHighChartsJs.getSplineForPerSequenceGC(chart));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	public static Panel getPerBaseNContentPanel(WaspChart2D chart) throws PanelException {
		try {
			return getViewPanel(chart.getTitle(), 8, BabrahamHighChartsJs.getBasicSpline(chart, 5, null, null, null, 0, 100));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public static Panel getSeqLengthDistributionPanel(WaspChart2D chart) throws PanelException {
		try {
			return getViewPanel(chart.getTitle(), 9, BabrahamHighChartsJs.getBasicSpline(chart, 10, null, null, null, 0, null));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	public static Panel getSeqDuplicationPanel(WaspChart2D chart) throws PanelException {
		try {
			return getViewPanel(chart.getTitle(), 10, BabrahamHighChartsJs.getBasicSpline(chart, null, null, null, null, 0, null));
		} catch (ChartException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}

	public static Panel getOverrepresentedSeqPanel(WaspChart chart) throws PanelException {
		return getViewPanel(chart.getTitle(), 11, BabrahamHighChartsJs.getTableRepresentation(chart));
	}
	
	public static Panel getKmerProfilesPanel(WaspChart chart) throws PanelException{
		return getViewPanel(chart.getTitle(), 12, BabrahamHighChartsJs.getTableRepresentation(chart));
	}

}
