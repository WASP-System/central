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

	dlcol: false,
	dllinkfld: '',
	dlcoltip: "Download",

	dlselect: false,
	dlbtntxt: "Download selected",
	dlbtnalign: 'center',

	grpdl: false,
	grpdltip: "Download all",
	grpdlalign: 'left',

	statusfld: null,

	gbucsccol: false,
	gbucscfld: '',
	gbucsctip: 'View in UCSC Genome Browser',

	/**
	 * Custom function used for column renderer
	 * @param {Object} val
	 */
	status: function (val) {
		if (val.match(/complete/i) != null) {
			return '<span style="color:green;">' + val + '</span>';
		} else if (val.match(/start/i) != null) {
			return '<span style="color:blue;">' + val + '</span>';
		} else if (val.match(/pending/i) != null) {
			return '<span style="color:pink;">' + val + '</span>';
		} else if (val.match(/unknown/i) != null) {
			return '<span style="color:orange;">' + val + '</span>';
		} else if (val.match(/fail/i) != null) {
			return '<span style="color:red;">' + val + '</span>';
		} else {
			return '<span style="color:gold;">' + val + '</span>';
		}
	},

	listeners: {
		cellclick: function (view, td, cellIndex, record, tr, rowIndex, e, eOpts) {
			//Ext.Msg.alert('Selected Record', 'td : ' + td + ' tr: ' + tr);
		},
		celldblclick: function (view, td, cellIndex, record, tr, rowIndex, e, eOpts) {
			//Ext.Msg.alert('Selected Record', 'td : ' + td + ' tr: ' + tr);
		}
	},

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

		var actioncol = {
			xtype: 'rowactions',
			header: 'Actions',
			actions: [],
			keepSelection: true
		};
		
		if (this.dlcol && this.dllinkfld != '') {
			actioncol.actions.push({
				iconCls: 'icon-clear-group',
				qtip: this.dlcoltip,
				callback: function (grid, record, action, idx, col, e, target) {
					window.location = record.get(grid.dllinkfld);
				}
			});

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

		}

		if (this.gbucsccol && this.gbucscfld != '') {
			actioncol.actions.push({
				iconCls: 'icon-gb-ucsc',
				qtip: this.gbucsctip,
				callback: function (grid, record, action, idx, col, e, target) {
					window.location = record.get(grid.gbucscfld);
				}
			});
		}

		if (actioncol.actions.length > 0)
			this.columns.push(actioncol);

		if (this.statusfld != null) {
			this.columns.forEach(function (element, index, array) {
				if (element.dataIndex == grid.statusfld) {
					Ext.apply(element, {
						renderer: grid.status
					});
				}
			});
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