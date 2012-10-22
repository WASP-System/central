<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
caption:"<fmt:message key="platformunit.platformunit_list.label" />",

<%--next line shows grid row as popup in readonly format --%>
ondblClickRow: function(rowid) {
	$("#grid_id").jqGrid('viewGridRow',rowid,_viewAttr);
},