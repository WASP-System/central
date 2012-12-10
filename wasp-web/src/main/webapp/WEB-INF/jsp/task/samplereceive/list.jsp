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
<c:forEach items="${jobList}" var="job">
	<c:if test='${currentJobId != "-1"}'>
 		<tr><td colspan="6" style='background-color:black'></td></tr>
 	</c:if>
	<tr class="FormData">
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_jobId.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_jobName.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_submitter.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_molecule.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_sample.label" /></th>
		<th class="label-centered" style="font-weight:bold; background-color:#FAF2D6"><fmt:message key="task.samplereceive_action.label" /></th>
	</tr>
	<c:set var="samplesList" value="${jobAndSamplesMap.get(job)}" scope="page" />
	<form action="<c:url value="/task/samplereceive/receive.do"/>" id="theForm<c:out value="${job.getJobId()}" />" method="POST">
	<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobId" value="${job.getJobId()}"> 
	<c:forEach items="${samplesList}" var="sample" varStatus="status">	
		<tr class="FormData">
			<c:choose>
				<c:when test="${currentJobId !=  job.getJobId()}">
					<td style='text-align:center'><a href="<c:url value="/sampleDnaToLibrary/listJobSamples/${job.getJobId()}.do" />">J<c:out value="${job.getJobId()}" /></a></td>          
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
			<td style='text-align:center'><c:out value="${sample.getName()}" /> (<c:out value="${sample.getSampleId()}" />)</td>
			<td style='text-align:center'>
				<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleId" value="${sample.getSampleId()}"> 
	 			<select class="FormElement ui-widget-content ui-corner-all" name="receivedStatus<c:out value="${job.getJobId()}" />" size="1" >
	 				<option value="">--SELECT--
	 				<option value="RECEIVED">RECEIVED
	 				<option value="WITHDRAWN">WITHDRAWN
				</select>
			</td>
		</tr>
		
		<c:if test="${status.last}">
				<td style='text-align:center'>&nbsp;</td>
				<td style='text-align:center'>&nbsp;</td>
				<td style='text-align:center'>&nbsp;</td>
				<td style='text-align:center'>&nbsp;</td>
				<td style='text-align:center'>&nbsp;</td>
				<td style='text-align:center'>
					<span style="font-size:10px"><a href="javascript:void(0)" onclick='set("receivedStatus<c:out value="${job.getJobId()}" />", "RECEIVED");'>set all received</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick='set("receivedStatus<c:out value="${job.getJobId()}" />", "WITHDRAWN");'>set all withdrawn</a><br /></span>
					<input class="FormElement ui-widget-content ui-corner-all" type="reset" value="<fmt:message key="task.samplereceive_reset.label" />">
					<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="<fmt:message key="task.samplereceive_submit.label" />">
				</td>
		</c:if>
		
		<c:set var="currentJobId" value="${job.getJobId()}" scope="page" />	
	</c:forEach>
	</form>	
</c:forEach>

</table>
</c:otherwise>
</c:choose>