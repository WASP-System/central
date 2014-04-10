<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>WASP</title><head>
  <body>
    <h1>
      <c:out value="${runecell.run.name}"/>

      <c:out value="${runecell.resourcecell.name}"/>
    </h1>

    <div>
      <fmt:message key="run.celldetailResource.label"/>: <a href="/resource/detail/<c:out value="${runcell.run.resourceId}"/>.do"><c:out value="${rl.run.resource.name}"/></a>
    </div>

    <p>
      <fmt:message key="run.celldetailSample.label"/> 
      <a href="<wasp:relativeUrl value='sample/detail/${runcell.sampleId}.do' />">
        <c:out value="${runcell.sample.name}"/>
      </a>
    </p>

    <h2><fmt:message key="run.celldetailFiles.label"/></h2>
    <c:forEach items="${runcellfile}" var="f">
      <p>
      <c:out value="${f.file.filelocation}"/>
      <a href="<wasp:relativeUrl value='run/cell/file/${runcell.runId}/${runcell.runCellId}/${f.fileId}.do' />">
        <c:out value="${f.name}"/>
      </a>
      </p>
    </c:forEach>

  </body>
</html>

