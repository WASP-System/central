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
	
	static protected  Logger logger = LoggerFactory.getLogger(WaspServiceImpl.class);	
	
	public static PanelTab getSummaryPanelTab(Status jobStatus, Job job, Strategy strategy,	String softwareName) {

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
		content.addColumn(new GridColumn("Analysis Status", "Status", 150, 0));//header,dataIndex
		
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
	}
}
