<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
	
    <h1>
      <c:out value="${run.name}"/>
    </h1>
    <div>
      <fmt:message key="run.detailResource.label"/>: <a href="/wasp/resource/list.do?selId=<c:out value="${run.resourceId}"/>"><c:out value="${run.resource.name}"/></a>
    </div>

<table class="EditTable ui-widget ui-widget-content">

	<c:set var="_area" value="run" scope="request" />
	<c:set var="_metaList" value="${run.runMeta}" scope="request" />
	<c:import url="/WEB-INF/jsp/meta_ro.jsp" />

    <p>
      <fmt:message key="run.detailSample.label"/>
      <a href="/wasp/sample/detail/<c:out value="${run.sampleId}"/>.do">
        <c:out value="${run.sample.name}"/>
      </a>
    </p>

    <p>
    <fmt:message key="run.detailRunLanes.label"/>
    <c:forEach items="${runcell}" var="rl">
      <div>
      -- <fmt:message key="run.detailSamples.label"/>:
      <a href="/wasp /sample/detail/<c:out value="${rl.sampleId}"/>.do">
        <c:out value="${rl.sample.name}"/>
      </a>
      -- <fmt:message key="run.detailResourceCells.label"/>:
      (<a href="/wasp/run/lane/detail/<c:out value="${rl.runId}"/>/<c:out value="${rl.runCellId}"/>.do">
        <c:out value="${rl.resourceCell.name}"/>
      </a>)
      </div>
    </c:forEach>
    </p>

    <h2><fmt:message key="run.detailFiles.label"/></h2>
    <c:forEach items="${runfile}" var="f">
      <p>
      <c:out value="${f.file.filelocation}"/>
      <a href="/wasp/run/file/<c:out value="${f.runId}"/>/<c:out value="${f.fileId}"/>.do">
        <c:out value="${f.name}"/>
      </a>
      </p>
    </c:forEach>

  </body>
</html>

