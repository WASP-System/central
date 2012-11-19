<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<title><fmt:message key="pageTitle.job/comments.label"/></title>
<h1><fmt:message key="pageTitle.job/comments.label"/> - J<c:out value="${job.jobId}" /></h1>
<table class="data" style="margin: 0px 0">
	<c:if test='${permissionToAddEditComment==true}'>
		<tr ><td class="label" style="background-color:#FAF2D6" nowrap colspan="2"><fmt:message key="jobComment.addNewJobComment.label" /></td></tr>
		<tr><td align="center" colspan="2"><form  method='post' name='commentForm' action="<c:url value="/job/comments/${job.jobId}.do" />" onsubmit="return validate();">
		<textarea id="comment" name="comment" cols="70" rows="4"></textarea><br />
		<input type='submit' value='<fmt:message key="jobComment.submitNewComment.label" />'/></form></td></tr>
	</c:if>
		<tr>
			<td class="label" style="background-color:#FAF2D6" nowrap><fmt:message key="pageTitle.job/comments.label"/></td>
			<td class="label" style="background-color:#FAF2D6" nowrap><fmt:message key="jobComment.jobCommentDate.label"/></td>
		</tr>
		<c:forEach items="${facilityJobCommentsList}" var="facilityJobComment">
		<tr>
			<td style="padding-top:20px;padding-bottom:20px;padding-left:20px;padding-right:10px; " width="70%"><c:out value="${facilityJobComment.getValue()}" /></td>
			<td style="padding-top:20px;padding-bottom:20px;padding-left:10px;padding-right:20px; " width="30%"><fmt:formatDate value="${facilityJobComment.getDate()}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		</tr>
		</c:forEach>
	

</table>
