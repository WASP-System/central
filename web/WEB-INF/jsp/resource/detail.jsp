<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>WASP</title><head>
  <body>
    <h1>
      <c:out value="${resource.name}"/>
    </h1>


    <c:forEach items="${resourcemeta}" var="meta">
      <p>
      <span><c:out value="${meta.k}" /></span>
      <span><c:out value="${meta.v}" /></span>
      </p>
    </c:forEach>


    <p>
    <h2>Resource Lanes</h2>
    <c:forEach items="${resourcelane}" var="rl">
      <div>
      <a href="/wasp/resource/lane/detail/<c:out value="${rl.resourceLaneId}"/>.do">
        <c:out value="${rl.name}"/>
      </a>
      </div>
    </c:forEach>
    </p>

    <h2>Run</h2>
    <c:forEach items="${run}" var="r">
      <div>
      <a href="/wasp/run/detail/<c:out value="${r.runId}"/>.do">
        <c:out value="${r.name}"/>
      </a>
      </div>
    </c:forEach>
    </p>

  </body>
</html>

