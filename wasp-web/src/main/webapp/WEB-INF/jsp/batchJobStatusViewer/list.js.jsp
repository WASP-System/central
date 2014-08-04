<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/ext-all-dev.js"></script>
<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/packages/ext-theme-neptune/build/ext-theme-neptune.js"></script>
<script type="text/javascript"	src="<wasp:relativeUrl value='scripts/extjs/wasp/WaspNamespaceDefinition.js.jsp' />"></script>
<link rel="stylesheet" type="text/css" href="<wasp:relativeUrl value='css/ext-theme-neptune-all-wasp.css' />" />
<link rel="stylesheet" type="text/css" href="<wasp:relativeUrl value='css/treeGrid.css' />" />

<style>
	.infoIcon {
        background-image: url(<wasp:relativeUrl value="images/information_30x30.png" />) !important;
        background-size: 15px 15px;
	}
	.infoNotAvailableIcon {
        background-image: none;
	}
</style>

<script type="text/javascript">

Ext.require([
    'Ext.data.*',
    'Ext.button.*',
    'Ext.tree.*',
    'Wasp.store.TreeGridStore'
]);

// we want to setup a data model and store instead of using dataUrl this is mirrored in BatchJobTreeModel.java
// such that when a list of BatchJobTreeModel data is returned as Json, it maps directly to the BatchTreeModel model class defined below
Ext.define('BatchTreeModel', {
    extend: 'Ext.data.TreeModel',
    fields: [
        {name: 'name',     type: 'string'},
        {name: 'executionId',     type: 'long'},
        {name: 'startTime', type: 'string'},
        {name: 'endTime',     type: 'string'},
        {name: 'status',     type: 'string'},
        {name: 'exitCode', type: 'string'},
        {name: 'exitMessage',     type: 'string'},
        {name: 'resultAvailable', type: 'boolean'}
    ]
});


var itemsPerPage = 14;

var treeGridStore = Ext.create('Wasp.store.TreeGridStore', {
    model: 'BatchTreeModel',
    remoteSort: true,
    pageSize: itemsPerPage,
    proxy: {
        type: 'ajax',
        enablePaging: true,
        url: 'getDetailsJson.do',
       	reader: {
           	type:'json',
            root: 'modelList',
            totalProperty: 'totalCount',
            model: 'BatchTreeModel',
            listeners: {
                exception: function(treeGridStore, response, op) {
                	window.location = window.location.pathname;
                }
            }
        },
        extraParams: {
        	displayParam: "All"
        }
    },
    root: {
    	id:'node-root',
    	expanded: true
    }
});

Ext.define('StepInfoModel', {
	extend: 'Ext.data.Model',
	fields: [
		{name: 'info', type: 'string'},
		{name: 'script', type: 'string'},
		{name: 'stdout', type: 'string'},
		{name: 'stderr', type: 'string'},
		{name: 'clusterReport', type: 'string'}
	]
});

var infoStore = Ext.create('Ext.data.Store',{
	model: 'StepInfoModel',
	proxy: {
		type: 'ajax',
		url: 'getStepInfoJson.do',
		reader:{
			type: 'json',
			root: 'modelList',
			enablePaging: false,
			model: 'StepInfoModel',
			listeners: {
                exception: function(infoStore, response, op) {
                	window.location = window.location.pathname;
                }
            }
			
		}
	}
});


function displayInfoData(jobExecutionId, stepExecutionId){
	 $("#wait_dialog-modal").dialog("open");
	infoStore.load({
	    params: {
	    	stepExecutionId: stepExecutionId,
	    	jobExecutionId: jobExecutionId
	    },
	    callback: function(records, operation, success) {
	    	// need to do all work in callback as loading is asynchronous and we 
	    	// can only be sure we have access to retrieved data when inside the callback which is
	    	// executed on data loading
	    	rec = infoStore.first();
   	   		win = Ext.create('Ext.window.Window', {
   			title: 'Status Information for Step Execution with id ' + stepExecutionId,
  			    header: {
  			        titlePosition: 2,
  			        titleAlign: 'center'
  			    },
  			    renderTo: Ext.getBody(),
  			    closable: true,
  			    maximizable: true,
  			  	closeAction: 'hide',
  			    width: 800,
  			    minWidth: 350,
  			    height: 600,
  			    layout: 'fit',
  			    items: [{
  			        region: 'center',
  			        xtype: 'tabpanel',
  			        items: [{
  // 			            title: 'Status',
 // 			            html: ''
 // 			        }, {
  			            title: 'Submission Info',
  			            html: rec.get('info'),
  			            autoScroll: true,
  			        }, {
  			            title: 'Script',
  			            html: rec.get('script'),
  			            autoScroll: true,
  			        }, {
  			            title: 'StdOut (tail)',
  			            html: rec.get('stdout'),
  			            autoScroll: true,
  			        }, {
  			            title: 'StdErr (tail)',
  			            html: rec.get('stderr'),
  			            autoScroll: true,
  			        }, {
  			            title: 'Cluster Report',
  			            html: rec.get('clusterReport'),
  			            autoScroll: true,
  			        }]
  			    }]
   			});
   	   	 	$("#wait_dialog-modal").dialog("close");
    		win.show();

	    },
	    scope: this
	});
};


Ext.onReady(function() {

    var tree = Ext.create('Ext.tree.Panel', {
        title: '<fmt:message key="batchViewer.panel.label"/>',
        width: $('#content').width(),
        height: $('#content').height(),
        renderTo: 'batchJobStatusViewer',
        collapsible: false,
        useArrows: true,
        rootVisible: false,
        store: treeGridStore,
        forceFit: true,
        multiSelect: false,
        columns: [{
        	xtype: 'treecolumn', //this is so we know which column will show the tree
            text: '<fmt:message key="batchViewer.nameCol.label"/>',
            width: 420,
            sortable: true,
            dataIndex: 'name'
        },{
            text: '<fmt:message key="batchViewer.idCol.label"/>',
            width: 70,
            sortable: true,
            dataIndex: 'executionId'
        }, {
            text: '<fmt:message key="batchViewer.startedCol.label"/>',
        	align: 'center',
            width: 150,
            sortable: true,
            dataIndex: 'startTime'
        }, {
        	text: '<fmt:message key="batchViewer.endedCol.label"/>',
        	align: 'center',
            width: 150,
            sortable: true,
            dataIndex: 'endTime'
        }, {
        	text: '<fmt:message key="batchViewer.statusCol.label"/>',
        	align: 'center',
            width: 50,
            sortable: true,
            dataIndex: 'exitCode'
        },{
        	text: '',
        	align: 'center',
        	sortable: false,
            xtype: 'actioncolumn',
            width: 50,
            items: [{
            	iconCls: 'infoIcon',
                tooltip: 'Get Job Information',
                tooltipType: 'title',
                getClass: function(v, meta, rec) {
                    if (rec.get('resultAvailable') == true) {
                        return 'infoIcon';
                    } else {
                        return 'infoNotAvailableIcon';
                    }
                },
                handler: function(grid, rowIndex, colIndex) {
                	// action to be performed when icon clicked
                	var rec = grid.getStore().getAt(rowIndex);
                	if (rec.get('resultAvailable') == true){
                		id = rec.get('id');
                		stepExecId = rec.get('executionId');
                		jobExecId = id.substring(2, id.indexOf('SE'));
                		displayInfoData(jobExecId, stepExecId);
                	}
                    //Ext.Msg.alert('info', 'showing Job Info for ' + rec.get('executionId') );
                }
            }]
        }],
        tbar: [{
            text: '<fmt:message key="batchViewer.showAllButton.label"/>',
            scope: this,
            handler: function (){
            	treeGridStore.getProxy().extraParams.displayParam = "All";
            	treeGridStore.loadPage(1);
            }
        }, {
            text: "<fmt:message key='batchViewer.showActiveButton.label'/> <img src='<wasp:relativeUrl value="images/gears_green_30x30.png" />' height='12' />",
            scope: this,
            handler: function (){
            	treeGridStore.getProxy().extraParams.displayParam = "Active";
            	treeGridStore.loadPage(1);
            }
        }, {
            text: "<fmt:message key='batchViewer.showCompletedButton.label'/> <img src='<wasp:relativeUrl value="images/pass.png" />' height='12' />",
            scope: this,
            handler: function (){
            	treeGridStore.getProxy().extraParams.displayParam = "Completed";
            	treeGridStore.loadPage(1);
            }
        }, {
            text: "<fmt:message key='batchViewer.showTerminatedButton.label'/> <img src='<wasp:relativeUrl value="images/stop_yellow_25x25.png" />' height='12' />",
            scope: this,
            handler: function (){
            	treeGridStore.getProxy().extraParams.displayParam = "Terminated";
            	treeGridStore.loadPage(1);
            }
        }, {
            text: "<fmt:message key='batchViewer.showFailedButton.label'/> <img src='<wasp:relativeUrl value="images/fail.png" />' height='12' />",
            scope: this,
            handler: function (){
            	treeGridStore.getProxy().extraParams.displayParam = "Failed";
            	treeGridStore.loadPage(1);
            }
        }],
        bbar: { // bottom tool bar for paging
            xtype: 'pagingtoolbar',
            emptyMsg: "<fmt:message key='batchViewer.pagingEmptyMsg.label'/>",
            pageSize: itemsPerPage,
            store: treeGridStore,
            displayInfo: true
        },
        listeners: {
        	sortchange: function(ct, column, direction, eOpts) {
        		// after sorting be sure to load the first page again
            	treeGridStore.loadPage(1);
            }
        }
    });
    jQuery(window).bind('resize', function () {
   	 tree.setWidth($('#content').width());
   	 tree.setHeight($('#content').height());
	}).trigger('resize');
    
    treeGridStore.loadPage(1);
});

</script>