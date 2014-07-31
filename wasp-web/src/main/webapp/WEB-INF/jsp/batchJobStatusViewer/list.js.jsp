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
        background-image: url(<wasp:relativeUrl value="images/information_unavailable_30x30.png" />) !important;
        background-size: 15px 15px;
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
        {name: 'exitMessage',     type: 'string'}
    ]
});


var itemsPerPage = 14;

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

var win;

function displayInfo(){

	if (!win) {
	    win = Ext.create('widget.window', {
	        title: 'Layout Window with title <em>after</em> tools',
	        header: {
	            titlePosition: 2,
	            titleAlign: 'center'
	        },
	        closable: true,
	        closeAction: 'hide',
	        width: 600,
	        minWidth: 350,
	        height: 350,
	        tools: [{type: 'pin'}],
	        layout: {
	            type: 'border',
	            padding: 5
	        },
	        items: [{
	            region: 'center',
	            xtype: 'tabpanel',
	            items: [{
	                // LTR even when example is RTL so that the code can be read
	                rtl: false,
	                title: 'Bogus Tab',
	                html: '<p>Window configured with:</p><pre style="margin-left:20px"><code>header: {\n    titlePosition: 2,\n    titleAlign: "center"\n},\ntools: [{type: "pin"}],\nclosable: true</code></pre>'
	            }, {
	                title: 'Another Tab',
	                html: 'Hello world 2'
	            }, {
	                title: 'Closable Tab',
	                html: 'Hello world 3',
	                closable: true
	            }]
	        }]
	    });
	}
	
	if (win.isVisible()) {
	    win.hide();
	} else {
	    win.show();
	}
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
        store: store,
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
            width: 150,
            sortable: true,
            dataIndex: 'startTime'
        }, {
        	text: '<fmt:message key="batchViewer.endedCol.label"/>',
            width: 150,
            sortable: true,
            dataIndex: 'endTime'
        }, {
        	text: '<fmt:message key="batchViewer.statusCol.label"/>',
            width: 70,
            sortable: true,
            dataIndex: 'exitCode'
        },{
        	text: 'Info',
        	sortable: false,
            xtype: 'actioncolumn',
            width: 50,
            items: [{
            	iconCls: 'infoIcon',
                tooltip: 'Get Job Information',
                getClass: function(v, meta, rec) {
                    if (rec.get('leaf') == true) {
                        return 'infoIcon';
                    } else {
                        return 'infoNotAvailableIcon';
                    }
                },
                handler: function(grid, rowIndex, colIndex) {
                	var rec = grid.getStore().getAt(rowIndex);
                	if (rec.get('leaf') == true)
                		displayInfo();
                    //Ext.Msg.alert('info', 'showing Job Info for ' + rec.get('executionId') );
                }
            }]
        }],
        tbar: [{
            text: '<fmt:message key="batchViewer.showAllButton.label"/>',
            scope: this,
            handler: function (){
            	store.getProxy().extraParams.displayParam = "All";
            	store.loadPage(1);
            }
        }, {
            text: "<fmt:message key='batchViewer.showActiveButton.label'/> <img src='<wasp:relativeUrl value="images/gears_green_30x30.png" />' height='12' />",
            scope: this,
            handler: function (){
            	store.getProxy().extraParams.displayParam = "Active";
            	store.loadPage(1);
            }
        }, {
            text: "<fmt:message key='batchViewer.showCompletedButton.label'/> <img src='<wasp:relativeUrl value="images/pass.png" />' height='12' />",
            scope: this,
            handler: function (){
            	store.getProxy().extraParams.displayParam = "Completed";
            	store.loadPage(1);
            }
        }, {
            text: "<fmt:message key='batchViewer.showTerminatedButton.label'/> <img src='<wasp:relativeUrl value="images/stop_yellow_25x25.png" />' height='12' />",
            scope: this,
            handler: function (){
            	store.getProxy().extraParams.displayParam = "Terminated";
            	store.loadPage(1);
            }
        }, {
            text: "<fmt:message key='batchViewer.showFailedButton.label'/> <img src='<wasp:relativeUrl value="images/fail.png" />' height='12' />",
            scope: this,
            handler: function (){
            	store.getProxy().extraParams.displayParam = "Failed";
            	store.loadPage(1);
            }
        }],
        bbar: { // bottom tool bar for paging
            xtype: 'pagingtoolbar',
            emptyMsg: "<fmt:message key='batchViewer.pagingEmptyMsg.label'/>",
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