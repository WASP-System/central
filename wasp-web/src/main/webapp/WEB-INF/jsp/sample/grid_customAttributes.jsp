<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

caption:"<fmt:message key="sample.sample_list.label" />",


rowNum: 20,
height: 450,
sortable: true,
sortname: name,
sortorder: "desc",
caption:"List of Samples",

ondblClickRow: function(rowid) {		
				$("#grid_id").jqGrid('viewGridRow',rowid, _editAttr);
			},

