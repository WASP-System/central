<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />
<title><fmt:message key="pageTitle.job/comments.label"/></title>
<h1><fmt:message key="pageTitle.job/comments.label"/> - J<c:out value="${job.jobId}" /></h1>

<form  method='post' name='commentForm' action="<c:url value="/job/comments/${job.jobId}.do" />" onsubmit="return validate();">
	<table class="data" style="margin: 0px 0">
		<tr  ><td class="label-centered" style="background-color:#FAF2D6" nowrap><fmt:message key="jobComment.addNewJobComment.label" /></td></tr>
		<tr><td align="center"><textarea id="comment" name="comment" cols="70" rows="4"></textarea><br />
		<input type='submit' value='<fmt:message key="jobComment.submitNewComment.label" />'/></td></tr>
	</table>
</form>
<c:forEach items="${facilityJobCommentsList}" var="facilityJobComment">
<br />
<c:out value="${facilityJobComment}" />
</c:forEach>		
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
		