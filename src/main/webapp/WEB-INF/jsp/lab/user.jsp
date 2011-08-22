<%@ include file="/WEB-INF/jsp/include.jsp" %>

    <c:forEach items="${labuser}" var="ul">
      <p>
      <a href="/wasp/user/detail/<c:out value="${ul.user.userId}" />.do"><c:out
value="${ul.user.login}" /></a>
        <c:out value="${ul.user.firstName}" />
        <c:out value="${ul.user.lastName}" />
      <span><c:out value="${ul.role.name}" /></span>

      <c:if test="${ul.role.roleName == 'lx'}">
        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lu.do"/>">Activate</a>
      </c:if>
      <c:if test="${ul.role.roleName == 'lu'}">
        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lm.do"/>">PROMOTE to LM</a>
        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lx.do"/>">Deactivate</a>
      </c:if>
      <c:if test="${ul.role.roleName == 'lm'}">
        <a href="<c:url value="/lab/user/role/${lab.labId}/${ul.user.userId}/lu.do"/>">DEMOTE to LU</a>
      </c:if>

      </p>
    </c:forEach>

