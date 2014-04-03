<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
	
    <h1>
      <c:out value="${run.name}"/>
    </h1>
    <div>
      <fmt:message key="run.detailResource.label"/>: <a href="<c:url value='resource/list.do?selId=${run.resourceId}' />">${run.resource.name}</a>
    </div>

<table class="EditTable ui-widget ui-widget-content">

	<c:set var="_area" value="run" scope="request" />
	<c:set var="_metaList" value="${run.runMeta}" scope="request" />
	<c:import url="/WEB-INF/jsp/meta_ro.jsp" />

    <p>
      <fmt:message key="run.detailSample.label"/>
      <a href="<c:url value='sample/detail/${run.sampleId}.do' />">
        <c:out value="${run.sample.name}"/>
      </a>
    </p>

    <p>
    <fmt:message key="run.detailRunCells.label"/>
    <c:forEach items="${runcell}" var="rl">
      <div>
      -- <fmt:message key="run.detailSamples.label"/>:
      <a href="/wasp /sample/detail/<c:out value="${rl.sampleId}"/>.do">
        <c:out value="${rl.sample.name}"/>
      </a>
      -- <fmt:message key="run.detailResourceCells.label"/>:
      (<a href="<c:url value='run/cell/detail/${rl.runId}/${rl.runCellId}.do' />">
        <c:out value="${rl.resourceCell.name}"/>
      </a>)
      </div>
    </c:forEach>
    </p>

    <h2><fmt:message key="run.detailFiles.label"/></h2>
    <c:forEach items="${runfile}" var="f">
      <p>
      <c:out value="${f.file.filelocation}"/>
      <a href="<c:url value='run/file/${f.runId}/${f.fileId}.do' />">
        <c:out value="${f.name}"/>
      </a>
      </p>
    </c:forEach>

  </body>
</html>

