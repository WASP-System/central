<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<br /><br />
<table class="EditTable ui-widget ui-widget-content">
<tr class="FormData">
	<td class="CaptionTD"><fmt:message key="puLimitPriorToAssign.chooseMachine.label"/>:</td>
	<td  class="DataTD">
		<form method="GET" action="<wasp:relativeUrl value="/facility/platformunit/limitPriorToAssign.do" />">
			<select class="FormElement ui-widget-content ui-corner-all" name="resourceCategoryId" size="1" onchange="this.parentNode.submit()">
			<option value="0">--<fmt:message key="puLimitPriorToAssign.selectMachine.label"/>--
			<c:forEach items="${resourceCategories}" var="rc">
			<c:set var="selectedFlag" value=""/>
			<c:if test='${resourceCategoryId==rc.resourceCategoryId}'>
				<c:set var="selectedFlag" value="SELECTED"/>
			</c:if>
			<option value="<c:out value="${rc.resourceCategoryId}" />" <c:out value="${selectedFlag}" /> ><c:out value="${rc.name}" /> 
			</c:forEach>
			</select>
		</form>
	</td>
</tr>

<c:if test='${resourceCategoryId > "0"}'>
<tr class="FormData">
	<td class="CaptionTD"><fmt:message key="puLimitPriorToAssign.chooseJob.label"/>:</td>
	<td class="DataTD">
	<form method="GET" action="<wasp:relativeUrl value="/facility/platformunit/assign.do" />">
		<input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="resourceCategoryId" value="<c:out value="${resourceCategoryId}" />" />
		<select class="FormElement ui-widget-content ui-corner-all" name="jobsToWorkWith" size="1">
			<option value="0">--<fmt:message key="puLimitPriorToAssign.selectJob.label"/>--
			<c:if test='${fn:length(jobList) > "1"}'>
				<option value="<c:out value="-1" />"><fmt:message key="puLimitPriorToAssign.allAvailableJobs.label"/> 
			</c:if>
			<c:forEach items="${jobList}" var="j">
				<option value="<c:out value="${j.jobId}" />"><fmt:message key="puLimitPriorToAssign.job.label"/> J<c:out value="${j.jobId}" /> 
			</c:forEach>
		 </select>
		 <br /><br />
		 <c:if test='${fn:length(jobList) > "0"}'> 
		  <input class="FormElement ui-widget-content ui-corner-all" type="submit" value="Submit">  
		 </c:if>
	</form> 
	</td>
</tr>
</c:if>
</table>