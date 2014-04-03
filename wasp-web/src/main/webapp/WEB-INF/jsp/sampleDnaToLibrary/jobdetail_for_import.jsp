<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<div >
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobId.label" />:</td><td class="DataTD"><a style="color: #801A00;" href="<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/${job.jobId}.do" />">J<c:out value="${job.jobId}" /></a></td></tr>
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
		    <fmt:formatDate value="${jobApprovalsCommentsMap.get(jobApproveCode).getDate()}" pattern="yyyy-MM-dd" var="date" />
		  	<wasp:comment value="${jobApprovalsCommentsMap.get(jobApproveCode).getValue()} (${jobApprovalsCommentsMap.get(jobApproveCode).getUser().getNameFstLst()}; ${date})" />
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
				<a href="<wasp:relativeUrl value='file/downloadFile.do?id=${file.getFileId()}' />">${file.getFileName()}</a>
				<c:if test='${file.getDescription()!=null && file.getDescription()!=""}' > (<c:out value="${file.getDescription()}" />)</c:if>
				<br />
			</c:forEach>
		</td>
	</tr>
</c:if>
--%>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobComments.label" />:</td><td class="DataTD"><a style="color: #801A00;" href="<wasp:relativeUrl value="/job/comments/${job.jobId}.do" />">
<fmt:message key="jobdetail_for_import.jobCommentsView.label" /><sec:authorize access="hasRole('su') or hasRole('ft') or hasRole('fm')"><fmt:message key="jobdetail_for_import.jobCommentsPlusAddEdit.label" /></sec:authorize>
</a> </td></tr>
<tr class="FormData">
 <td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobAnalysisParameters.label" />:</td>
 <td class="DataTD">
 	<a style="color: #801A00;" href="<wasp:relativeUrl value="/job/analysisParameters/${job.jobId}.do" />"><fmt:message key="jobdetail_for_import.jobAnalysisParametersView.label" /></a>
 </td>
</tr>
<tr class="FormData">
 <td class="CaptionTD"><fmt:message key="jobdetail_for_import.dataFiles.label" />:</td>
 <td class="DataTD">
 	<a style="color: #801A00;" href="<wasp:relativeUrl value="/jobresults/treeview/job/${job.jobId}.do" />"><fmt:message key="jobdetail_for_import.dataFilesView.label" /></a> | <a style="color: #801A00;" href="<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.jobId}/runs.do" />">By Runs</a> | <a style="color: #801A00;" href="<wasp:relativeUrl value="/datadisplay/mps/jobs/${job.jobId}/samples.do" />">By Samples</a> 	
 </td>
</tr>
<%--no longer needed 
<tr class="FormData">
 <td class="CaptionTD">Test:</td>
 <td class="DataTD">
 	<a style="color: #801A00;" href="<wasp:relativeUrl value="/job/${job.jobId}/homepage.do" />">Home</a> 	
 </td>
</tr>
--%>
</table>
</div>