<%@ include file="/WEB-INF/jsp/taglib.jsp" %>



<h1>Run List</h1> 
<a href="/wasp/user/create/form">.do">create</a>

<table cellpadding="0" cellspacing="0" border="0">
<c:forEach items="${run}" var="r">
<div>
<tr>
<td>
<a href="/wasp/run/detail/<c:out value="${r.runId}" />.do"><c:out value="${r.name}" /></a>
</td>
<td>
</td>
</tr>
</c:forEach>
</table>

