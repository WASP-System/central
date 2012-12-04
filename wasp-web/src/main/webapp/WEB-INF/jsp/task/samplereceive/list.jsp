<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<title><fmt:message key="pageTitle.task/samplereceive/list.label"/></title>
<h1><fmt:message key="task.samplereceive_title.label" /></h1>

<c:choose>
<c:when test="${jobList.size()==0}">
<h2><fmt:message key="samplereceivetask.subtitle_none.label" /></h2>
</c:when>
<c:otherwise>

<c:set var="currentJobId" value="-1" scope="page" />
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_jobId.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_jobName.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_submitter.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_sample.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_molecule.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_action.label" /></th></tr>

<c:forEach items="${jobList}" var="job">
	<c:if test='${currentJobId != "-1" && currentJobId !=  job.getJobId()}'>
 		<tr><td colspan="6" style='background-color:black'></td></tr>
 	</c:if>
	<c:set var="samplesList" value="${jobAndSamplesMap.get(job)}" scope="page" />
	<c:forEach items="${samplesList}" var="sample">
		<tr class="FormData">
		<td style='text-align:center'><a href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.getJobId()}.do" />">J<c:out value="${job.getJobId()}" /></a></td>          
		<td style='text-align:center'><c:out value="${job.getName()}" /></td>
		<td style='text-align:center'><c:out value="${job.getUser().getFirstName()}" /> <c:out value="${job.getUser().getLastName()}" /></td>
		<td style='text-align:center'><c:out value="${sample.getName()}" /></td>
		<td style='text-align:center'><c:out value="${sample.getSampleType().getName()}" /></td>
		<td style='text-align:center'>
			
		<form action="<c:url value="/task/samplereceive/receive.do"/>" method="POST" onsubmit="return validate(this)">
 		<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleId" value="${sample.getSampleId()}"> 
 		<input class="FormElement ui-widget-content ui-corner-all" type="radio" id = "receivedStatus" name = "receivedStatus" value = "RECEIVED"><fmt:message key="task.samplereceive_received.label" /> &nbsp;<input class="FormElement ui-widget-content ui-corner-all" type="radio" id = "receivedStatus" name = "receivedStatus" value = "WITHDRAWN"><fmt:message key="task.samplereceive_withdrawn.label" /> &nbsp;<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="task.samplereceive_submit.label" />">
		</form>
				
		</td>
		</tr>
		<c:set var="currentJobId" value="${job.getJobId()}" scope="page" />	
	</c:forEach>	
</c:forEach>

</table>
</c:otherwise>
</c:choose>