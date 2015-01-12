Ext.require(['Ext.grid.*', 'Ext.data.*', 'Ext.form.field.Number',
		'Ext.form.field.Date', 'Ext.tip.QuickTipManager', 'Ext.tip.ToolTip',
		'Ext.selection.CheckboxModel', 'Wasp.RowActions']);

function mergeDownloadLinks(records, linkfield) {

	var links = records[0].get(linkfield);

	if (records.length > 1) {
		// trim off the file name if any
		var pattUUID = /[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}/i;
		var n = links.search(pattUUID);
		links = links.substring(0,n+36);
		
		for (var i = 1; i < records.length; i++) {
			var uuid = records[i].get(linkfield);
			n = uuid.search(pattUUID);
			links += "," + uuid.substring(n, n+36);//uuid.lastIndexOf('/') + 1);
		}
		links += "/FileGroup.zip";
	}

	return links;
}

var rowHeight = 30, gridHeaderHeight = 30;
var minGridHeight = 200, maxGridHeight = 650;

Ext.define('Wasp.GridPortlet', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.gridportlet',

	fields : [],
	data : [],
	columns : [],

	actionset : [],

	height : minGridHeight,

	grouping : false,
	groupfield : '',
	groupheader : '{name}',
	groupactiondatamap : {},

	statusfld : null,

	/**
	 * Custom function used for column renderer
	 * 
	 * @param {Object}
	 *            val
	 */
	// DataTabViewing.Status values: COMPLETED, STARTED, PENDING, FAILED, UNKNOWN, NOT_APPLICABLE, INCOMPLETE
	status : function(val) {
		if (val == "COMPLETED") {
			return '<span style="color:green;">Completed</span>';
		} else if (val == "STARTED") {
			return '<span style="color:green;">Started</span>';
		} else if (val == "PENDING") {
			return '<span style="color:green;">Pending</span>';
		} else if (val == "FAILED") {
			return '<span style="color:red;">Failed</span>';
		} else if (val == "UNKNOWN") {
			return '<span style="color:orange;">Unknown</span>';
		} else if (val == "NOT_APPLICABLE") {
			return '<span style="color:orange;">N/A</span>';
		} else if (val == "INCOMPLETE") {
			return '<span style="color:orange;">Incomplete</span>';
		} else {
			return "";
		}
	},

	listeners : {
		cellclick : function(view, td, cellIndex, record, tr, rowIndex, e,
				eOpts) {
			// Ext.Msg.alert('Selected Record', 'td : ' + td + ' tr: ' + tr);
		},
		celldblclick : function(view, td, cellIndex, record, tr, rowIndex, e,
				eOpts) {
			// Ext.Msg.alert('Selected Record', 'td : ' + td + ' tr: ' + tr);
		},
		headerclick: function( grid, col, e ) {
		},
		viewready : function(grid) {
			var view = grid.view;

			// record the current cellIndex
			grid.mon(view, {
				uievent : function(type, view, cell, recordIndex, cellIndex, e) {
					grid.cellIndex = cellIndex;
					grid.recordIndex = recordIndex;
				}
			});

			grid.tip = Ext.create('Ext.tip.ToolTip', {
				target : view.el,
				delegate : '.x-grid-cell',
				trackMouse : true,
				maxWidth : 700,
				renderTo : Ext.getBody(),
				listeners : {
					beforeshow : function updateTipBody(tip) {
						if (!Ext.isEmpty(grid.cellIndex)
								&& grid.cellIndex !== -1) {
							var header = grid.headerCt.getGridColumns()[grid.cellIndex];
							if (!header.shownInTtp
									|| header.xtype === 'rowactions') {
								return false;
							}
							var val = grid.getStore().getAt(grid.recordIndex)
									.get(header.dataIndex);
							tip.update(header.xtype == 'datecolumn'
									? Ext.util.Format.date(val, header.format)
									: val);
						}
					}
				}
			});
		}
	},

	initToolTip : function(view) {
		// var view = this.view.getView();
		this.toolTip = Ext.create('Ext.tip.ToolTip', {
			target : view.el,
			delegate : view.cellSelector,
			trackMouse : true,
			renderTo : Ext.getBody(),
			listeners : {
				beforeshow : function(tip) {
					var trigger = tip.triggerElement, parent = tip.triggerElement.parentElement, columnTitle = view
							.getHeaderByCell(trigger).text, columnDataIndex = view
							.getHeaderByCell(trigger).dataIndex, columnText = view
							.getRecord(parent).get(columnDataIndex).toString();
					if (columnText) {
						tip.update("<b>" + columnTitle + ":</b> " + columnText);
					} else {
						return false;
					}
				}
			}
		});
	},

	initComponent : function() {
		var grid = this;

		Ext.tip.QuickTipManager.init();

		var myStore = Ext.create('Ext.data.ArrayStore', {
					fields : this.fields,
					autoLoad : true,
					data : this.data
				});

		var rowCnt = myStore.getTotalCount();

		// enable grouping view
		if (this.grouping) {
			myStore.groupField = this.groupfield;
			myStore.group(this.groupfield);

			var groupingFeature = Ext.create('Ext.grid.feature.Grouping', {
						groupHeaderTpl : this.groupheader
					});
			Ext.apply(this, {
						features : groupingFeature
					});

			rowCnt += myStore.getGroups().length;
		}

		var actioncol = {
			xtype : 'rowactions',
			header : 'Actions',
			minWidth : 80,
			actions : [],
			callbacks : {
			// 'icon-gb-ucsc': function(grid, record, action, row, col) {
			// window.open(record.get(grid.gblink), '_blank');
			// },
			// 'icon-gb-ensembl': function(grid, record, action, row, col) {
			// window.open(record.get(grid.gblink), '_blank');
			// },
			// 'icon-gb-igv': function(grid, record, action, row, col) {
			// window.open(record.get(grid.gblink), '_blank');
			// }
			},
			callbacks2: {},
			sortable: false,
			keepSelection : true
		};

		for (var akey in this.actionset) {
			var action = this.actionset[akey];
			var strcbfunc;
			if (action.callbackFunctionType === 'DOWNLOAD') {
				strcbfunc = '{"'
						+ action.iconClassName
						+ '": "function(grid, record, action, row, col){linktext = record.get(\'cb'
						+ action.icnHashCode.toString().replace('-', '_')
						+ '\');window.location=linktext;}"}';

				var fstr = 'function(grid, record, action, row, col){linktext = record.get(\'cb'
						+ action.icnHashCode.toString().replace('-', '_')
						+ '\');window.prompt(\'Copy download link to clipboard: Ctrl+C, Enter\', linktext);return false;}';
				var configobj2 = JSON.parse('{"' + action.iconClassName + '": [] }');
				configobj2[action.iconClassName].push({text:'Show link', handler: new Function('return ' + fstr)()});
				Ext.apply(actioncol.callbacks2, configobj2);
			} else if (action.callbackFunctionType === 'OPEN_IN_NEW_BROWSER_WIN') {
				strcbfunc = '{"'
						+ action.iconClassName
						+ '": "function(grid, record, action, row, col){window.open(record.get(\'cb'
						+ action.icnHashCode.toString().replace('-', '_')
						+ '\'), \'_blank\');}"}';
			} else if (action.callbackFunctionType === 'OPEN_IN_CSS_WIN') {
				strcbfunc = '{"'
						+ action.iconClassName
						+ '": "function(grid, record, action, row, col){getPanelDisplayWindowForFilegroup(record.get(\'cb'
						+ action.icnHashCode.toString().replace('-', '_')
						+ '\'));}"}';
			} else {
				strcbfunc = '{"'
						+ action.iconClassName
						+ '": "function(grid, record, action, row, col){alert(\'Action type '
						+ action.callbackFunctionType
						+ ' is not supported.\');}"}';
			}

			var configobj = JSON.parse(strcbfunc, function(key, value) {
						if (value && (typeof value === 'string')
								&& value.indexOf("function") === 0) {
							// we can only pass a function as string in JSON ==>
							// doing a real function
							// eval("var jsFunc = " + value);
							var jsFunc = new Function('return ' + value)();
							return jsFunc;
						}

						return value;
					});
			// var configobj = JSON.parse('{ "'+action.iconClassName+'":
			// function(grid, record, action, row, col) {
			// eval('+action.callbackContent+'); } }');
			Ext.apply(actioncol.callbacks, configobj);
			
//			if (strcbfunc2) {
//				var configobj2 = JSON.parse(strcbfunc2, function(key, value) {
//						if (value && (typeof value === 'string')
//								&& value.indexOf("function") === 0) {
//							// we can only pass a function as string in JSON ==>
//							// doing a real function
//							// eval("var jsFunc = " + value);
//							var jsFunc = new Function('return ' + value)();
//							return jsFunc;
//						}
//
//						return value;
//					});
//				actioncol.callbacks2.push(
//					{	
//						text: 'Show link',
//						handler: configobj2
//					}
//				);
//				Ext.apply(actioncol.callbacks2, configobj2);
//			}
			
			actioncol.actions.push({
						iconIndex : 'icon'
								+ action.icnHashCode.toString().replace('-',
										'_'),
						qtipIndex : 'tip'
								+ action.icnHashCode.toString().replace('-',
										'_'),
						hideIndex : 'hide'
								+ action.icnHashCode.toString().replace('-',
										'_')

					});

			if (action.group == true && action.callbackFunctionType === 'DOWNLOAD') {
				actioncol.groupActions = [{
					iconCls : action.groupIconClassName,
					qtip : action.groupTooltip,
					align : action.groupAlign.toLowerCase(),
					callback : function(grid, records, groupAction, groupValue) {
						if (records.length > 0 && groupAction in grid.groupactiondatamap)
							linktext = mergeDownloadLinks(records, grid.groupactiondatamap[groupAction]);
							window.location = linktext;
							
					},
					callback2 : [ 
						{
							text: 'Show link',
							handler: function(grid, records, groupAction, groupValue) {
								linktext = mergeDownloadLinks(records, grid.groupactiondatamap[groupAction]);
								window.prompt('Copy download link to clipboard: Ctrl+C, Enter', linktext);
							}
						}
					]
				}];
				grid.groupactiondatamap[action.groupIconClassName] = 'cb' + action.icnHashCode.toString().replace('-', '_');
			}
		};

		// Add download action buttons to the action column
		// if (this.dlcol && this.dllinkfld != '') {
		// actioncol.actions.push({
		// iconCls: 'icon-download',
		// qtip: this.dlcoltip,
		// hideIndex: this.hidedl,
		// callback: function (grid, record, action, idx, col, e, target) {
		// window.location = record.get(grid.dllinkfld);
		// }
		// });
		//
		// // If grouping view is set, add group download button
		// if (this.grpdl) {
		// actioncol.groupActions = [{
		// iconCls: 'icon-group-download',
		// qtip: this.grpdltip,
		// align: this.grpdlalign,
		// callback: function (grid, records, action, groupValue) {
		// if (records.length > 0)
		// window.location = mergeDownloadLinks(records, grid.dllinkfld);
		// }
		// }];
		// }
		//
		// }

		// Add genome browser buttons to the action column
		// if (this.gbcol && this.gblink != '') {
		// actioncol.actions.push({
		// iconIndex: this.gbtype,
		// qtipIndex: this.gbttp,
		// hideIndex: this.hidegb
		// });
		// }

		// Avoid multiple insertion of action column
		if (actioncol.actions.length > 0) {
			var acol = this.columns[this.columns.length - 1];
			if (acol === undefined || acol.xtype === undefined
					|| acol.xtype !== 'rowactions')
				this.columns.push(actioncol);
		}

		// Add colorful renderer to the status column
		if (this.statusfld != null) {
			this.columns.forEach(function(element, index, array) {
						if (element.dataIndex == grid.statusfld) {
							Ext.apply(element, {
										renderer : grid.status
									});
						}
					});
		}

		// add cell align and header align to columns
		this.columns.forEach(function(element, index, array) {
					if (element.cellAlign != 'undefined') {
						Ext.apply(element, {
									align : element.cellAlign
								});
					}
					if (element.headerAlign != 'undefined') {
						Ext.apply(element, {
									style : 'text-align: '
											+ element.headerAlign
								});
					}
				});

		// enable selecting multiple files to download
		if (this.dlselect && this.dllinkfld != '') {
			Ext.apply(this, {
				selModel : Ext.create('Ext.selection.CheckboxModel', {
							singleSelect : false,
							sortable : false,
							checkOnly : false,
							mode : 'SIMPLE',
							listeners : {
								selectionchange : function(me, selected, eOpts) {
									var dlbtn = grid.down('button[text="'
											+ grid.dlbtntxt + '"]');
									if (selected.length == 0) {
										dlbtn.disable();
									} else {
										dlbtn.enable();
									}
								}
							}
						}),
				dockedItems : [{
					xtype : 'toolbar',
					dock : 'bottom',
					ui : 'footer',
					layout : {
						pack : this.dlbtnalign
					},
					items : [{
						minWidth : 80,
						disabled : true,
						text : this.dlbtntxt,
						handler : function(me, e) {
							var records = grid.getSelectionModel()
									.getSelection();
							if (records.length > 0) {
								window.location = mergeDownloadLinks(records,
										grid.dllinkfld);
							}
						}
					}]
				}]
			});
		}

		// adjust grid height by the number of rows in the grid
		var gridHeight = rowCnt * rowHeight + gridHeaderHeight;
		gridHeight = (gridHeight < minGridHeight) ? minGridHeight : gridHeight;
		gridHeight = (gridHeight > maxGridHeight) ? maxGridHeight : gridHeight;
		Ext.apply(this, {
					store : myStore,
					columns : this.columns,
					height : gridHeight
				});

		this.callParent(arguments);
	}
});