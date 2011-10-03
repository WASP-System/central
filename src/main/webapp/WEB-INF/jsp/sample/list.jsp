<%@ include file="/WEB-INF/jsp/taglib.jsp" %>


<h1>Sample List</h1> 
<a href="/wasp/user/create/form">.do">create</a>

<table cellpadding="0" cellspacing="0" border="0">
<c:forEach items="${sample}" var="s">
<div>
<tr>
<td>
<a href="/wasp/run/detail/<c:out value="${s.runId}" />.do"><c:out value="${s.name}" /></a>
</td>
<td>
</td>
</tr>
</c:forEach>
</table>

