<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<div >
<table>
<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobWorkflow.label" />:</td><td class="DataTD"><c:out value="${job.workflow.name}" /></td></tr>
<c:forEach items="${extraJobDetailsMap.keySet()}" var="detailKey">
	<tr><td class="CaptionTD"><fmt:message key="${detailKey}" />:</td><td class="DataTD"><c:out value="${extraJobDetailsMap.get(detailKey)}" /></td></tr>
</c:forEach>
</table>
</div>