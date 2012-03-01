<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<title><fmt:message key="pageTitle.task/fmrequote/list.label"/></title>
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
          <form action="<c:url value="/task/fmrequote/requote.do"/>" method="POST">
            <input type="stateId" value="${s.stateId}">
            <input type="jobId" value="${sj.jobId}">
            <input type="amount" value="">
            <input type="submit">
          </form>
        </c:if>
      </c:forEach>

      </div>
    </c:forEach>


</table>

