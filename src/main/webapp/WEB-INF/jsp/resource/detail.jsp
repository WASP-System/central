<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1>
  <c:out value="${resource.name}"/>
</h1>


<table>
	<c:set var="_area" value = "resource" scope="request"/>	
	<c:set var="_metaList" value = "${resource.resourceMeta}" scope="request" />		
	<c:import url="/WEB-INF/jsp/meta_ro.jsp"/>
</table>

<%-- 
<c:forEach items="${resourcemeta}" var="meta">
  <p>
  <span><c:out value="${meta.k}" /></span>
  <span><c:out value="${meta.v}" /></span>
  </p>
</c:forEach>
 --%>

<h2>Resource Lanes</h2>
<c:forEach items="${resourcelane}" var="rl">
  <div>
  <a href="/wasp/resource/lane/detail/<c:out value="${rl.resourceLaneId}"/>.do">
    <c:out value="${rl.name}"/>
  </a>
  </div>
</c:forEach>


<h2>Run</h2>
<c:forEach items="${run}" var="r">
  <div>
  <a href="/wasp/run/detail/<c:out value="${r.runId}"/>.do">
    <c:out value="${r.name}"/>
  </a>
  </div>
</c:forEach>

