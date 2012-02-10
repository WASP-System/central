<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
caption:"<fmt:message key="platformunitInstance.platformunitinstance_list.label" />",

editurl: '/wasp/facility/platformunit/instance/updateJSON.do',

addurl: '/wasp/facility/platformunit/instance/updateJSON.do',


ondblClickRow: function(rowid) {
	$("#grid_id").jqGrid('viewGridRow',rowid,_viewAttr);
}