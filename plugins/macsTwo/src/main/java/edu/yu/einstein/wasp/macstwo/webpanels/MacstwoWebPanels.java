package edu.yu.einstein.wasp.macstwo.webpanels;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.plugin.mps.genomebrowser.GenomeBrowserProviding;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.viewpanel.Action;
import edu.yu.einstein.wasp.viewpanel.Action.CallbackFunctionType;
import edu.yu.einstein.wasp.viewpanel.GridColumn;
import edu.yu.einstein.wasp.viewpanel.GridContent;
import edu.yu.einstein.wasp.viewpanel.GridDataField;
import edu.yu.einstein.wasp.viewpanel.GridPanel;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

public class MacstwoWebPanels {

	static protected  Logger logger = LoggerFactory.getLogger(WaspServiceImpl.class);
	
	//even newer organizational format: 9-4-14
	public static GridPanel getPluginSpecificFileDefinitionsPanel(List<String> fileDescriptionList){//List<String> fileDescriptionShortNameList, Map<String, String> fileDescriptionShortNamefileDescriptionMap){
		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("MACS2 File Descriptions");
		panel.setDescription("MACS2 File Descriptions");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		
		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		//content.addDataFields(new GridDataField("MAC2FileType", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Description", "String"));//dataIndex, datatype

		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		//content.addColumn(new GridColumn("MACS2 File Type", "MAC2FileType", 200, 0));//header,dataIndex		width=200; flex=0
		content.addColumn(new GridColumn("Description", "Description", 1));//header,dataIndex		flex=1
		/*
		for(String fileDescriptionShortName : fileDescriptionShortNameList){
			String fileDescription = "";
			fileDescription = fileDescriptionShortNamefileDescriptionMap.get(fileDescriptionShortName);
			if(!fileDescription.trim().isEmpty()){
				List<String> row = new ArrayList<String>();			
				row.add(fileDescriptionShortName);
				row.add(fileDescription);			
				content.addDataRow(row);//add the new row to the content
			}
		}
		*/
		for(String description : fileDescriptionList){
			List<String> row = new ArrayList<String>();			
			row.add(description);			
			content.addDataRow(row);//add the new row to the content
		}
		panel.setContent(content);//add content to panel
		return panel;
	}
	
	//new organizational format: 9-2-14
	public static GridPanel getSamplePairsByAnalysisPanel(List<FileGroup> macs2AnalysisFileGroupList, Map<FileGroup,Sample> fileGroupTestSampleMap, Map<FileGroup,Sample> fileGroupControlSampleMap){

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
		return panel;
	}
	
	//new organizational format: 9-2-14
	public static GridPanel getCommandsByAnalysisPanel(List<FileGroup> macs2AnalysisFileGroupList, Map<FileGroup, List<Software>> fileGroupSoftwareUsedMap, Map<FileGroup, String> fileGroupCommandLineMap){

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
			StringBuilder softwareUsedStringBuilder = new StringBuilder();
			for(Software software : softwareList){
				if(softwareUsedStringBuilder.length() > 0){
					softwareUsedStringBuilder.append("<br />");
				}
				softwareUsedStringBuilder.append(software.getName());
			}
			String softwareUsedString = new String(softwareUsedStringBuilder);
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
		return panel;
	}
	
	//new organizational format: 9-2-14
	public static GridPanel getLibrariesAndBamFilesUsedByAnalysisPanel(List<FileGroup> macs2AnalysisFileGroupList, Map<FileGroup, List<Sample>> fileGroupLibrariesUsedMap, Map<FileGroup, List<FileHandle>> fileGroupBamFilesUsedMap){

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
				StringBuilder libraryNamesAsStringBuilder = new StringBuilder();
				for(Sample library : libraries){
					if(libraryNamesAsStringBuilder.length()>0){libraryNamesAsStringBuilder.append("<br />");}
					libraryNamesAsStringBuilder.append(library.getName());
				}
				row.add(new String(libraryNamesAsStringBuilder));
			}
			
			List<FileHandle>  bamFiles = fileGroupBamFilesUsedMap.get(fileGroup);
			if(bamFiles.isEmpty()){
				row.add("Error");
			}
			else{
				StringBuilder bamFileNamesAsStringBuilder = new StringBuilder();
				for(FileHandle fh : bamFiles){
					if(bamFileNamesAsStringBuilder.length()>0){bamFileNamesAsStringBuilder.append("<br />");}
					bamFileNamesAsStringBuilder.append(fh.getFileName());
				}
				row.add(new String(bamFileNamesAsStringBuilder));
			}
			
			content.addDataRow(row);//add the new row to the content			
		}
				
		panel.setContent(content);//add content to panel
		
		return panel;
	}
	
	//new organizational format: 9-2-14
	public static GridPanel getFripCalculationByAnalysisPanel(List<FileGroup> macs2AnalysisFileGroupList, Map<FileGroup, String> fileGroupFripCalculationMap){
		
		//create the panelTab to house the panel
		PanelTab panelTab = new PanelTab();
		panelTab.setName("FRiP (%)");
		panelTab.setNumberOfColumns(1);

		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Fraction Of Reads Within Peaks (%)");
		panel.setDescription("Fraction Of Reads Within Peaks (%)");
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
		
		return panel;
	}
	
	//9-30-14
	public static GridPanel getFilesByAnalysisPanel(WaspPluginRegistry pluginRegistry, FileUrlResolver fileUrlResolver, List<FileGroup> macs2AnalysisFileGroupList, Map<FileGroup, List<FileGroup>> outerCollectionFileGroupInnerFileGroupListMap, Map<FileGroup, Double>outerCollectionFileGroupFripMap){
		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Files By Analysis");
		panel.setDescription("Files By Analysis");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		panel.setGrouping(true);
		panel.setGroupField("Analysis");
		panel.setAllowSelectDownload(true);
		panel.setAllowGroupDownload(true);
		panel.setSelectDownloadText("Download Selected");

		//create content (think of it as the table)
		GridContent content = new GridContent();
		//create the data model 
		content.addDataFields(new GridDataField("Analysis", "String"));//THIS WILL BE THE UNIQUE GROUPING FIELD //dataIndex, datatype
		content.addDataFields(new GridDataField("File", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Size", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("MD5", "String"));//dataIndex, datatype
		
		//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
		///////unique grouping field is NOT displayed: content.addColumn(new GridColumn("Analysis", "Analysis"));//header,dataIndex		
		content.addColumn(new GridColumn("File", "File", 1, true));//header,dataIndex					flex=1, shown in tooltip
		content.addColumn(new GridColumn("Size", "Size", 100, 0));//header,dataIndex					width=270; flex=0
		content.addColumn(new GridColumn("MD5", "MD5", 170, 0));//header,dataIndex					width=270; flex=0
		
		for(FileGroup outerCollectionFileGroup : macs2AnalysisFileGroupList){	
			
			String headerForGroup = outerCollectionFileGroup.getDescription();
			Double frip = outerCollectionFileGroupFripMap.get(outerCollectionFileGroup);
			if(frip!=null){
				Double fripAsPercent = frip * 100;
				DecimalFormat myFormat = new DecimalFormat("0.00000");
				String formatedFrip = myFormat.format(fripAsPercent);
				headerForGroup += " (FRiP: " + formatedFrip + " %)";
			}
			for(FileGroup innerFileGroup : outerCollectionFileGroupInnerFileGroupListMap.get(outerCollectionFileGroup)){
				List<String> row = new ArrayList<String>();	
				row.add(headerForGroup);//won't be displayed on each row, but will be the header for each section (but must be part of the row)

				FileHandle fileHandle = new ArrayList<FileHandle>(innerFileGroup.getFileHandles()).get(0);
				row.add(fileHandle.getFileName());
				Integer sizeK = fileHandle.getSizek();
				if(sizeK!=null){
					row.add(fileHandle.getSizek().toString());
				}else{row.add("");}
				row.add(fileHandle.getMd5hash());				
				content.addDataRow(row);//add the new row to the content						
				List<Action> actionList = new ArrayList<Action>();
				// add download action to the list
				String resolvedURL = "";
				try{
					resolvedURL = fileUrlResolver.getURL(fileHandle).toString();
				}catch(Exception e){logger.debug("UNABLE TO RESOLVE URL for file: " + fileHandle.getFileName());}
				actionList.add(new Action("icon-download", "Download", CallbackFunctionType.DOWNLOAD, resolvedURL));
				///actionList.add(new Action("icon-view-file", "View", CallbackFunctionType.OPEN_IN_CSS_WIN, new ArrayList<FileGroup>(fileHandle.getFileGroup()).get(0).getId().toString()));
				actionList.add(new Action("icon-view-file", "View", CallbackFunctionType.OPEN_IN_CSS_WIN, innerFileGroup.getId().toString()));
				
				List<GenomeBrowserProviding> plugins = new ArrayList<>();				
				plugins.addAll(pluginRegistry.getPlugins(GenomeBrowserProviding.class));
				for(GenomeBrowserProviding plugin : plugins){
					Action action = plugin.getAction(innerFileGroup);
					if(action != null){
						actionList.add(action);
					}
				}
				content.addActions(actionList);				
			}			
		}
		
		panel.setContent(content);//add content to panel
		
		return panel;
	}
	
	
}
