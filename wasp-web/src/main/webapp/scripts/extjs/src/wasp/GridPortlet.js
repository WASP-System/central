Ext.define('Ext.wasp.GridPortlet', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.gridportlet',
    uses: [
        'Ext.data.ArrayStore'
    ],
    tabPanel : null,
    myData: [],
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
        } else {
            return '<span style="color:red;">Failed</span>';
        }
        return val;
    },
    
    clickBehaviour : function(grid, td, cellIndex, record, tr, rowIndex, e){
    	if (this.tabPanel != null)
    		this.tabPanel.setActiveTab(rowIndex + 1);
    	this.getView().deselect(record);
    },

    initComponent: function(){

        var store = Ext.create('Ext.data.ArrayStore', {
            fields: [
               {name: 'plugins'},
               {name: 'description'},
               {name: 'status'}
            ],
            data: this.myData
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
            	'cellclick' : this.clickBehaviour
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
