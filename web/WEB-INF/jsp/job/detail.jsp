<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>WASP</title><head>
  <body>
    <h1>
      <c:out value="${job.name}"/>
    </h1>

    <p>
      Lab
      <a href="/wasp/lab/detail/<c:out value="${job.lab.labId}"/>.do">
        <c:out value="${job.lab.name}"/>
      </a>
    </p>

    <p>
      Submitting User
      <a href="/wasp/user/detail/<c:out value="${job.user.userId}"/>.do">
        <c:out value="${job.user.login}"/>
      </a>
      <c:out value="${job.user.firstName}"/>
      <c:out value="${job.user.lastName}"/>
    </p>

    <c:forEach items="${jobmeta}" var="meta">
      <p>
      <span><c:out value="${meta.k}" /></span>
      <span><c:out value="${meta.v}" /></span>
      </p>
    </c:forEach>


    <h2>SAMPLES</h2>
    <c:forEach items="${jobsample}" var="s">
      <p>
      <c:out value="${s.sample.typeSample.name}"/>
      <a href="/wasp/sample/detail/<c:out value="${s.sampleId}"/>.do">
        <c:out value="${s.sample.name}"/>
      </a>
      </p>
    </c:forEach>

    <h2>State</h2>
    <c:forEach items="${statejob}" var="s">
      <p>
      <c:out value="${s.state.name}"/>
      <c:out value="${s.state.task.name}"/>
      <c:out value="${s.state.status}"/>
      </p>
    </c:forEach>

    <h2>FILES</h2>
    <c:forEach items="${jobfile}" var="f">
      <p>
      <c:out value="${f.file.filelocation}"/>
      <a href="/wasp/job/file/<c:out value="${f.jobId}"/>/<c:out value="${f.fileId}"/>.do">
        <c:out value="${f.name}"/>
      </a>
      </p>
    </c:forEach>

  </body>
</html>

