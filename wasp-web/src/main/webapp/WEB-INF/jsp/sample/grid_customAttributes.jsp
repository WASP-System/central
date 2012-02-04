

rowNum: 20,
height: 450,
sortable: true,
sortname: name,
sortorder: "desc",
caption:"List of Samples",

ondblClickRow: function(rowid) {		
				$("#grid_id").jqGrid('viewGridRow',rowid, _editAttr);
			},

