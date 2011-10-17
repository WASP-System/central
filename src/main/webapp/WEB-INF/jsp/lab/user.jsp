<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

    <font color="red"><wasp:message /></font>

    <c:forEach items="${labuser}" var="ul">
      <p>
      <a href="/wasp/user/detail_ro/<c:out value="${ul.user.userId}" />.do"><c:out value="${ul.user.login}" /></a>
        <c:out value="${ul.user.firstName}" />
        <c:out value="${ul.user.lastName}" />
        <c:out value="${ul.user.email}" />
        <c:if test="${ul.user.isActive == 1}" > <fmt:message key="labuser.active.label"/> 
        </c:if>
        <c:if test="${ul.user.isActive == 0}" > <fmt:message key="labuser.inactive.label"/>
        </c:if>
      <span><c:out value="${ul.role.name}" /></span>

      <c:if test="${ul.role.roleName == 'lx'}">
        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lu.do"/>"><fmt:message key="labuser.status_activate.label"/></a>
      </c:if>
      <c:if test="${ul.role.roleName == 'lu'}">
        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lm.do"/>"><fmt:message key="labuser.status_promoteLM.label"/></a>
        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lx.do"/>"><fmt:message key="labuser.status_deactivate.label"/></a>
      </c:if>
      <c:if test="${ul.role.roleName == 'lm'}">
        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lu.do"/>"><fmt:message key="labuser.status_demoteLU.label"/></a>
      </c:if>
<%--       <c:if test="${ul.role.roleName == 'lp'}"> --%>
<%--         <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lu.do"/>"><fmt:message key="userPending.action_approve.label"/></a> --%>
<%--         <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/xx.do"/>"><fmt:message key="userPending.action_reject.label"/></a> --%>
<%--       </c:if> --%>

      </p>
    </c:forEach>
    <p>
    <a href="/wasp/auth/newuser.do"><fmt:message key="lab.adduser.label" /></a> | <a href="/wasp/lab/detail_ro/<c:out value="${lab.departmentId}"/>/<c:out value="${lab.labId}"/>.do"><fmt:message key="lab.detail.label" /></a> 
	</p>
    <c:if test="${! empty labuserpending}">
      <fmt:message key="labuser.request_title.label"/>
      <c:forEach items="${labuserpending}" var="up">
        <p>
        <c:out value="${up.firstName}" />
        <c:out value="${up.lastName}" />
        <c:out value="${up.email}" />
          <a href="<c:url value="/lab/userpending/approve/${lab.labId}/${up.userPendingId}.do"/>"><fmt:message key="userPending.action_approve.label"/></a>
          <a href="<c:url value="/lab/userpending/reject/${lab.labId}/${up.userPendingId}.do"/>"><fmt:message key="userPending.action_reject.label"/></a>
        </p>
      </c:forEach>
    </c:if>
