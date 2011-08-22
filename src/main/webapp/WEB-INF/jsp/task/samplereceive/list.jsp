<%@ include file="/WEB-INF/jsp/include.jsp" %>



<h1>List List</h1>
<table cellpadding="0" cellspacing="0" border="0">
<a href="/wasp/task/detail/<c:out value="${task.taskId}" />.do"><c:out value="${task.name}" /></a>

    <c:forEach items="${states}" var="s">
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


</table>

