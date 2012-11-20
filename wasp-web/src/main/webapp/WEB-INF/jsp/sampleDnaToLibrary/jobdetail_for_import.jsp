<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobId.label" />:</td><td class="DataTD">J<c:out value="${job.jobId}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobName.label" />:</td><td class="DataTD"><c:out value="${job.name}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobSubmitter.label" />:</td><td class="DataTD"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobPI.label" />:</td><td class="DataTD"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobSubmissionDate.label" />:</td><td class="DataTD"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobWorkflow.label" />:</td><td class="DataTD"><c:out value="${job.workflow.name}" /></td></tr>
<%-- 
<c:forEach items='${extraJobDetailsMap}' var="detail">
	<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.${detail.key}.label" />:</td><td class="DataTD"><c:out value='${detail.value}' /> </td></tr>
</c:forEach>
--%>

<c:set var="extra" value="${extraJobDetailsMap}" scope="page" />
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.Machine.label" />:</td><td class="DataTD"><c:out value='${extraJobDetailsMap.get("Machine")}' /> </td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.Read_Length.label" />:</td><td class="DataTD"><c:out value='${extraJobDetailsMap.get("Read Length")}' /> </td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.Read_Type.label" />:</td><td class="DataTD"><c:out value='${extraJobDetailsMap.get("Read Type")}' /> </td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.PI_Approval.label" />:</td><td class="DataTD"><c:out value='${extraJobDetailsMap.get("PI Approval")}' /> </td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.DA_Approval.label" />:</td><td class="DataTD"><c:out value='${extraJobDetailsMap.get("DA Approval")}' /> </td></tr>
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.Quote_Job_Price.label" />:</td><td class="DataTD"><c:out value='${extraJobDetailsMap.get("Quote Job Price")}' /></td></tr>
<%--   
<c:set var="quote" value='${extraJobDetailsMap.get("Quote Job Price")}' scope="page" />
  <c:choose>   
   <c:when test='${quote == "Awaiting Quote" || quote == "Unknown" || quote == "Not Yet Set"}'><c:out value='${quote}' /></c:when>
   <c:otherwise><fmt:formatNumber type="currency" value='${quote}' /></c:otherwise>
  </c:choose>
</td></tr>
--%>

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
<tr class="FormData"><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobComments.label" />:</td><td class="DataTD"><a style="color: #801A00;" href="<c:url value="/job/comments/${job.jobId}.do" />"><fmt:message key="jobdetail_for_import.jobCommentsAnchor.label" /></a> </td></tr>
</table>