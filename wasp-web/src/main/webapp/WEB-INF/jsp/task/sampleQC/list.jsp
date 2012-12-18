<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<%-- 
<title><fmt:message key="pageTitle.task/sampleqc/list.label"/></title>
<h1><fmt:message key="task.sampleqc_title.label" /></h1>

<c:choose>
<c:when test="${jobList.size()==0}">
<h2><fmt:message key="task.sampleqc_subtitle_none.label" /></h2>
</c:when>
<c:otherwise>

<c:set var="currentJobId" value="-1" scope="page" />
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.sampleqc_jobId.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.sampleqc_jobName.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.sampleqc_submitter.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.libraryqc_sample.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.libraryqc_molecule.label" /></th><th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.libraryqc_action.label" /></th></tr>

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
			
		<form action="<c:url value="/task/sampleqc/qc.do"/>" method="POST" onsubmit="return validate(this)">
 		<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleId" value="${sample.getSampleId()}"> 
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
<br /><hr /><br />
<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
--%>
<%--serves no purpose here<title><fmt:message key="pageTitle.task/samplereceive/list.label"/></title>--%>
<h1><fmt:message key="task.sampleqc_title.label" /></h1>

<c:choose>
<c:when test="${jobList.size()==0}">
<h2><fmt:message key="task.sampleqc_subtitle_none.label" /></h2>
</c:when>
<c:otherwise>

<c:set var="currentJobId" value="-1" scope="page" />
<table class="EditTable ui-widget ui-widget-content">
<c:forEach items="${jobList}" var="job">
	<c:if test='${currentJobId != "-1"}'>
 		<tr><td colspan="6" style='background-color:black'></td></tr>
 	</c:if>
	<tr class="FormData">
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.sampleqc_jobId.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.sampleqc_jobName.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.sampleqc_submitter.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.sampleqc_molecule.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.sampleqc_sample.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.sampleqc_action.label" /></th>
	</tr>
	<c:set var="samplesList" value="${jobAndSamplesMap.get(job)}" scope="page" />
	<c:forEach items="${samplesList}" var="sample" varStatus="status">	
		<tr class="FormData">
			<c:choose>
				<c:when test="${currentJobId !=  job.getJobId()}">
					<td style='text-align:center;'><a style="color: #801A00;" href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.getJobId()}.do" />">J<c:out value="${job.getJobId()}" /></a></td>          
					<td style='text-align:center'><c:out value="${job.getName()}" /></td>
					<td style='text-align:center'><c:out value="${job.getUser().getFirstName()}" /> <c:out value="${job.getUser().getLastName()}" /></td>
				</c:when>
				<c:otherwise>
					<td style='text-align:center'>&nbsp;</td>
					<td style='text-align:center'>&nbsp;</td>
					<td style='text-align:center'>&nbsp;</td>
				</c:otherwise>
			</c:choose>
			<td style='text-align:center'><c:out value="${sample.getSampleType().getName()}" /></td>
			<td style='text-align:center'><c:out value="${sample.getName()}" /> </td>
			<td style='text-align:center'>
			
				<form action="<c:url value="/task/sampleqc/qc.do"/>" id="theForm<c:out value="${sample.getSampleId()}" />" method="POST" onsubmit="return validate(this);">
 				<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleId" value="${sample.getSampleId()}"> 
 				<input class="FormElement ui-widget-content ui-corner-all" type="radio" id = "qcStatus" name = "qcStatus" value = "PASSED"><fmt:message key="task.sampleqc_passed.label" /> &nbsp;
 				<input class="FormElement ui-widget-content ui-corner-all" onclick='selectedFail("theForm<c:out value="${sample.getSampleId()}" />");' type="radio" id = "qcStatus" name = "qcStatus" value = "FAILED"><fmt:message key="task.sampleqc_failed.label" /><br />
 				<textarea id="comment" name="comment" cols="25" rows="2"></textarea><br />
					<input class="FormElement ui-widget-content ui-corner-all" type="reset" value="<fmt:message key="task.sampleqc_reset.label" />">
				<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="task.sampleqc_submit.label" />">
				</form>

			</td>
		</tr>	
		
		<c:if test="${!status.last}">
				<td >&nbsp;</td>
				<td >&nbsp;</td>
				<td >&nbsp;</td>
				<td colspan="3" style='border-top: 2px solid gray;'>&nbsp;</td>
		</c:if>
		
		
		<c:set var="currentJobId" value="${job.getJobId()}" scope="page" />	
	</c:forEach>
</c:forEach>

</table>
</c:otherwise>
</c:choose>
