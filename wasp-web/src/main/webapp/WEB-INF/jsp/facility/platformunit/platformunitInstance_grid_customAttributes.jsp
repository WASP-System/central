<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
caption:"<fmt:message key="platformunitInstance.platformunitinstance_list.label" />",

url:'/wasp/facility/platformunit/instance/listJSON.do?selId=${param.selId}&sampleSubtypeId=${param.sampleSubtypeId}&sampleTypeId=${param.sampleTypeId}',


editurl: '/wasp/facility/platformunit/instance/updateJSON.do?selId=${param.selId}&sampleSubtypeId=${param.sampleSubtypeId}&sampleTypeId=${param.sampleTypeId}',

addurl: '/wasp/facility/platformunit/instance/updateJSON.do?selId=${param.selId}&sampleSubtypeId=${param.sampleSubtypeId}&sampleTypeId=${param.sampleTypeId}',


ondblClickRow: function(rowid) {
	$("#grid_id").jqGrid('editGridRow',rowid,_editAttr);
}