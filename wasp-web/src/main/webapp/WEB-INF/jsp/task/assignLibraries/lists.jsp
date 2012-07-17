<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br />

<title><fmt:message key="pageTitle.task/assignLibraries/lists.label"/></title>
<h1><fmt:message key="pageTitle.task/assignLibraries/lists.label"/></h1>

<c:set var="currentJobId" value="-1" scope="page" />
<h2><fmt:message key="jobListAssignLibrary.tableHeader.label" /></h2>
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListAssignLibrary.jobId.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListAssignLibrary.jobName.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListAssignLibrary.submitter.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListAssignLibrary.pi.label" /></th></tr>

<c:choose>
<c:when test="${jobList.size()==0}">
		<tr class="FormData"><td colspan="4" style='text-align:center'>None</td></tr>
</c:when>
<c:otherwise>
<c:forEach items="${jobList}" var="job">	
		<tr class="FormData">
		<td style='text-align:center'>J<c:out value="${job.getJobId()}" /> [<a href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.getJobId()}.do" />">view</a>]</td>          
		<td style='text-align:center'><c:out value="${job.getName()}" /></td>
		<td style='text-align:center'><c:out value="${job.getUser().getFirstName()}" /> <c:out value="${job.getUser().getLastName()}" /></td>
		<td style='text-align:center'><c:out value="${job.getLab().getUser().getFirstName()}" /> <c:out value="${job.getLab().getUser().getLastName()}" /></td>
		</tr>
		<c:set var="currentJobId" value="${job.getJobId()}" scope="page" />	
</c:forEach>
</c:otherwise>
</c:choose>
</table>
<br />
<h2><fmt:message key="activePlatformUnit.tableHeader.label" /></h2>
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="activePlatformUnit.name.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="activePlatformUnit.barcode.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="activePlatformUnit.type.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListAssignLibrary.numberLanes.label" /></th></tr>

<c:choose>
<c:when test="${activePlatformUnits.size()==0}">
		<tr class="FormData"><td colspan="4" style='text-align:center'>None</td></tr>
</c:when>
<c:otherwise>
<c:forEach items="${activePlatformUnits}" var="platformUnit" varStatus="status">	
		<tr class="FormData">
		<td style='text-align:center'><c:out value="${platformUnit.getName()}" /> [<a href="<c:url value="/facility/platformunit/showPlatformUnit/${platformUnit.getSampleId()}.do" />">view</a>]</td>          
		<td style='text-align:center'><c:out value="${barcodes[status.index]}" /></td>
		<td style='text-align:center'><c:out value="${platformUnit.getSampleSubtype().getName()}" /> </td>
		<td style='text-align:center'><c:out value="${lanes[status.index]}" /></td>
		</tr>
</c:forEach>
</c:otherwise>
</c:choose>
</table>