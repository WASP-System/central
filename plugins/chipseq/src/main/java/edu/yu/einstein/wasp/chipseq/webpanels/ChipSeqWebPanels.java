package edu.yu.einstein.wasp.chipseq.webpanels;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.List;
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
		
		PanelTab panelTab = new PanelTab();logger.debug("***************2");

		panelTab.setName("Summary");logger.debug("***************3");
		panelTab.setDescription("testDescription");logger.debug("***************4");
		WebPanel panel = new WebPanel();logger.debug("***************5");
		panel.setTitle("Summary");logger.debug("***************6");
		panel.setDescription("Summary");logger.debug("***************7");
		panel.setResizable(true);
		panel.setMaximizable(true);	

		panel.setOrder(1);
		WebContent content = new WebContent();logger.debug("***************8");
		content.setHtmlCode("<div id=\"summary-grid\"></div>");logger.debug("***************9");
		panel.setContent(content);logger.debug("***************10");

		
		//String script = "Ext.get('myLeslieButton').on('click', function(){alert('This is the Leslie Button'); });";
		//String script = "Ext.create('Ext.grid.Panel', {columns: [ {text: \"Name\", width:120, dataIndex: 'Name'}, {text: \"dob\", width: 380, dataIndex: 'dob'} ], renderTo:'example-grid', width: 500, height: 280 });";
		//String script = "Ext.define('Person',{ extend: 'Ext.data.Model', fields: [ 'Name', 'dob' ] }); var store = Ext.create('Ext.data.Store', { model: 'Person', data : [{Name: 'Ed', dob: 'Spencer'}, {Name: 'Tommy', dob: 'Maintz'}, {Name: 'Aaron', dob: 'Conran'}, {Name: 'Jamie', dob: 'Avins'}] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Name\", width:120, dataIndex: 'Name'}, {text: \"dob\", width: 300, dataIndex: 'dob'} ], renderTo:'example-grid', width: 500, height: 280 });";
		String script = "Ext.define('Summary',{ extend: 'Ext.data.Model', fields: [ 'Strategy', 'Description', 'Workflow', 'Software', 'Status' ] }); var store = Ext.create('Ext.data.Store', { model: 'Summary', data : [{Strategy: '"+strategy.getDisplayStrategy()+"', Description: '"+strategy.getDescription()+"', Workflow: '"+job.getWorkflow().getName()+"', Software: '" + softwareName+"', Status: '"+jobStatus.toString()+"'}] }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Strategy\", width:150, dataIndex: 'Strategy'}, {text: \"Description\", flex: 1, dataIndex: 'Description'}, {text: \"Workflow\", width: 150, dataIndex: 'Workflow'}, {text: \"Main Software\", width: 200, dataIndex: 'Software'}, {text: \"Status\", width: 150, dataIndex: 'Status'} ], renderTo:'summary-grid', height: 300 });";

		panel.setExecOnRenderCode(script);
		panel.setExecOnExpandCode(" ");
		panel.setExecOnResizeCode(" ");
		// does nothing: content.setScriptCode(script);

		
		////////////////////////content.setScriptCode("Ext.get('myAJButton').on('click', function(){alert('You clicked me, AJ'); return false;});");
		////////content.setScriptCode("Ext.define('Person',{ extend: 'Ext.data.Model', fields: [ 'Name', 'dob' ] }); var store = Ext.create('Ext.data.Store', { model: 'Person', autoLoad: true, proxy: { type: 'memory', data: createFakeData(10), reader: {type: 'array'} } }); Ext.create('Ext.grid.Panel', { store: store, columns: [ {text: \"Name\", width:120, dataIndex: 'Name'}, {text: \"dob\", width: 380, dataIndex: 'dob'} ], renderTo:'example-grid', width: 500, height: 280 });");
		//////content.setScriptCode("Ext.require([    'Ext.data.*',    'Ext.grid.*']);function getRandomDate() {    var from = new Date(1900, 0, 1).getTime();    var to = new Date().getTime();    return new Date(from + Math.random() * (to - from));}function createFakeData(count) {        var firstNames   = ['Ed', 'Tommy', 'Aaron', 'Abe'];        var lastNames    = ['Spencer', 'Maintz', 'Conran', 'Elias'];                    var data = [];        for (var i = 0; i < count ; i++) {            var dob = getRandomDate();                       var firstNameId = Math.floor(Math.random() * firstNames.length);            var lastNameId  = Math.floor(Math.random() * lastNames.length);            var name        = Ext.String.format(\"{0} {1}\", firstNames[firstNameId], lastNames[lastNameId]);            data.push([name, dob]);        }        return data;    }    Ext.define('Person',{        extend: 'Ext.data.Model',        fields: [            'Name', 'dob'        ]    });    // create the Data Store    var store = Ext.create('Ext.data.Store', {        model: 'Person',        autoLoad: true,        proxy: {            type: 'memory',                data: createFakeData(10),                reader: {                    type: 'array'                }        }    });    // create the grid    Ext.create('Ext.grid.Panel', {        store: store,        columns: [            {text: \"Name\", width:120, dataIndex: 'Name'},            {text: \"dob\", width: 380, dataIndex: 'dob'}        ],        renderTo:'example-grid',        width: 500,        height: 280    });");
		////////////////////panel.setExecOnRenderCode(content.getScriptCode());
		//



		
		//Set<URI> dependencies =  new LinkedHashSet<URI>(); // load order is important
		//dependencies.add(new URI("http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/ext-all-dev.js"));
		//dependencies.add(new URI("http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/packages/ext-theme-neptune/build/ext-theme-neptune.js"));
		//content.setScriptDependencies(dependencies);
		panelTab.addPanel(panel);
		panelTab.setNumberOfColumns(1);


		return panelTab;
	}

}
