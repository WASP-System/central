Ext.require([
	'Ext.grid.*',
	'Ext.data.*',
	'Ext.form.field.Number',
	'Ext.form.field.Date',
	'Ext.tip.QuickTipManager',
	'Ext.selection.CheckboxModel',
	'Wasp.RowActions'
]);

function mergeLinks(records, linkfield) {
	
	var links = records[0].get(linkfield);
	
	for (var i = 1; i < records.length; i++) {
		var uuid = records[i].get(linkfield);
		links += "," + uuid.substring(uuid.lastIndexOf('/') + 1);
	}
	
	return links;
}

Ext.define('Wasp.GridPortlet', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.gridportlet',

	fields: [],
	data: [],
	columns: [],

	height: 300,

	grouping: false,
	groupfield: '',
	groupheader: '{name}',

	dllinkfld: '',
	dlcol: false,
	dlcoltip: "Download",
	
	dlselect: false,
	dlbtntxt: "Download selected",
	dlbtnalign: 'center',
	
	grpdl: false,
	grpdltip: "Download all",
	grpdlalign: 'left',

	initComponent: function () {
		var grid = this;

		Ext.tip.QuickTipManager.init();

		var myStore = Ext.create('Ext.data.ArrayStore', {
			fields: this.fields,
			autoLoad: true,
			data: this.data
		});

		if (this.grouping) {
			myStore.groupField = this.groupfield;
			myStore.group(this.groupfield);

			var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
				groupHeaderTpl: this.groupheader
			});
			Ext.apply(this, {
				features: groupingFeature
			});
		}

		if (this.dlcol && this.dllinkfld != '') {
			var actioncol = {
				xtype: 'rowactions',
				actions: [{
					iconCls: 'icon-clear-group',
					qtip: this.dlcoltip,
					callback: function (grid, record, action, idx, col, e, target) {
						window.location = record.get(grid.dllinkfld);
					}
				}],
				keepSelection: true
			};

			if (this.grpdl) {
				actioncol.groupActions = [{
					iconCls: 'icon-grid',
					qtip: this.grpdltip,
					align: this.grpdlalign,
					callback: function (grid, records, action, groupValue) {
						if (records.length > 0)
							window.location = mergeLinks(records, grid.dllinkfld);
					}
				}];
			}

			this.columns.push(actioncol);
		}

		Ext.apply(this, {
			store: myStore,
			columns: this.columns,
			height: this.height
		});

		if (this.dlselect && this.dllinkfld != '') {
			Ext.apply(this, {
				selModel: Ext.create('Ext.selection.CheckboxModel', {
					singleSelect: false,
					sortable: false,
					checkOnly: false,
					mode: 'SIMPLE',
					listeners: {
						selectionchange: function (me, selected, eOpts) {
							var dlbtn = grid.down('button[text="' + grid.dlbtntxt + '"]');
							if (selected.length == 0) {
								dlbtn.disable();
							} else {
								dlbtn.enable();
							}
						}
					}
				}),
				dockedItems: [{
					xtype: 'toolbar',
					dock: 'bottom',
					ui: 'footer',
					layout: {
						pack: this.dlbtnalign
					},
					items: [{
						minWidth: 80,
						disabled: true,
						text: this.dlbtntxt,
						handler: function (me, e) {
							var records = grid.getSelectionModel()
								.getSelection();
							if (records.length > 0) {
								window.location = mergeLinks(records, grid.dllinkfld);
							}
						}
				}]
			}]
			});
		}

		this.callParent(arguments);
	}
});