<%@ include file="/WEB-INF/jsp/taglib.jsp" %>
<wasp:message /><br />
<table><tr><td>Choose A Machine</td><td>
<form method="GET" action="<c:url value="/facility/platformunit/limitPriorToAssign.do" />">
<select name="resourceCategoryId" size="1" onchange="this.parentNode.submit()">
<option value="0">--SELECT A MACHINE--
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
<tr><td>Choose A Job</td><td>
<form method="GET" action="<c:url value="/facility/platformunit/assign.do" />">
<input type="hidden" name="resourceCategoryId" value="<c:out value="${resourceCategoryId}" />" />
<select name="jobsToWorkWith" size="1">
	<option value="0">--SELECT A JOB--
	<c:if test='${fn:length(jobList) > "1"}'>
		<option value="<c:out value="-1" />"><c:out value="All Available Jobs" /> 
	</c:if>
	<c:forEach items="${jobList}" var="j">
		<option value="<c:out value="${j.jobId}" />"><c:out value="${j.jobId}" /> 
	</c:forEach>
 </select>
 <br /><br />
 <c:if test='${fn:length(jobList) > "0"}'> 
  <input type="submit" value="Submit">  
 </c:if>
</form> 
</td></tr>
</c:if>
</table>