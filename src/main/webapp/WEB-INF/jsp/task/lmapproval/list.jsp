<%@ include file="/WEB-INF/jsp/include.jsp" %>



<h1>List List</h1>
<table cellpadding="0" cellspacing="0" border="0">
<a href="/wasp/task/detail/<c:out value="${task.taskId}" />.do"><c:out value="${task.name}" /></a>

    <c:forEach items="${states}" var="s">
      <div>
      <c:out value="${s.name}" />
      <c:forEach items="${s.statejob}" var="sj">
        <c:out value="${sj.job.name}" />

        <c:out value="${s.status}" />
        <c:if test="${s.status == 'RUNNING'}">
          <a href="<c:url value="/task/lmapproval/${sj.job.labId}/${s.stateId}/OK.do"/>">OK</a>
          <a href="<c:url value="/task/lmapproval/${sj.job.labId}/${s.stateId}/NO.do"/>">NO</a>
        </c:if>
      </c:forEach>

      </div>
    </c:forEach>


</table>

