Ext.define('Wasp.FileDownloadGridPortlet', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.gridportlet',
    uses: [
        'Ext.data.ArrayStore',
        'Ext.selection.CheckboxModel',
        'Wasp.Component'
    ],
    tabPanel : null,
    fileDownloadArray: [],
    height: 300,
    /**
     * Custom function used for column renderer
     * @param {Object} val
     */
    
    onCellClick : function(grid, td, cellIndex, record, tr, rowIndex, e){
//    	if (this.tabPanel != null){
//    		var tabId = grid.getStore().getAt(rowIndex).get("tabId");
//    		if (tabId != null)
//    			this.tabPanel.setActiveTab(tabId);
//    	}
//    	this.getView().deselect(record);
    },

    initComponent: function(){

        var store = Ext.create('Ext.data.ArrayStore', {
            fields: [
               {name: 'checked'},
               {name: 'filename'},
               {name: 'checksum'},
               {name: 'size'},
               {name: 'link'}
            ],
            data: this.fileDownloadArray
        });

        var sm = Ext.create('Ext.selection.CheckboxModel', {
			singleSelect: false,
			sortable: false,
			checkOnly: true//,
//			renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
//				metaData.tdCls = Ext.baseCSSPrefix + 'grid-cell-special';
//				return '<div class="' + Ext.baseCSSPrefix + 'grid-row-checker">&#160;</div>';   
//			}
		});
        
        Ext.apply(this, {
            //height: 300,
            height: this.height,
            store: store,
            stripeRows: true,
            columnLines: true,
            forceFit: true,
            selModel: sm,
            listeners: {
            	'cellclick' : this.onCellClick
            },
            columns: [
//	            {
//	            	id: 'checked',
//	            	dataIndex: 'checked', 
//					xtype: 'componentcolumn', 
//					renderer: function(enabled) { 
//						return { 
//							checked: enabled, 
//							xtype: 'checkbox' 
//						}; 
//					}
//	            },
	            {
	                id       :'filename',
	                text   : 'Name',
	                width    : 200,
	                sortable : true,
	                dataIndex: 'filename'
	            },{
	                text   : 'MD5 Checksum',
	                //width: 120,
	                flex: 1,
	                sortable : true,
	                dataIndex: 'checksum'
	            },{
	                text   : 'Size',
	                width: 120,
	                sortable : true,
	                dataIndex: 'size'
	            }
			],
			dockedItems: [{
				xtype: 'toolbar',
				dock: 'bottom',
				ui: 'footer',
				layout: {
					pack: 'center'
				},
				items: [{
					minWidth: 80,
					text: 'Download Selected'
				}]
			}]
        });

        this.callParent(arguments);
    }
});
