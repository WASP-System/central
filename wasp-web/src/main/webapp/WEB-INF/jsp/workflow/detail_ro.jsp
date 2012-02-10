<%@ include file="/WEB-INF/jsp/taglib.jsp"%>
<wasp:message />
<h1>
	<c:out value="${workflow.name}" />
</h1>

<p>
iName: 
	<c:out value="${workflow.name}" />
</p>

<p>
isActive: 
	<c:out value="${workflow.isActive}" />
</p>



Resources:
    <c:forEach items="${workflow.workflowresource}" var="r">
<p>
<c:out value="${r.resource.resourceId}" />
<c:out value="${r.resource.name}" />
<c:out value="${r.resource.platform}" />
<c:out value="${r.resource.typeResource.name}" />
</p>
    </c:forEach>

<table>

	<c:set var="_area" value="workflow" scope="request" />
	<c:set var="_metaList" value="${workflow.workflowMeta}" scope="request" />
	<c:import url="/WEB-INF/jsp/meta_ro.jsp" />

	<sec:authorize
		access="hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')">
		<tr>
			<td><a
				href="<c:url value="/workflow/detail_rw/${workflow.workflowId}.do" />">Edit</a>
			</td>
		</tr>
	</sec:authorize>
</table>


<h2>Job</h2>
<c:forEach items="${job}" var="j">
	<div>
		<a href="/wasp/run/detail/<c:out value="${j.jobId}"/>.do"> <c:out
				value="${j.name}" /> </a>
	</div>
</c:forEach>

