caption:"Flow Cell List",

editurl: '/wasp/facility/platformunit/instance/updateJSON.do?selId=${param.selId}',

ondblClickRow: function(rowid) {
	$("#grid_id").jqGrid('viewGridRow',rowid,_viewAttr);
}