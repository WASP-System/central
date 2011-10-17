<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1><fmt:message key="pageTitle.lab/pendinguser/list.label"/></h1>

<table cellpadding="0" cellspacing="0" border="0">
<c:forEach items="${labuserpending}" var="l">
<!-- sec:authorize access="
     hasRole('god') or
     hasRole('lm-' + ${l.labId})
   "-->
        <p>
        <c:out value="${l.firstName}" />
        <c:out value="${l.lastName}" />
        <c:out value="${l.email}" />
          <a href="<c:url value="/lab/userpending/approve/${lab.labId}/${l.userPendingId}.do"/>"><fmt:message key="userPending.action_approve.label"/></a>
          <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${l.userPendingId}.do"/>"><fmt:message key="userPending.action_reject.label"/></a>
        </p>
<!-- /sec:authorize-->
</c:forEach>
</table>

