<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>WASP</title><head>
  <body>
    <h1>
      <c:out value="${runelane.run.name}"/>

      <c:out value="${runelane.resourcecell.name}"/>
    </h1>

    <div>
      Resource: <a href="/resource/detail/<c:out value="${runcell.run.resourceId}"/>.do"><c:out value="${rl.run.resource.name}"/></a>
    </div>

    <p>
      Sample 
      <a href="/wasp/sample/detail/<c:out value="${runcell.sampleId}"/>.do">
        <c:out value="${runcell.sample.name}"/>
      </a>
    </p>

    <h2>FILES</h2>
    <c:forEach items="${runcellfile}" var="f">
      <p>
      <c:out value="${f.file.filelocation}"/>
      <a href="/wasp/run/lane/file/<c:out value="${runcell.runId}"/>/<c:out value="${runcell.runCellId}"/>/<c:out value="${f.fileId}"/>.do">
        <c:out value="${f.name}"/>
      </a>
      </p>
    </c:forEach>

  </body>
</html>

