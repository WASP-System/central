<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>WASP</title><head>
  <body>
    <h1><c:out value="${task.name}"/></h1>

    <c:forEach items="${state}" var="s">
      <p>
      <c:out value="${s.status}" />
      <a href="/wasp/task/state/detail/<c:out value="${task.taskId}" />/<c:out value="${s.stateId}" />.do"><c:out value="${s.name}" /></a>

      <c:forEach items="${s.statejob}" var="sj">
        <a href="/wasp/job/detail/<c:out value="${sj.jobId}" />"><c:out value="${sj.job.name}" /></a>
      </c:forEach>
      </p>
    </c:forEach>


  </body>
</html>
