<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
caption:"<fmt:message key="department.lab_list.label" />",

url: '/wasp/department/detail/${departmentId}/listLabJSON.do?selId=${param.selId}',

ondblClickRow: function(rowid) {
	$("#grid_id").jqGrid('viewGridRow',rowid,_viewAttr);
}