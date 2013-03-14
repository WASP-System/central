<%@ include file="/WEB-INF/jsp/taglib.jsp"%>

<h1>
	<c:out value="${resource.name}" />
</h1>


<table class="EditTable ui-widget ui-widget-content">
	<tr class="FormData">
		<td class="CaptionTD"><fmt:message key="resource.resourceTypeId.label" />:</td>
		<td class="DataTD"><c:forEach var="type" items="${resourceTypes}">
				<c:if test="${type.resourceTypeId == resource.resourceTypeId}">
					<c:out value="${type.name}" />
				</c:if>
			</c:forEach></td>
	</tr>

	<c:set var="_area" value="resource" scope="request" />
	<c:set var="_metaList" value="${resource.resourceMeta}" scope="request" />
	<c:import url="/WEB-INF/jsp/meta_ro.jsp" />

	<sec:authorize
		access="hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')">
		<tr class="FormData">
			<td class="submit"><a
				href="<c:url value="/resource/detail_rw/${resource.resourceId}.do" />">Edit</a>
			</td>
		</tr>
	</sec:authorize>
</table>

<%-- 
<c:forEach items="${resourcemeta}" var="meta">
  <p>
  <span><c:out value="${meta.k}" /></span>
  <span><c:out value="${meta.v}" /></span>
  </p>
</c:forEach>
 --%>

<h2>Resource Cells</h2>
<c:forEach items="${resourcecell}" var="rl">
	<div>
		<a
			href="/wasp/resource/cell/detail/<c:out value="${rl.resourceCellId}"/>.do">
			<c:out value="${rl.name}" /> </a>
	</div>
</c:forEach>


<h2>Run</h2>
<c:forEach items="${run}" var="r">
	<div>
		<a href="/wasp/run/detail/<c:out value="${r.runId}"/>.do"> <c:out
				value="${r.name}" /> </a>
	</div>
</c:forEach>

