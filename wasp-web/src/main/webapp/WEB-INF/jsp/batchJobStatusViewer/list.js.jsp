<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/ext-all-dev.js"></script>
<script type="text/javascript" src="http://extjs-public.googlecode.com/svn/tags/extjs-4.2.1/release/packages/ext-theme-neptune/build/ext-theme-neptune.js"></script>
<link rel="stylesheet" type="text/css" href="/wasp/css/ext-theme-neptune-all-wasp.css" />

<style type="text/css">
    .task {
        background-image: url(/wasp/css/ext/images/icons/fam/cog.gif) !important;
    }
    .task-folder {
        background-image: url(/wasp/css/ext/images/icons/fam/folder_go.gif) !important;
    }
</style>


<script type="text/javascript">

Ext.require([
    'Ext.data.*',
    'Ext.tree.*'
]);

//we want to setup a model and store instead of using dataUrl
Ext.define('Task', {
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


Ext.onReady(function() {
    

    //Ext.ux.tree.TreeGrid is no longer a Ux. You can simply use a tree.TreePanel
    var tree = Ext.create('Ext.tree.Panel', {
        title: 'Job Status Viewer',
        width: $('#content').width(),
        height: $('#content').height(),
        renderTo: 'batchJobStatusViewer',
        collapsible: true,
        useArrows: true,
        rootVisible: false,
        store: Ext.create('Ext.data.TreeStore', {
            model: 'Task',
            proxy: {
                type: 'ajax',
                url: 'getDetailsJson.do'
                
            },
            root: {
            	text: '.',
            	id:'node-root',
            	expanded: true
            }, 
            sortOnLoad: true, 
            sorters: { property: 'executionId', direction : 'DESC' }
        }),
        multiSelect: true,
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
            dataIndex: 'executionId',
            folderSort: true
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
            width: 450,
            dataIndex: 'exitMessage'
        }]
    });
    jQuery(window).bind('resize', function () {
   	 tree.setWidth($('#content').width());
   	 tree.setHeight($('#content').height());
	}).trigger('resize');
});

</script>