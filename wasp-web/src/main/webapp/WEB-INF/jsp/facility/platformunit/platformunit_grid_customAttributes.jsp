<%--Doubt this is really used anymore 11/30/12 dubin --%>
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
caption:"<fmt:message key="platformunitInstance.platformunitinstance_list.label" />",

url:'<wasp:relativeUrl value="facility/platformunit/selid/listJSON.do?selId=${param.selId}" />',

ondblClickRow: function(rowid) {
	$("#grid_id").jqGrid('viewGridRow',rowid,_viewAttr);
}