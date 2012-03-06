<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<title><fmt:message key="pageTitle.task/detail.label"/></title>

<h1>List</h1>
<table cellpadding="0" cellspacing="0" border="0">
<c:forEach items="${task}" var="t">

<tr class="FormData">
<td class="DataTD">
<a href="/wasp/task/detail/<c:out value="${t.taskId}" />.do"><c:out value="${t.name}" /></a>
</td>
<td>
</td>
</tr>
</c:forEach>
</table>

