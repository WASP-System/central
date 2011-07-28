<%@ include file="/WEB-INF/jsp/include.jsp" %>



<sec:authorize access="
   hasRole('god') or
   hasRole('da-*') or
   hasRole('lu-*')
">

<h1>List List</h1>
<table cellpadding="0" cellspacing="0" border="0">
<c:forEach items="${lab}" var="l">
<sec:authorize access="
     hasRole('god') or
     hasRole('da-' + ${l.departmentId}) or
     hasRole('lu-' + ${l.labId})
   ">
  <tr>
  <td>
  <a href="/wasp/lab/detail/<c:out value="${l.labId}" />.do"><c:out value="${l.name}" /></a>
  </td>
  </tr>
</sec:authorize>
</c:forEach>
</table>

</sec:authorize>
