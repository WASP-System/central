package edu.yu.einstein.wasp.plugin.picard.webpanels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.viewpanel.GridColumn;
import edu.yu.einstein.wasp.viewpanel.GridContent;
import edu.yu.einstein.wasp.viewpanel.GridDataField;
import edu.yu.einstein.wasp.viewpanel.GridPanel;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

public class PicardWebPanels {
	
	static protected  Logger logger = LoggerFactory.getLogger(WaspServiceImpl.class);	

	public static PanelTab getAlignmentMetrics(Map<String,Map<String,String>> metrics, String bamFileName){
		//create the panelTab to house the panel
				PanelTab panelTab = new PanelTab();
				panelTab.setName("Alignment Metrics");
				panelTab.setNumberOfColumns(1);

				//create the panel
				GridPanel panel = new GridPanel();
				panel.setTitle("Alignment Metrics: " + bamFileName);
				panel.setDescription("Alignment Metrics");
				panel.setResizable(true);
				panel.setMaximizable(true);	
				panel.setOrder(1);
				panel.setGrouping(true);
				panel.setGroupField("Title");
				
				//create content (think of it as the table)
				GridContent content = new GridContent();
				//create the data model 
				content.addDataFields(new GridDataField("Title", "String"));//THIS WILL BE THE UNIQUE GROUPING FIELD //dataIndex, datatype
				content.addDataFields(new GridDataField("Stat", "String"));//dataIndex, datatype
				content.addDataFields(new GridDataField("Value", "String"));//dataIndex, datatype
				

				//create columns and associate each column with its displayed header and a data model attribute (dataIndex)
				///////don't want this to display: content.addColumn(new GridColumn("Title", "Title"));//header,dataIndex		
				content.addColumn(new GridColumn("Stat", "Stat", 300, 0));//header,dataIndex	 width=250; flex=0	
				//content.addColumn(new GridColumn("Value", "Value", 250,0));//header,dataIndex	 flex=1	
				
				//used for the extra col
				content.addColumn(new GridColumn("Value", "Value", 120, 0));//header,dataIndex	 width=300; flex=0	
				content.addColumn(new GridColumn("", "", 1));//header,dataIndex	 flex=1 ---the extra one
				
				//create rows with data
				for (String title : metrics.keySet()) {					
					
					Map<String,String> statAndValueMap = metrics.get(title);
					
					for(String statKey : statAndValueMap.keySet()){	
						List<String> row = new ArrayList<String>();	
						row.add(title);//won't be displayed, but must be part of the row
						row.add(statKey);
						String value = statAndValueMap.get(statKey);
						row.add(value);	
						row.add("");	//the extra col. 
						content.addDataRow(row);//add the new row to the content
					}
					
				}
				panel.setContent(content);//add content to panel
				panelTab.addPanel(panel);//add panel to panelTab
				return panelTab;
	}

}
