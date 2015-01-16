<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<%--  TODO: Declare style in css file (e.g. /src/main/webapp/css/base.css), not in .jsp and reuse where possible !!!! --%>

<%--serves no purpose here<title><fmt:message key="pageTitle.task/samplereceive/list.label"/></title>--%>
<h1><fmt:message key="task.sampleqc_title.label" /></h1>

<c:choose>
<c:when test="${jobList.size()==0}">
<h2><fmt:message key="task.sampleqc_subtitle_none.label" /></h2>
</c:when>
<c:otherwise>

<%-- original
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
					<td style='text-align:center;'><a style="color: #801A00;" href="<wasp:relativeUrl value="job/${job.getJobId()}/homepage.do" />">J<c:out value="${job.getJobId()}" /></a></td>          
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
			
				<form action="<wasp:relativeUrl value="task/sampleqc/qc.do"/>" id="theForm<c:out value="${sample.getSampleId()}" />" method="POST" onsubmit="return validate(this);">
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
--%>

<c:set var="currentJobId" value="-1" scope="page" />
<table class="EditTable ui-widget ui-widget-content">
<c:forEach items="${jobList}" var="job">
	<c:if test='${currentJobId != "-1"}'>
 		<tr><td colspan="7" style='background-color:black'></td></tr>
 	</c:if>
	<tr class="FormData">
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_jobId.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_jobName.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_submitter.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_molecule.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_sample.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_sampleinternalid.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_action.label" /></th>
	</tr>
	<c:set var="samplesList" value="${jobAndSamplesMap.get(job)}" scope="page" />
	<form action="<wasp:relativeUrl value="task/sampleqc/qc.do"/>" name="theForm<c:out value="${job.getJobId()}" />" id="theForm<c:out value="${job.getJobId()}" />" method="POST" onsubmit="return validateqcform(this);">
	<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobId" value="${job.getJobId()}"> 
	<c:forEach items="${samplesList}" var="sample" varStatus="status">	
		<tr class="FormData">
			<c:choose>
				<c:when test="${currentJobId !=  job.getJobId()}">
					<td style='text-align:center;'><a style="color: #801A00;" href="<wasp:relativeUrl value="job/${job.getJobId()}/homepage.do" />">J<c:out value="${job.getJobId()}" /></a></td>          
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
			<td style='text-align:center'><c:out value="${sample.getId()}" /> </td>
			<td style='text-align:center'>
				<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleId" value="${sample.getSampleId()}"> 
	 			<select class="FormElement ui-widget-content ui-corner-all" id="qcStatus<c:out value="${sample.getSampleId()}" />" name="qcStatus<c:out value="${sample.getSampleId()}" />" size="1" >
	 				<option value=""><fmt:message key="task.samplereceive_select.label" />
	 				<option value="PASSED"><fmt:message key="task.sampleqc_passed.label" />
	 				<option value="FAILED"><fmt:message key="task.sampleqc_failed.label" />
				</select>
				<textarea style='vertical-align:middle' id="comment<c:out value="${sample.getSampleId()}" />" name="comment<c:out value="${sample.getSampleId()}" />" cols="20" rows="2" placeholder="provide reason if failed"></textarea>
			</td>
		</tr>
		
		<c:if test="${status.last}">
			<tr class="FormData">
				<td style='text-align:center'>&nbsp;</td>
				<td style='text-align:center'>&nbsp;</td>
				<td style='text-align:center'>&nbsp;</td>
				<td style='text-align:center'>&nbsp;</td>
				<td style='text-align:center'>&nbsp;</td>
				<td style='text-align:center'>&nbsp;</td>
				<td style='text-align:center'>
					<span style="font-size:10px"><a href="javascript:void(0)" onclick='set("theForm<c:out value="${job.getJobId()}" />", "PASSED");'>set all passed</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick='set("theForm<c:out value="${job.getJobId()}" />", "FAILED");'>set all failed</a><br /></span>
					<input class="FormElement ui-widget-content ui-corner-all" type="reset" value="<fmt:message key="task.samplereceive_reset.label" />">
					<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="task.samplereceive_submit.label" />">
				</td>
			</tr>
		</c:if>
		
		<c:set var="currentJobId" value="${job.getJobId()}" scope="page" />	
	</c:forEach>
	</form>	
</c:forEach>

</table>
</c:otherwise>
</c:choose>