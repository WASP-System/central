package edu.yu.einstein.wasp.macstwo.webpanels;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.viewpanel.GridColumn;
import edu.yu.einstein.wasp.viewpanel.GridContent;
import edu.yu.einstein.wasp.viewpanel.GridDataField;
import edu.yu.einstein.wasp.viewpanel.GridPanel;
import edu.yu.einstein.wasp.viewpanel.PanelTab;
import edu.yu.einstein.wasp.viewpanel.WebContent;
import edu.yu.einstein.wasp.viewpanel.WebPanel;

public class MacstwoWebPanels {

	static protected  Logger logger = LoggerFactory.getLogger(WaspServiceImpl.class);	
	
	//use this
	public static PanelTab getFileTypeDefinitions(List<FileType> fileTypeList){
		
		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("File Types");
		panelTab.setNumberOfColumns(1);

		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("File Type Deescriptions");
		panel.setDescription("FileTypes Deescriptions");
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
			//if(fileType.getExtensions().endsWith("_model.png")){//macstwo specific
			//	continue;
			//}
			List<String> row = new ArrayList<String>();			
			row.add(fileType.getName());
			row.add(fileType.getDescription());			
			content.addDataRow(row);//add the new row to the content
		}
						
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
	}
	//this is new
	public static PanelTab getSamplePairsByAnalysis(List<FileGroup> macs2AnalysisFileGroupList, Map<FileGroup,Sample> fileGroupTestSampleMap, Map<FileGroup,Sample> fileGroupControlSampleMap){

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("Sample Pairs");
		panelTab.setNumberOfColumns(1);

		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Sample Pairs");
		panel.setDescription("Sample Pairs");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		
		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		content.addDataFields(new GridDataField("Analysis", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("IP Sample", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Control Sample", "String"));//dataIndex, datatype
		//////content.addDataFields(new GridDataField("Command", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		content.addColumn(new GridColumn("Analysis", "Analysis", 500, 0));//header,dataIndex	     width=250; flex=0
		content.addColumn(new GridColumn("IP Sample", "IP Sample", 250, 0));//header,dataIndex	     width=250; flex=0	
		content.addColumn(new GridColumn("Control Sample", "Control Sample",  1));//header,dataIndex	 width=250; flex=0	
		//content.addColumn(new GridColumn("Command Line", "Command", 1));//header,dataIndex               flex=1
		
		//create rows with  information
		for(FileGroup fileGroup : macs2AnalysisFileGroupList){
				
			if(fileGroupTestSampleMap.isEmpty()){logger.debug("fileGroupTestSampleMap is empty ");}
			List<String> row = new ArrayList<String>();
			row.add(fileGroup.getDescription());
			Sample testSample = fileGroupTestSampleMap.get(fileGroup);
			String testSampleName = "Unexpected Error";
			if(testSample!=null){
				testSampleName = testSample.getName();
				
			}else{logger.debug("testSample is null");}
			Sample controlSample = fileGroupControlSampleMap.get(fileGroup);
			String controlSampleName = "None";
			if(controlSample!=null && controlSample.getId().intValue()>0){
				controlSampleName = controlSample.getName();				
			}
			logger.debug("testSampleName = " + testSampleName);
			logger.debug("controlSampleName = " + controlSampleName);
			row.add(testSampleName);
			row.add(controlSampleName);
			content.addDataRow(row);//add the new row to the content			
		}
				
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
	}
	
	//this is new
	public static PanelTab getCommandsByAnalysis(List<FileGroup> macs2AnalysisFileGroupList, Map<FileGroup, List<Software>> fileGroupSoftwareUsedMap, Map<FileGroup, String> fileGroupCommandLineMap){

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("Commands");
		panelTab.setNumberOfColumns(1);

		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Commands");
		panel.setDescription("Commands");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		
		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		content.addDataFields(new GridDataField("Analysis", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Major Software", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Commands", "String"));//dataIndex, datatype
		
		//////content.addDataFields(new GridDataField("Command", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		content.addColumn(new GridColumn("Analysis", "Analysis", 500, 0));//header,dataIndex	     width=500; flex=0
		content.addColumn(new GridColumn("Major Software", "Major Software", 150, 0));//header,dataIndex	     width=250; flex=0
		content.addColumn(new GridColumn("Commands", "Commands", 1));//header,dataIndex	      flex=1
		
		//create rows with  information
		for(FileGroup fileGroup : macs2AnalysisFileGroupList){
			
			List<Software> softwareList  = fileGroupSoftwareUsedMap.get(fileGroup);
			StringBuffer softwareUsedStringBuffer = new StringBuffer();
			for(Software software : softwareList){
				if(softwareUsedStringBuffer.length() > 0){
					softwareUsedStringBuffer.append("<br />");
				}
				softwareUsedStringBuffer.append(software.getName());
			}
			String softwareUsedString = new String(softwareUsedStringBuffer);
			if(softwareUsedString.isEmpty()){
				softwareUsedString = "";
			}			
			
			String commands = fileGroupCommandLineMap.get(fileGroup);
			if(commands == null){
				commands = "";
			}
			List<String> row = new ArrayList<String>();
			row.add(fileGroup.getDescription());
			row.add(softwareUsedString);
			row.add(commands);
			content.addDataRow(row);//add the new row to the content			
		}
				
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
	}
	
	//this is new
	public static PanelTab getLibrariesAndBamFilesUsedByAnalysis(List<FileGroup> macs2AnalysisFileGroupList, Map<FileGroup, List<Sample>> fileGroupLibrariesUsedMap, Map<FileGroup, List<FileHandle>> fileGroupBamFilesUsedMap){

			//create the panelTab to house the panel
			PanelTab panelTab = new PanelTab();
			panelTab.setName("Libraries & Bam Files");
			panelTab.setNumberOfColumns(1);

			//create the panel
			GridPanel panel = new GridPanel();
			panel.setTitle("Libraries & Bam Files Used");
			panel.setDescription("Libraries & Bam Files Used");
			panel.setResizable(true);
			panel.setMaximizable(true);	
			panel.setOrder(1);
			
			//create content (think of it as the table)
			GridContent content = new GridContent();
			//create the data model 
			content.addDataFields(new GridDataField("Analysis", "String"));//dataIndex, datatype
			content.addDataFields(new GridDataField("Libraries Used", "String"));//dataIndex, datatype
			content.addDataFields(new GridDataField("Bam Files Used", "String"));//dataIndex, datatype
			
			//////content.addDataFields(new GridDataField("Command", "String"));//dataIndex, datatype

			//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
			content.addColumn(new GridColumn("Analysis", "Analysis", 500, 0));//header,dataIndex	     width=500; flex=0
			content.addColumn(new GridColumn("Libraries Used", "Libraries Used", 150, 0));//header,dataIndex	width=300;      flex=0
			content.addColumn(new GridColumn("Bam Files Used", "Bam Files Used", 1));//header,dataIndex	      flex=1
			
			//create rows with  information
			for(FileGroup fileGroup : macs2AnalysisFileGroupList){				
				
				List<String> row = new ArrayList<String>();
				row.add(fileGroup.getDescription());
				
				List<Sample>  libraries = fileGroupLibrariesUsedMap.get(fileGroup);
				if(libraries.isEmpty()){
					row.add("Error");
				}
				else{
					StringBuffer libraryNamesAsStringBuffer = new StringBuffer();
					for(Sample library : libraries){
						if(libraryNamesAsStringBuffer.length()>0){libraryNamesAsStringBuffer.append("<br />");}
						libraryNamesAsStringBuffer.append(library.getName());
					}
					row.add(new String(libraryNamesAsStringBuffer));
				}
				
				List<FileHandle>  bamFiles = fileGroupBamFilesUsedMap.get(fileGroup);
				if(bamFiles.isEmpty()){
					row.add("Error");
				}
				else{
					StringBuffer bamFileNamesAsStringBuffer = new StringBuffer();
					for(FileHandle fh : bamFiles){
						if(bamFileNamesAsStringBuffer.length()>0){bamFileNamesAsStringBuffer.append("<br />");}
						bamFileNamesAsStringBuffer.append(fh.getFileName());
					}
					row.add(new String(bamFileNamesAsStringBuffer));
				}
				
				content.addDataRow(row);//add the new row to the content			
			}
					
			panel.setContent(content);//add content to panel
			panelTab.addPanel(panel);//add panel to panelTab
			return panelTab;
		}
	
	//this is new
	public static PanelTab getFripCalculationByAnalysis(List<FileGroup> macs2AnalysisFileGroupList, Map<FileGroup, String> fileGroupFripCalculationMap){
		
		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("FRiP (%)");
		panelTab.setNumberOfColumns(1);

		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Fraction Of Reads Within Peaks (%)<br />Note: ENCODE Consortium scrutinizes experiments with FRiP < 1%");
		panel.setDescription("Fraction Of Reads Within Peaks (%)<br />Note: ENCODE Consortium scrutinizes experiments with FRiP < 1%");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		
		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		content.addDataFields(new GridDataField("Analysis", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("FRiP (%)", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		content.addColumn(new GridColumn("Analysis", "Analysis", 600, 0));//header,dataIndex		width=300; flex=0
		content.addColumn(new GridColumn("FRiP (%)", "FRiP (%)", 1));//header,dataIndex		flex=1
		
		//create rows with  information
		for(FileGroup fileGroup : macs2AnalysisFileGroupList){
			List<String> row = new ArrayList<String>();			
			row.add(fileGroup.getDescription());
			row.add(fileGroupFripCalculationMap.get(fileGroup));			
			content.addDataRow(row);//add the new row to the content
		}
						
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
		return panelTab;
	}
	//this is new
	public static PanelTab getFilesByAnalysis(List<FileGroup> macs2AnalysisFileGroupList, 
											Map<FileGroup,List<FileHandle>> fileGroupFileHandleListMap, 
											Map<FileHandle,String> fileHandleResolvedURLMap, 
											Map<FileGroup, Double> fileGroupFripPercentMap){
		
		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("Files By Analysis");
		panelTab.setNumberOfColumns(1);
	
		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Files By Analysis");
		panel.setDescription("Files By Analysis");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		panel.setGrouping(true);
		panel.setGroupField("Analysis");
		panel.setHasDownload(true);
		panel.setDownloadLinkField("Download");
		panel.setAllowSelectDownload(true);
		panel.setAllowGroupDownload(true);
		panel.setSelectDownloadText("Download Selected");
		
		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		content.addDataFields(new GridDataField("Analysis", "String"));//THIS WILL BE THE UNIQUE GROUPING FIELD //dataIndex, datatype
		content.addDataFields(new GridDataField("FileType", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("File", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("MD5", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Download", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		///////don't want this to display: content.addColumn(new GridColumn("Analysis", "Analysis"));//header,dataIndex		
		content.addColumn(new GridColumn("File Type", "FileType", 150, 0));//header,dataIndex	width=150; flex=0	
		content.addColumn(new GridColumn("File", "File", 1));//header,dataIndex					flex=1
		content.addColumn(new GridColumn("MD5", "MD5", 270, 0));//header,dataIndex					width=270; flex=0
		////content.addColumn(new GridColumn(" ", "Download", 100, 0));//header is single space string,dataIndex	width=100; flex=0
		
		for(FileGroup fileGroup : macs2AnalysisFileGroupList){				
			for(FileHandle fileHandle : fileGroupFileHandleListMap.get(fileGroup)){				
				List<String> row = new ArrayList<String>();	
				String headerForGroup = fileGroup.getDescription();
				Double frip = fileGroupFripPercentMap.get(fileGroup);
				if(frip!=null){
					Double fripAsPercent = frip * 100;
					DecimalFormat myFormat = new DecimalFormat("0.00000");
					String formatedFrip = myFormat.format(fripAsPercent);
					headerForGroup += " (FRiP: " + formatedFrip + " %)";
				}
				row.add(headerForGroup);//won't be displayed on each row, but will be the header for each section (but must be part of the row)
				row.add(fileHandle.getFileType().getName());
				row.add(fileHandle.getFileName());
				row.add(fileHandle.getMd5hash());
				row.add(fileHandleResolvedURLMap.get(fileHandle));
				content.addDataRow(row);//add the new row to the content
			}			
		}
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
	
		return panelTab;		 
	}
	//this is new
	public static PanelTab getFilesByFileType(List<FileGroup> macs2AnalysisFileGroupList, 
											Map<FileGroup,List<FileHandle>> fileGroupFileHandleListMap, 
											Map<FileHandle,String> fileHandleResolvedURLMap, 
											List<FileType> fileTypeList){
		
		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("Files By Type");
		panelTab.setNumberOfColumns(1);
	
		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Files By Type");
		panel.setDescription("Files By Type");
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
		content.addDataFields(new GridDataField("FileType", "String"));//THIS WILL BE THE UNIQUE GROUPING FIELD //dataIndex, datatype
		content.addDataFields(new GridDataField("File", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("MD5", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Download", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		///////don't want this to display: content.addColumn(new GridColumn("Analysis", "Analysis"));//header,dataIndex		
		
		content.addColumn(new GridColumn("File", "File", 500));//header,dataIndex					flex=1
		content.addColumn(new GridColumn("MD5", "MD5", 270, 0));//header,dataIndex					width=270; flex=0
		////content.addColumn(new GridColumn(" ", "Download", 100, 0));//header is single space string,dataIndex	width=100; flex=0
		
		for(FileType fileType : fileTypeList){						
			for(FileGroup fileGroup : macs2AnalysisFileGroupList){
				for(FileHandle fileHandle : fileGroupFileHandleListMap.get(fileGroup)){
					if(fileHandle.getFileType().getId().intValue()== fileType.getId().intValue()){
						List<String> row = new ArrayList<String>();					
						row.add(fileType.getName());//won't be displayed on each row, but will be the header for each section (but must be part of the row)
						row.add(fileHandle.getFileName());
						row.add(fileHandle.getMd5hash());
						row.add(fileHandleResolvedURLMap.get(fileHandle));
						content.addDataRow(row);//add the new row to the content
					}
				}
			}
		}
		panel.setContent(content);//add content to panel
		panelTab.addPanel(panel);//add panel to panelTab
	
		return panelTab;		 
	}
	//this is new
	public static PanelTab getModelPNGFilesByAnalysis(List<FileGroup> macs2AnalysisFileGroupList, Map<FileGroup,List<FileHandle>> fileGroupFileHandleListMap, Map<FileHandle,String> fileHandleResolvedURLMap){

		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("Model View");
		panelTab.setNumberOfColumns(2);
		
		int counter = 1;
		for(FileGroup fileGroup : macs2AnalysisFileGroupList){				
			for(FileHandle fileHandle : fileGroupFileHandleListMap.get(fileGroup)){	
				if(fileHandle.getFileType().getExtensions().endsWith("_model.png")){//macstwo specific
					String resolvedURL = fileHandleResolvedURLMap.get(fileHandle);
					if(fileHandle==null || resolvedURL==null || resolvedURL.isEmpty()){//unexpected
						continue;
					}
					//create the panel
					WebPanel panel = new WebPanel();
					panel.setTitle(fileGroup.getDescription());
					panel.setDescription(fileGroup.getDescription());
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
		return panelTab;
	}
}
