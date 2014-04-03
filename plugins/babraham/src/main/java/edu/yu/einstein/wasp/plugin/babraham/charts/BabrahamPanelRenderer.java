package edu.yu.einstein.wasp.plugin.babraham.charts;

import java.net.URI;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.WaspChart;
import edu.yu.einstein.wasp.charts.WaspChart2D;
import edu.yu.einstein.wasp.exception.ChartException;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.service.MessageService;
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
	
	@Value("${wasp.host.servletname:wasp}")
	private String servletName;

	public static final String BABRAHAM_CHARTS_CSS_PATH = "css/babraham/babraham.css";
	
	private static final int LONG_LIST_CUTOFF = 20;
	

	private Panel getViewPanel(String title, int order, WebContent content) throws PanelException{
		try{
			WebPanel webPanel = new WebPanel();
			webPanel.setTitle(title);
			webPanel.setResizable(true);
			webPanel.setMaximizable(true);
			webPanel.setOrder(order);
			content.addCssDependency(new URI(servletName + "/" + BABRAHAM_CHARTS_CSS_PATH));
			webPanel.setExecOnRenderCode(content.getScriptCode());
			webPanel.setExecOnResizeCode(content.getScriptCode());
			webPanel.setContent(content);
			return webPanel;
		} catch (Exception e){
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public  Panel getQCResultsSummaryPanel(JSONObject chartJson, MessageService messageService) throws PanelException{
		try {
			WaspChart chart = WaspChart.getChart(chartJson, WaspChart.class);
			return getViewPanel(chart.getLocalizedTitle(messageService), 1, BabrahamHighChartsJs.getQCSummaryTableRepresentation(chart, messageService));
		} catch (ChartException | JSONException  e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public  Panel getBasicStatsPanel(JSONObject chartJson, MessageService messageService) throws PanelException{
		try {
			WaspChart chart = WaspChart.getChart(chartJson, WaspChart.class);
			return getViewPanel(chart.getLocalizedTitle(messageService), 2, BabrahamHighChartsJs.getKeyValueTableRepresentation(chart, messageService));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public Panel getPerBaseSeqQualityPanel(JSONObject chartJson, MessageService messageService) throws PanelException{
		try {
			WaspBoxPlot chart = WaspChart.getChart(chartJson, WaspBoxPlot.class);
			return getViewPanel(chart.getLocalizedTitle(messageService), 3, BabrahamHighChartsJs.getPerBaseSeqQualityPlotHtml(chart, messageService));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public Panel getPerSeqQualityPanel(JSONObject chartJson, MessageService messageService) throws PanelException {
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getLocalizedTitle(messageService), 4, BabrahamHighChartsJs.getBasicSpline(chart,2, null, null, null, 0, null, messageService));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	public Panel getGetPerBaseSeqContentPanel(JSONObject chartJson, MessageService messageService) throws PanelException {
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getLocalizedTitle(messageService), 5, BabrahamHighChartsJs.getSplineForBases(chart, messageService));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public Panel getPerBaseGcContentPanel(JSONObject chartJson, MessageService messageService) throws PanelException {
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getLocalizedTitle(messageService), 6, BabrahamHighChartsJs.getBasicSpline(chart, 5, null, null, null, 0, 100, messageService));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public Panel getPerSeqGcContentPanel(JSONObject chartJson, MessageService messageService) throws PanelException{
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getLocalizedTitle(messageService), 7, BabrahamHighChartsJs.getSplineForPerSequenceGC(chart, messageService));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public Panel getPerBaseNContentPanel(JSONObject chartJson, MessageService messageService) throws PanelException {
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getLocalizedTitle(messageService), 8, BabrahamHighChartsJs.getBasicSpline(chart, 5, null, null, null, 0, 100, messageService));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	
	public Panel getSeqLengthDistributionPanel(JSONObject chartJson, MessageService messageService) throws PanelException {
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getLocalizedTitle(messageService), 9, BabrahamHighChartsJs.getBasicSpline(chart, 10, null, null, null, 0, null, messageService));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public Panel getSeqDuplicationPanel(JSONObject chartJson, MessageService messageService) throws PanelException {
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getLocalizedTitle(messageService), 10, BabrahamHighChartsJs.getBasicSpline(chart, null, null, null, null, 0, null, messageService));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}

	
	public Panel getOverrepresentedSeqPanel(JSONObject chartJson, MessageService messageService) throws PanelException {
		try {
			WaspChart chart = WaspChart.getChart(chartJson, WaspChart.class);
			List<List<Object>> data = chart.getDataSeries().get(0).getData();  
			if (LONG_LIST_CUTOFF < data.size())
				chart.getDataSeries().get(0).setData(data.subList(0, LONG_LIST_CUTOFF));
			return getViewPanel(chart.getLocalizedTitle(messageService), 11, BabrahamHighChartsJs.getTableRepresentation(chart, messageService));
		} catch (JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	
	public Panel getKmerProfilesPanel(JSONObject chartJson, MessageService messageService) throws PanelException{
		try {
			WaspChart chart = WaspChart.getChart(chartJson, WaspChart.class);
			List<List<Object>> data = chart.getDataSeries().get(0).getData();  
			if (LONG_LIST_CUTOFF < data.size())
				chart.getDataSeries().get(0).setData(data.subList(0, LONG_LIST_CUTOFF));
			return getViewPanel(chart.getLocalizedTitle(messageService), 12, BabrahamHighChartsJs.getTableRepresentation(chart, messageService));
		} catch (JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
	
	public Panel getFastQScreenPanel(JSONObject chartJson, MessageService messageService) throws PanelException{
		try {
			WaspChart2D chart = WaspChart.getChart(chartJson, WaspChart2D.class);
			return getViewPanel(chart.getLocalizedTitle(messageService), 1, BabrahamHighChartsJs.getBarChartFastQScreen(chart, messageService));
		} catch (ChartException | JSONException e) {
			throw new PanelException("Caught unexpected exception generating WebPanel", e);
		}
	}
}
