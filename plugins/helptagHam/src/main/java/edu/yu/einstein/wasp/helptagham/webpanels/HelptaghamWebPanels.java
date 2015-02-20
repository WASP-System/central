package edu.yu.einstein.wasp.helptagham.webpanels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.plugin.mps.genomebrowser.GenomeBrowserProviding;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.viewpanel.Action;
import edu.yu.einstein.wasp.viewpanel.Action.CallbackFunctionType;
import edu.yu.einstein.wasp.viewpanel.GridColumn;
import edu.yu.einstein.wasp.viewpanel.GridContent;
import edu.yu.einstein.wasp.viewpanel.GridDataField;
import edu.yu.einstein.wasp.viewpanel.GridPanel;

public class HelptaghamWebPanels {

	static protected  Logger logger = LoggerFactory.getLogger(WaspServiceImpl.class);
	
	public static GridPanel getPluginSpecificFileDefinitionsPanel(List<String> fileDescriptionList) {
		GridPanel panel = new GridPanel();
		panel.setTitle("HAM for HELPtagging File Descriptions");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		
		GridContent content = new GridContent();
		content.addDataFields(new GridDataField("Description", "String"));//dataIndex, datatype
		content.addColumn(new GridColumn("Description", "Description", 1));// header,dataIndex flex=1

		for(String description : fileDescriptionList){
			List<String> row = new ArrayList<String>();			
			row.add(description);			
			content.addDataRow(row);
		}
		panel.setContent(content);
		return panel;
	}
	
	public static GridPanel getSamplePairsByAnalysisPanel(List<FileGroup> analysisFileGroupList, Map<FileGroup,Sample> fileGroupHpa2SampleMap, Map<FileGroup,Sample> fileGroupMsp1SampleMap){
		GridPanel panel = new GridPanel();
		panel.setTitle("Sample Pairs");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		
		GridContent content = new GridContent();
		content.addDataFields(new GridDataField("Analysis", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Hpa2/Beta-GT Sample", "String"));// dataIndex, datatype
		content.addDataFields(new GridDataField("Msp1 Sample", "String"));// dataIndex, datatype

		content.addColumn(new GridColumn("Analysis", "Analysis", 500, 0));//header,dataIndex	     width=250; flex=0
		content.addColumn(new GridColumn("Hpa2/Beta-GT Sample", "Hpa2/Beta-GT Sample", 250, 0));// header,dataIndex width=250; flex=0
		content.addColumn(new GridColumn("Msp1 Sample", "Msp1 Sample", 1));// header,dataIndex width=250; flex=0
		
		//create rows with  information
		for(FileGroup fileGroup : analysisFileGroupList){
				
			if (fileGroupHpa2SampleMap.isEmpty()) {
				logger.debug("fileGroupHpa2SampleMap is empty ");
			}
			List<String> row = new ArrayList<String>();
			row.add(fileGroup.getDescription());
			Sample hpa2Sample = fileGroupHpa2SampleMap.get(fileGroup);
			String hpa2SampleName = "Unexpected Error";
			if (hpa2Sample != null) {
				hpa2SampleName = hpa2Sample.getName();
				
			} else {
				logger.debug("hpa2Sample is null");
			}
			Sample msp1Sample = fileGroupMsp1SampleMap.get(fileGroup);
			String msp1SampleName = "Standard Reference";
			if (msp1Sample != null && msp1Sample.getId().intValue() > 0) {
				msp1SampleName = msp1Sample.getName();
			}
			logger.debug("hpa2SampleName = " + hpa2SampleName);
			logger.debug("msp1SampleName = " + msp1SampleName);
			row.add(hpa2SampleName);
			row.add(msp1SampleName);
			content.addDataRow(row);
		}
				
		panel.setContent(content);
		return panel;
	}
	
	public static GridPanel getLibrariesAndHcountFilesUsedByAnalysisPanel(List<FileGroup> analysisFileGroupList,
			Map<FileGroup, List<Sample>> fileGroupLibrariesUsedMap, Map<FileGroup, List<FileHandle>> fileGroupHcountFilesUsedMap) {

		GridPanel panel = new GridPanel();
		panel.setTitle("Libraries & Hcount Files Used");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		
		GridContent content = new GridContent();
		content.addDataFields(new GridDataField("Analysis", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Libraries Used", "String"));//dataIndex, datatype
		content.addDataFields(new GridDataField("Hcount Files Used", "String"));// dataIndex, datatype
		
		content.addColumn(new GridColumn("Analysis", "Analysis", 500, 0));//header,dataIndex	     width=500; flex=0
		content.addColumn(new GridColumn("Libraries Used", "Libraries Used", 150, 0));//header,dataIndex	width=300;      flex=0
		content.addColumn(new GridColumn("Hcount Files Used", "Hcount Files Used", 1));// header,dataIndex flex=1
		
		//create rows with  information
		for (FileGroup fileGroup : analysisFileGroupList) {
			
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
			
			List<FileHandle> bamFiles = fileGroupHcountFilesUsedMap.get(fileGroup);
			if(bamFiles.isEmpty()){
				row.add("Error");
			}
			else{
				StringBuilder fileNamesAsStringBuilder = new StringBuilder();
				for(FileHandle fh : bamFiles){
					if (fileNamesAsStringBuilder.length() > 0) {
						fileNamesAsStringBuilder.append("<br />");
					}
					fileNamesAsStringBuilder.append(fh.getFileName());
				}
				row.add(new String(fileNamesAsStringBuilder));
			}
			
			content.addDataRow(row);
		}
				
		panel.setContent(content);
		return panel;
	}
	
	public static GridPanel getFilesByAnalysisPanel(WaspPluginRegistry pluginRegistry, FileUrlResolver fileUrlResolver, FileService fileService,
			List<FileGroup> analysisFileGroupList, Map<FileGroup, List<FileGroup>> outerCollectionFileGroupInnerFileGroupListMap) {
		//create the panel
		GridPanel panel = new GridPanel();
		panel.setTitle("Files By Analysis");
		panel.setResizable(true);
		panel.setMaximizable(true);	
		panel.setOrder(1);
		panel.setGrouping(true);
		panel.setGroupField("Analysis");

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
		
		for(FileGroup outerCollectionFileGroup : analysisFileGroupList){	
			String headerForGroup = outerCollectionFileGroup.getDescription();
			for(FileGroup innerFileGroup : outerCollectionFileGroupInnerFileGroupListMap.get(outerCollectionFileGroup)){
				List<String> row = new ArrayList<String>();	
				row.add(headerForGroup);//won't be displayed on each row, but will be the header for each section (but must be part of the row)

				FileHandle fileHandle = new ArrayList<FileHandle>(innerFileGroup.getFileHandles()).get(0);
				row.add(fileHandle.getFileName());
				Integer sizeK = fileHandle.getSizek();
				if(sizeK!=null){
					row.add(fileHandle.getSizek().toString());
				} else {
					row.add("");
				}
				row.add(fileHandle.getMd5hash());				
				content.addDataRow(row);//add the new row to the content						
				List<Action> actionList = new ArrayList<Action>();
				// add download action to the list
				String resolvedURL = "";
				try{
					resolvedURL = fileUrlResolver.getURL(fileHandle).toString();
				} catch (Exception e) {
					logger.debug("UNABLE TO RESOLVE URL for file: " + fileHandle.getFileName());
				}
				actionList.add(new Action("icon-download", "Download", CallbackFunctionType.DOWNLOAD, resolvedURL));
				
				if (!fileService.getTabViewProvidingPluginsByFileGroup(innerFileGroup).isEmpty())
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
		
		panel.setContent(content);
		return panel;
	}
	
	
}
