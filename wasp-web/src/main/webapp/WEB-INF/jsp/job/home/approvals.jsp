<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<div >
<table>
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
</table>
</div>