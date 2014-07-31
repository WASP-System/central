Ext.define('Wasp.PluginSummaryGridPortlet', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.psgridportlet',
    uses: [
        'Ext.data.ArrayStore'
    ],
    tabPanel : null,
    statusData: [],
    height: 300,
    /**
     * Custom function used for column renderer
     * @param {Object} val
     */
    status: function(val) {
    	if (val == 'COMPLETED') {
            return '<span style="color:green;">Completed</span>';
        } else if (val == 'STARTED') {
            return '<span style="color:green;">Running</span>';
        } else if (val == 'UNKNOWN') {
            return '<span style="color:orange;">Not Invoked</span>';
        } else if (val == 'NOT_APPLICABLE') {
            return '<span style="color:orange;">N/A</span>';
        } else {
            return '<span style="color:red;">Failed</span>';
        }
        return val;
    },
    
    onCellClick : function(grid, td, cellIndex, record, tr, rowIndex, e){
    	if (this.tabPanel != null){
    		var tabId = grid.getStore().getAt(rowIndex).get("tabId");
    		if (tabId != null)
    			this.tabPanel.setActiveTab(tabId);
    	}
    	this.getView().deselect(record);
    },

    initComponent: function(){

        var store = Ext.create('Ext.data.ArrayStore', {
            fields: [
               {name: 'plugins'},
               {name: 'description'},
               {name: 'status'},
               {name: 'tabId'}
            ],
            data: this.statusData
        });

        Ext.apply(this, {
            //height: 300,
            height: this.height,
            store: store,
            stripeRows: true,
            columnLines: true,
            forceFit: true,
            selModel: {  allowDeselect: true }, 
            listeners: {
            	'cellclick' : this.onCellClick
            },
            columns: [{
                id       :'plugins',
                text   : 'Name',
                width    : 200,
                sortable : true,
                dataIndex: 'plugins'
            },{
                text   : 'Description',
                //width: 120,
                flex: 1,
                sortable : true,
                dataIndex: 'description'
            },{
                text   : 'Status',
                width    : 150,
                sortable : true,
                renderer : this.status,
                dataIndex: 'status'
            }]
        });

        this.callParent(arguments);
    }
});
