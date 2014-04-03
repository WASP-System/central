<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
caption:"<fmt:message key="job2quote.job2quote_list.label" />",
url:"<c:url value='" + _area + ' />"/listAllJSON.do?selId=${param.selId}",

ondblClickRow: function(rowid) {
	<%--should suppress the ondoubleclick row being visible --%>
},