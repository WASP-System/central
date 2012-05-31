<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobId.label" />:</td><td class="DataTD">J<c:out value="${job.jobId}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobName.label" />:</td><td class="DataTD"><c:out value="${job.name}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobSubmitter.label" />:</td><td class="DataTD"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobPI.label" />:</td><td class="DataTD"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobSubmissionDate.label" />:</td><td class="DataTD"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobWorkflow.label" />:</td><td class="DataTD"><c:out value="${job.workflow.name}" /></td></tr>
<c:forEach items='${extraJobDetailsMap}' var="detail">
	<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.${detail.key}.label" />:</td><td class="DataTD"><c:out value='${detail.value}' /> </td></tr>
</c:forEach>
<c:if test="${not empty files}">
	<tr class="FormData">
		<td class="CaptionTD"><fmt:message key="jobdetail_for_import.files.label" />:</td>
		<td class="DataTD">
			<c:forEach items="${files}" var="file">
				<a href="/wasp/jobsubmit/downloadFile.do?id=<c:out value="${file.getFileId()}" />">${file.getFileName()}</a> (<c:out value="${file.getDescription()}" />)<br />
			</c:forEach>
		</td>
	</tr>
</c:if>
</table>