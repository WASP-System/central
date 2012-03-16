<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<title><fmt:message key="pageTitle.task/samplereceive/list.label"/></title>
<h1><fmt:message key="task.samplereceive.title_label" /></h1>
<c:choose>
<c:when test="${states.size()==0}">
<h2><fmt:message key="samplereceivetask.subtitle_none.label" /></h2>
</c:when>
<c:otherwise>
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData"><td class="label-centered">JobID</td><td class="label-centered">Job Name</td><td class="label-centered">Submitter</td><td class="label-centered">Sample</td><td class="label-centered">Molecule</td><td class="label-centered">Action</td></tr>
 <c:forEach items="${states}" var="s">
 <c:forEach items="${s.statesample}" var="ss">
<tr class="FormData"><td style='text-align:center'><c:out value="${s.statejob[0].job.jobId}" /></td>          
	<td style='text-align:center'><c:out value="${s.statejob[0].job.name}" /></td>
	<td style='text-align:center'><c:out value="${s.statejob[0].job.user.lastName}" />,<c:out value="${s.statejob[0].job.user.firstName.substring(0,1)}" /></td>
	<td style='text-align:center'><c:out value="${ss.sample.name}" /></td>
	<td style='text-align:center'><c:out value="${ss.sample.typeSample.name}" /></td>
	<td style='text-align:center'><form action="<c:url value="/task/samplereceive/receive.do"/>" method="POST" onsubmit="return validate(this);">
 		<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="stateId" value="${s.stateId}">
 		<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="sampleId" value="${ss.sampleId}"> 
 		<input class="FormElement ui-widget-content ui-corner-all" type="radio" id = "receivedStatus" name = "receivedStatus" value = "RECEIVED">Received&nbsp;<input class="FormElement ui-widget-content ui-corner-all" type="radio" id = "receivedStatus" name = "receivedStatus" value = "WITHDRAWN">Withdrawn &nbsp;<input class="FormElement ui-widget-content ui-corner-all" type="submit" value="Submit">
		
		</form>
	</td>
</tr>
</c:forEach>
 </c:forEach>
</table>
</c:otherwise>
</c:choose>
