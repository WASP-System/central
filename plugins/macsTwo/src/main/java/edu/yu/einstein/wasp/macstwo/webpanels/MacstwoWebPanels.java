package edu.yu.einstein.wasp.macstwo.webpanels;

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

public class MacstwoWebPanels {

	static protected  Logger logger = LoggerFactory.getLogger(WaspServiceImpl.class);	
/* SUMMARY is performed in chipSeq plugin or in core */	
	public static PanelTab getSummaryPanelTab2222123456789012345678901234567890(Status jobStatus, Job job, Strategy strategy,	String softwareName) {

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
	
	public static PanelTab getSamplePairs(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, Map<String,String> sampleIdControlIdCommandLineMap){
		
		logger.debug("********inside getSamplePairsPanelTab()");

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
	
	public static PanelTab getSampleLibraryRuns(List<Sample> testSampleList, Map<Sample, List<Sample>> sampleLibraryListMap, Map<Sample, List<String>> libraryRunInfoListMap){
		
		logger.debug("********inside getSampleLibraryRunsPanelTab()");

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
	public static PanelTab getFileTypeDefinitions(List<FileType> fileTypeList){
		
		logger.debug("********inside getFileTypeDefinitionsPanelTab()");

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
	public static PanelTab getFilesBySample(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, List<FileType> fileTypeList, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){

		logger.debug("********inside getFilesDisplayedBySamplePanelTab()");

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
		panel.setGroupFieldName("SamplePairs");
		panel.setHasDownload(true);
		panel.setDownloadLinkFieldName("Download");
		
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
	
	public static PanelTab getFilesByFileType(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, List<FileType> fileTypeList, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){
	
		logger.debug("********inside getFilesDisplayedByFileTypePanelTab()");

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
		panel.setGroupFieldName("FileType");
		panel.setHasDownload(true);
		panel.setDownloadLinkFieldName("Download");
		
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
	public static PanelTab getModelImages(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, List<FileType> fileTypeList, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){
		
		logger.debug("********inside getAllModelPNGFilesDisplayedInPanelsTab() at 1");

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

}
