<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<title><fmt:message key="pageTitle.job/comments.label"/></title>
<h1><fmt:message key="pageTitle.job/comments.label"/> - J<c:out value="${job.jobId}" /></h1>
<table class="data" style="margin: 0px 0">
	<c:if test='${permissionToAddEditComment==true}'>
		<tr  ><td class="label" style="background-color:#FAF2D6" nowrap><fmt:message key="jobComment.addNewJobComment.label" /></td></tr>
		<tr><td align="center"><form  method='post' name='commentForm' action="<c:url value="/job/comments/${job.jobId}.do" />" onsubmit="return validate();">
		<textarea id="comment" name="comment" cols="70" rows="4"></textarea><br />
		<input type='submit' value='<fmt:message key="jobComment.submitNewComment.label" />'/></form></td></tr>
	</c:if>
	<c:if test='${fn:length(facilityJobCommentsList) > 0}'>
		<tr  ><td class="label" style="background-color:#FAF2D6" nowrap><fmt:message key="pageTitle.job/comments.label"/></td></tr>
	</c:if>
		<c:forEach items="${facilityJobCommentsList}" var="facilityJobComment">
			<tr><td style="padding-top:20px;padding-bottom:20px;padding-left:20px;padding-right:20px; "><c:out value="${facilityJobComment}" /></td></tr>
		</c:forEach>
	

</table>
<%-- 
<c:forEach items="${facilityJobCommentsList}" var="facilityJobComment">
<br />
<c:out value="${facilityJobComment}" />
</c:forEach>
<br />
--%>
<%-- 
<table class="data" style="margin: 0px 0">
		<tr  ><td style="background-color:#FAF2D6;font-weight:bold" nowrap><fmt:message key="pageTitle.job/comments.label"/></td></tr>
		<c:forEach items="${facilityJobCommentsList}" var="facilityJobComment">
		<tr><td><c:out value="${facilityJobComment}" /></td></tr>
		</c:forEach>
</table>
--%>

		
		<%-- 
		<tr ><td ><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td><td><fmt:message key="jobdetail_for_import.jobSubmitter.label" /></td></tr>
		<tr ><td ><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td><td><fmt:message key="jobdetail_for_import.jobPI.label" /></td></tr>

		<c:forEach items="${additionalJobViewers}" var="additionalJobViewer">
			<tr><td ><c:out value="${additionalJobViewer.getFirstName()} ${additionalJobViewer.getLastName()}"/></td>
			<td>
				<c:if test='${currentWebViewerIsSuperuserSubmitterOrPI==true || currentWebViewer.getUserId() == additionalJobViewer.getUserId()}'>
					<a  href='javascript:void(0)' onclick = 'if(confirm("Do you really want to remove this viewer?")){location.href="<c:url value="/sampleDnaToLibrary/removeViewerFromJob/${job.jobId}/${additionalJobViewer.getUserId()}.do" />";}'>remove</a>
				</c:if>
			</td>
		</c:forEach>
		
		<c:if test='${currentWebViewerIsSuperuserSubmitterOrPI==true}'>
 			<input type='hidden' name='jobId' value='<c:out value="${job.jobId}" />'/>			 				
			<tr ><td colspan="2"  class="label-centered" style="background-color:#FAF2D6" nowrap><fmt:message key="listJobSamples.addNewViewer.label" /></td></tr>
 			<tr><td ><fmt:message key="listJobSamples.newViewerEmailAddress.label" />: </td><td ><input type='text' name='newViewerEmailAddress' id="newViewerEmailAddress" size='20' maxlength='50'></td></tr>
			<tr><td colspan="2" align="center"><input type='submit' value='<fmt:message key="listJobSamples.submit.label" />'/></td></tr>
			
		</c:if>
		--%>
		