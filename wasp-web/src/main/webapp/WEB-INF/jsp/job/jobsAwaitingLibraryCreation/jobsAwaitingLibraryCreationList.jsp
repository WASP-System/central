<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />

<title><fmt:message key="pageTitle.job/jobsAwaitingLibraryCreation/jobsAwaitingLibraryCreationList.label"/></title>
<h1><fmt:message key="pageTitle.job/jobsAwaitingLibraryCreation/jobsAwaitingLibraryCreationList.label"/></h1>

<c:choose>
<c:when test="${jobList.size()==0}">
<h2><fmt:message key="jobListCreateLibrary.noneNeeded.label" /></h2>
</c:when>
<c:otherwise>

<c:set var="currentJobId" value="-1" scope="page" />
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListCreateLibrary.jobId.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListCreateLibrary.jobName.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListCreateLibrary.submitter.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListCreateLibrary.pi.label" /></th></tr>

<c:forEach items="${jobList}" var="job">
<%-- 	
	<c:if test='${currentJobId != "-1" && currentJobId !=  job.getJobId()}'>
 		<tr><td colspan="6" style='background-color:black'></td></tr>
 	</c:if>
--%>	
		<tr class="FormData">
		<td style='text-align:center'><a href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.getJobId()}.do" />">J<c:out value="${job.getJobId()}" /></a></td>          
		<td style='text-align:center'><c:out value="${job.getName()}" /></td>
		<td style='text-align:center'><c:out value="${job.getUser().getFirstName()}" /> <c:out value="${job.getUser().getLastName()}" /></td>
		<td style='text-align:center'><c:out value="${job.getLab().getUser().getFirstName()}" /> <c:out value="${job.getLab().getUser().getLastName()}" /></td>
		
		</tr>
		<c:set var="currentJobId" value="${job.getJobId()}" scope="page" />	
</c:forEach>

</table>
</c:otherwise>
</c:choose>