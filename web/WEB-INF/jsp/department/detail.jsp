<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>WASP</title><head>
  <body>
    <h1><c:out value="${department.name}"/></h1>

    <c:forEach items="${departmentuser}" var="u">
      <p>
      <a href="/wasp/user/detail/<c:out value="${u.user.userId}" />.do"><c:out value="${u.user.login}" /></a>
        <c:out value="${u.user.firstName}" />
        <c:out value="${u.user.lastName}" />
      <span><c:out value="${u.role.name}" /></span>
      </p>
    </c:forEach>

    <h2>Labs</h2>
    <c:forEach items="${lab}" var="l">
      <div>
        <a href="/wasp/lab/detail/<c:out value="${l.labId}" />.do">
        <c:out value="${l.name}" />
        </a>
      </div>
    </c:forEach>

  </body>
</html>
