<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>WASP</title><head>
  <body>
    <h1>WASP</h1>
    <p><c:out value="${now}"/></p>
    <hr>
    <p><c:out value="${user.email}"/></p>
    <p><c:out value="${user.firstName}"/></p>
    <p><c:out value="${user.lastName}"/></p>
    <c:forEach items="${user.labUser}" var="labUser">
      <p><c:out value="${labUser.lab.name}"/></p>
    </c:forEach>

  </body>
</html>
