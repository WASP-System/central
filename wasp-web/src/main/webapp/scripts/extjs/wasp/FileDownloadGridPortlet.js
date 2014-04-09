Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.form.field.Number',
    'Ext.form.field.Date',
    'Ext.tip.QuickTipManager',
    'Ext.selection.CheckboxModel',
    'Wasp.RowActions'
]);

Ext.define('File', {
    extend: 'Ext.data.Model',
    idProperty: 'fid',
    fields: [
//        {name: 'fgid', type: 'int'},
        {name: 'fgname', type: 'string'},
        {name: 'fglink', type: 'string'},
        {name: 'fid', type: 'int'},
        {name: 'fname', type: 'string'},
        {name: 'md5', type: 'string'},
        {name: 'size', type: 'int'},
        //{name: 'updated', type: 'date', dateFormat:'m/d/Y'},
        {name: 'link', type: 'string'}
    ]
});

Ext.define('Wasp.FileDownloadGridPortlet', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.fdgridportlet',
//    uses: [
//        'Ext.grid.*',
//        'Ext.data.*',
//        'Ext.selection.CheckboxModel',
//        'Wasp.Component'
//    ],
    fgListStr: "",
    
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
    	var grid = this;

        var store = Ext.create('Ext.data.JsonStore', {
//            fields: [
////               {name: 'filename'},
////               {name: 'checksum'},
////               {name: 'size'},
////               {name: 'link'}
//            	'id', 'link', 'name'
//            ],
//            data: this.filegroupList
			model: 'File',
			groupField: 'fgname',
			proxy: {
				type: 'ajax',
				url : '../../getFileGroupsDetailJson.do?fglist='+this.fgListStr,
				reader: {
					type: 'json'
				},
				afterRequest: function(req, res) {
					//console.log("Ahoy!", req.operation.response);    
				}
			},
			autoLoad : true
        });

        var sm = Ext.create('Ext.selection.CheckboxModel', {
			singleSelect: false,
			sortable: false,
			checkOnly: false//,
//			renderer:function(value,metaData,record,rowIndex,colIndex,store,view){
//				metaData.tdCls = Ext.baseCSSPrefix + 'grid-cell-special';
//				return '<div class="' + Ext.baseCSSPrefix + 'grid-row-checker">&#160;</div>';   
//			}
		});
		
		var groupingFeature = Ext.create('Ext.grid.feature.Grouping',{
			groupHeaderTpl: 'File Group: {name} ({rows.length} File{[values.rows.length > 1 ? "s" : ""]})'
		});

		Ext.tip.QuickTipManager.init();
        
        Ext.apply(this, {
            //height: 300,
            height: this.height,
            store: store,
            stripeRows: true,
            columnLines: true,
            forceFit: true,
            selModel: sm,
//            listeners: {
//            	'cellclick' : this.onCellClick
//            },
			features: [
            	groupingFeature
//			{
//				id: 'group',
//				ftype: 'grouping',
//				groupHeaderTpl: '{name}',
//				hideGroupedHeader: true,
//				enableGroupingMenu: false
//			}
			],
            columns: [
	            {
	            	text: 'File',
		            flex: 1,
		            //tdCls: 'filehandle',
		           // sortable: true,
		            dataIndex: 'fname'//,
		            //hideable: false
	            },
	            {
					header: 'MD5 Checksum',
					width: 400,
					//sortable: false,
					dataIndex: 'md5'
				},
				{
	                text   : 'Size',
	                width: 100,
	                sortable : true,
	                dataIndex: 'size'
	            },
//	            {
////	            	text: 'Download',
//	            	width: 120,
//	            	sortable: false,
//	            	dataIndex: 'downloading', 
//		            xtype: 'componentcolumn', 
//		 
//		            renderer: function(value, m, record) { 
//		                if (value === 100) { 
//		                    return 'Complete'; 
//		                }
//		                
//		                if (value === -1) { 
//		                    return 'Fail'; 
//		                }
//		 
//		                if (Ext.isDefined(value)) { 
//		                    setTimeout(function() { 
//		                        // This works because calling set() causes a view refresh 
//		                        record.set('downloading', value + 5); 
//		                    }, 250); 
//		 
//		                    return { 
//		                        animate: false, 
//		                        value: value / 100, 
//		                        xtype: 'progressbar' 
//		                    }; 
//		                } 
//		 
//		                return { 
//		                    text: 'Download', 
//		                    xtype: 'button', 
//		 
//		                    handler: function() {
//		                    	$.fileDownload(record.get('link'))
//		                    		.done(function () { 
//		                    			//alert('File download a success!'); 
//		                    			record.set('downloading', 100); 
//		                    		})
//		                    		.fail(function () {
//										//alert('File download failed!');
//		                    			record.set('downloading', -1)
//									});
//		                        //record.set('downloading', 0); 
//		                    } 
//		                }; 
//		            }
//	            },
	            {
					xtype: 'rowactions',
					actions: [{
						iconCls: 'icon-clear-group',
						qtip: 'Download',
						callback: function (grid, record, action, idx, col, e, target) {
							//Ext.Msg.alert('Download File', record.get('link'));
							window.location = record.get('link');
						}
					}],
					keepSelection: true,
					groupActions: [{
						iconCls: 'icon-grid',
						qtip: 'Download Files in the Group',
						align: 'left',
						callback: function (grid, records, action, groupValue) {
							//Ext.Msg.alert('Download File Group',  records[0].get('fglink'));
							window.location = records[0].get('fglink');
						}
					}]
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
					text: 'Download Selected',
					handler: function () {
						var record = grid.getSelectionModel().getSelection();
						if (record.length == 0) {
							Ext.MessageBox.show({
								title: "Alert",
								msg: "Please select at least one file."
							})
							return;
						} else {
							var links = record[0].get("link");
							for (var i = 1; i < record.length; i++) {
								var uuid = record[i].get("link");
								links += "," + uuid.substring(uuid.lastIndexOf('/')+1);
							}
//							Ext.MessageBox.show({
//								title: "links selected",
//								msg: links
//							});
							window.location = links;
						}
					}
				}]
			}]
        });

        this.callParent(arguments);
    }
});
