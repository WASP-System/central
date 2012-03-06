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
        <c:if test="${s.status == 'WAITING'}">
          <form action="<c:url value="/task/fmpayment/payment.do"/>" method="POST">
            <input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="stateId" value="${s.stateId}">
            <input class="FormElement ui-widget-content ui-corner-all" type="hidden" name="jobId" value="${sj.jobId}">
            <input class="FormElement ui-widget-content ui-corner-all" name="amount" value="">
            <input class="FormElement ui-widget-content ui-corner-all" type="submit" value="Payment Received">
          </form>
        </c:if>
      </c:forEach>

      </div>
    </c:forEach>


</table>

