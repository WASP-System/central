<%@ include file="/WEB-INF/jsp/include.jsp" %>



<h1>Resource List</h1> 
<a href="/wasp/resource/create/form.do">create</a>

<table cellpadding="0" cellspacing="0" border="0">
<c:forEach items="${resource}" var="r">
<div>
<tr>
<td>
<a href="/wasp/resource/detail/<c:out value="${r.resourceId}" />.do"><c:out value="${r.name}" /></a>
</td>
<td>
</td>
</tr>
</c:forEach>
</table>

