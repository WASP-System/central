package edu.yu.einstein.wasp.chipseq.webpanels;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.exception.ChartException;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.viewpanel.Panel;
import edu.yu.einstein.wasp.viewpanel.PanelTab;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;
import edu.yu.einstein.wasp.viewpanel.DataTabViewing.Status;

public class ChipSeqWebPanels {
	
	static protected  Logger logger = LoggerFactory.getLogger(WaspServiceImpl.class);
	
	public static PanelTab getSummaryPanelTab(Status jobStatus, Job job, Strategy strategy,	String softwareName) {
		
		PanelTab panelTab = new PanelTab();

		panelTab.setName("Summary");
		//panelTab.setDescription("testDescription");
		WebPanel panel = new WebPanel();
		panel.setTitle("Summary");
		panel.setDescription("Summary");
		panel.setResizable(true);
		panel.setMaximizable(true);	

		panel.setOrder(1);
		WebContent content = new WebContent();
		content.setHtmlCode("<div id=\"summary-grid\"></div>");
		panel.setContent(content);
		String script = "Ext.define('Summary',{ extend: 'Ext.data.Model', fields: [ 'Strategy', 'Description', 'Workflow', 'Software', 'Status' ] }); var store = Ext.create('Ext.data.Store', { model: 'Summary', data : [{Strategy: '"+strategy.getDisplayStrategy()+"', Description: '"+strategy.getDescription()+"', Workflow: '"+job.getWorkflow().getName()+"', Software: '" + softwareName+"', Status: '"+jobStatus.toString()+"'}] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Strategy\", width:150, dataIndex: 'Strategy'}, {text: \"Description\", flex: 1, dataIndex: 'Description'}, {text: \"Workflow\", width: 150, dataIndex: 'Workflow'}, {text: \"Main Software\", width: 200, dataIndex: 'Software'}, {text: \"Status\", width: 150, dataIndex: 'Status'} ], renderTo:'summary-grid', height: 300 });";
		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		panelTab.setNumberOfColumns(1);

		return panelTab;
	}

	public static PanelTab getSamplePairsPanelTab(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap){
		
		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("Pairs");
		//panelTab.setDescription("testDescription");
		WebPanel panel = new WebPanel();
		panel.setTitle("Pairs");
		panel.setDescription("Pairs");
		panel.setResizable(true);
		panel.setMaximizable(true);	

		panel.setOrder(1);
		WebContent content = new WebContent();
		content.setHtmlCode("<div id=\"samplePairs-grid\"></div>");
		panel.setContent(content);
		/* for testing only
		String string1 = "the_test_sample";
		String string2 = "control_SAMPLE";
		String script = "Ext.define('SamplePairs',{ extend: 'Ext.data.Model', fields: [ 'TestSample', 'ControlSample' ] }); var store = Ext.create('Ext.data.Store', { model: 'SamplePairs', data : [{TestSample: '"+string1+"', ControlSample: '"+string2+"'}, {TestSample: '"+string1+"', ControlSample: '"+string2+"'}] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Test Sample\",  width:300, dataIndex: 'TestSample'}, {text: \"Control Sample\",  flex: 1, dataIndex: 'ControlSample'} ], renderTo:'samplePairs-grid', height: 300 });";
		*/
		StringBuffer stringBuffer = new StringBuffer();
		for(Sample testSample : testSampleList){
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			for(Sample controlSample : controlSampleList){
				if(stringBuffer.length()>0){
					stringBuffer.append(", ");
				}
				stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"'}");
			}
		}
		String theData = new String(stringBuffer);
		String script = "Ext.define('SamplePairs',{ extend: 'Ext.data.Model', fields: [ 'TestSample', 'ControlSample' ] }); var store = Ext.create('Ext.data.Store', { model: 'SamplePairs', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Test Sample\",  width:300, dataIndex: 'TestSample'}, {text: \"Control Sample\",  flex: 1, dataIndex: 'ControlSample'} ], renderTo:'samplePairs-grid', height: 300 });";
		
		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		panelTab.setNumberOfColumns(1);

		return panelTab;
	}
	
	public static PanelTab getSampleRunsPanelTab(List<Sample> testSampleList, Map<Sample, List<String>> sampleRunInfoMap){
		
		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("Runs");
		//panelTab.setDescription("testDescription");
		WebPanel panel = new WebPanel();
		panel.setTitle("Runs");
		panel.setDescription("Runs");
		panel.setResizable(true);
		panel.setMaximizable(true);	

		panel.setOrder(1);
		WebContent content = new WebContent();
		content.setHtmlCode("<div id=\"sampleRuns-grid\"></div>");
		panel.setContent(content);
		/* for testing only
		String string1 = "the_test_sample";
		String string2 = "the run info";
		String script = "Ext.define('SampleRuns',{ extend: 'Ext.data.Model', fields: [ 'Sample', 'Run' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleRuns', data : [{Sample: '"+string1+"', Run: '"+string2+"'}, {Sample: '"+string1+"', Run: '"+string2+"'}] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Sample\",  width:300, dataIndex: 'Sample'}, {text: \"Run\",  flex: 1, dataIndex: 'Run'} ], renderTo:'sampleRuns-grid', height: 300 });";
		*/
		StringBuffer stringBuffer = new StringBuffer();
		for(Sample testSample : testSampleList){
			if(stringBuffer.length()>0){
				stringBuffer.append(", ");
			}
			List<String> runInfoList = sampleRunInfoMap.get(testSample);
			StringBuffer completeRunInfoAsStringBuffer = new StringBuffer();
			for(String runInfo: runInfoList){
				if(completeRunInfoAsStringBuffer.length() > 0){
					completeRunInfoAsStringBuffer.append("<br />");
				}
				completeRunInfoAsStringBuffer.append(runInfo);
			}
			String completeRunInfoAsString = new String(completeRunInfoAsStringBuffer);
			stringBuffer.append("{Sample: '"+testSample.getName()+"', Runs: '"+completeRunInfoAsString+"'}");			
		}
		String theData = new String(stringBuffer);
		String script = "Ext.define('SampleRuns',{ extend: 'Ext.data.Model', fields: [ 'Sample', 'Runs' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleRuns', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Sample\",  width:300, dataIndex: 'Sample'}, {text: \"Runs\",  flex: 1, dataIndex: 'Runs'} ], renderTo:'sampleRuns-grid', height: 300 });";
		
		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		panelTab.setNumberOfColumns(1);

		return panelTab;
	}

}
