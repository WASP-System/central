<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<br />

<title><fmt:message key="pageTitle.task/assignLibraries/lists.label"/></title>
<h1><fmt:message key="pageTitle.task/assignLibraries/lists.label"/></h1>

<c:set var="currentJobId" value="-1" scope="page" />
<h2><fmt:message key="jobListAssignLibrary.tableHeader.label" /></h2>
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListAssignLibrary.jobId.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListAssignLibrary.jobName.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListAssignLibrary.submitter.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListAssignLibrary.pi.label" /></th></tr>

<c:choose>
<c:when test="${jobList.size()==0}">
		<tr class="FormData"><td colspan="4" style='text-align:center'><fmt:message key="jobListAssignLibrary.none.label" /></td></tr>
</c:when>
<c:otherwise>
<c:forEach items="${jobList}" var="job">	
		<tr class="FormData">
		<td style='text-align:center'>J<c:out value="${job.getJobId()}" /> [<a href="<wasp:relativeUrl value="job/${job.getJobId()}/homepage.do" />"><fmt:message key="jobListAssignLibrary.view.label" /></a>]</td>          
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
<tr class="FormData"><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="activePlatformUnit.name.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="activePlatformUnit.barcode.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="activePlatformUnit.type.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="jobListAssignLibrary.numberCells.label" /></th></tr>

<c:choose>
<c:when test="${activePlatformUnitsWithViewLinks.size()==0}">
		<tr class="FormData"><td colspan="4" style='text-align:center'><fmt:message key="jobListAssignLibrary.none.label" /></td></tr>
</c:when>
<c:otherwise>
<c:forEach items="${activePlatformUnitsWithViewLinks.keySet()}" var="platformUnit" varStatus="status">	
		<tr class="FormData">
		<td style='text-align:center'><c:out value="${platformUnit.getName()}" /> [<a href="<wasp:relativeUrl value="${activePlatformUnitsWithViewLinks.get(platformUnit)}" />"><fmt:message key="jobListAssignLibrary.view.label" /></a>]</td>          
		<td style='text-align:center'><c:out value="${barcodes[status.index]}" /></td>
		<td style='text-align:center'><c:out value="${platformUnit.getSampleSubtype().getName()}" /> </td>
		<td style='text-align:center'><c:out value="${cells[status.index]}" /></td>
		</tr>
</c:forEach>
</c:otherwise>
</c:choose>
</table>