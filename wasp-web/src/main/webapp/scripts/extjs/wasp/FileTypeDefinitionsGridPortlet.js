Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.form.field.Number',
    'Ext.form.field.Date',
    'Ext.tip.QuickTipManager',
    'Ext.selection.CheckboxModel',
    'Wasp.RowActions'
]);

Ext.define('Wasp.FileTypeDefinitionsGridPortlet', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.ftdgridportlet',

    myData: { records: [{ "FileType": "Type1", "Description":"Desc1"}] },

    initComponent: function(){
    	var grid = this;

        var myStore = Ext.create('Ext.data.JsonStore', {
			root: 'records',
			fields: [ 'FileType', 'Description' ],
			autoLoad: true,
			data: { records: [{ "FileType": "Type1", "Description":"Desc1"}] }
        });

        Ext.apply(this, {
            store: myStore,
            columns: [
	            {
	            	text: 'File type',
		            width: 200,
		            dataIndex: 'FileType'
	            },
	            {
					header: 'Description',
					width: 2000,
					dataIndex: 'Description'
				}
			],
			renderTo:'fileTypeDescription-grid', 
			height: 300
		});

        this.callParent(arguments);
    }
});
