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
    'Ext.grid.*',
    'Ext.tree.*',
    'Ext.tip.*'
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
    Ext.tip.QuickTipManager.init();

    var store = Ext.create('Ext.data.TreeStore', {
        model: 'Task',
        proxy: {
            type: 'ajax',
            //the store will get the content from the .json file
            url: 'getDetailsJson.do'
        },
        root: {
        	nodeType: 'async',
        	text: '.',
        	id:'node-root'
        }, 
        sortOnLoad: true, 
        sorters: { property: 'executionId', direction : 'DESC' }
    });

    //Ext.ux.tree.TreeGrid is no longer a Ux. You can simply use a tree.TreePanel
    var tree = Ext.create('Ext.tree.Panel', {
        title: 'Core Team Projects',
        width: $('#content').width(),
        height: $('#content').height(),
        renderTo: 'batchJobStatusViewer',
        collapsible: true,
        useArrows: true,
        rootVisible: false,
        store: store,
        multiSelect: true,
        columns: [{
        	xtype: 'treecolumn', //this is so we know which column will show the tree
            text: 'Name',
            width: 300,
            sortable: true,
            dataIndex: 'name'
        },{
            text: 'Id',
            width: 75,
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
            width: 140,
            sortable: true,
            dataIndex: 'exitCode'
        }, {
        	text: 'Status Message',
            sortable: false,
            width: 500,
            dataIndex: 'exitMessage'
        }]
    });
    jQuery(window).bind('resize', function () {
   	 tree.setWidth($('#content').width());
   	 tree.setHeight($('#content').height());
	}).trigger('resize');
});


<%--
json response is:

 {
    "text": ".",
    "children": [
        {
            "task": "Project: Shopping",
            "duration": 13.25,
            "user": "Tommy Maintz",
            "iconCls": "task-folder",
            "expanded": true,
            "children": [
                {
                    "task": "Housewares",
                    "duration": 1.25,
                    "user": "Tommy Maintz",
                    "iconCls": "task-folder",
                    "children": [
                        {
                            "task": "Kitchen supplies",
                            "duration": 0.25,
                            "user": "Tommy Maintz",
                            "leaf": true,
                            "iconCls": "task"
                        }, {
                            "task": "Groceries",
                            "duration": .4,
                            "user": "Tommy Maintz",
                            "leaf": true,
                            "iconCls": "task",
                            "done": true
                        }, {
                            "task": "Cleaning supplies",
                            "duration": .4,
                            "user": "Tommy Maintz",
                            "leaf": true,
                            "iconCls": "task"
                        }, {
                            "task": "Office supplies",
                            "duration": .2,
                            "user": "Tommy Maintz",
                            "leaf": true,
                            "iconCls": "task"
                        }
                    ]
                }, {
                    "task": "Remodeling",
                    "duration": 12,
                    "user": "Tommy Maintz",
                    "iconCls": "task-folder",
                    "expanded": true,
                    "children": [
                        {
                            "task": "Retile kitchen",
                            "duration": 6.5,
                            "user": "Tommy Maintz",
                            "leaf": true,
                            "iconCls": "task"
                        }, {
                            "task": "Paint bedroom",
                            "duration": 2.75,
                            "user": "Tommy Maintz",
                            "iconCls": "task-folder",
                            "children": [
                                {
                                    "task": "Ceiling",
                                    "duration": 1.25,
                                    "user": "Tommy Maintz",
                                    "iconCls": "task",
                                    "leaf": true
                                }, {
                                    "task": "Walls",
                                    "duration": 1.5,
                                    "user": "Tommy Maintz",
                                    "iconCls": "task",
                                    "leaf": true
                                }
                            ]
                        }, {
                            "task": "Decorate living room",
                            "duration": 2.75,
                            "user": "Tommy Maintz",
                            "leaf": true,
                            "iconCls": "task",
                            "done": true
                        }, {
                            "task": "Fix lights",
                            "duration": .75,
                            "user": "Tommy Maintz",
                            "leaf": true,
                            "iconCls": "task",
                            "done": true
                        }, {
                            "task": "Reattach screen door",
                            "duration": 2,
                            "user": "Tommy Maintz",
                            "leaf": true,
                            "iconCls": "task"
                        }
                    ]
                }
            ]
        }, {
            "task": "Project: Testing",
            "duration": 2,
            "user": "Core Team",
            "iconCls": "task-folder",
            "children": [
                {
                    "task": "Mac OSX",
                    "duration": 0.75,
                    "user": "Tommy Maintz",
                    "iconCls": "task-folder",
                    "children": [
                        {
                            "task": "FireFox",
                            "duration": 0.25,
                            "user": "Tommy Maintz",
                            "iconCls": "task",
                            "leaf": true
                        }, {
                            "task": "Safari",
                            "duration": 0.25,
                            "user": "Tommy Maintz",
                            "iconCls": "task",
                            "leaf": true
                        }, {
                            "task": "Chrome",
                            "duration": 0.25,
                            "user": "Tommy Maintz",
                            "iconCls": "task",
                            "leaf": true
                        }
                    ]
                }, {
                    "task": "Windows",
                    "duration": 3.75,
                    "user": "Darrell Meyer",
                    "iconCls": "task-folder",
                    "children": [
                        {
                            "task": "FireFox",
                            "duration": 0.25,
                            "user": "Darrell Meyer",
                            "iconCls": "task",
                            "leaf": true
                        }, {
                            "task": "Safari",
                            "duration": 0.25,
                            "user": "Darrell Meyer",
                            "iconCls": "task",
                            "leaf": true
                        }, {
                            "task": "Chrome",
                            "duration": 0.25,
                            "user": "Darrell Meyer",
                            "iconCls": "task",
                            "leaf": true
                        }, {
                            "task": "Internet Explorer",
                            "duration": 3,
                            "user": "Darrell Meyer",
                            "iconCls": "task",
                            "leaf": true
                        }
                    ]
                }, {
                    "task": "Linux",
                    "duration": 0.5,
                    "user": "Aaron Conran",
                    "iconCls": "task-folder",
                    "children": [
                        {
                            "task": "FireFox",
                            "duration": 0.25,
                            "user": "Aaron Conran",
                            "iconCls": "task",
                            "leaf": true
                        }, {
                            "task": "Chrome",
                            "duration": 0.25,
                            "user": "Aaron Conran",
                            "iconCls": "task",
                            "leaf": true
                        }
                    ]
                }
            ]
        }
    ]
}

--%>

</script>