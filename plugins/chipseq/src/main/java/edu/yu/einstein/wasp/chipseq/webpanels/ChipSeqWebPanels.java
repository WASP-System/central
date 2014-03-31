package edu.yu.einstein.wasp.chipseq.webpanels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.viewpanel.DataTabViewing.Status;
import edu.yu.einstein.wasp.viewpanel.GridColumn;
import edu.yu.einstein.wasp.viewpanel.GridContent;
import edu.yu.einstein.wasp.viewpanel.GridDataField;
import edu.yu.einstein.wasp.viewpanel.GridPanel;
import edu.yu.einstein.wasp.viewpanel.PanelTab;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;

public class ChipSeqWebPanels {
	
	//see this webpage for model grid http://docs.sencha.com/extjs/4.2.1/#!/guide/grid
	
	static protected  Logger logger = LoggerFactory.getLogger(WaspServiceImpl.class);	
	
	public static PanelTab getSummaryPanelTab2222(Status jobStatus, Job job, Strategy strategy,	String softwareName) {

		logger.debug("********inside getSummaryPanelTab2222()");

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("Summary");
		panelTab.setNumberOfColumns(1);

		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Summary");
		panel.setDescription("Summary");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		panel.setStatusField("Status");
		
		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		content.addDataFields(new GridDataField("Strategy", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Description", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Workflow", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Software", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Status", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		content.addColumn(new GridColumn("Strategy", "Strategy", 150, 0));//header,dataIndex	set width=150, flex=0	
		content.addColumn(new GridColumn("Description", "Description", 1));//header,dataIndex	set flex=1	
		content.addColumn(new GridColumn("Workflow", "Workflow", 150, 0));//header,dataIndex		
		content.addColumn(new GridColumn("Main Software", "Software", 200, 0));//header,dataIndex		
		content.addColumn(new GridColumn("Status", "Status", 150, 0));//header,dataIndex
		
		//create rows with  information
		List<String> row = new ArrayList<String>();
		row.add(strategy.getDisplayStrategy());
		row.add(strategy.getDescription());
		row.add(job.getWorkflow().getName());
		row.add(softwareName);
		row.add(jobStatus.toString());
		content.addDataRow(row);//add row to content
		
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
		/*
		content.setHtmlCode("<div id=\"summary-grid\"></div>");
		//////content.setHtmlCode("<iframe id=\"reportframe\" width=\"100%\" src=\"http://www.einstein.yu.edu\"></iframe>");
		panel.setContent(content);
		String script = "Ext.define('Summary',{ extend: 'Ext.data.Model', fields: [ 'Strategy', 'Description', 'Workflow', 'Software', 'Status' ] }); var store = Ext.create('Ext.data.Store', { model: 'Summary', data : [{Strategy: '"+strategy.getDisplayStrategy()+"', Description: '"+strategy.getDescription()+"', Workflow: '"+job.getWorkflow().getName()+"', Software: '" + softwareName+"', Status: '"+jobStatus.toString()+"'}] }); Ext.create('Ext.grid.Panel', { id:'summary-panel', store: store,  columns: [ {text: \"Strategyee\", width:150, dataIndex: 'Strategy'}, {text: \"Description\", flex:1, dataIndex: 'Description'}, {text: \"Workflow\", width: 150, dataIndex: 'Workflow'}, {text: \"Main Software\", width: 200, dataIndex: 'Software'}, {text: \"Status\", width: 150, dataIndex: 'Status'} ], renderTo:'summary-grid', height:500 }); ";
		//String script = dataModel + " var store = Ext.create('Ext.data.Store', { model: 'Summary', data : [{Strategy: '"+strategy.getDisplayStrategy()+"', Description: '"+strategy.getDescription()+"', Workflow: '"+job.getWorkflow().getName()+"', Software: '" + softwareName+"', Status: '"+jobStatus.toString()+"'}] }); Ext.create('Ext.grid.Panel', { id:'summary-panel', store: store,  columns: [ {text: \"Strategy\", width:150, dataIndex: 'Strategy'}, {text: \"Description\", flex:1, dataIndex: 'Description'}, {text: \"Workflow\", width: 150, dataIndex: 'Workflow'}, {text: \"Main Software\", width: 200, dataIndex: 'Software'}, {text: \"Status\", width: 150, dataIndex: 'Status'} ], renderTo:'summary-grid', height:500 }); ";
		//String script = dataModel+" "+dataStore+" "+"Ext.create('Ext.grid.Panel', { id:'summary-panel', store: store,  columns: [ {text: \"Strategy\", width:150, dataIndex: 'Strategy'}, {text: \"Description\", flex:1, dataIndex: 'Description'}, {text: \"Workflow\", width: 150, dataIndex: 'Workflow'}, {text: \"Main Software\", width: 200, dataIndex: 'Software'}, {text: \"Status\", width: 150, dataIndex: 'Status'} ], renderTo:'summary-grid', height:500 }); ";
				
		//both these work (but throw firebug exception)
		//String script = "var panel = Ext.create('Ext.form.Panel', { renderTo: 'summary-grid', title: 'I am a Panel', html: 'This is not an anchor<br /> but this is: <a href=\"http://www.einstein.yu.edu\" >This is the einstein anchor</a><br /><br /> and the next line should be an iframe:<br /><iframe id=\"reportframe\" width=\"100%\" src=\"http://www.einstein.yu.edu\"></iframe><br />do you see the iframe?'}); "; 
		//String script = "var panel = Ext.create('Ext.panel.Panel', { renderTo: 'summary-grid', title: 'I am a Panel.Panel', html: 'This is not an anchor<br /> but this is: <a href=\"http://www.einstein.yu.edu\" >This is the einstein anchor to view the medical school home page</a><br /><br /> and the next line should be an iframe:<br /><iframe id=\"reportframe\" width=\"100%\" src=\"http://www.einstein.yu.edu\"></iframe><br />do you see the iframe?'}); "; 
		
		logger.debug("***********getSummaryPanelTab script: "+script);
		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode("var theDiv = $('summary-grid'); Ext.getCmp('summary-panel').setSize(theDiv.offsetWidth, undefined);");
		//panel.setExecOnResizeCode("Ext.getCmp('summary-panel').setSize(this.width, undefined);");
		panel.setExecOnResizeCode("var theDiv = $('summary-grid'); Ext.getCmp('summary-panel').setSize(theDiv.offsetWidth, undefined);");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		panelTab.setNumberOfColumns(1);

		return panelTab;
		*/
	}
	
	public static PanelTab getSamplePairsPanelTab2222(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, Map<String,String> sampleIdControlIdCommandLineMap){
		
		logger.debug("********inside getSamplePairsPanelTab2222()");

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("Sample Pairs");
		panelTab.setNumberOfColumns(1);

		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Sample Pairs Used For Aggregate Analysis");
		panel.setDescription("Sample Pairs Used For Aggregate Analysis");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		
		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		content.addDataFields(new GridDataField("TestSample", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("ControlSample", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Command", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		content.addColumn(new GridColumn("Test Sample", "TestSample", 250, 0));//header,dataIndex	     width=250; flex=0	
		content.addColumn(new GridColumn("Control Sample", "ControlSample", 250, 0));//header,dataIndex	 width=250; flex=0	
		content.addColumn(new GridColumn("Command Line", "Command", 1));//header,dataIndex               flex=1
		
		//create rows with  information
		for(Sample testSample : testSampleList){
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			for(Sample controlSample : controlSampleList){
				
				List<String> row = new ArrayList<String>();
				
				row.add(testSample.getName());
				row.add(controlSample.getName());
				
				String command = sampleIdControlIdCommandLineMap.get(testSample.getId().toString()+"::"+controlSample.getId().toString());
				if(command==null || command.isEmpty()){
					command = " ";
				}
				else{
					command = command.replaceAll("\\n", "");//the workunit may put a newline at the end, which is incompatible with Extjs grids
				}
				row.add(command);
				
				content.addDataRow(row);//add the new row to the content
			}
		}
				
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
		
		/*
		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("Sample Pairs");
		//panelTab.setDescription("testDescription");
		WebPanel panel = new WebPanel();
		panel.setTitle("Sample Pairs Used For Aggregate Analysis");
		panel.setDescription("Sample Pairs Used For Aggregate Analysis");
		panel.setResizable(true);
		panel.setMaximizable(true);	

		panel.setOrder(1);
		WebContent content = new WebContent();
		content.setHtmlCode("<div id=\"samplePairs-grid\"></div>");
		panel.setContent(content);
		StringBuffer stringBuffer = new StringBuffer();
		for(Sample testSample : testSampleList){
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			for(Sample controlSample : controlSampleList){
				if(stringBuffer.length()>0){
					stringBuffer.append(", ");
				}
				String command = sampleIdControlIdCommandLineMap.get(testSample.getId().toString()+"::"+controlSample.getId().toString());
				if(command==null || command.isEmpty()){
					command = " ";
				}
				else{
					command = command.replaceAll("\\n", "");//the workunit may put a newline at the end, which is incompatible with Extjs grids
				}
				stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"', Command: '"+command+"'}");
			}
		}
		String theData = new String(stringBuffer);
		String script = "Ext.define('SamplePairs',{ extend: 'Ext.data.Model', fields: [ 'TestSample', 'ControlSample', 'Command' ] }); var store = Ext.create('Ext.data.Store', { model: 'SamplePairs', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Test Sample\",  width:250, dataIndex: 'TestSample'}, {text: \"Control Sample\",  width:250, dataIndex: 'ControlSample'}, {text: \"Command Line\",  width: 2000, dataIndex: 'Command'} ], renderTo:'samplePairs-grid', height: 300 });";
		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		panelTab.setNumberOfColumns(1);

		return panelTab;
		*/
	}
	
	public static PanelTab getSampleLibraryRunsPanelTab2222(List<Sample> testSampleList, Map<Sample, List<Sample>> sampleLibraryListMap, Map<Sample, List<String>> libraryRunInfoListMap){
		
		logger.debug("********inside getSampleLibraryRunsPanelTab2222()");

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("Runs");
		panelTab.setNumberOfColumns(1);

		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Runs Used For Aggregate Analysis");
		panel.setDescription("Runs Used For Aggregate Analysis");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		
		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		content.addDataFields(new GridDataField("Sample", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Library", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Runs", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		content.addColumn(new GridColumn("Submitted Sample", "Sample", 250, 0));//header,dataIndex		width=250; flex=0
		content.addColumn(new GridColumn("Library", "Library", 250, 0));//header,dataIndex				width=250; flex=0
		content.addColumn(new GridColumn("Runs", "Runs", 1));//header,dataIndex							flex=1
		
		//create rows with  information
		for(Sample testSample : testSampleList){
			List<Sample> libraryList = sampleLibraryListMap.get(testSample);
			for(Sample library : libraryList){
				
				List<String> row = new ArrayList<String>();
				
				row.add(testSample.getName());
				row.add(library.getName());
				
				List<String> runInfoList = libraryRunInfoListMap.get(library);
				StringBuffer completeRunInfoAsStringBuffer = new StringBuffer();
				for(String runInfo: runInfoList){
					if(completeRunInfoAsStringBuffer.length() > 0){
						completeRunInfoAsStringBuffer.append("<br />");
					}
					completeRunInfoAsStringBuffer.append(runInfo);
				}
				String completeRunInfoAsString = new String(completeRunInfoAsStringBuffer);				
				row.add(completeRunInfoAsString);
				
				content.addDataRow(row);//add the new row to the content
			}
		}
				
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
		/*
		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("Runs");
		//panelTab.setDescription("testDescription");
		WebPanel panel = new WebPanel();
		panel.setTitle("Runs Used For Aggregate Analysis");
		panel.setDescription("Runs Used For Aggregate Analysis");
		panel.setResizable(true);
		panel.setMaximizable(true);	

		panel.setOrder(1);
		WebContent content = new WebContent();
		content.setHtmlCode("<div id=\"sampleLibraryRuns-grid\"></div>");
		panel.setContent(content);
	
		StringBuffer stringBuffer = new StringBuffer();
		for(Sample testSample : testSampleList){
			List<Sample> libraryList = sampleLibraryListMap.get(testSample);
			for(Sample library : libraryList){
				if(stringBuffer.length()>0){
					stringBuffer.append(", ");
				}
				List<String> runInfoList = libraryRunInfoListMap.get(library);
				StringBuffer completeRunInfoAsStringBuffer = new StringBuffer();
				for(String runInfo: runInfoList){
					if(completeRunInfoAsStringBuffer.length() > 0){
						completeRunInfoAsStringBuffer.append("<br />");
					}
					completeRunInfoAsStringBuffer.append(runInfo);
				}
				String completeRunInfoAsString = new String(completeRunInfoAsStringBuffer);
				stringBuffer.append("{Sample: '"+testSample.getName()+"', Library: '"+library.getName()+"', Runs: '"+completeRunInfoAsString+"'}");			
			}
		}
		String theData = new String(stringBuffer);
		String script = "Ext.define('SampleLibraryRuns',{ extend: 'Ext.data.Model', fields: [ 'Sample', 'Library', 'Runs' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleLibraryRuns', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Submitted Sample\",  width:250, dataIndex: 'Sample'}, {text: \"Library\",  width:250, dataIndex: 'Library'}, {text: \"Runs\",  flex: 1, dataIndex: 'Runs'} ], renderTo:'sampleLibraryRuns-grid', height: 300 });";
		
		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		panelTab.setNumberOfColumns(1);

		return panelTab;
		*/
	}
	public static PanelTab getFileTypeDefinitionsPanelTab2222(List<FileType> fileTypeList){
		
		logger.debug("********inside getFileTypeDefinitionsPanelTab2222()");

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("File Types");
		panelTab.setNumberOfColumns(1);

		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("File Type Definitions For Aggregate Analysis");
		panel.setDescription("FileTypes Used For Aggregate Analysis");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		
		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		content.addDataFields(new GridDataField("FileType", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Description", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		content.addColumn(new GridColumn("File Type", "FileType", 200, 0));//header,dataIndex		width=200; flex=0
		content.addColumn(new GridColumn("Description", "Description", 1));//header,dataIndex		flex=1
		
		//create rows with  information
		for(FileType fileType : fileTypeList){
			
			List<String> row = new ArrayList<String>();			
			row.add(fileType.getName());
			row.add(fileType.getDescription());			
			content.addDataRow(row);//add the new row to the content
		}
						
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
		/*
		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("FileTypes");
		//panelTab.setDescription("testDescription");
		WebPanel panel = new WebPanel();
		panel.setTitle("FileTypes Generated By Aggregate Analysis");
		panel.setDescription("FileTypes Generated By Aggregate Analysis");
		panel.setResizable(true);
		panel.setMaximizable(true);	

		panel.setOrder(1);
		WebContent content = new WebContent();
		content.setHtmlCode("<head><style type=\"text/css\">.multilineColumn .x-grid-cell-inner {white-space: normal;}.x-grid-row-over .x-grid-cell-inner { font-weight: bold; white-space: normal;}</style></head>" +
				"<div id=\"fileTypeDescription-grid\"></div>");
		//content.setHtmlCode("<div id=\"fileTypeDescription-grid\"></div>");
		panel.setContent(content);
		
		
		StringBuffer stringBuffer = new StringBuffer();
		for(FileType fileType : fileTypeList){
			if(stringBuffer.length()>0){
				stringBuffer.append(", ");
			}
			stringBuffer.append("{FileType: '"+fileType.getName()+"', Description: '"+fileType.getDescription()+"'}");
		}
		String theData = new String(stringBuffer);
		
		//shows the entire line only when mouse rollover
		//String script = "Ext.define('FileTypeDescriptions',{ extend: 'Ext.data.Model', fields: [ 'FileType', 'Description', ] }); var store = Ext.create('Ext.data.Store', { model: 'FileTypeDescriptions', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"File Type\",  width:200, dataIndex: 'FileType'}, {text: \"Description\",  flex:1, dataIndex: 'Description', renderer: function(value, metaData, record, rowIndex, colIndex, store) { metaData.css = 'multilineColumn'; return value;}     } ], renderTo:'fileTypeDescription-grid', height: 300 });";
		
		//shows the entire line at all times
		String script = "Ext.define('FileTypeDescriptions',{ extend: 'Ext.data.Model', fields: [ 'FileType', 'Description', ] }); var store = Ext.create('Ext.data.Store', { model: 'FileTypeDescriptions', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"File Type\",  width:200, dataIndex: 'FileType'}, {text: \"Description\",  flex:1, dataIndex: 'Description' } ], renderTo:'fileTypeDescription-grid', height: 300 });";

		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		panelTab.setNumberOfColumns(1);

		return panelTab;
		*/
	}
	public static PanelTab getAllFilesDisplayedBySampleUsingGroupingGridPanelTab2222(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, List<FileType> fileTypeList, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){

		logger.debug("********inside getAllFilesDisplayedBySampleUsingGroupingGridPanelTab2222()");

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("Files By Sample Pairs");
		panelTab.setNumberOfColumns(1);

		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Files By Sample Pairs");
		panel.setDescription("Files By Sample Pairs");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		panel.setGrouping(true);
		panel.setGroupField("SamplePairs");
		panel.setHasDownload(true);
		panel.setDownloadLinkField("Download");
		
		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		content.addDataFields(new GridDataField("SamplePairs", "String"));//THIS WILL BE THE UNIQUE GROUPING FIELD //dataIndex, datatype
		content.addDataFields(new GridDataField("FileType", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("File", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("MD5", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Download", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		///////don't want this to display: content.addColumn(new GridColumn("SamplePairs", "SamplePairs"));//header,dataIndex		
		content.addColumn(new GridColumn("File Type", "FileType", 150, 0));//header,dataIndex	width=150; flex=0	
		content.addColumn(new GridColumn("File", "File", 1));//header,dataIndex					flex=1
		content.addColumn(new GridColumn("MD5", "MD5", 270, 0));//header,dataIndex					width=270; flex=0
		////content.addColumn(new GridColumn(" ", "Download", 100, 0));//header is single space string,dataIndex	width=100; flex=0
		
		//create rows with  information
		for(Sample testSample : testSampleList){
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			for(Sample controlSample : controlSampleList){
				
				String samplePairs = "Test Sample: " + testSample.getName() + "; Control: " + controlSample.getName();
				
				for(FileType fileType : fileTypeList){
					
					if(fileType.getIName().endsWith("_model.png")){//macstwo specific
						continue;
					}
					
					List<String> row = new ArrayList<String>();
					
					
					FileHandle fileHandle = sampleIdControlIdFileTypeIdFileHandleMap.get(testSample.getId().toString() + "::" + controlSample.getId().toString() + "::" + fileType.getId().toString());
					String resolvedURL = fileHandleResolvedURLMap.get(fileHandle);
					if(fileHandle==null || resolvedURL==null || resolvedURL.isEmpty()){//unexpected
						continue;
					}
					else{
						row.add(samplePairs);//won't be displayed, but must be part of the row
						row.add(fileType.getName());
						row.add(fileHandle.getFileName());
						row.add(fileHandle.getMd5hash());
						row.add(resolvedURL);
						content.addDataRow(row);//add the new row to the content
					}
				}			
			}
		}
						
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
		/*
		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("Files By Sample Pairs");
		panelTab.setNumberOfColumns(1);
		//panelTab.setDescription("testDescription");
		
		WebPanel panel = new WebPanel();
		panel.setTitle("Files By Sample Pairs");
		///panel.setDescription("Files By Sample");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		WebContent content = new WebContent();
		String divId = "filesGroupedBySamplePairs-grid";				
		content.setHtmlCode("<div id=\""+divId+"\"></div>");
		panel.setContent(content);
		
		StringBuffer stringBuffer = new StringBuffer();
		for(Sample testSample : testSampleList){
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			for(Sample controlSample : controlSampleList){
				
				String samplePairs = "Test Sample: " + testSample.getName() + "; Control: " + controlSample.getName();

				
				for(FileType fileType : fileTypeList){
					if(stringBuffer.length()>0){
						stringBuffer.append(", ");
					}
					FileHandle fileHandle = sampleIdControlIdFileTypeIdFileHandleMap.get(testSample.getId().toString() + "::" + controlSample.getId().toString() + "::" + fileType.getId().toString());
					String resolvedURL = fileHandleResolvedURLMap.get(fileHandle);
					//if(fileHandle==null){
					//	stringBuffer.append("{FileType: '"+fileType.getName()+"', File: 'no file', MD5: ' ', Download: ' '}");
					//}
					//else{
						//stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"', File: '"+fileHandle.getFileName()+"', MD5: '"+fileHandle.getMd5hash()+"', Download: '"+"<a href=\""+resolvedURL+"\"><img src=\"ext/images/icons/fam/disk.png\" /></a>"+"'}");
						stringBuffer.append("{SamplePairs: '"+samplePairs+"', FileType: '"+fileType.getName()+"', File: '"+fileHandle.getFileName()+"', MD5: '"+fileHandle.getMd5hash()+"', Download: '"+resolvedURL+"'}");
					//}
				}
				
				
			}
		}
		String theData = new String(stringBuffer);
		
		//normal grid
		//String script = "Ext.require(['Ext.grid.*','Ext.data.*','Ext.form.field.Number','Ext.form.field.Date','Ext.tip.QuickTipManager','Ext.selection.CheckboxModel','Wasp.RowActions']); Ext.tip.QuickTipManager.init(); Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'SamplePairs', 'FileType', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Sample Pairs\",  width:150, dataIndex: 'SamplePairs'},{text: \"FileType\",  width:150, dataIndex: 'FileType'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:270, dataIndex: 'MD5', renderer: function(val, meta, record){var tip = record.get('MD5'); meta.tdAttr = 'data-qtip=\"' + tip + '\"'; return val; } }, {header:\" \", width: 100, xtype: 'rowactions', actions: [{iconCls: 'icon-clear-group', qtip: 'Download', callback: function(grip, record, action, idx, col, e, target){window.location=record.get('Download');}}], keepSelection: true     }, ], renderTo:'"+divId+"', height: 300 });";
		
		//grouping grid test with all fields
		//String script = "Ext.require(['Ext.grid.*','Ext.data.*','Ext.form.field.Number','Ext.form.field.Date','Ext.tip.QuickTipManager','Ext.selection.CheckboxModel','Wasp.RowActions']); Ext.tip.QuickTipManager.init(); Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'SamplePairs', 'FileType', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile',  groupField: 'SamplePairs', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Sample Pairs\",  width:150, dataIndex: 'SamplePairs'},{text: \"FileType\",  width:150, dataIndex: 'FileType'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:270, dataIndex: 'MD5', renderer: function(val, meta, record){var tip = record.get('MD5'); meta.tdAttr = 'data-qtip=\"' + tip + '\"'; return val; } }, {header:\" \", width: 100, xtype: 'rowactions', actions: [{iconCls: 'icon-clear-group', qtip: 'Download', callback: function(grip, record, action, idx, col, e, target){window.location=record.get('Download');}}], keepSelection: true     }, ],  features: [{ftype: 'grouping' }], renderTo:'"+divId+"', height: 300 });";
		
		//group grid with the goupedField not displayed on every row
		String script = "Ext.require(['Ext.grid.*','Ext.data.*','Ext.form.field.Number','Ext.form.field.Date','Ext.tip.QuickTipManager','Ext.selection.CheckboxModel','Wasp.RowActions']); Ext.tip.QuickTipManager.init(); Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'SamplePairs', 'FileType', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile',  groupField: 'SamplePairs', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"File Type\",  width:150, dataIndex: 'FileType'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:270, dataIndex: 'MD5', renderer: function(val, meta, record){var tip = record.get('MD5'); meta.tdAttr = 'data-qtip=\"' + tip + '\"'; return val; } }, {header:\" \", width: 100, xtype: 'rowactions', actions: [{iconCls: 'icon-clear-group', qtip: 'Download', callback: function(grip, record, action, idx, col, e, target){window.location=record.get('Download');}}], keepSelection: true     }, ],  features: [{ftype: 'grouping', groupHeaderTpl: 'File Type: {name}' }], renderTo:'"+divId+"', height: 300 });";
		
		//example from web; it works fine
		//String script = "var store = Ext.create('Ext.data.Store', {        storeId: 'employeeStore',        fields: ['name', 'seniority', 'department'],        groupField: 'department',        data: {            'employees': [{                \"name\": \"Michael Scott\",                \"seniority\": 7,                \"department\": \"Management\"            }, {                \"name\": \"Dwight Schrute\",                \"seniority\": 2,               \"department\": \"Sales\"            }, {                \"name\": \"Jim Halpert\",               \"seniority\": 3,                \"department\": \"Sales\"            }, {                \"name\": \"Kevin Malone\",                \"seniority\": 4,                \"department\": \"Accounting\"            }, {                \"name\": \"Angela Martin\",                \"seniority\": 5,                \"department\": \"Accounting\"            }]        },        proxy: {            type: 'memory',            reader: {                type: 'json',                root: 'employees'            }        }    });    Ext.create('Ext.grid.Panel', {        title: 'Employees',        store: Ext.data.StoreManager.lookup('employeeStore'),        columns: [{            text: 'Name',            dataIndex: 'name'        }, {            text: 'Seniority',            dataIndex: 'seniority'        }],        features: [{            ftype: 'grouping'        }],        width: 200,        height: 275,        renderTo:'"+divId+"'    });";
		
		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		return panelTab;
		*/
	}
	
	public static PanelTab getAllFilesDisplayedByFileTypeUsingGroupingGridPanelTab2222(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, List<FileType> fileTypeList, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){
	
		logger.debug("********inside getAllFilesDisplayedByFileTypeUsingGroupingGridPanelTab2222()");

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("Files By File Type");
		panelTab.setNumberOfColumns(1);

		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Files By File Type");
		panel.setDescription("Files By File Type");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		panel.setGrouping(true);
		panel.setGroupField("FileType");
		panel.setHasDownload(true);
		panel.setDownloadLinkField("Download");
		
		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		content.addDataFields(new GridDataField("TestSample", "String")); //dataIndex, datatype
		content.addDataFields(new GridDataField("ControlSample", "String")); //dataIndex, datatype
		content.addDataFields(new GridDataField("FileType", "String"));//THIS WILL BE THE UNIQUE GROUPING FIELD //dataIndex, datatype
		content.addDataFields(new GridDataField("File", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("MD5", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Download", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		content.addColumn(new GridColumn("Test Sample", "TestSample", 150, 0));//header,dataIndex		
		content.addColumn(new GridColumn("Control Sample", "ControlSample", 150, 0));//header,dataIndex		
		content.addColumn(new GridColumn("File", "File", 1));//header,dataIndex		
		content.addColumn(new GridColumn("MD5", "MD5", 270, 0));//header,dataIndex		
		///content.addColumn(new GridColumn(" ", "Download", 100, 0));//header is single space string,dataIndex		
		
		//create rows with  information
		for(Sample testSample : testSampleList){
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			for(Sample controlSample : controlSampleList){
				
				//not needed: can probaly remove: String samplePairs = "Test Sample: " + testSample.getName() + "; Control: " + controlSample.getName();
				
				for(FileType fileType : fileTypeList){
					
					if(fileType.getExtensions().endsWith("_model.png")){//macstwo specific
						continue;
					}
					
					List<String> row = new ArrayList<String>();					
					
					FileHandle fileHandle = sampleIdControlIdFileTypeIdFileHandleMap.get(testSample.getId().toString() + "::" + controlSample.getId().toString() + "::" + fileType.getId().toString());
					String resolvedURL = fileHandleResolvedURLMap.get(fileHandle);
					if(fileHandle==null || resolvedURL==null || resolvedURL.isEmpty()){//unexpected
						continue;
					}
					else{
						row.add(testSample.getName());
						row.add(controlSample.getName());
						row.add(fileType.getName());//won't be displayed, but must be part of the row
						row.add(fileHandle.getFileName());
						row.add(fileHandle.getMd5hash());
						row.add(resolvedURL);
						content.addDataRow(row);//add the new row to the content
					}
				}			
			}
		}
						
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
	}

	//macstwo specific
	public static PanelTab getAllModelPNGFilesDisplayedInPanelsTab2222(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, List<FileType> fileTypeList, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){
		
		logger.debug("********inside getAllModelPNGFilesDisplayedInPanelsTab2222() at 1");

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("Model View");
		panelTab.setNumberOfColumns(1);
		
		int counter = 1;
		for(Sample testSample : testSampleList){
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			for(Sample controlSample : controlSampleList){
				
				String samplePairs = "Test Sample: " + testSample.getName() + "; Control: " + controlSample.getName();
				
				for(FileType fileType : fileTypeList){
					//******************Robert A Dubin TODO ToDo change to model.png!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					if(fileType.getExtensions().endsWith("_model.pdf")){//macstwo specific
						logger.debug("********inside getAllModelPNGFilesDisplayedInPanelsTab2222() at 2");
						FileHandle fileHandle = sampleIdControlIdFileTypeIdFileHandleMap.get(testSample.getId().toString() + "::" + controlSample.getId().toString() + "::" + fileType.getId().toString());
						String resolvedURL = fileHandleResolvedURLMap.get(fileHandle);
						if(fileHandle==null || resolvedURL==null || resolvedURL.isEmpty()){//unexpected
							continue;
						}
						logger.debug("********inside getAllModelPNGFilesDisplayedInPanelsTab2222() at 3");
						//create the panel
						WebPanel panel = new WebPanel();
						panel.setTitle(samplePairs);
						panel.setDescription(samplePairs);
						panel.setHeight(900);
						panel.setResizable(true);
						panel.setMaximizable(true);	
						panel.setOrder(counter++);
						
						WebContent content = new WebContent();
						//content.setHtmlCode("<img width=\"200\" height=\"200\" src=\"http://localhost:8080/wasp/images/fail.png\" />");
						//content.setHtmlCode("<iframe width=\"200\" height=\"200\" src=\"http://www.einstein.yu.edu\" ></iframe>");
						//content.setHtmlCode("<iframe width=\"470\" height=\"900\" src=\"http://localhost:8080/wasp/file/fileHandle/1160/view.do\" ></iframe>");//works nicely!
						content.setHtmlCode("<iframe width=\"470\" height=\"900\" src=\"http://localhost:8080/wasp/file/fileHandle/"+fileHandle.getId()+"/view.do\" ></iframe>");
						
						///file/fileHandle/${fileHandle.getId()}/view.do"
						
						panel.setContent(content);
						panelTab.addPanel(panel);//add panel to panelTab	
						logger.debug("********inside getAllModelPNGFilesDisplayedInPanelsTab2222() at 4 with counter = " + counter);
					}					
				}			
			}
		}	
		logger.debug("********inside getAllModelPNGFilesDisplayedInPanelsTab2222() at END");
		return panelTab;
	}

	
	
	public static PanelTab getSummaryPanelTab(Status jobStatus, Job job, Strategy strategy,	String softwareName) {

		String name = "Summary";
		Map<String,Map<String,String>> fieldMap = new LinkedHashMap<String, Map<String,String>>();//iteration order maintains the order in which keys were inserted into the map
		Map<String,String> strategyMap = new HashMap<String,String>();
		strategyMap.put("data", strategy.getDisplayStrategy());
		strategyMap.put("width", "150");
		fieldMap.put("Strategy", strategyMap);		
		Map<String,String> descriptionMap = new HashMap<String,String>();
		descriptionMap.put("data", strategy.getDescription());
		descriptionMap.put("flex", "1");
		fieldMap.put("Description", descriptionMap);		
		Map<String,String> workflowMap = new HashMap<String,String>();
		workflowMap.put("data", job.getWorkflow().getName());
		workflowMap.put("width", "150");
		fieldMap.put("Workflow", workflowMap);
		Map<String,String> softwareMap = new HashMap<String,String>();
		softwareMap.put("data", softwareName);
		softwareMap.put("width", "200");
		fieldMap.put("Software", softwareMap);
		Map<String,String> statusMap = new HashMap<String,String>();
		statusMap.put("data", jobStatus.toString());//status of the aggregateAnalysis	
		statusMap.put("width", "150");
		fieldMap.put("Status", statusMap);

		
		Map<String,String> fieldDataMap = new LinkedHashMap<String,String>();//iteration order is the order in which keys were inserted into the map
		fieldDataMap.put("Strategy", strategy.getDisplayStrategy());
		fieldDataMap.put("Description", strategy.getDescription());
		fieldDataMap.put("Workflow", job.getWorkflow().getName());
		fieldDataMap.put("Software", softwareName);
		fieldDataMap.put("Status", jobStatus.toString());//status of the aggregateAnalysis	
		String dataModel = defineDataModel(name, fieldDataMap, fieldMap); 
		if(dataModel==null||dataModel.isEmpty()){
			return null;//do this with all
		}
		logger.debug("***********getSummaryPanelTab dataModel: "+dataModel);
		String dataStore = createDataStore(name,  fieldDataMap, fieldMap);
		logger.debug("***********getSummaryPanelTab dataStore: "+dataStore);
		//String gridPanel = createGridPanel(name, fieldDataMap);
		//logger.debug("***********getSummaryPanelTab gridPanel: "+gridPanel.toString());
		
		
		PanelTab panelTab = new PanelTab();

		panelTab.setName("Summary");
		//panelTab.setDescription("testDescription");
		WebPanel panel = new WebPanel();
		panel.setTitle("Summary");
		panel.setDescription("Summary");
		panel.setResizable(false);
		panel.setMaximizable(false);	

		panel.setOrder(1);
		WebContent content = new WebContent();
		content.setHtmlCode("<div id=\"summary-grid\"></div>");
		//////content.setHtmlCode("<iframe id=\"reportframe\" width=\"100%\" src=\"http://www.einstein.yu.edu\"></iframe>");
		panel.setContent(content);
		String script = "Ext.define('Summary',{ extend: 'Ext.data.Model', fields: [ 'Strategy', 'Description', 'Workflow', 'Software', 'Status' ] }); var store = Ext.create('Ext.data.Store', { model: 'Summary', data : [{Strategy: '"+strategy.getDisplayStrategy()+"', Description: '"+strategy.getDescription()+"', Workflow: '"+job.getWorkflow().getName()+"', Software: '" + softwareName+"', Status: '"+jobStatus.toString()+"'}] }); Ext.create('Ext.grid.Panel', { id:'summary-panel', store: store,  columns: [ {text: \"Strategyee\", width:150, dataIndex: 'Strategy'}, {text: \"Description\", flex:1, dataIndex: 'Description'}, {text: \"Workflow\", width: 150, dataIndex: 'Workflow'}, {text: \"Main Software\", width: 200, dataIndex: 'Software'}, {text: \"Status\", width: 150, dataIndex: 'Status'} ], renderTo:'summary-grid', height:500 }); ";
		//String script = dataModel + " var store = Ext.create('Ext.data.Store', { model: 'Summary', data : [{Strategy: '"+strategy.getDisplayStrategy()+"', Description: '"+strategy.getDescription()+"', Workflow: '"+job.getWorkflow().getName()+"', Software: '" + softwareName+"', Status: '"+jobStatus.toString()+"'}] }); Ext.create('Ext.grid.Panel', { id:'summary-panel', store: store,  columns: [ {text: \"Strategy\", width:150, dataIndex: 'Strategy'}, {text: \"Description\", flex:1, dataIndex: 'Description'}, {text: \"Workflow\", width: 150, dataIndex: 'Workflow'}, {text: \"Main Software\", width: 200, dataIndex: 'Software'}, {text: \"Status\", width: 150, dataIndex: 'Status'} ], renderTo:'summary-grid', height:500 }); ";
		//String script = dataModel+" "+dataStore+" "+"Ext.create('Ext.grid.Panel', { id:'summary-panel', store: store,  columns: [ {text: \"Strategy\", width:150, dataIndex: 'Strategy'}, {text: \"Description\", flex:1, dataIndex: 'Description'}, {text: \"Workflow\", width: 150, dataIndex: 'Workflow'}, {text: \"Main Software\", width: 200, dataIndex: 'Software'}, {text: \"Status\", width: 150, dataIndex: 'Status'} ], renderTo:'summary-grid', height:500 }); ";
				
		//both these work (but throw firebug exception)
		//String script = "var panel = Ext.create('Ext.form.Panel', { renderTo: 'summary-grid', title: 'I am a Panel', html: 'This is not an anchor<br /> but this is: <a href=\"http://www.einstein.yu.edu\" >This is the einstein anchor</a><br /><br /> and the next line should be an iframe:<br /><iframe id=\"reportframe\" width=\"100%\" src=\"http://www.einstein.yu.edu\"></iframe><br />do you see the iframe?'}); "; 
		//String script = "var panel = Ext.create('Ext.panel.Panel', { renderTo: 'summary-grid', title: 'I am a Panel.Panel', html: 'This is not an anchor<br /> but this is: <a href=\"http://www.einstein.yu.edu\" >This is the einstein anchor to view the medical school home page</a><br /><br /> and the next line should be an iframe:<br /><iframe id=\"reportframe\" width=\"100%\" src=\"http://www.einstein.yu.edu\"></iframe><br />do you see the iframe?'}); "; 
		
		logger.debug("***********getSummaryPanelTab script: "+script);
		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode("var theDiv = $('summary-grid'); Ext.getCmp('summary-panel').setSize(theDiv.offsetWidth, undefined);");
		//panel.setExecOnResizeCode("Ext.getCmp('summary-panel').setSize(this.width, undefined);");
		panel.setExecOnResizeCode("var theDiv = $('summary-grid'); Ext.getCmp('summary-panel').setSize(theDiv.offsetWidth, undefined);");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		panelTab.setNumberOfColumns(1);

		return panelTab;
	}
	private static String createGridPanel(String name, Map<String,String> fieldDataMap, Map<String,Map<String,String>> fieldMap){
		if(name==null || name.isEmpty() || fieldDataMap==null || fieldDataMap.isEmpty()){
			return null;
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		//Ext.create('Ext.grid.Panel', { id:'summary-panel', store: store,  columns: [ {text: \"Strategy\", width:150, dataIndex: 'Strategy'}, {text: \"Description\", flex:1, dataIndex: 'Description'}, {text: \"Workflow\", width: 150, dataIndex: 'Workflow'}, {text: \"Main Software\", width: 200, dataIndex: 'Software'}, {text: \"Status\", width: 150, dataIndex: 'Status'} ], renderTo:'summary-grid', height:500 }); ";		
		stringBuffer.append("Ext.create('Ext.grid.Panel', { id:'summary-panel', store: store,  columns: [ ");
		int counter = 0;
		for(String key : fieldMap.keySet()){//insert order guaranteed as this is a LinkedHashMap
			if(counter++ > 0){stringBuffer.append(",");}
			Map<String, String> internalMap = fieldMap.get(key);
			if(internalMap==null||internalMap.isEmpty()){
				return null;
			}
			
			stringBuffer.append(" " +key+": '"+internalMap.get("data")+"'");
		}
		stringBuffer.append(" }]});");
		logger.debug("***********createGridPanel: "+stringBuffer.toString());		
		
		return new String(stringBuffer);
	}
	
	private static String createDataStore(String name, Map<String,String> fieldDataMap, Map<String,Map<String,String>> fieldMap){
		if(name==null || name.isEmpty() || fieldDataMap==null || fieldDataMap.isEmpty()){
			return null;
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		// var store = Ext.create('Ext.data.Store', { model: 'Summary', data : [{Strategy: '"+strategy.getDisplayStrategy()+"', Description: '"+strategy.getDescription()+"', Workflow: '"+job.getWorkflow().getName()+"', Software: '" + softwareName+"', Status: '"+jobStatus.toString()+"'}] });
		stringBuffer.append("var store = Ext.create('Ext.data.Store', { model: '"+name+"', data : [{ ");
		int counter = 0;
		//for(String key : fieldDataMap.keySet()){//insert order guaranteed as this is a LinkedHashMap
		//	if(counter++ > 0){stringBuffer.append(",");}
		//	stringBuffer.append(" " +key+": '"+fieldDataMap.get(key)+"'");
		//}
		for(String key : fieldMap.keySet()){//insert order guaranteed as this is a LinkedHashMap
			if(counter++ > 0){stringBuffer.append(",");}
			Map<String, String> internalMap = fieldMap.get(key);
			if(internalMap==null||internalMap.isEmpty()||!internalMap.containsKey("data")){
				return null;
			}
			stringBuffer.append(" " +key+": '"+internalMap.get("data")+"'");
		}
		stringBuffer.append(" }]});");
		logger.debug("***********createStore via fieldMap: "+stringBuffer.toString());		
		
		return new String(stringBuffer);
	}
	private static String defineDataModel(String name, Map<String,String> fieldDataMap, Map<String,Map<String,String>> fieldMap){
		if(name==null || name.isEmpty() || fieldDataMap==null || fieldDataMap.isEmpty()){
			return null;
		}
		StringBuffer stringBuffer = new StringBuffer();
		//"Ext.define('Summary',{ extend: 'Ext.data.Model', fields: [ 'Strategy', 'Description', 'Workflow', 'Software', 'Status' ] });"
		stringBuffer.append("Ext.define('"+name+"', {extend: 'Ext.data.Model', fields: [ ");
		int counter = 0;
		//for(String key : fieldDataMap.keySet()){//insert order guaranteed as this is a LinkedHashMap
		for(String key : fieldMap.keySet()){//insert order guaranteed as this is a LinkedHashMap
			if(counter++ > 0){stringBuffer.append(",");}
			stringBuffer.append("'"+key+"'");
		}
		stringBuffer.append(" ]});");
		logger.debug("***********createModel via fieldMap: "+stringBuffer.toString());
		return new String(stringBuffer);
	}
	
	public static PanelTab getSamplePairsPanelTab(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, Map<String,String> sampleIdControlIdCommandLineMap){
		
		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("Sample Pairs");
		//panelTab.setDescription("testDescription");
		WebPanel panel = new WebPanel();
		panel.setTitle("Sample Pairs Used For Aggregate Analysis");
		panel.setDescription("Sample Pairs Used For Aggregate Analysis");
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
				String command = sampleIdControlIdCommandLineMap.get(testSample.getId().toString()+"::"+controlSample.getId().toString());
				if(command==null || command.isEmpty()){
					command = " ";
				}
				else{
					command = command.replaceAll("\\n", "");//the workunit may put a newline at the end, which is incompatible with Extjs grids
				}
				stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"', Command: '"+command+"'}");
			}
		}
		String theData = new String(stringBuffer);
		String script = "Ext.define('SamplePairs',{ extend: 'Ext.data.Model', fields: [ 'TestSample', 'ControlSample', 'Command' ] }); var store = Ext.create('Ext.data.Store', { model: 'SamplePairs', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Test Sample\",  width:250, dataIndex: 'TestSample'}, {text: \"Control Sample\",  width:250, dataIndex: 'ControlSample'}, {text: \"Command Line\",  width: 2000, dataIndex: 'Command'} ], renderTo:'samplePairs-grid', height: 300 });";
		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		panelTab.setNumberOfColumns(1);

		return panelTab;
	}

	public static PanelTab getSampleLibraryRunsPanelTab(List<Sample> testSampleList, Map<Sample, List<Sample>> sampleLibraryListMap, Map<Sample, List<String>> libraryRunInfoListMap){
		
		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("Runs");
		//panelTab.setDescription("testDescription");
		WebPanel panel = new WebPanel();
		panel.setTitle("Runs Used For Aggregate Analysis");
		panel.setDescription("Runs Used For Aggregate Analysis");
		panel.setResizable(true);
		panel.setMaximizable(true);	

		panel.setOrder(1);
		WebContent content = new WebContent();
		content.setHtmlCode("<div id=\"sampleLibraryRuns-grid\"></div>");
		panel.setContent(content);
		/* for testing only
		String string1 = "the_test_sample";
		String string2 = "the run info";
		String script = "Ext.define('SampleRuns',{ extend: 'Ext.data.Model', fields: [ 'Sample', 'Run' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleRuns', data : [{Sample: '"+string1+"', Run: '"+string2+"'}, {Sample: '"+string1+"', Run: '"+string2+"'}] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Sample\",  width:300, dataIndex: 'Sample'}, {text: \"Run\",  flex: 1, dataIndex: 'Run'} ], renderTo:'sampleRuns-grid', height: 300 });";
		*/
		StringBuffer stringBuffer = new StringBuffer();
		for(Sample testSample : testSampleList){
			List<Sample> libraryList = sampleLibraryListMap.get(testSample);
			for(Sample library : libraryList){
				if(stringBuffer.length()>0){
					stringBuffer.append(", ");
				}
				List<String> runInfoList = libraryRunInfoListMap.get(library);
				StringBuffer completeRunInfoAsStringBuffer = new StringBuffer();
				for(String runInfo: runInfoList){
					if(completeRunInfoAsStringBuffer.length() > 0){
						completeRunInfoAsStringBuffer.append("<br />");
					}
					completeRunInfoAsStringBuffer.append(runInfo);
				}
				String completeRunInfoAsString = new String(completeRunInfoAsStringBuffer);
				stringBuffer.append("{Sample: '"+testSample.getName()+"', Library: '"+library.getName()+"', Runs: '"+completeRunInfoAsString+"'}");			
			}
		}
		String theData = new String(stringBuffer);
		String script = "Ext.define('SampleLibraryRuns',{ extend: 'Ext.data.Model', fields: [ 'Sample', 'Library', 'Runs' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleLibraryRuns', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Submitted Sample\",  width:250, dataIndex: 'Sample'}, {text: \"Library\",  width:250, dataIndex: 'Library'}, {text: \"Runs\",  flex: 1, dataIndex: 'Runs'} ], renderTo:'sampleLibraryRuns-grid', height: 300 });";
		
		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		panelTab.setNumberOfColumns(1);

		return panelTab;
	}
	public static PanelTab getFileTypeDefinitionsPanelTab(List<FileType> fileTypeList){
		
		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("FileTypes");
		//panelTab.setDescription("testDescription");
		WebPanel panel = new WebPanel();
		panel.setTitle("FileTypes Generated By Aggregate Analysis");
		panel.setDescription("FileTypes Generated By Aggregate Analysis");
		panel.setResizable(true);
		panel.setMaximizable(true);	

		panel.setOrder(1);
		WebContent content = new WebContent();
		content.setHtmlCode("<head><style type=\"text/css\">.multilineColumn .x-grid-cell-inner {white-space: normal;}.x-grid-row-over .x-grid-cell-inner { font-weight: bold; white-space: normal;}</style></head>" +
				"<div id=\"fileTypeDescription-grid\"></div>");
		//content.setHtmlCode("<div id=\"fileTypeDescription-grid\"></div>");
		panel.setContent(content);
		/* for testing only
		String string1 = "the_test_sample_filepage";
		String string2 = "control_SAMPLE_filepage";
		String script = "Ext.define('SamplePairs',{ extend: 'Ext.data.Model', fields: [ 'TestSample', 'ControlSample' ] }); var store = Ext.create('Ext.data.Store', { model: 'SamplePairs', data : [{TestSample: '"+string1+"', ControlSample: '"+string2+"'}, {TestSample: '"+string1+"', ControlSample: '"+string2+"'}] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Test Sample\",  width:300, dataIndex: 'TestSample'}, {text: \"Control Sample\",  flex: 1, dataIndex: 'ControlSample'} ], renderTo:'fileTypeDescription-grid', height: 300 });";
		*/
		
		StringBuffer stringBuffer = new StringBuffer();
		for(FileType fileType : fileTypeList){
			if(stringBuffer.length()>0){
				stringBuffer.append(", ");
			}
			stringBuffer.append("{FileType: '"+fileType.getName()+"', Description: '"+fileType.getDescription()+"'}");
		}
		String theData = new String(stringBuffer);
		
		//shows the entire line only when mouse rollover
		//String script = "Ext.define('FileTypeDescriptions',{ extend: 'Ext.data.Model', fields: [ 'FileType', 'Description', ] }); var store = Ext.create('Ext.data.Store', { model: 'FileTypeDescriptions', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"File Type\",  width:200, dataIndex: 'FileType'}, {text: \"Description\",  flex:1, dataIndex: 'Description', renderer: function(value, metaData, record, rowIndex, colIndex, store) { metaData.css = 'multilineColumn'; return value;}     } ], renderTo:'fileTypeDescription-grid', height: 300 });";
		
		//shows the entire line at all times
		String script = "Ext.define('FileTypeDescriptions',{ extend: 'Ext.data.Model', fields: [ 'FileType', 'Description', ] }); var store = Ext.create('Ext.data.Store', { model: 'FileTypeDescriptions', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"File Type\",  width:200, dataIndex: 'FileType'}, {text: \"Description\",  flex:1, dataIndex: 'Description' } ], renderTo:'fileTypeDescription-grid', height: 300 });";

		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		panelTab.setNumberOfColumns(1);

		return panelTab;
	}
	public static PanelTab getFilePanelTab(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, FileType fileType, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){

		PanelTab panelTab = new PanelTab();
		
		panelTab.setName(fileType.getName());
		//panelTab.setDescription("testDescription");
		WebPanel panel = new WebPanel();
		panel.setTitle(fileType.getName());
		panel.setDescription(fileType.getName());
		panel.setResizable(true);
		panel.setMaximizable(true);	

		panel.setOrder(1);
		WebContent content = new WebContent();
		content.setHtmlCode("<div id=\""+fileType.getIName()+"-grid\"></div>");
		panel.setContent(content);
		/* for testing only
		String string1 = "the_test_sample_filepage";
		String string2 = "control_SAMPLE_filepage";
		String script = "Ext.define('SamplePairs',{ extend: 'Ext.data.Model', fields: [ 'TestSample', 'ControlSample' ] }); var store = Ext.create('Ext.data.Store', { model: 'SamplePairs', data : [{TestSample: '"+string1+"', ControlSample: '"+string2+"'}, {TestSample: '"+string1+"', ControlSample: '"+string2+"'}] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Test Sample\",  width:300, dataIndex: 'TestSample'}, {text: \"Control Sample\",  flex: 1, dataIndex: 'ControlSample'} ], renderTo:'"+fileType.getIName()+"-grid', height: 300 });";
		*/
		
		StringBuffer stringBuffer = new StringBuffer();
		for(Sample testSample : testSampleList){
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			for(Sample controlSample : controlSampleList){
				if(stringBuffer.length()>0){
					stringBuffer.append(", ");
				}
				FileHandle fileHandle = sampleIdControlIdFileTypeIdFileHandleMap.get(testSample.getId().toString() + "::" + controlSample.getId().toString() + "::" + fileType.getId().toString());
				String resolvedURL = fileHandleResolvedURLMap.get(fileHandle);
				if(fileHandle==null){
					stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"', File: 'no file', MD5: ' ', Download: ' '}");
				}
				else{
					//stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"', File: '"+fileHandle.getFileName()+"', MD5: '"+fileHandle.getMd5hash()+"', Download: '"+"<a href=\""+resolvedURL+"\"><img src=\"ext/images/icons/fam/disk.png\" /></a>"+"'}");
					stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"', File: '"+fileHandle.getFileName()+"', MD5: '"+fileHandle.getMd5hash()+"', Download: '"+resolvedURL+"'}");
				}
			}
		}
		String theData = new String(stringBuffer);
		
		String script = "Ext.require(['Ext.grid.*','Ext.data.*','Ext.form.field.Number','Ext.form.field.Date','Ext.tip.QuickTipManager','Ext.selection.CheckboxModel','Wasp.RowActions']); Ext.tip.QuickTipManager.init(); Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'TestSample', 'ControlSample', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Test Sample\",  width:110, dataIndex: 'TestSample'}, {text: \"Control Sample\",  width:110, dataIndex: 'ControlSample'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:100, dataIndex: 'MD5', renderer: function(val, meta, record){var tip = record.get('MD5'); meta.tdAttr = 'data-qtip=\"' + tip + '\"'; return val; } }, {header:\"Download\", width: 500, xtype: 'rowactions', actions: [{iconCls: 'icon-clear-group', qtip: 'Download', callback: function(grip, record, action, idx, col, e, target){window.location=record.get('Download');}}], keepSelection: true     }, ], renderTo:'"+fileType.getIName()+"-grid', height: 300 });";
		
		
		//String script = "Ext.require(['Ext.grid.*','Ext.data.*','Ext.form.field.Number','Ext.form.field.Date','Ext.tip.QuickTipManager','Ext.selection.CheckboxModel','Wasp.RowActions']); Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'TestSample', 'ControlSample', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Test Sample\",  width:110, dataIndex: 'TestSample'}, {text: \"Control Sample\",  width:110, dataIndex: 'ControlSample'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:100, dataIndex: 'MD5'}, {text: \"Download\",  width:100, dataIndex: 'Download'}, ], renderTo:'"+fileType.getIName()+"-grid', height: 300 });";
		
		//String script = "Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'TestSample', 'ControlSample', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Test Sample\",  width:110, dataIndex: 'TestSample'}, {text: \"Control Sample\",  width:110, dataIndex: 'ControlSample'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:100, dataIndex: 'MD5'}, {text: \"Download\",  width:100, dataIndex: 'Download',  renderer: function(val, meta, record){return '<a href=\"'+val+'\">robert dubin</a>';} }, ], renderTo:'"+fileType.getIName()+"-grid', height: 300 });";
		
		//String script = "Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'TestSample', 'ControlSample', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Test Sample\",  width:110, dataIndex: 'TestSample'}, {text: \"Control Sample\",  width:110, dataIndex: 'ControlSample'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:100, dataIndex: 'MD5'}, {text: \"Download\",  width:100, dataIndex: 'Download', renderer: function(val, meta, record){meta.tdCls = 'icon-clear-group'} }, ], renderTo:'"+fileType.getIName()+"-grid', height: 300 });";
		//String script = "Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'TestSample', 'ControlSample', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Test Sample\",  width:110, dataIndex: 'TestSample'}, {text: \"Control Sample\",  width:110, dataIndex: 'ControlSample'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:100, dataIndex: 'MD5'}, {text: \"Download\",  width:100, dataIndex: 'Download', renderer: function(value){return Ext.String.format('<a href=\"{0}\">lesliedownload</a>', value);} }, ], renderTo:'"+fileType.getIName()+"-grid', height: 300 });";
		
		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		panelTab.setNumberOfColumns(1);

		
		
		
		int order = 2;
		if(fileType.getName().equalsIgnoreCase("modelpdf")){
			for(Sample testSample : testSampleList){
				List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
				for(Sample controlSample : controlSampleList){
					FileHandle fileHandle = sampleIdControlIdFileTypeIdFileHandleMap.get(testSample.getId().toString() + "::" + controlSample.getId().toString() + "::" + fileType.getId().toString());
					if(fileHandle!=null){
						WebPanel panel3 = new WebPanel();
						panel3.setTitle(fileHandle.getFileName());
						panel3.setDescription(fileHandle.getFileName());
						panel3.setResizable(true);
						panel3.setMaximizable(true);	
						panel3.setOrder(order++);
						WebContent content3 = new WebContent();
						//content3.setHtmlCode("<iframe  width=\"100%\" src=\"http://www.einstein.yu.edu\"></iframe>");
						//content3.setHtmlCode("<iframe height=\"100%\" width=\"100%\" src=\"http://localhost:8080/wasp/images/dubinModel.pdf\"></iframe>");
						//content3.setHtmlCode("<iframe  height=\"100%\" width=\"100%\" src=\"/wasp/images/dubinModel.pdf\"></iframe>");
						content3.setHtmlCode("<iframe  height=\"100%\" width=\"100%\" src=\"/wasp/file/fileHandle/1159/view.do\"></iframe>");
						
						panel3.setContent(content3);
						panelTab.addPanel(panel3);	
					}
				}
			}
		}
		
		
		
		
		return panelTab;
	}
	
	public static PanelTab getAllFilesDisplayedBySampleUsingGroupingGridPanelTab(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, List<FileType> fileTypeList, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){

		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("Files By Sample Pairs");
		panelTab.setNumberOfColumns(1);
		//panelTab.setDescription("testDescription");
		
		WebPanel panel = new WebPanel();
		panel.setTitle("Files By Sample Pairs");
		///panel.setDescription("Files By Sample");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		WebContent content = new WebContent();
		String divId = "filesGroupedBySamplePairs-grid";				
		content.setHtmlCode("<div id=\""+divId+"\"></div>");
		panel.setContent(content);
		
		StringBuffer stringBuffer = new StringBuffer();
		for(Sample testSample : testSampleList){
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			for(Sample controlSample : controlSampleList){
				
				String samplePairs = "Test Sample: " + testSample.getName() + "; Control: " + controlSample.getName();

				
				for(FileType fileType : fileTypeList){
					if(stringBuffer.length()>0){
						stringBuffer.append(", ");
					}
					FileHandle fileHandle = sampleIdControlIdFileTypeIdFileHandleMap.get(testSample.getId().toString() + "::" + controlSample.getId().toString() + "::" + fileType.getId().toString());
					String resolvedURL = fileHandleResolvedURLMap.get(fileHandle);
					//if(fileHandle==null){
					//	stringBuffer.append("{FileType: '"+fileType.getName()+"', File: 'no file', MD5: ' ', Download: ' '}");
					//}
					//else{
						//stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"', File: '"+fileHandle.getFileName()+"', MD5: '"+fileHandle.getMd5hash()+"', Download: '"+"<a href=\""+resolvedURL+"\"><img src=\"ext/images/icons/fam/disk.png\" /></a>"+"'}");
						stringBuffer.append("{SamplePairs: '"+samplePairs+"', FileType: '"+fileType.getName()+"', File: '"+fileHandle.getFileName()+"', MD5: '"+fileHandle.getMd5hash()+"', Download: '"+resolvedURL+"'}");
					//}
				}
				
				
			}
		}
		String theData = new String(stringBuffer);
		
		//normal grid
		//String script = "Ext.require(['Ext.grid.*','Ext.data.*','Ext.form.field.Number','Ext.form.field.Date','Ext.tip.QuickTipManager','Ext.selection.CheckboxModel','Wasp.RowActions']); Ext.tip.QuickTipManager.init(); Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'SamplePairs', 'FileType', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Sample Pairs\",  width:150, dataIndex: 'SamplePairs'},{text: \"FileType\",  width:150, dataIndex: 'FileType'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:270, dataIndex: 'MD5', renderer: function(val, meta, record){var tip = record.get('MD5'); meta.tdAttr = 'data-qtip=\"' + tip + '\"'; return val; } }, {header:\" \", width: 100, xtype: 'rowactions', actions: [{iconCls: 'icon-clear-group', qtip: 'Download', callback: function(grip, record, action, idx, col, e, target){window.location=record.get('Download');}}], keepSelection: true     }, ], renderTo:'"+divId+"', height: 300 });";
		
		//grouping grid test with all fields
		//String script = "Ext.require(['Ext.grid.*','Ext.data.*','Ext.form.field.Number','Ext.form.field.Date','Ext.tip.QuickTipManager','Ext.selection.CheckboxModel','Wasp.RowActions']); Ext.tip.QuickTipManager.init(); Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'SamplePairs', 'FileType', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile',  groupField: 'SamplePairs', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Sample Pairs\",  width:150, dataIndex: 'SamplePairs'},{text: \"FileType\",  width:150, dataIndex: 'FileType'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:270, dataIndex: 'MD5', renderer: function(val, meta, record){var tip = record.get('MD5'); meta.tdAttr = 'data-qtip=\"' + tip + '\"'; return val; } }, {header:\" \", width: 100, xtype: 'rowactions', actions: [{iconCls: 'icon-clear-group', qtip: 'Download', callback: function(grip, record, action, idx, col, e, target){window.location=record.get('Download');}}], keepSelection: true     }, ],  features: [{ftype: 'grouping' }], renderTo:'"+divId+"', height: 300 });";
		
		//group grid with the goupedField not displayed on every row
		String script = "Ext.require(['Ext.grid.*','Ext.data.*','Ext.form.field.Number','Ext.form.field.Date','Ext.tip.QuickTipManager','Ext.selection.CheckboxModel','Wasp.RowActions']); Ext.tip.QuickTipManager.init(); Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'SamplePairs', 'FileType', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile',  groupField: 'SamplePairs', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"File Type\",  width:150, dataIndex: 'FileType'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:270, dataIndex: 'MD5', renderer: function(val, meta, record){var tip = record.get('MD5'); meta.tdAttr = 'data-qtip=\"' + tip + '\"'; return val; } }, {header:\" \", width: 100, xtype: 'rowactions', actions: [{iconCls: 'icon-clear-group', qtip: 'Download', callback: function(grip, record, action, idx, col, e, target){window.location=record.get('Download');}}], keepSelection: true     }, ],  features: [{ftype: 'grouping', groupHeaderTpl: 'File Type: {name}' }], renderTo:'"+divId+"', height: 300 });";
		
		//example from web; it works fine
		//String script = "var store = Ext.create('Ext.data.Store', {        storeId: 'employeeStore',        fields: ['name', 'seniority', 'department'],        groupField: 'department',        data: {            'employees': [{                \"name\": \"Michael Scott\",                \"seniority\": 7,                \"department\": \"Management\"            }, {                \"name\": \"Dwight Schrute\",                \"seniority\": 2,               \"department\": \"Sales\"            }, {                \"name\": \"Jim Halpert\",               \"seniority\": 3,                \"department\": \"Sales\"            }, {                \"name\": \"Kevin Malone\",                \"seniority\": 4,                \"department\": \"Accounting\"            }, {                \"name\": \"Angela Martin\",                \"seniority\": 5,                \"department\": \"Accounting\"            }]        },        proxy: {            type: 'memory',            reader: {                type: 'json',                root: 'employees'            }        }    });    Ext.create('Ext.grid.Panel', {        title: 'Employees',        store: Ext.data.StoreManager.lookup('employeeStore'),        columns: [{            text: 'Name',            dataIndex: 'name'        }, {            text: 'Seniority',            dataIndex: 'seniority'        }],        features: [{            ftype: 'grouping'        }],        width: 200,        height: 275,        renderTo:'"+divId+"'    });";
		
		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		return panelTab;
	}
	
	public static PanelTab getAllFilesDisplayedByFileTypeUsingGroupingGridPanelTab(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, List<FileType> fileTypeList, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){

		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("Files By File Type");
		panelTab.setNumberOfColumns(1);
		//panelTab.setDescription("testDescription");
		
		WebPanel panel = new WebPanel();
		panel.setTitle("Files By File Type");
		///panel.setDescription("Files By Sample");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		WebContent content = new WebContent();
		String divId = "filesGroupedByFileType-grid";				
		content.setHtmlCode("<div id=\""+divId+"\"></div>");
		panel.setContent(content);
		
		StringBuffer stringBuffer = new StringBuffer();
		for(Sample testSample : testSampleList){
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			for(Sample controlSample : controlSampleList){
				
				String samplePairs = "Test Sample: " + testSample.getName() + "; Control: " + controlSample.getName();

				
				for(FileType fileType : fileTypeList){
					if(stringBuffer.length()>0){
						stringBuffer.append(", ");
					}
					FileHandle fileHandle = sampleIdControlIdFileTypeIdFileHandleMap.get(testSample.getId().toString() + "::" + controlSample.getId().toString() + "::" + fileType.getId().toString());
					String resolvedURL = fileHandleResolvedURLMap.get(fileHandle);
					//if(fileHandle==null){
					//	stringBuffer.append("{FileType: '"+fileType.getName()+"', File: 'no file', MD5: ' ', Download: ' '}");
					//}
					//else{
						//stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"', File: '"+fileHandle.getFileName()+"', MD5: '"+fileHandle.getMd5hash()+"', Download: '"+"<a href=\""+resolvedURL+"\"><img src=\"ext/images/icons/fam/disk.png\" /></a>"+"'}");
						stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"', SamplePairs: '"+samplePairs+"', FileType: '"+fileType.getName()+"', File: '"+fileHandle.getFileName()+"', MD5: '"+fileHandle.getMd5hash()+"', Download: '"+resolvedURL+"'}");
					//}
				}
				
				
			}
		}
		String theData = new String(stringBuffer);
		
		//normal grid
		//String script = "Ext.require(['Ext.grid.*','Ext.data.*','Ext.form.field.Number','Ext.form.field.Date','Ext.tip.QuickTipManager','Ext.selection.CheckboxModel','Wasp.RowActions']); Ext.tip.QuickTipManager.init(); Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'SamplePairs', 'FileType', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Sample Pairs\",  width:150, dataIndex: 'SamplePairs'},{text: \"FileType\",  width:150, dataIndex: 'FileType'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:270, dataIndex: 'MD5', renderer: function(val, meta, record){var tip = record.get('MD5'); meta.tdAttr = 'data-qtip=\"' + tip + '\"'; return val; } }, {header:\" \", width: 100, xtype: 'rowactions', actions: [{iconCls: 'icon-clear-group', qtip: 'Download', callback: function(grip, record, action, idx, col, e, target){window.location=record.get('Download');}}], keepSelection: true     }, ], renderTo:'"+divId+"', height: 300 });";
		
		//grouping grid test with all fields
		//String script = "Ext.require(['Ext.grid.*','Ext.data.*','Ext.form.field.Number','Ext.form.field.Date','Ext.tip.QuickTipManager','Ext.selection.CheckboxModel','Wasp.RowActions']); Ext.tip.QuickTipManager.init(); Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'SamplePairs', 'FileType', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile',  groupField: 'SamplePairs', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Sample Pairs\",  width:150, dataIndex: 'SamplePairs'},{text: \"FileType\",  width:150, dataIndex: 'FileType'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:270, dataIndex: 'MD5', renderer: function(val, meta, record){var tip = record.get('MD5'); meta.tdAttr = 'data-qtip=\"' + tip + '\"'; return val; } }, {header:\" \", width: 100, xtype: 'rowactions', actions: [{iconCls: 'icon-clear-group', qtip: 'Download', callback: function(grip, record, action, idx, col, e, target){window.location=record.get('Download');}}], keepSelection: true     }, ],  features: [{ftype: 'grouping' }], renderTo:'"+divId+"', height: 300 });";
		
		//group grid with the goupedField not displayed on every row
		String script = "Ext.require(['Ext.grid.*','Ext.data.*','Ext.form.field.Number','Ext.form.field.Date','Ext.tip.QuickTipManager','Ext.selection.CheckboxModel','Wasp.RowActions']); Ext.tip.QuickTipManager.init(); Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'TestSample', 'ControlSample', 'SamplePairs', 'FileType', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile',  groupField: 'FileType', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Test Sample\",  width:150, dataIndex: 'TestSample'}, {text: \"Control Sample\",  width:150, dataIndex: 'ControlSample'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:270, dataIndex: 'MD5', renderer: function(val, meta, record){var tip = record.get('MD5'); meta.tdAttr = 'data-qtip=\"' + tip + '\"'; return val; } }, {header:\" \", width: 100, xtype: 'rowactions', actions: [{iconCls: 'icon-clear-group', qtip: 'Download', callback: function(grip, record, action, idx, col, e, target){window.location=record.get('Download');}}], keepSelection: true     }, ],  features: [{ftype: 'grouping', groupHeaderTpl: 'File Type: {name}' }], renderTo:'"+divId+"', height: 300 });";
		
		//example from web; it works fine
		//String script = "var store = Ext.create('Ext.data.Store', {        storeId: 'employeeStore',        fields: ['name', 'seniority', 'department'],        groupField: 'department',        data: {            'employees': [{                \"name\": \"Michael Scott\",                \"seniority\": 7,                \"department\": \"Management\"            }, {                \"name\": \"Dwight Schrute\",                \"seniority\": 2,               \"department\": \"Sales\"            }, {                \"name\": \"Jim Halpert\",               \"seniority\": 3,                \"department\": \"Sales\"            }, {                \"name\": \"Kevin Malone\",                \"seniority\": 4,                \"department\": \"Accounting\"            }, {                \"name\": \"Angela Martin\",                \"seniority\": 5,                \"department\": \"Accounting\"            }]        },        proxy: {            type: 'memory',            reader: {                type: 'json',                root: 'employees'            }        }    });    Ext.create('Ext.grid.Panel', {        title: 'Employees',        store: Ext.data.StoreManager.lookup('employeeStore'),        columns: [{            text: 'Name',            dataIndex: 'name'        }, {            text: 'Seniority',            dataIndex: 'seniority'        }],        features: [{            ftype: 'grouping'        }],        width: 200,        height: 275,        renderTo:'"+divId+"'    });";
		
		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);
		panelTab.addPanel(panel);
		return panelTab;
	}
	
	
	public static PanelTab getAllFilesDisplayedBySamplePanelTab(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, List<FileType> fileTypeList, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){

		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("Files By Sample");
		panelTab.setNumberOfColumns(1);
		//panelTab.setDescription("testDescription");
		/*
		WebPanel panel = new WebPanel();
		panel.setTitle("Files By Sample");
		panel.setDescription("Files By Sample");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		WebContent content = new WebContent();
		content.setHtmlCode("This is test webpanel of Files By Sample");
		panel.setContent(content);
		panelTab.addPanel(panel);
		*/
		int order = 1;
		for(Sample testSample : testSampleList){
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			for(Sample controlSample : controlSampleList){
				WebPanel panel = new WebPanel();
				panel.setTitle("Test Sample: " + testSample.getName() + "; Control: " + controlSample.getName());
				///panel.setDescription("Files By Sample");
				panel.setResizable(true);
				panel.setMaximizable(true);	
				panel.setOrder(order++);
				WebContent content = new WebContent();
				String divId = "allFilesBySample_" + testSample.getId() + "_" + controlSample.getId() + "-grid";				
				content.setHtmlCode("<div id=\""+divId+"\"></div>");
				panel.setContent(content);
				
				StringBuffer stringBuffer = new StringBuffer();
				for(FileType fileType : fileTypeList){
					if(stringBuffer.length()>0){
						stringBuffer.append(", ");
					}
					FileHandle fileHandle = sampleIdControlIdFileTypeIdFileHandleMap.get(testSample.getId().toString() + "::" + controlSample.getId().toString() + "::" + fileType.getId().toString());
					String resolvedURL = fileHandleResolvedURLMap.get(fileHandle);
					if(fileHandle==null){
						stringBuffer.append("{FileType: '"+fileType.getName()+"', File: 'no file', MD5: ' ', Download: ' '}");
					}
					else{
						//stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"', File: '"+fileHandle.getFileName()+"', MD5: '"+fileHandle.getMd5hash()+"', Download: '"+"<a href=\""+resolvedURL+"\"><img src=\"ext/images/icons/fam/disk.png\" /></a>"+"'}");
						stringBuffer.append("{FileType: '"+fileType.getName()+"', File: '"+fileHandle.getFileName()+"', MD5: '"+fileHandle.getMd5hash()+"', Download: '"+resolvedURL+"'}");
					}
				}
				
				String theData = new String(stringBuffer);
				
				String script = "Ext.require(['Ext.grid.*','Ext.data.*','Ext.form.field.Number','Ext.form.field.Date','Ext.tip.QuickTipManager','Ext.selection.CheckboxModel','Wasp.RowActions']); Ext.tip.QuickTipManager.init(); Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'FileType', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"FileType\",  width:150, dataIndex: 'FileType'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:270, dataIndex: 'MD5', renderer: function(val, meta, record){var tip = record.get('MD5'); meta.tdAttr = 'data-qtip=\"' + tip + '\"'; return val; } }, {header:\" \", width: 100, xtype: 'rowactions', actions: [{iconCls: 'icon-clear-group', qtip: 'Download', callback: function(grip, record, action, idx, col, e, target){window.location=record.get('Download');}}], keepSelection: true     }, ], renderTo:'"+divId+"', height: 300 });";
				panel.setExecOnRenderCode(script);
				panel.setExecOnExpandCode(" ");
				panel.setExecOnResizeCode(" ");
				// does nothing: content.setScriptCode(script);
				panelTab.addPanel(panel);
			}
		}

		return panelTab;
	}
	
	
	public static PanelTab getAllFilesDisplayedByFileTypePanelTab(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, List<FileType> fileTypeList, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){

		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("Files By FileType");
		panelTab.setNumberOfColumns(1);
		//panelTab.setDescription("testDescription");
		/*
		WebPanel panel = new WebPanel();
		panel.setTitle("Files By Sample");
		panel.setDescription("Files By Sample");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		WebContent content = new WebContent();
		content.setHtmlCode("This is test webpanel of Files By Sample");
		panel.setContent(content);
		panelTab.addPanel(panel);
		*/
		int order = 1;
		for(FileType fileType : fileTypeList){
			
			WebPanel panel = new WebPanel();
			panel.setTitle("FileType: " + fileType.getName());
			///panel.setDescription("Files By Sample");
			panel.setResizable(true);
			panel.setMaximizable(true);	
			panel.setOrder(order++);
			WebContent content = new WebContent();
			String divId = "allFilesByFileType_" + fileType.getIName() + "-grid";				
			content.setHtmlCode("<div id=\""+divId+"\"></div>");
			panel.setContent(content);
			
			StringBuffer stringBuffer = new StringBuffer();
			
			for(Sample testSample : testSampleList){
				List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
				for(Sample controlSample : controlSampleList){
					if(stringBuffer.length()>0){
						stringBuffer.append(", ");
					}
					FileHandle fileHandle = sampleIdControlIdFileTypeIdFileHandleMap.get(testSample.getId().toString() + "::" + controlSample.getId().toString() + "::" + fileType.getId().toString());
					String resolvedURL = fileHandleResolvedURLMap.get(fileHandle);
					if(fileHandle==null){
						stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"', File: 'no file', MD5: ' ', Download: ' '}");
					}
					else{
						//stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"', File: '"+fileHandle.getFileName()+"', MD5: '"+fileHandle.getMd5hash()+"', Download: '"+"<a href=\""+resolvedURL+"\"><img src=\"ext/images/icons/fam/disk.png\" /></a>"+"'}");
						stringBuffer.append("{TestSample: '"+testSample.getName()+"', ControlSample: '"+controlSample.getName()+"', File: '"+fileHandle.getFileName()+"', MD5: '"+fileHandle.getMd5hash()+"', Download: '"+resolvedURL+"'}");
					}
				}
			}
			
			String theData = new String(stringBuffer);
			
			String script = "Ext.require(['Ext.grid.*','Ext.data.*','Ext.form.field.Number','Ext.form.field.Date','Ext.tip.QuickTipManager','Ext.selection.CheckboxModel','Wasp.RowActions']); Ext.tip.QuickTipManager.init(); Ext.define('SampleControlFile',{ extend: 'Ext.data.Model', fields: [ 'TestSample', 'ControlSample', 'File', 'MD5', 'Download' ] }); var store = Ext.create('Ext.data.Store', { model: 'SampleControlFile', data : ["+theData+"] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Test Sample\",  width:150, dataIndex: 'TestSample'}, {text: \"Control Sample\",  width:150, dataIndex: 'ControlSample'}, {text: \"File\",  flex: 1, dataIndex: 'File'}, {text: \"MD5\",  width:150, dataIndex: 'MD5', renderer: function(val, meta, record){var tip = record.get('MD5'); meta.tdAttr = 'data-qtip=\"' + tip + '\"'; return val; } }, {header:\" \", width: 500, xtype: 'rowactions', actions: [{iconCls: 'icon-clear-group', qtip: 'Download', callback: function(grip, record, action, idx, col, e, target){window.location=record.get('Download');}}], keepSelection: true     }, ], renderTo:'"+divId+"', height: 300 });";
			panel.setExecOnRenderCode(script);
			panel.setExecOnExpandCode(" ");
			panel.setExecOnResizeCode(" ");
			// does nothing: content.setScriptCode(script);
			panelTab.addPanel(panel);
		}
	
		return panelTab;
	}
	
	
	
	public static PanelTab getTestPanelTab(){
		
		PanelTab panelTab = new PanelTab();
		
		panelTab.setName("TestPanel");
		panelTab.setNumberOfColumns(1);
		//panelTab.setDescription("testDescription");
		WebPanel panel = new WebPanel();
		panel.setTitle("TestPanel");
		panel.setDescription("TestPanel");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		WebContent content = new WebContent();
		content.setHtmlCode("This is test webpanel 1");
		panel.setContent(content);
		panelTab.addPanel(panel);
		
		WebPanel panel2 = new WebPanel();
		panel2.setTitle("TestPanel2");
		panel2.setDescription("TestPanel2");
		panel2.setResizable(true);
		panel2.setMaximizable(true);	
		panel2.setOrder(2);
		WebContent content2 = new WebContent();
		//content2.setHtmlCode("<iframe  width=\"100%\" src=\"http://www.einstein.yu.edu\"></iframe>");
		content2.setHtmlCode("<iframe  height=\"90%\" width=\"90%\" src=\"http://localhost:8080/wasp/images/dubinModel.pdf\"></iframe>");
		//content2.setHtmlCode("<iframe width=\"100%\" src=\"/wasp/file/fileHandle/1158/view.do\"></iframe>");
		
		panel2.setContent(content2);
		panelTab.addPanel(panel2);		

		WebPanel panel3 = new WebPanel();
		panel3.setTitle("P3 - file coming from C3!!");
		panel3.setDescription("file coming from C3");
		panel3.setResizable(true);
		panel3.setMaximizable(true);	
		panel3.setOrder(3);//doesn't appear to do anything:  panel3.setHeight(10000);
		WebContent content3 = new WebContent();
		//content3.setHtmlCode("<iframe  width=\"100%\" src=\"http://www.einstein.yu.edu\"></iframe>");
		//content3.setHtmlCode("<iframe height=\"100%\" width=\"100%\" src=\"http://localhost:8080/wasp/images/dubinModel.pdf\"></iframe>");
		//content3.setHtmlCode("<iframe  height=\"100%\" width=\"100%\" src=\"/wasp/images/dubinModel.pdf\"></iframe>");
		content3.setHtmlCode("<iframe  height=\"90%\" width=\"90%\" src=\"/wasp/file/fileHandle/1159/view.do\"></iframe>");
		
		panel3.setContent(content3);
		panelTab.addPanel(panel3);		

		WebPanel panel4 = new WebPanel();
		panel4.setTitle("P4 - file coming from C4!!");
		panel4.setDescription("file coming from C4");
		panel4.setResizable(true);
		panel4.setMaximizable(true);	
		panel4.setOrder(4);//doesn't appear to do anything:  panel3.setHeight(10000);
		WebContent content4 = new WebContent();
		//content3.setHtmlCode("<iframe  width=\"100%\" src=\"http://www.einstein.yu.edu\"></iframe>");
		//content3.setHtmlCode("<iframe height=\"100%\" width=\"100%\" src=\"http://localhost:8080/wasp/images/dubinModel.pdf\"></iframe>");
		//content3.setHtmlCode("<iframe  height=\"100%\" width=\"100%\" src=\"/wasp/images/dubinModel.pdf\"></iframe>");
		content4.setHtmlCode("<table border=\"1\">" +
				"<tr><th>this is a header</th></tr>" +
				"<tr><td>this is a row</td></tr>" +
				"<tr><td>this is another row</td></tr>" +
				"<tr><td>this is and yet a third row</td></tr>" +
				"</table>");
		
		panel4.setContent(content4);
		panelTab.addPanel(panel4);

		WebPanel panel5 = new WebPanel();
		panel5.setTitle("TestPanel5");
		panel5.setDescription("TestPanel5");
		panel5.setResizable(true);
		panel5.setMaximizable(true);	
		panel5.setOrder(5);
		WebContent content5 = new WebContent();
		content5.setHtmlCode("<img  height=\"200\" width=\"200\" src=\"http://localhost:8080/wasp/images/dubinModel.pdf\" />");
		
		panel5.setContent(content5);
		panelTab.addPanel(panel5);		
/*would need ajax, then convert to image base 64 and put into the <img id
		WebPanel panel6 = new WebPanel();
		panel6.setTitle("TestPanel6");
		panel6.setDescription("TestPanel6");
		panel6.setResizable(true);
		panel6.setMaximizable(true);	
		panel6.setOrder(6);
		WebContent content6 = new WebContent();
		content5.setHtmlCode("<img  height=\"200\" width=\"200\" src=\"/wasp/file/fileHandle/1159/view.do\" />");
		
		panel6.setContent(content6);
		panelTab.addPanel(panel6);		
*/
		return panelTab;
	}
}
