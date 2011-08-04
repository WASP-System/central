<%@ include file="/WEB-INF/jsp/include.jsp" %>

<h1>User List</h1>
 <a href="/wasp/user/create/form.do">create</a>
<table cellpadding="0" cellspacing="0" border="0">
<c:forEach items="${user}" var="u">
<div>
<tr>
<td>
<a href="/wasp/user/detail_ro/<c:out value="${u.userId}" />.do"><c:out value="${u.login}" /></a>
</td>
<td>
<c:out value="${u.firstName}" />
<c:out value="${u.lastName}" />
</td>
</tr>
</c:forEach>
</table>

