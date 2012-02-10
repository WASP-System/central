<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1><fmt:message key="task.samplereceive.title_label" /></h1>
 <wasp:message /><br />
<table cellpadding="1" cellspacing="1" border="2">
<!-- <a href="/wasp/task/detail/<c:out value="${task.taskId}" />.do"><c:out value="${task.name}" /></a>  -->

 <!--   <c:forEach items="${states}" var="s">
      <div>
      <c:out value="${s.name}" />
      <c:forEach items="${s.statesample}" var="ss">
        <c:out value="${s.statejob[0].job.name}" /> --
        <c:out value="${ss.sample.name}" /> 

        <c:out value="${s.status}" />
        <c:if test="${s.status == 'WAITING'}">
          <div>
          <form action="<c:url value="/task/samplereceive/receive.do"/>" method="POST">
            <input type="hidden" name="stateId" value="${s.stateId}">
            <input type="hidden" name="sampleId" value="${ss.sampleId}">
            <input type="submit" value="Sample Received">
          </form>
          </div>
        </c:if>
      </c:forEach>

      </div>
    </c:forEach>
-->
<tr><th>JobID</th><th>Job Name</th><th>Submitter</th><th>Sample</th><th>Action</th></tr>
 <c:forEach items="${states}" var="s">
 <c:forEach items="${s.statesample}" var="ss">
<tr><td style='text-align:center'><c:out value="${s.statejob[0].job.jobId}" /></td>          
	<td style='text-align:center'><c:out value="${s.statejob[0].job.name}" /></td>
	<td style='text-align:center'><c:out value="${s.statejob[0].job.user.lastName}" />,<c:out value="${s.statejob[0].job.user.firstName.substring(0,1)}" /></td>
	<td style='text-align:center'><c:out value="${ss.sample.name}" /></td>
	<td style='text-align:center'><form action="<c:url value="/task/samplereceive/receive.do"/>" method="POST" onsubmit="return validate(this);">
 		<input type="hidden" name="stateId" value="${s.stateId}">
 		<input type="hidden" name="sampleId" value="${ss.sampleId}"> 
 		<input type="radio" id = "receivedStatus" name = "receivedStatus" value = "RECEIVED">Received&nbsp;<input type="radio" id = "receivedStatus" name = "receivedStatus" value = "NEVER COMING">Never Coming &nbsp;<input type="submit" value="Submit">
		
		</form>
	</td>
</tr>
</c:forEach>
 </c:forEach>
</table>

