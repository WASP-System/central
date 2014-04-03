<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<title><fmt:message key="pageTitle.task/samplereceive/list.label"/></title>
<h1><fmt:message key="task.samplereceive_title.label" /></h1>
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD">Job ID:</td><td class="DataTD"><a href="<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/${job.jobId}.do" />">J<c:out value="${job.jobId}" /></a></td></tr>
<tr class="FormData"><td class="CaptionTD">Job Name:</td><td class="DataTD"><c:out value="${job.name}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Submitter:</td><td class="DataTD"><c:out value="${job.user.firstName}" /> <c:out value="${job.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">PI:</td><td class="DataTD"><c:out value="${job.lab.user.firstName}" /> <c:out value="${job.lab.user.lastName}" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Submitted:</td><td class="DataTD"><fmt:formatDate value="${job.createts}" type="date" /></td></tr>
<tr class="FormData"><td class="CaptionTD">Workflow:</td><td class="DataTD"><c:out value="${job.workflow.name}" /></td></tr>
<c:forEach items='${extraJobDetailsMap}' var="detail">
	<tr class="FormData"><td class="CaptionTD">  <c:out value='${detail.key}' />   </td><td class="DataTD"> <c:out value='${detail.value}' /> </td></tr>
</c:forEach>
</table>
<br /> 
<c:choose>

<c:when test="${submittedSamplesList.size()==0}">
<h2>No Samples Found For This Job Submission</h2>
</c:when>

<c:otherwise>
<c:set var="atLeastOneSampleCanBeUpdated" value="" scope="page" />
<form action="<wasp:relativeUrl value="/task/updatesamplereceive.do"/>" method="POST">
<input type="hidden" name="jobId" value="${job.getJobId()}"> 		
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="CaptionTD" style='text-align:center'>Sample Name</td><td class="CaptionTD" style='text-align:center'>Molecule</td><td class="CaptionTD" style='text-align:center'>Status</td></tr>
 <c:forEach items="${submittedSamplesList}" var="sample" varStatus="counter">
 	<tr class="FormData"><td style='text-align:center'><c:out value="${sample.getName()}" /></td>          
	<td style='text-align:center'><c:out value="${sample.getSampleType().getName()}" /></td>
	<td style='text-align:center'>
	<c:set var="sampleHasBeenProcessed" value="${sampleHasBeenProcessedList[counter.index].booleanValue()}" scope="page" />
	<c:set var="sampleStatus" value="${receiveSampleStatusList[counter.index]}" scope="page" />
	<c:choose>
	<c:when test="${sampleHasBeenProcessed==false}">
		<c:set var="atLeastOneSampleCanBeUpdated" value="1" scope="page" />
 		<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleId" value="${sample.getSampleId()}"> 
 		<select class="FormElement ui-widget-content ui-corner-all" name="receivedStatus" size="1" >
			<c:forEach items="${receiveSampleOptionsList}" var="selectOption">
				<c:set var="selected" value="" scope="page" />
				<c:if test="${selectOption == sampleStatus}">
					<c:set var="selected" value="SELECTED" scope="page" />
				</c:if>
				<option value="<c:out value="${selectOption}" />" <c:out value="${selected}" /> ><c:out value="${selectOption}" /> 
			</c:forEach>
		</select>
	</c:when>
	<c:otherwise>
		<c:out value="${sampleStatus}" />
	</c:otherwise>
	</c:choose>
	</td>
</tr>
</c:forEach>
<c:if test="${atLeastOneSampleCanBeUpdated == '1'}">
	<tr><td colspan='3' style='text-align:center'><input type='submit' value='Submit' class="ui-widget ui-widget-content fm-button" /><input class=" ui-widget ui-widget-content fm-button" type='reset' value='Reset'/><input class="ui-widget ui-widget-content fm-button" type="button" value="Cancel" onclick='location.href="<wasp:relativeUrl value="/sampleDnaToLibrary/listJobSamples/${job.jobId}.do" />"' /></td></tr>
</c:if>
</table>
</form>
</c:otherwise>

</c:choose>