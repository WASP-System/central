<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>WASP</title><head>
  <body>
    <h1><c:out value="${department.name}"/></h1>

    Department Admins
    <c:forEach items="${departmentuser}" var="u">
      <p>
      <a href="/wasp/user/detail/<c:out value="${u.user.userId}" />.do"><c:out value="${u.user.login}" /></a>
        <c:out value="${u.user.firstName}" />
        <c:out value="${u.user.lastName}" />
        <a href="/wasp/department/user/roleRemove/<c:out value="${department.departmentId}" />/<c:out value="${u.user.userId}" />.do">
          Remove
        </a>
      </p>
    </c:forEach>

    <form name="f" action="<c:url value='/department/user/roleAdd.do'/>" method="POST">
      Email Address: 
      <input type='hidden' name='departmentId' value='<c:out value="${department.departmentId}" />'/>
      <input type='text' name='useremail' value=''/>
      <input type="submit" value="Add Department Admin" />
 
    </form>

    <h2>Labs</h2>
    <c:forEach items="${lab}" var="l">
      <div>
        <a href="/wasp/lab/detail_ro/<c:out value="${department.departmentId}" />/<c:out value="${l.labId}" />.do">
        <c:out value="${l.name}" />
        </a>
      </div>
    </c:forEach>

    <h2>Pending Lab</h2>
    <c:forEach items="${labpending}" var="lp">
      <div>
      	<a href="/wasp/lab/pending/detail_ro/<c:out value="${department.departmentId}" />/<c:out value="${lp.labPendingId}" />.do">
      	<c:out value="${lp.name}" />
      	</a>
      </div>
    </c:forEach>

  </body>
</html>
