<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<div >
<table>
	<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobSubmitter.label" />:</td><td class="DataTD"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
	<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobPI.label" />:</td><td class="DataTD"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
	<tr><td class="CaptionTD">Submitted:</td><td class="DataTD"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
	<tr><td class="CaptionTD">Status:</td><td class="DataTD"><c:out value="${jobStatus}" /></td></tr>

	<tr><td>&nbsp;</td><td></td></tr>
	
	<c:forEach items="${jobApprovalsMap.keySet()}" var="jobApproveCode">
		<tr>
			<td class="CaptionTD"><fmt:message key="status.${jobApproveCode}.label" />:</td>
			<td class="DataTD"><fmt:message key="status.${jobApprovalsMap.get(jobApproveCode)}.label" />		
			  <c:if test="${not empty jobApprovalsCommentsMap.get(jobApproveCode)}"> 
			    <fmt:formatDate value="${jobApprovalsCommentsMap.get(jobApproveCode).getDate()}" pattern="yyyy-MM-dd" var="date" />
			  	<wasp:comment value="${jobApprovalsCommentsMap.get(jobApproveCode).getValue()} (${jobApprovalsCommentsMap.get(jobApproveCode).getUser().getNameFstLst()}; ${date})" />
			  </c:if>
			</td>
		</tr>
	</c:forEach> 
	
	<tr><td>&nbsp;</td><td></td></tr>

	<tr><td class="CaptionTD"><fmt:message key="jobdetail_for_import.jobWorkflow.label" />:</td><td class="DataTD"><c:out value="${job.workflow.name}" /></td></tr>
	<c:forEach items="${extraJobDetailsMap.keySet()}" var="detailKey">
		<tr><td class="CaptionTD"><fmt:message key="${detailKey}" />:</td><td class="DataTD"><c:out value="${extraJobDetailsMap.get(detailKey)}" /></td></tr>
	</c:forEach>
	

</table>
</div>
<br /><br />