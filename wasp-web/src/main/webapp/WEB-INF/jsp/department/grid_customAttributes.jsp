caption:  'Labs in ${departmentName}',
url: '/wasp/department/detail/${departmentId}/listLabJSON.do?selId=${param.selId}',
ondblClickRow: function(rowid) {
	$("#grid_id").jqGrid('viewGridRow',rowid,_viewAttr);
}