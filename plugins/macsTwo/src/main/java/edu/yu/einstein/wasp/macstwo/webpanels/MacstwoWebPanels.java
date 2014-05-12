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

	public static PanelTab getSamplePairs(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, Map<String,String> sampleIdControlIdCommandLineMap){

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
	}
	
	public static PanelTab getFrips(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, Map<String,String> sampleIdControlIdFripMap){

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("FRiP");
		panelTab.setNumberOfColumns(1);

		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Fraction Of Reads Within Peaks");
		panel.setDescription("Fraction Of Reads Within Peaks");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		
		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		content.addDataFields(new GridDataField("TestSample", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("ControlSample", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("FRiP", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		content.addColumn(new GridColumn("Test Sample", "TestSample", 250, 0));//header,dataIndex	     width=250; flex=0	
		content.addColumn(new GridColumn("Control Sample", "ControlSample", 250, 0));//header,dataIndex	 width=250; flex=0	
		content.addColumn(new GridColumn("FRiP", "FRiP", 1));//header,dataIndex               flex=1
		
		//create rows with  information
		for(Sample testSample : testSampleList){
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			for(Sample controlSample : controlSampleList){
				
				List<String> row = new ArrayList<String>();
				
				row.add(testSample.getName());
				row.add(controlSample.getName());
				
				String frip = sampleIdControlIdFripMap.get(testSample.getId().toString()+"::"+controlSample.getId().toString());
				if(frip==null || frip.isEmpty()){
					frip = "?";
				}
				row.add(frip);
				
				content.addDataRow(row);//add the new row to the content
			}
		}
				
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
	}
	
	public static PanelTab getSampleLibraryRuns(List<Sample> testSampleList, Map<Sample, List<Sample>> sampleLibraryListMap, Map<Sample, List<String>> libraryRunInfoListMap){

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
	}
	public static PanelTab getFileTypeDefinitions(List<FileType> fileTypeList){
		
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
			if(fileType.getExtensions().endsWith("_model.png")){//macstwo specific
				continue;
			}
			List<String> row = new ArrayList<String>();			
			row.add(fileType.getName());
			row.add(fileType.getDescription());			
			content.addDataRow(row);//add the new row to the content
		}
						
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
	}
	public static PanelTab getFilesBySample(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, List<FileType> fileTypeList, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){

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
		panel.setAllowSelectDownload(true);
		panel.setAllowGroupDownload(true);
		panel.setSelectDownloadText("Download Selected");
		
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
	}
	
	public static PanelTab getFilesByFileType(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, List<FileType> fileTypeList, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){

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
		panel.setAllowSelectDownload(true);
		panel.setAllowGroupDownload(true);
		panel.setSelectDownloadText("Download Selected");
		
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

	public static PanelTab getModelImages(List<Sample> testSampleList, Map<Sample, List<Sample>> testSampleControlSampleListMap, List<FileType> fileTypeList, Map<String, FileHandle>  sampleIdControlIdFileTypeIdFileHandleMap, Map<FileHandle, String> fileHandleResolvedURLMap, Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("Model View");
		panelTab.setNumberOfColumns(2);
		
		int counter = 1;
		for(Sample testSample : testSampleList){
			List<Sample> controlSampleList = testSampleControlSampleListMap.get(testSample);
			for(Sample controlSample : controlSampleList){
				
				String samplePairs = "Test Sample: " + testSample.getName() + "; Control: " + controlSample.getName();
				
				for(FileType fileType : fileTypeList){
					if(fileType.getExtensions().endsWith("_model.png")){//macstwo specific
						FileHandle fileHandle = sampleIdControlIdFileTypeIdFileHandleMap.get(testSample.getId().toString() + "::" + controlSample.getId().toString() + "::" + fileType.getId().toString());
						String resolvedURL = fileHandleResolvedURLMap.get(fileHandle);
						if(fileHandle==null || resolvedURL==null || resolvedURL.isEmpty()){//unexpected
							continue;
						}
						//create the panel
						WebPanel panel = new WebPanel();
						panel.setTitle(samplePairs);
						panel.setDescription(samplePairs);
						panel.setHeight(900);
						panel.setResizable(true);
						panel.setMaximizable(true);	
						panel.setOrder(counter++);
						
						WebContent content = new WebContent();
						//works nicely:
						content.setHtmlCode("<img src= '"+resolvedURL+"' height='800' width='400'>");
						
						//this works, but uses iframe which we do not want:
						//content.setHtmlCode("<iframe width=\"470\" height=\"900\" src=\"http://localhost:8080/wasp/file/fileHandle/"+fileHandle.getId()+"/view.do\" ></iframe>");
						
						//apparently works fine:
						//content.setHtmlCode("<img width=\"200\" height=\"200\" src=\"http://localhost:8080/wasp/images/fail.png\" />");
						
						//doesn't work
						//content.setHtmlCode("<img src= '<wasp:url fileAccessor= '${"+fileHandle.getId().toString()+"}' />' height='800' width='400'>");
						
						panel.setContent(content);
						panelTab.addPanel(panel);//add panel to panelTab	
					}					
				}			
			}
		}	
		return panelTab;
	}

}
