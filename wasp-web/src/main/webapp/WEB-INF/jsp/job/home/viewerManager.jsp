<%@ include file="/WEB-INF/jsp/taglib.jsp" %> 

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<c:import url="/WEB-INF/jsp/job/home/fadingMessage.jsp" />
<br />
<form  method='post' id='addJobViewerFormId' onsubmit='postFormWithAjax("addJobViewerFormId","<wasp:relativeUrl value="job/${job.getId()}/viewerManager.do" />"); return false;'>
	<table class="data" style="margin: 0px 0px">
	
		<c:if test='${currentWebViewerIsSuperuserOrJobSubmitterOrJobPI==true}'>
			<tr ><th colspan="2"  class="label" nowrap><fmt:message key="jobHomeViewerManager.grantOtherUsers.label" /></th></tr>
				<tr><td ><fmt:message key="listJobSamples.newViewerEmailAddress.label" />: </td><td ><input type='text' name='newViewerEmailAddress' id="newViewerEmailAddress" size='30' maxlength='50' value="<c:out value="${newViewerEmailAddress}" />"></td></tr>
	
			<c:if test="${fn:length(errorMessage)>0}">
				<tr><td colspan="2" align="center" style="color:red;font-weight:bold"><c:out value="${errorMessage}" /></td></tr>
			</c:if>
			<tr><td colspan="2" align="center"><input type='submit' value='Submit'/></td></tr>
		</c:if>	
		<c:forEach items="${jobViewers}" var="jobViewer">
			<c:choose>
				<c:when test="${jobViewer.getId()==jobSubmitter.getId() && jobViewer.getId()==jobPI.getId()}"> <%-- submitter is also lab PI --%>
					<tr><td ><c:out value="${jobViewer.lastName}" />, <c:out value="${jobViewer.firstName}" /></td><td style="text-align:center"><fmt:message key="jobdetail_for_import.jobSubmitter.label" /> &amp; <fmt:message key="jobdetail_for_import.jobPI.label" /></td></tr>
				</c:when>
				<c:when test="${jobViewer.getId()==jobSubmitter.getId()}">
					<tr><td ><c:out value="${jobViewer.lastName}" />, <c:out value="${jobViewer.firstName}" /></td><td style="text-align:center"><fmt:message key="jobdetail_for_import.jobSubmitter.label" /></td></tr>
				</c:when>
				<c:when test="${jobViewer.getId()==jobPI.getId()}">
					<tr><td ><c:out value="${jobViewer.lastName}" />, <c:out value="${jobViewer.firstName}" /></td><td style="text-align:center"><fmt:message key="jobdetail_for_import.jobPI.label" /></td></tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td ><c:out value="${jobViewer.lastName}" />, <c:out value="${jobViewer.firstName}" /></td>
						<td class="DataTD" style="text-align:center">
							<c:if test='${currentWebViewerIsSuperuserOrJobSubmitterOrJobPI==true || currentWebViewer.getUserId() == jobViewer.getUserId()}'>
								<a  href='javascript:void(0)' onclick = 'if(confirm("<fmt:message key="listJobSamples.removeJobViewer.label" />")){doGetWithAjax("<wasp:relativeUrl value="job/${job.getId()}/user/${jobViewer.getUserId()}/removeJobViewer.do" />"); return false; }'><fmt:message key="listJobSamples.remove.label" /></a>
							</c:if>
						</td>
					</tr>
				
				</c:otherwise>
			</c:choose>
		</c:forEach>		
	</table>
</form>
<br />