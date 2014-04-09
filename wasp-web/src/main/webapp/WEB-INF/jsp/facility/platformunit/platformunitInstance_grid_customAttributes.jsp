<%--Doubt this is really used anymore 11/30/12 dubin --%>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
caption:"<fmt:message key="platformunitInstance.platformunitinstance_list.label" />",

url:'<wasp:relativeUrl value="facility/platformunit/instance/listJSON.do?selId=${param.selId}&sampleSubtypeId=${param.sampleSubtypeId}&sampleTypeId=${param.sampleTypeId}" />',


editurl: '<wasp:relativeUrl value="facility/platformunit/instance/updateJSON.do?selId=${param.selId}&sampleSubtypeId=${param.sampleSubtypeId}&sampleTypeId=${param.sampleTypeId}" />',

addurl: '<wasp:relativeUrl value="facility/platformunit/instance/updateJSON.do?selId=${param.selId}&sampleSubtypeId=${param.sampleSubtypeId}&sampleTypeId=${param.sampleTypeId}" />',


ondblClickRow: function(rowid) {
	$("#grid_id").jqGrid('editGridRow',rowid,_editAttr);
}