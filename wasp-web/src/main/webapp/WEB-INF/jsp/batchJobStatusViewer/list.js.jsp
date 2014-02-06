<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/ext-all-dev.js"></script>
<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/packages/ext-theme-neptune/build/ext-theme-neptune.js"></script>
<script type="text/javascript"	src="/wasp/scripts/extjs/wasp/WaspNamespaceDefinition.js"></script>
<link rel="stylesheet" type="text/css" href="/wasp/css/ext-theme-neptune-all-wasp.css" />
<link rel="stylesheet" type="text/css" href="/wasp/css/treeGrid.css" />


<script type="text/javascript">

Ext.require([
    'Ext.data.*',
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
        {name: 'exitMessage',     type: 'string'}
    ]
});


var itemsPerPage = 15;

var store = Ext.create('Wasp.store.TreeGridStore', {
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
                exception: function(store, response, op) {
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


Ext.onReady(function() {

    var tree = Ext.create('Ext.tree.Panel', {
        title: 'Job Status Viewer',
        width: $('#content').width(),
        height: $('#content').height(),
        renderTo: 'batchJobStatusViewer',
        collapsible: false,
        useArrows: true,
        rootVisible: false,
        store: store,
        multiSelect: false,
        columns: [{
        	xtype: 'treecolumn', //this is so we know which column will show the tree
            text: 'Name',
            width: 400,
            sortable: true,
            dataIndex: 'name'
        },{
            text: 'Id',
            width: 70,
            sortable: true,
            dataIndex: 'executionId'
        }, {
            text: 'Started',
            width: 150,
            sortable: true,
            dataIndex: 'startTime'
        }, {
        	text: 'Ended',
            width: 150,
            sortable: true,
            dataIndex: 'endTime'
        }, {
        	text: 'Status',
            width: 145,
            sortable: true,
            dataIndex: 'exitCode'
        }, {
        	text: 'Status Message',
            sortable: false,
            width: 400,
            dataIndex: 'exitMessage'
        }],
        tbar: [{
            text: 'Show All',
            scope: this,
            handler: function (){
            	store.getProxy().extraParams.displayParam = "All";
            	store.loadPage(1);
            }
        }, {
            text: 'Show Active',
            scope: this,
            handler: function (){
            	store.getProxy().extraParams.displayParam = "Active";
            	store.loadPage(1);
            }
        }, {
            text: 'Show Completed',
            scope: this,
            handler: function (){
            	store.getProxy().extraParams.displayParam = "Completed";
            	store.loadPage(1);
            }
        }, {
            text: 'Show Terminated',
            scope: this,
            handler: function (){
            	store.getProxy().extraParams.displayParam = "Terminated";
            	store.loadPage(1);
            }
        }, {
            text: 'Show Failed',
            scope: this,
            handler: function (){
            	store.getProxy().extraParams.displayParam = "Failed";
            	store.loadPage(1);
            }
        }],
        bbar: { // bottom tool bar for paging
            xtype: 'pagingtoolbar',
            emptyMsg: "No Batch Job Executions to display",
            pageSize: itemsPerPage,
            store: store,
            displayInfo: true
        },
        listeners: {
        	sortchange: function(ct, column, direction, eOpts) {
        		// after sorting be sure to load the first page again
            	store.loadPage(1);
            }
        }
    });
    jQuery(window).bind('resize', function () {
   	 tree.setWidth($('#content').width());
   	 tree.setHeight($('#content').height());
	}).trigger('resize');
    
    store.loadPage(1);
});

</script>