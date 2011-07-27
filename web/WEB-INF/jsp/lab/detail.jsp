<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>WASP</title><head>
  <body>
    <h1><c:out value="${lab.name}"/></h1>
    <c:forEach items="${labmeta}" var="meta">
      <p>
      <span><c:out value="${meta.k}" /></span>
      <span><c:out value="${meta.v}" /></span>
      </p>
    </c:forEach>

    <c:forEach items="${labuser}" var="ul">
      <p>
      <a href="/wasp/user/detail/<c:out value="${ul.user.userId}" />.do"><c:out value="${ul.user.login}" /></a>
        <c:out value="${ul.user.firstName}" />
        <c:out value="${ul.user.lastName}" />
      <span><c:out value="${ul.role.name}" /></span>
      </p>
    </c:forEach>

    <c:if test="${!  empty( job ) }">   
      <h2>Jobs</h2>
      <c:forEach items="${job}" var="j">
        <div>
          <a href="/wasp/job/detail/<c:out value="${j.jobId}" />.do">
          <c:out value="${j.name}" />
          </a>
        </div>
      </c:forEach>
    </c:if>

    <div>
      <a href="/wasp/job/create/form.do?labid=<c:out value="${lab.labId}" />">create job</a>
    </div>

  </body>
</html>
