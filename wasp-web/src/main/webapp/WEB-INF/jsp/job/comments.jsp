<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<title><fmt:message key="pageTitle.job/comments.label"/></title>
<h1><fmt:message key="pageTitle.job/comments.label"/> - J<c:out value="${job.jobId}" /></h1>
<table class="data" style="margin: 0px 0">
	<c:if test='${permissionToAddEditComment==true}'>
		<tr ><th class="label" nowrap colspan="2"><fmt:message key="jobComment.addNewJobComment.label" /></th></tr>
		<tr><td align="center" colspan="2"><form  method='post' name='commentForm' action="<wasp:relativeUrl value="job/comments/${job.jobId}.do" />" onsubmit="return validate();">
		<textarea id="comment" name="comment" cols="70" rows="4" maxlength="250"></textarea><br />
		<input type='submit' value='<fmt:message key="jobComment.submitNewComment.label" />'/></form></td></tr>
	</c:if>
	<c:if test='${fn:length(userSubmittedJobCommentsList) > 0}'>
		<tr>
			<th class="label" nowrap><fmt:message key="jobComment.jobCommentDate.label"/></th>
			<th class="label" nowrap><fmt:message key="jobComment.jobSubmitterComment.label"/></th>
		</tr>	
		<tr>
			<td style="padding-top:20px;padding-bottom:20px;padding-left:10px;padding-right:20px; " width="20%"><fmt:formatDate value="${userSubmittedJobCommentsList[0].getDate()}" pattern="yyyy-MM-dd" /></td>
			<%-- for potential problems with escapeXML in next c:out see: http://www.coderanch.com/t/535302/JSP/java/Keeping-line-breaks-String --%>
			<td style="padding-top:20px;padding-bottom:20px;padding-left:20px;padding-right:10px; " width="80%"><c:out value="${userSubmittedJobCommentsList[0].getValue()}" escapeXml="false"  /> (<c:out value="${userSubmittedJobCommentsList[0].getUser().getNameFstLst()}" escapeXml="false"  />)</td>
		</tr>
	</c:if>
	
	<c:if test='${fn:length(facilityJobCommentsList) > 0}'>
		<tr>
			<th class="label" nowrap><fmt:message key="jobComment.jobCommentDate.label"/></th>
			<th class="label" nowrap><fmt:message key="jobComment.facilityComment.label"/></th>
		</tr>	
		<c:forEach items="${facilityJobCommentsList}" var="facilityJobComment">
		<tr>
			<td style="padding-top:20px;padding-bottom:20px;padding-left:10px;padding-right:20px; " width="20%"><fmt:formatDate value="${facilityJobComment.getDate()}" pattern="yyyy-MM-dd" /></td>
			<%-- for potential problems with escapeXML in next c:out see: http://www.coderanch.com/t/535302/JSP/java/Keeping-line-breaks-String --%>
			<td style="padding-top:20px;padding-bottom:20px;padding-left:20px;padding-right:10px; " width="80%"><c:out value="${facilityJobComment.getValue()}" escapeXml="false"  /> (<c:out value="${facilityJobComment.getUser().getNameFstLst()}" escapeXml="false"  />)</td>
		</tr>
		</c:forEach>
	</c:if>

</table>
