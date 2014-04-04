<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1><fmt:message key="pageTitle.lab/pendinguser/list.label"/></h1>

<table cellpadding="0" cellspacing="0" border="0">
<c:if test="${empty labuserpending && empty userpending}">
    <p><fmt:message key="userPending.no_pending_users.label"/></p>
</c:if>
<c:forEach items="${userpending}" var="l">
<!-- sec:authorize access="
     hasRole('su') or
     hasRole('lm-' + ${l.labId})
   "-->
        <p>
        <c:out value="${l.firstName}" />
        <c:out value="${l.lastName}" />
        <c:out value="${l.email}" />
          <a href="<wasp:relativeUrl value="lab/userpending/approve/${lab.labId}/${l.userPendingId}.do"/>"><fmt:message key="userPending.action_approve.label"/></a>
          <a href="<wasp:relativeUrl value="lab/userpending/reject/${lab.labId}/${l.userPendingId}.do"/>"><fmt:message key="userPending.action_reject.label"/></a>
        </p>
<!-- /sec:authorize-->
</c:forEach>
<c:forEach items="${labuserpending}" var="l">
        <p>
        <c:out value="${l.user.firstName}" />
        <c:out value="${l.user.lastName}" />
        <c:out value="${l.user.email}" />
          <a href="<wasp:relativeUrl value="lab/labuserpending/approve/${l.labId}/${l.labUserId}.do"/>"><fmt:message key="userPending.action_approve.label"/></a>
          <a href="<wasp:relativeUrl value="lab/labuserpending/reject/${l.labId}/${l.labUserId}.do"/>"><fmt:message key="userPending.action_reject.label"/></a>
        </p>
<!-- /sec:authorize-->
</c:forEach>
</table>

