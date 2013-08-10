package edu.yu.einstein.wasp.plugin.babraham.charts;

import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.WaspChart;
import edu.yu.einstein.wasp.charts.WaspChart2D;
import edu.yu.einstein.wasp.exception.ChartException;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.viewpanel.Panel;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;

/**
 * 
 * @author asmclellan
 *
 */
@Service
public class BabrahamPanelRenderer {

	public static final String BABRAHAM_CHARTS_CSS_PATH = "/wasp/css/babraham/babraham.css";
	

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
	
	
	public static Panel getQCResultsSummaryPanel(JSONObject chartJson) throws PanelException{
		try {
			WaspChart chart = WaspChart.getChart(chartJson, WaspChart.class);
			return getViewPanel(chart.getTitle(), 1, BabrahamHighChartsJs.getQCSummaryTableRepresentation(chart));
		} catch (ChartException | JSONException  e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public static Panel getBasicStatsPanel(JSONObject chartJson) throws PanelException{
		try {
			WaspChart chart = WaspChart.getChart(chartJson, WaspChart.class);
			return getViewPanel(chart.getTitle(), 2, BabrahamHighChartsJs.getKeyValueTableRepresentation(chart));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public static Panel getPerBaseSeqQualityPanel(JSONObject chartJson) throws PanelException{
		try {
			WaspBoxPlot chart = WaspChart.getChart(chartJson, WaspBoxPlot.class);
			return getViewPanel(chart.getTitle(), 3, BabrahamHighChartsJs.getPerBaseSeqQualityPlotHtml(chart));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public static Panel getPerSeqQualityPanel(JSONObject chartJson) throws PanelException {
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getTitle(), 4, BabrahamHighChartsJs.getBasicSpline(chart,2, null, null, null, 0, null));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	public static Panel getGetPerBaseSeqContentPanel(JSONObject chartJson) throws PanelException {
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getTitle(), 5, BabrahamHighChartsJs.getSplineForBases(chart));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public static Panel getPerBaseGcContentPanel(JSONObject chartJson) throws PanelException {
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getTitle(), 6, BabrahamHighChartsJs.getBasicSpline(chart, 5, null, null, null, 0, 100));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public static Panel getPerSeqGcContentPanel(JSONObject chartJson) throws PanelException{
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getTitle(), 7, BabrahamHighChartsJs.getSplineForPerSequenceGC(chart));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public static Panel getPerBaseNContentPanel(JSONObject chartJson) throws PanelException {
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getTitle(), 8, BabrahamHighChartsJs.getBasicSpline(chart, 5, null, null, null, 0, 100));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	
	public static Panel getSeqLengthDistributionPanel(JSONObject chartJson) throws PanelException {
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getTitle(), 9, BabrahamHighChartsJs.getBasicSpline(chart, 10, null, null, null, 0, null));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public static Panel getSeqDuplicationPanel(JSONObject chartJson) throws PanelException {
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getTitle(), 10, BabrahamHighChartsJs.getBasicSpline(chart, null, null, null, null, 0, null));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}

	
	public static Panel getOverrepresentedSeqPanel(JSONObject chartJson) throws PanelException {
		try {
			WaspChart chart = WaspChart.getChart(chartJson, WaspChart.class);
			return getViewPanel(chart.getTitle(), 11, BabrahamHighChartsJs.getTableRepresentation(chart));
		} catch (JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public static Panel getKmerProfilesPanel(JSONObject chartJson) throws PanelException{
		try {
			WaspChart chart = WaspChart.getChart(chartJson, WaspChart.class);
			return getViewPanel(chart.getTitle(), 12, BabrahamHighChartsJs.getTableRepresentation(chart));
		} catch (JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	public static Panel getFastQScreenPanel(JSONObject chartJson) throws PanelException{
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getTitle(), 1, BabrahamHighChartsJs.getBarChartFastQScreen(chart));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
}
