<%@ include file="/WEB-INF/jsp/include.jsp" %>



<h1>List List</h1>
<table cellpadding="0" cellspacing="0" border="0">
<c:forEach items="${lab}" var="l">
<div>
<tr>
<td>
<a href="/wasp/lab/detail/<c:out value="${l.labId}" />.do"><c:out value="${l.name}" /></a>
</td>
<td>
</td>
</tr>
</c:forEach>
</table>

