<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<div>
<%-- <form  method='post' name='addJobViewer' action="<c:url value="/sampleDnaToLibrary/addJobViewer.do" />" onsubmit="return validate_email();">--%>
<form  method='post' name='addJobViewerForm' id='addJobViewerForm' action="" onsubmit='postForm("addJobViewerForm","<c:url value="/job/${job.getId()}/viewerManager.do" />"); return false;' >
	<table class="data" style="margin: 0px 0px">
	
	<c:if test='${currentWebViewerIsSuperuserSubmitterOrPI==true}'>
		<%-- 
		<tr ><th colspan="2"  class="label" nowrap>Grant Other WASP Users Ability To View This Job</th></tr>
		<tr ><th colspan="2"  style="background:gray;" nowrap></th></tr>
		<tr ><th colspan="2"  class="label" nowrap><fmt:message key="listJobSamples.addNewViewer.label" /></th></tr>
		--%>
		<tr ><th colspan="2"  class="label" nowrap>Grant Other WASP Users Ability To View This Job</th></tr>
			<tr><td ><fmt:message key="listJobSamples.newViewerEmailAddress.label" />: </td><td ><input type='text' name='newViewerEmailAddress' id="newViewerEmailAddress" size='30' maxlength='50' value="<c:out value="${newViewerEmailAddress}" />"></td></tr>

		<c:if test="${fn:length(errorMessage)>0}">
			<tr><td colspan="2" align="center" style="color:red;font-weight:bold"><c:out value="${errorMessage}" /></td></tr>
		</c:if>
		<c:if test="${fn:length(successMessage)>0}">
			<tr><td colspan="2" align="center" style="color:green;font-weight:bold"><c:out value="${successMessage}" /></td></tr>
		</c:if>
		
		<tr><td colspan="2" align="center"><input name="bu" id="bu" class="button" type="button" value="Submit" onclick='postForm("addJobViewerForm","<c:url value="/job/${job.getId()}/viewerManager.do" />");' /></td></tr>
	</c:if>	
	
	<tr  ><th colspan="2" class="label" nowrap><fmt:message key="listJobSamples.jobViewers.label" /></th></tr>
	<tr ><td ><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td><td><fmt:message key="jobdetail_for_import.jobSubmitter.label" /></td></tr>
	<tr ><td ><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td><td><fmt:message key="jobdetail_for_import.jobPI.label" /></td></tr>

	<c:forEach items="${additionalJobViewers}" var="additionalJobViewer">
		<tr><td ><c:out value="${additionalJobViewer.getFirstName()} ${additionalJobViewer.getLastName()}"/></td>
			<td class="DataTD">
				<c:if test='${currentWebViewerIsSuperuserSubmitterOrPI==true || currentWebViewer.getUserId() == additionalJobViewer.getUserId()}'>
					<a  href='javascript:void(0)' onclick = 'if(confirm("Do you really want to remove this viewer?")){doGetWithAjax("<c:url value="/job/${job.getId()}/user/${additionalJobViewer.getUserId()}/removeJobViewer.do" />");}'><fmt:message key="listJobSamples.remove.label" /></a>
				</c:if>
			</td>
		</tr>
	</c:forEach>
	
	
	</table>
</form>
</div>
<br /><br />