<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<title><fmt:message key="pageTitle.task/libraryqc/list.label"/></title>
<h1><fmt:message key="task.libraryqc_title.label" /></h1>

<c:choose>
<c:when test="${jobList.size()==0}">
<h2><fmt:message key="libraryqctask.subtitle_none.label" /></h2>
</c:when>
<c:otherwise>

<c:set var="currentJobId" value="-1" scope="page" />
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.libraryqc_jobId.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.libraryqc_jobName.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.libraryqc_submitter.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.libraryqc_sample.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.libraryqc_molecule.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.libraryqc_action.label" /></th></tr>

<c:forEach items="${jobList}" var="job">
	<c:if test='${currentJobId != "-1" && currentJobId !=  job.getJobId()}'>
 		<tr><td colspan="6" style='background-color:black'></td></tr>
 	</c:if>
	<c:set var="librariesList" value="${jobAndSamplesMap.get(job)}" scope="page" />
	<c:forEach items="${librariesList}" var="library">
		<tr class="FormData">
		<td style='text-align:center'><a href="<c:url value="/libraryDnaToLibrary/listJobSamples/${job.getJobId()}.do" />">J<c:out value="${job.getJobId()}" /></a></td>          
		<td style='text-align:center'><c:out value="${job.getName()}" /></td>
		<td style='text-align:center'><c:out value="${job.getUser().getFirstName()}" /> <c:out value="${job.getUser().getLastName()}" /></td>
		<td style='text-align:center'><c:out value="${library.getName()}" /></td>
		<td style='text-align:center'><c:out value="${library.getSampleType().getName()}" /></td>
		<td style='text-align:center'>
			
		<form action="<c:url value="/task/libraryqc/qc.do"/>" method="POST" onsubmit="return validate(this)">
 		<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="libraryId" value="${sample.getSampleId()}"> 
 		<input class="FormElement ui-widget-content ui-corner-all" type="radio" id = "qcStatus" name = "qcStatus" value = "PASSED"><fmt:message key="task.libraryqc_passed.label" /> &nbsp;<input class="FormElement ui-widget-content ui-corner-all" type="radio" id = "qcStatus" name = "qcStatus" value = "FAILED"><fmt:message key="task.libraryqc_failed.label" /> &nbsp;<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="task.libraryqc_submit.label" />">
		</form>
				
		</td>
		</tr>
		<c:set var="currentJobId" value="${job.getJobId()}" scope="page" />	
	</c:forEach>	
</c:forEach>

</table>
</c:otherwise>
</c:choose>