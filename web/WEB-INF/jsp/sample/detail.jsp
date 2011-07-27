<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <h1><c:out value="${sample.name}"/></h1>
  <div><c:out value="${sample.typeSample.name}"/></div>

  <c:forEach items="${samplemeta}" var="meta">
      <p>
      <span><c:out value="${meta.k}" /></span>
      <span><c:out value="${meta.v}" /></span>
      </p>
  </c:forEach>


    <h2>Relations</h2>
    <h3>Parents</h3>
    <c:forEach items="${parentsample}" var="s">
      <a href="/wasp/sample/detail/<c:out value="${s.sourceSampleId}"/>.do">
        <c:out value="${s.sample.name}"/>
      </a>
      <div><c:out value="${s.sample.typeSample.name}"/></div>
    </c:forEach>
    <h3>Children</h3>
    <c:forEach items="${childsample}" var="s">
      <a href="/wasp/sample/detail/<c:out value="${s.sampleId}"/>.do">
        <c:out value="${s.sample.name}"/>
      </a>
      <div><c:out value="${s.sample.typeSample.name}"/></div>
    </c:forEach>

    <h2>JOBs</h2>
    <c:forEach items="${jobsample}" var="j">
      <p>
      <c:out value="${s.sample.typeSample.name}"/>
      <a href="/wasp/job/detail/<c:out value="${j.jobId}"/>.do">
        <c:out value="${j.job.name}"/>
      </a>
      </p>
    </c:forEach>

    <h2>FILES</h2>
    <c:forEach items="${samplefile}" var="f">
      <p>
      <c:out value="${f.file.filelocation}"/>
      <a href="/wasp/sample/file/<c:out value="${f.sampleId}"/>/<c:out value="${f.fileId}"/>.do">
        <c:out value="${f.name}"/>
      </a>
      </p>
    </c:forEach>

  </body>
</html>

