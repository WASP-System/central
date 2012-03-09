<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
rowNum: 8,
height: 400,
pager: '#gridpager',
caption:"<fmt:message key='workflow.listname.label' />",

ondblClickRow: function(rowid) {
	$("#grid_id").jqGrid('viewGridRow',rowid,_viewAttr);
},
