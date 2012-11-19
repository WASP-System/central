<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<title><fmt:message key="pageTitle.job/comments.label"/></title>
<h1><fmt:message key="pageTitle.job/comments.label"/> - J<c:out value="${job.jobId}" /></h1>
<table class="data" style="margin: 0px 0">
	<c:if test='${permissionToAddEditComment==true}'>
		<tr ><th class="label" nowrap colspan="2"><fmt:message key="jobComment.addNewJobComment.label" /></th></tr>
		<tr><td align="center" colspan="2"><form  method='post' name='commentForm' action="<c:url value="/job/comments/${job.jobId}.do" />" onsubmit="return validate();">
		<textarea id="comment" name="comment" cols="70" rows="4"></textarea><br />
		<input type='submit' value='<fmt:message key="jobComment.submitNewComment.label" />'/></form></td></tr>
	</c:if>
	<c:if test='${fn:length(facilityJobCommentsList) > 0}'>
		<tr>
			<th class="label" nowrap><fmt:message key="jobComment.jobCommentDate.label"/></th>
			<th class="label" nowrap><fmt:message key="pageTitle.job/comments.label"/></th>
		</tr>	
		<c:forEach items="${facilityJobCommentsList}" var="facilityJobComment">
		<tr>
			<td style="padding-top:20px;padding-bottom:20px;padding-left:10px;padding-right:20px; " width="10%"><fmt:formatDate value="${facilityJobComment.getDate()}" pattern="MM-dd-yyyy" /></td>
			<td style="padding-top:20px;padding-bottom:20px;padding-left:20px;padding-right:10px; " width="90%"><c:out value="${facilityJobComment.getValue()}" /></td>
		</tr>
		</c:forEach>
	</c:if>

</table>
