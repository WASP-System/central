<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<script src="/wasp/scripts/jquery/jquerytools/jquery.tools-1.2.7.all.min.js" type="text/javascript" ></script>
<script>
$(document).ready(function() {
  	$(".wasptooltip a[title]").tooltip({ position: "top right"});
});
</script>
<div class="wasptooltip">
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobId.label" />:</td><td class="DataTD">J<c:out value="${job.jobId}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobName.label" />:</td><td class="DataTD"><c:out value="${job.name}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobSubmitter.label" />:</td><td class="DataTD"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobPI.label" />:</td><td class="DataTD"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobSubmissionDate.label" />:</td><td class="DataTD"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobWorkflow.label" />:</td><td class="DataTD"><c:out value="${job.workflow.name}" /></td></tr>
<c:forEach items="${extraJobDetailsMap.keySet()}" var="detailKey">
	<tr class="FormData"><td class="CaptionTD"><fmt:message key="${detailKey}" />:</td><td class="DataTD"><c:out value="${extraJobDetailsMap.get(detailKey)}" /></td></tr>
</c:forEach>

<c:if test="${not empty jobStatus}"> 
	<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobStatus.label" />:</td><td class="DataTD"><c:out value="${jobStatus}" /></td></tr>
</c:if>

<c:forEach items="${jobApprovalsMap.keySet()}" var="jobApproveCode">
	<tr class="FormData">
		<td class="CaptionTD"><fmt:message key="status.${jobApproveCode}.label" />:</td>
		<td class="DataTD"><fmt:message key="status.${jobApprovalsMap.get(jobApproveCode)}.label" />		
		  <c:if test="${not empty jobApprovalsCommentsMap.get(jobApproveCode)}"> 
			<a href="javascript:void(0)" title="<c:out value="${jobApprovalsCommentsMap.get(jobApproveCode).getValue()}" /> (<fmt:formatDate value="${jobApprovalsCommentsMap.get(jobApproveCode).getDate()}" pattern="MM-dd-yyyy" />)">[comment]</a>
		  </c:if>
		</td>
	</tr>
</c:forEach> 

<%-- 
<c:if test="${not empty files}">
	<tr class="FormData">
		<td class="CaptionTD"><fmt:message key="jobdetail_for_import.files.label" />:</td>
		<td class="DataTD">
			<c:forEach items="${files}" var="file">
				<a href="/wasp/file/downloadFile.do?id=<c:out value="${file.getFileId()}" />">${file.getFileName()}</a>
				<c:if test='${file.getDescription()!=null && file.getDescription()!=""}' > (<c:out value="${file.getDescription()}" />)</c:if>
				<br />
			</c:forEach>
		</td>
	</tr>
</c:if>
--%>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobComments.label" />:</td><td class="DataTD"><a style="color: #801A00;" href="<c:url value="/job/comments/${job.jobId}.do" />">
<fmt:message key="jobdetail_for_import.jobCommentsView.label" /><sec:authorize access="hasRole('su') or hasRole('ft') or hasRole('fm')"><fmt:message key="jobdetail_for_import.jobCommentsPlusAddEdit.label" /></sec:authorize>
</a> </td></tr>
</table>
</div>