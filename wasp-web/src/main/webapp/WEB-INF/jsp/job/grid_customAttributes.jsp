<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

caption: "<fmt:message key="job.job_list.label" />",
sortname: 'jobId',
sortorder: "desc",
viewrecords: true,
rownumbers: true,	
ignoreCase: true,
pager: '#gridpager',
height: "auto",
	
ondblClickRow: function(rowid) {
	$("#grid_id").jqGrid('viewGridRow',rowid,_viewAttr);
},



