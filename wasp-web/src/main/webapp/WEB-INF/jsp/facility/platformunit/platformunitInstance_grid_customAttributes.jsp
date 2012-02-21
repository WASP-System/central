<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
caption:"<fmt:message key="platformunitInstance.platformunitinstance_list.label" />",

url:'/wasp/facility/platformunit/instance/listJSON.do?selId=${param.selId}&subtypeSampleId=${param.subtypeSampleId}&typeSampleId=${param.typeSampleId}',


editurl: '/wasp/facility/platformunit/instance/updateJSON.do?selId=${param.selId}&subtypeSampleId=${param.subtypeSampleId}&typeSampleId=${param.typeSampleId}',

addurl: '/wasp/facility/platformunit/instance/updateJSON.do?selId=${param.selId}&subtypeSampleId=${param.subtypeSampleId}&typeSampleId=${param.typeSampleId}',


ondblClickRow: function(rowid) {
	$("#grid_id").jqGrid('viewGridRow',rowid,_viewAttr);
}