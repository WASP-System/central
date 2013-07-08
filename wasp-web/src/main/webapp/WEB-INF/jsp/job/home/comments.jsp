<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<div >

<table class="data" style="margin: 0px 0; width:600px" >
	<c:if test='${permissionToAddEditComment==true}'>
		<tr ><th class="label" nowrap colspan="3"><fmt:message key="jobComment.addNewJobComment.label" /></th></tr>
		<tr><td align="center" colspan="3">
			<%-- <form  method='post' name='commentForm' id='commentForm' action="" onsubmit='postForm("commentForm","<c:url value="/job/${job.getId()}/comments.do" />"); return false;' >--%>
			<form  method='post' name='commentForm' id='commentForm' action="" onsubmit='postFormWithAjax(this,"<c:url value="/job/${job.getId()}/comments.do" />"); return false;' >
				<textarea id="comment" name="comment" cols="70" rows="4"></textarea><br />
				<c:if test="${fn:length(errorMessage)>0}">
					<div id="robfadediv" >
					<span style="color:red;font-weight:bold;text-align:center"><c:out value="${errorMessage}" /></span>
					<br />
					</div>
					<script type="text/javascript">
					//http://papermashup.com/jquery-fading-a-div-after-a-certain-time/ 
					$(document).ready(function(){
						   setTimeout(function(){
						  $("#robfadediv").fadeOut("slow", function () {
						  $("#robfadediv").remove();
						      });
						 
						}, 2000);
						 }); 
					</script>
				</c:if>
				<input type='submit' value='<fmt:message key="jobComment.submitNewComment.label" />'/>
			</form>
		</td></tr>
	</c:if>
	<c:if test='${fn:length(userSubmittedJobCommentsList) > 0}'>
		<tr>
			<th class="label" nowrap><fmt:message key="jobComment.jobCommentDate.label"/></th>
			<th class="label" nowrap>Submitted By</th>
			<th class="label" nowrap><fmt:message key="jobComment.jobSubmitterComment.label"/></th>
		</tr>	
		<c:forEach items="${userSubmittedJobCommentsList}" var="userSubmittedJobComment">
			<tr>
				<td style="padding-top:5px;padding-bottom:5px;padding-left:5px;padding-right:5px;" nowrap><fmt:formatDate value="${userSubmittedJobComment.getDate()}" pattern="yyyy-MM-dd" /></td>
				<td style="padding-top:5px;padding-bottom:5px;padding-left:5px;padding-right:5px;" ><c:out value="${userSubmittedJobComment.getUser().getNameFstLst()}" /></td>
				<%-- for potential problems with escapeXML in next c:out see: http://www.coderanch.com/t/535302/JSP/java/Keeping-line-breaks-String --%>
				<td style="padding-top:5px;padding-bottom:5px;padding-left:5px;padding-right:5px;" ><c:out value="${userSubmittedJobComment.getValue()}" escapeXml="false"  /> </td>
			</tr>
		</c:forEach>
	</c:if>
	
	<c:if test='${fn:length(facilityJobCommentsList) > 0}'>
		<tr>
			<th class="label" nowrap><fmt:message key="jobComment.jobCommentDate.label"/></th>
			<th class="label" nowrap>Submitted By</th>
			<th class="label" nowrap><fmt:message key="jobComment.facilityComment.label"/></th>
		</tr>	
		<c:forEach items="${facilityJobCommentsList}" var="facilityJobComment">
		<tr>
			<td style="padding-top:5px;padding-bottom:5px;padding-left:5px;padding-right:5px;" nowrap><fmt:formatDate value="${facilityJobComment.getDate()}" pattern="yyyy-MM-dd" /></td>
			<td style="padding-top:5px;padding-bottom:5px;padding-left:5px;padding-right:5px;" ><c:out value="${facilityJobComment.getUser().getNameFstLst()}" /></td>
			<%-- for potential problems with escapeXML in next c:out see: http://www.coderanch.com/t/535302/JSP/java/Keeping-line-breaks-String --%>
			<td style="padding-top:5px;padding-bottom:5px;padding-left:5px;padding-right:5px;" ><c:out value="${facilityJobComment.getValue()}" escapeXml="false"  /> </td>
		</tr>
		</c:forEach>
	</c:if>

</table>

</div>

<br /><br />